# QuickSort & QuickSelect


### QuickSort

> 快速排序

三个要注意的点，都写在程序里了。

```java
public class Solution {
    /**
     * @param A an integer array
     * @return void
     */
    public void sortIntegers2(int[] A) {
        quickSort(A, 0, A.length - 1);
    }
    
    private void quickSort(int[] A, int start, int end) {
        if (start >= end) {
            return;
        }
        
        int left = start, right = end;
        // key point 1: pivot is the value, not the index
        int pivot = A[(start + end) / 2];

        // key point 2: every time you compare left & right, it should be 
        // left <= right not left < right
        while (left <= right) {
            // key point 3: A[left] < pivot not A[left] <= pivot
            while (left <= right && A[left] < pivot) {
                left++;
            }
            // key point 3: A[right] > pivot not A[right] >= pivot
            while (left <= right && A[right] > pivot) {
                right--;
            }
            if (left <= right) {
                int temp = A[left];
                A[left] = A[right];
                A[right] = temp;
                
                left++;
                right--;
            }
        }
        //退出循环时，应该是right<left的
        //一种可能是right + 1 = left
        //还有一种是right + 2 = left,两个中间有一个数（left++,right--同时触发时）
        //但是这不影响下面递归排序，中间那个肯定还是在中间的。
        quickSort(A, start, right);
        quickSort(A, left, end);
    }
}
```

### qucikSelect

选一个排序数组的第K个数，基本思想类似快排，先随机选pivot，然后排序，左边都小于pivot，右边都大于pivot，推出循环是`right<left`。如果K大于left呢，说明K在右边，就递归选择右边就好了（但是记得减去左边扔掉的数），如果k小于right呢,说明K在左边，递归调用左边即可。

例题如下：

### 215. Kth Largest Element in an Array

**注意**：在left和right交换的那个if语句里，交换完成后要`left++,right--`，多次在这里出错超时，一定不要忘！！！

```java
public class Solution {
    public int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length < k) return 0;
        return quickSelect(nums, 0, nums.length - 1, k);
    }
    private int quickSelect(int[] nums, int start, int end, int k) {
        if (start >= end) {
            return nums[start];
        }
        
        int left = start, right = end;
        int pivot = nums[(start + end) / 2];
        
        while (left <= right) {
            while (left <= right && nums[left] > pivot) {
                left++;
            }
            while (left <= right && nums[right] < pivot) {
                right--;
            }
            if (left <= right) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                //一定不要忘！！！！！
                left++;
                right--;
            }
        }
        
        if (start + k - 1 <= right) {
            return quickSelect(nums, start, right, k);
        } else if (start + k - 1 >= left) {
            return quickSelect(nums, left, end, k - (left - start));
        } else {
            //此为right和left中间还有一个元素的特殊情况（K正好在这个元素）
            return nums[right + 1];
        }
    }
}
```

### 414. Third Maximum Number

> 上面题的简化版，只让找数组（非空）中第三大的数（注意是不重复的）。如果数组没有第三大的，那么返回最大的。

思路：刚开始想的是开一个长度为3的数组，然后每次跟数组中最小的比较，如果大于最小的，替换即可。后来发现这样数组初始化时不好处理重复数据，如果前三个是{1，1，1}就蛋疼了，不能把重复数据进入数组才行。

后来看答案不需要数组，直接三个Integer对象表示前3大的数，按条件更新即可。需要注意的一点就是Integer对象判断值相等应用equals函数，不能用==，==只能判断引用相等否。

```java
public class Solution {
    public int thirdMax(int[] nums) {
        Integer max1 = null;
        Integer max2 = null;
        Integer max3 = null;
        
        for (Integer n : nums) {
            //Integer对象，这里不能用==, ==比较对象的引用，equals比较值
            if (n.equals(max1) || n.equals(max2) || n.equals(max3)) {
                continue;
            }
            if (max1 == null || n > max1) {
                max3 = max2;
                max2 = max1;
                max1 = n;
            } else if (max2 == null || n > max2) {
                max3 = max2;
                max2 = n;
            } else if (max3 == null || n > max3) {
                max3 = n;
            }
        }
        return max3 == null ? max1 : max3;
    }
}
```


### 75. Sort Colors

> 给定一个序列，含有0，1，2。0代表red。1代表white。2代表blue。将数组按红白蓝的顺序排序。不允许用库中的排序函数

思路：用一个i指针从左到右扫描，r记录红得个数，b记录蓝的个数，如果nums[i] == 0的话，就把r和i交换，并把r加1；如果是1，就不做，i++；如果是2，就把它换到最后边，并把b++。（每次r++后定位的就是下一个没排好的地方，如果是红，就搁到r，把r往后移，如果是1，r不动，等i碰到0的时候会换到r这个位置的。）

要注意两点，如果`nums[i] == 2`，把2换到后面的时候，i--，因为不知道换回来的这个是啥，要重新判断。
第二，i应从0到`length - b`而不是到最后，后面的已经排好了，就不要再去交换了。

```java
public class Solution {
    public void sortColors(int[] nums) {
        if (nums == null) {
            return;
        }
        
        int i;
        // r和b记录red和blue的个数。因此第一个不是r的下标就是r，第一个不是b的下标为len-1-b;
        int r = 0, b = 0;
        for (i = 0; i < nums.length - b; i++) {
            if (nums[i] == 0) {
                swap(nums, i, r);
                r++;
            } else if (nums[i] == 2) {
                swap(nums, i, nums.length - 1 - b);
                b++;
                i--;
            }
        }
    }
    
    private void swap(int[] A, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }
}
```

### Sort Colors II(LintCode)

> Given an array of n objects with k different colors (numbered from 1 to k), sort them so that objects of the same color are adjacent, with the colors in the order 1, 2, ... k.

思路：号称彩虹排序的。也是快排思想，不同的是pivot是选一个中间的颜色。

```java
class Solution {
    public void sortColors2(int[] colors, int k) {
        if (colors == null || colors.length < k) {
            return;
        }
        helper(colors, 0, colors.length - 1, 1, k);
    }
    
    private void helper(int[] colors, int start, int end, int colorFrom, int colorTo) {
        //递归终止条件，两个都终止
        if (start >= end) {
            return;
        }
        if (colorFrom == colorTo) {
            return;
        }
        
        int i = start;
        int j = end;
        int midColor = (colorFrom + colorTo) / 2;
        while (i <= j) {
            while (i <= j && colors[i] <= midColor) {
                i++;
            }
            while (i <= j && colors[j] > midColor) {
                j--;
            }
            if (i <= j) {
                int temp = colors[i];
                colors[i] = colors[j];
                colors[j] = temp;
                //不要忘！！！
                i++;
                j--;
            }
        }
        helper(colors, start, j, colorFrom, midColor);
        helper(colors, i, end, midColor + 1, colorTo);
    }
}
```
