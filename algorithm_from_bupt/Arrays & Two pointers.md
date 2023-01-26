#  Arrays & Two pointers

### 1. Two Sum

> Given an array of integers, return indices of the two numbers such that they add up to a specific target.

思路：用哈希表存储，`key`是值，`value`是索引，每次查找`value-nums[i]`是否在哈希表里，有的话取出索引跟i搭配即可。

```java
public class Solution {
    public int[] twoSum(int[] nums, int target) {
        int[] output = new int[2];
        if (nums == null || nums.length < 2) {
            return output;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int diff = target - nums[i];
            if (map.containsKey(diff)) {
                output[0] = map.get(diff);
                output[1] = i;
                break;
            } else {
                map.put(nums[i], i);
            }
        }
        return output;
    }
}
```


### 167. Two Sum II - Input array is sorted

> **排序**数组里找到和为target的两个值，返回这两个值的索引。

思路：两根指针的原始问题，一根指针从前往后，一根从后往前，如果两者相加大于target，那么说明和大了，因此后面指针减一，减小一点，如果两者相加小于target，那么说明和小了，前面指针加一，变大一点，直到找到。

```java
public class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int[] output = new int[2];
        if (numbers == null || numbers.length < 2) {
            return output;
        }
        
        int left = 0;
        int right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                output[0] = left + 1;
                output[1] = right + 1;
                break; //记得这里要break;
            } else if (sum > target) {
                right--;
            } else {
                left++;
            }
        }
        return output;
    }
}
```

### Two Sum - Data structure design

> 设计一个数据结构，能add，向数据结构里添加元素；还能find(value)，找数据结构中是否存在两个数相加等于value，返回boolean。

思路1：利用Two Sum I的做法，利用哈希表。

思路2：利用Two Sum II的做法，排序，然后两根指针分别从两头找。

```java
//思路1
public class TwoSum {

    private List<Integer> list = null;
    private Map<Integer, Integer> map = null;
    public TwoSum() {
        list = new ArrayList<Integer>();
        map = new HashMap<Integer, Integer>();
    }

    // Add the number to an internal data structure.
    public void add(int number) {
        // Write your code here
        if (map.containsKey(number)) {
            map.put(number, map.get(number) + 1);
        } else {
            map.put(number, 1);
            list.add(number);
        }
    }

    // Find if there exists any pair of numbers which sum is equal to the value.
    public boolean find(int value) {
        // Write your code here
        for (int i = 0; i < list.size(); i++) {
            int num1 = list.get(i), num2 = value - num1;
            if ((num1 == num2 && map.get(num1) > 1) || 
                (num1 != num2 && map.containsKey(num2))) 
                return true;
        }
        return false;
    }
}


// Your TwoSum object will be instantiated and called as such:
// TwoSum twoSum = new TwoSum();
// twoSum.add(number);
// twoSum.find(value);

//思路2
public class TwoSum {
    List<Integer> list = new ArrayList<>();
    // Add the number to an internal data structure.
    public void add(int number) {
        // Write your code here
        list.add(number);
        
    }

    // Find if there exists any pair of numbers which sum is equal to the value.
    public boolean find(int value) {
        // Write your code here
        if (list.isEmpty() || list.size() < 2) {
            return false;
        }
        Collections.sort(list, new Comparator<Integer>(){
            public int compare(Integer a, Integer b) {
                return a - b;
            }
        });

        int left = 0;
        int right = list.size() - 1;
        while (left < right) {
            if (list.get(left) + list.get(right) > value) {
                right--;
            } else if (list.get(left) + list.get(right) < value) {
                left++;
            } else {
                return true;
            }
        }
        return false;
    }
}
```



### Remove Duplicate Numbers in Array(LintCode)

> 给一个数组，移除掉重复元素。不需要保持原来的顺序，并返回不重复元素的个数

思路1：利用hashMAP，记录元素出现次数，然后返回出现次数为1的即可

思路2：先排序，然后两根指针，`len`指针记录不重复的，`i`指针遍历，当`i`跟`len`不同时，`len`就+1并更新元素，这样把不重复的元素都放在前`len`个里了，又因为`len`从0开始，因此`return len + 1`个

```java
//思路1：注意map里Map.Entry的使用
public class Solution {
    /**
     * @param nums an array of integers
     * @return the number of unique integers
     */
    public int deduplication(int[] nums) {
        // Write your code here
        Map<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], true);
        }
        
        int result = 0;
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            nums[result++] = entry.getKey();
        }
        return result;
    }
}
//思路2

```
### Two Sum - Unique pairs(LintCode)

> 找到不同的和为target的pair的个数

思路：先排序，然后两根指针找的时候，如果相等，则`left++, right--`，完了判断下新的`left`是否跟之前相同，若相同，继续`left++`，知道跳过相同值即可。`right`同理。

```java
public class Solution {
    /**
     * @param nums an array of integer
     * @param target an integer
     * @return an integer
     */
    public int twoSum6(int[] nums, int target) {
        // Write your code here
        if (nums == null || nums.length < 2) {
            return 0;
        }
        int count = 0;
        Arrays.sort(nums);
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                count++;
                left++;
                right--;
                while (left < right && nums[right] == nums[right + 1]) {
                    right--;
                }
                while (left < right && nums[left] == nums[left - 1]) {
                    left++;
                }
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return count;
    }
}
```

### 15. 3Sum

> 3sum问题，找到所有不同的`a+b+c = 0`的a, b, c。

在2Sum问题上多加一层for循环，从`i`开始到`nums.length-2`结束。每次对`(i+1, nums.lenth - 1)`做2sum问题。记得要有两次删除重复的操作。

这题最初次做的时候，想法是for循环，然后把i换到第一个元素位置，再对(1, length - 1)做2sum问题，这样是不对的，明显有重复，变成了有放回的！！

还有一个错误就是在里面判重时候要用while而不是if!

注意容易出错的点：

1. 忘记sort（）
2. 忘记外面大循环（第一个数）的去重
3. res append的时候记得append值而不是索引！！！
4. 第二个第三个数去重时候先`i ++ , j--`再进行while循环！！！（不是if）而且while循环也要注意`i<j`的条件

```python
class Solution(object):
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        res = []
        if not nums or len(nums) == 0:
            return res
        nums.sort()
        for t in range(len(nums) - 2):
            # skip duplicates(first elements)
            if t > 0 and nums[t] == nums[t - 1]:
                continue
            target = - nums[t]
            i = t + 1
            j = len(nums) - 1
            while i < j:
                s = nums[i] + nums[j]
                if s == target:
                    res.append([-target, nums[i], nums[j]])
                    # skip duplicates(second and third elements)
                    i += 1
                    j -= 1
                    while i < j and nums[i] == nums[i - 1]:
                        i += 1
                    while i < j and nums[j] == nums[j + 1]:
                        j -= 1
                elif s > target:
                    j -= 1
                else:
                    i += 1
        return res
```

### 16. 3Sum Closest

> Given an array S of n integers, find three integers in S such that the sum is closest to a given number, target. Return the sum of the three integers. You may assume that each input would have exactly one solution.

注意：return的是和，而不是最小的差。

```java
public class Solution {
    public int threeSumClosest(int[] nums, int target) {
        if (nums == null || nums.length < 3) {
            return 0;
        }
        
        Arrays.sort(nums);
        int res = nums[0] + nums[1] + nums[nums.length - 1];
        for (int i = 0; i < nums.length - 2; i++) {
            int left = i+1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum > target) {
                    right--;
                } else {
                    left++;
                }
                //这个判断在while循环里面，不是外面
                if (Math.abs(target - sum) < Math.abs(target - res)) {
                    res = sum;
                }
            }
        }
        return res;
    }
}
```


### 4.Median of Two Sorted Arrays

> 找两个有序数组的中位数

思路：写一个更牛逼的算法找两个有序数组的第K个数来解决这个问题。考虑找两个有序数组的第K个数，找这两个数组的第K/2个数，如果`A[k/2-1] < B[k/2-1]`，那么第K个数一定不在`A[k/2-1]`里面，那么就可以把A的前k/2部分扔掉，递归找A的`len - k/2`和B两个数组中的第`k - k/2`个数（因为扔掉了k/2个数）；如果`A[k/2-1]>len(A)`的话，那么第K个数一定不在B的前k/2中，所以扔掉B的前一部分。如此递归，递归出口为把A全部扔掉了，或者把B全部扔掉了，或者`K==1`时，返回两个数组第一个的最小值即可。

总结为：都不越界的话，谁的第k/2个值小，就扔掉谁的前一部分。谁的k/2越界了，就扔掉另一个的前一部分。

```java
public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len = nums1.length + nums2.length;
        if (len % 2 == 1) {
            return findKth(nums1, 0, nums2, 0, len/2 + 1);
        } else {
            return (findKth(nums1, 0, nums2, 0, len/2) + findKth(nums1, 0, nums2, 0, len/2 + 1)) / 2.0;
        }
    }
    private int findKth(int[] nums1, int start1, int[] nums2, int start2, int k) {
        //终止条件
        if (start1 >= nums1.length) {
            return nums2[start2 + k - 1];
        }
        if (start2 >= nums2.length) {
            return nums1[start1 + k - 1];
        }
        if (k == 1) {
            return Math.min(nums1[start1], nums2[start2]);
        }
        //这里设置的原因是如果k/2个数越界的问题
        int mid1 = Integer.MAX_VALUE, mid2 = Integer.MAX_VALUE;
        if (start1 + k/2 - 1 < nums1.length) mid1 = nums1[start1 + k/2 - 1];
        if (start2 + k/2 - 1 < nums2.length) mid2 = nums2[start2 + k/2 - 1];
        
        
        if (mid1 < mid2) {
            return findKth(nums1, start1 + k/2, nums2, start2, k - k/2);
        } else {
            return findKth(nums1, start1, nums2, start2 + k/2, k - k/2);
        }
    }
}
```


### 11. Container With Most Water

> 给定一个数组，数组的值为n个垂直的板子，问找到两个板子，这两个板子和x轴组成的容器盛水量最大

分析： 其实就是找 $max[(j - i) * min(height[i], height[j])]$

此题O(n2)的解法超时，因此肯定有O(n)的解法。

使用两根指针，`left`和`right`在两头开始遍历，每次遍历的时候呢，`height[left]`和`height[right]`谁小谁向里移动。

**为什么呢？**假设我们`left`在`a10`处, `right`在`a20`处。如果`a10 < a20`，此时盛水量为`water = (20 - 10) * a10`。

如果移动大的即`right`移动到`a19`，两种情况：1）`a19 > a10`, 则`water = （19 - 10） * a10`一定小于之前的water。2）`a19 < a10`， `water = (19 - 10) * a19`，两项都比之前的小，更比water小了。

因此，为了找更大的值，此时只能移动left。移动right不可能找到更大的值。

```java
public int maxArea(int[] height) {
        int water = 0;
        
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            int local = (right - left) * Math.min(height[left], height[right]);
            water = Math.max(local, water);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return water;
    }
```