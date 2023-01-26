# TopK问题的处理方式

题目为找出一些数中最大或最小的K个数或者第K个数：

* Partition
* Heap(PriorityQueue)
* MapReduce
* Spark

***

### 1. 使用快排的partition思想

使用快排的partition思想，当某一次partition完之后返回的index正好为k-1，那么数组前面k个正好是要找的。但前k个并不是排序的。

**缺点**是：1.要改变数组 2.平均时间复杂度O(n)，最坏O(n2)，不适合处理大量数据，有递归。

```java
public class Solution {
    public ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (input == null || input.length < k) {
            return list;
        }
        int start = 0;
        int end = input.length - 1;
        int index = partition(input, start, end);
        while (index != k-1) {
            if (index > k - 1) {
                end = index - 1;
                index = partition(input, start, end);
            } else {
                start = index + 1;
                index = partition(input, start, end);
            }
        }
        for (int i = 0; i < k; i++) {
            list.add(input[i]);
        }
        return list;
    }
    private int partition(int[] nums, int start, int end) {
        if (nums == null || start > end) {
            return -1;
        }
        // 最好随机选，再跟end交换，这里图省事选最后一个为pivot
        int pivot = nums[end];
        // less记小于pivot的个数
        int less = start - 1;
        // 这里i < end推出，end是pivot，不作比较
        for (int i = start; i < end; i++) {
            if (nums[i] < pivot) {
                less++;
                if (i != less) {
                    swap(nums, i, less);
                }
            }
        }
        //less+1 是分界线，把pivot换过来即可
        swap(nums, less + 1, end);
        return less + 1;
    }
    private void swap(int[] A, int i, int j) {
        int tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }
```



### 2. 使用堆

当数据量比较大的时候，使用堆是比较好的选择。维护一个大小为K的堆，然后依次扫描后面的所有数，满足条件入堆，不满足直接pass即可。时间复杂度O(nlogK)。

如果求最大的K个数，那么维护一个小根堆，如果求最小的K个数，则大根堆。

如果求第K个，那么heap[0]即可，如果求前K个，把堆中元素输出即可。

```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length < k) return 0;
        //维护一个大小为K的小根堆
        //叶子节点不用，因此从堆的 (lastIndex - 1) / 2 开始即可，倒序建堆
        for (int len = k / 2 - 1; len >= 0; len--) {
            buildMinHeap(nums, len, k-1);
        }
        //扫描后面的所有数，如果比堆顶(nums[0])大，那么入堆
        for (int i = k; i < nums.length; i++) {
            if (nums[i] > nums[0]) {
                swap(nums, i, 0);
                buildMinHeap(nums, 0, k-1);
            }
        }
        return nums[0];
    }
    private void buildMinHeap(int[] nums, int pos, int lastIndex) {
        // root: 2*pos    leftChild: 2*pos+1    rightChild: 2*pos+2
        while (2 * pos + 1 <= lastIndex) {
            //child记录两个孩子中值更小的那个
            int child = 2 * pos + 1;
            //当右孩子index在堆里并且右孩子更小的时候
            if (child < lastIndex && nums[child] > nums[child+1]) {
                child++;
            }
            //小根堆，因此当pos节点大于子节点时，交换，下沉pos节点
            if (nums[pos] > nums[child]) {
                swap(nums, pos, child);
                pos = child;
            } else {
                break;
            }
        }
    }
    private void swap(int[] A, int i, int j) {
        int tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }
}
```


### 条件允许的话，可以MapReduce分布式

使用python的Hadoop Streaming处理：

```python
//map -cat即可
    
//reduce - 1
import sys, os

query = None
current_query = None
current_count = 0

for line in stdin:
    query = line[:-1]
    if current_query == query:
        current_count += 1
    else:
        if current_query:
            print '%s\t%s' % (current_query, current_count)
        current_query = query
        current_count = 1
//这里要注意，因为当query!=current_query时才触发print条件，
//因此最后一个是不会触发该条件的，所以循环完要补上最后这一个。
if current_query:
    print '%s\t%s' % (current_query, current_count)

//reduce - 2
import sys, os
import heapq
max_num = 10
heap = []
for line in stdin:
    ln = line[:-1].split('\t')
    key = ln[0]
    count = int(ln[1])
    tup = (count, key)
    if len(heap) < max_num:
        heapq.heappush(heap, (count, key))
    else:
        heapq.heappushpop(heap, (count, key))

print [heapq.heappop(heap) for i in range(len(heap))]
```


### spark


```scala
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.storage.StorageLevel._

object TopK{
    def main(args: Array[String]) {
        val conf = new SparkConf().setAppName("打印前k多的查询")
        val sc = new SparkContext()
        //读入文件并按query进行计数 
        val query = sc.textFile(args(0)).map(a => (a, 1)).reduceByKey(_+_)
        val sorted = query.map{case (key, count) => (count, key)}.sortByKey(false)
        val topK = sorted.top(args(1).toInt)
        topK.collect().foreach(pringln)
        sc.stop()
    }
}
```