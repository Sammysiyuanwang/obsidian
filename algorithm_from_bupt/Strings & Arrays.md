# Strings & Arrays

### 189. Rotate Array

> Rotate an array of n elements to the right by k steps.

> For example, with `n = 7` and `k = 3`, the array `[1,2,3,4,5,6,7]` is rotated to `[5,6,7,1,2,3,4]`.

思路1（自己想到的）：常规思路，保存下后k个，然后依次赋值过去

思路2：分别翻转前面`n-k`的子串和后面`k`个子串，然后整体翻转(或者先整体翻转，再分别翻转`[0,k-1]`和`[k,end]`,效果一样。）

思路3：每次向右移动一个位置，循环k次

```java
//思路1
public void rotate(int[] nums, int k) {
            if (k == 0 || k == nums.length) return;
            if (k > nums.length) {
                k %= nums.length;
            }
            int[] temp = new int[k];
            int slow = nums.length - 1 - k;
            int fast = nums.length - 1;
            int j = slow + 1;
            for (int i = 0; i < k; i++) {
                temp[i] = nums[j++];
            }
            while (slow >= 0) {
                nums[fast--] = nums[slow--];
            }
            for (int i = 0; i < k; i++) {
                nums[i] = temp[i];
            }
    }

//思路2
public void rotate(int[] nums, int k) {
        if (k == 0 || k == nums.length) {
            return;
        }
        if (k > nums.length) {
            k %= nums.length;
        }
        
        reverse(nums, 0, nums.length - 1 - k);
        reverse(nums, nums.length - k, nums.length - 1);
        reverse(nums, 0, nums.length - 1);
    }
    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[end];
            nums[end] = nums[start];
            nums[start] = temp;
            
            start++;
            end--;
        }
    }
//思路3



```




### 287. Find the Duplicate Number

> Given an array nums containing n + 1 integers where each integer is between 1 and n (inclusive), prove that at least one duplicate number must exist. 
> 
> Assume that there is only one duplicate number, find the duplicate one.
> 
> 要走小于O(n2)的时间，O(1)的空间

思路1：大神的方法。把字符串看做一个链表，索引中存的值看做它的下一个的索引。因此，若有重复的元素，那么一定就有环。因此，变成了使用快慢指针找环的问题。

思路2：二分查找法。假设为1到10的数字即`n = 10`。如果没有重复的，`mid = 5`，则`<=5`的数字一定是5个，如果比5个多，说明重复数字在[1,5]区间，则`high = mid`，如果比5个少，则说明在[6,10]区间，则`low = mid + 1`。

```java
//判断环入口
public class Solution {
    public int findDuplicate(int[] nums) {
        if (nums == null || nums.length < 2) {
            return -1;
        }
        
        int slow = nums[0];
        int fast = nums[nums[0]];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[nums[fast]];
        }
        
        fast = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }
}
//二分查找
class Solution(object):
    def findDuplicate(self, nums):
        low = 1
        high = len(nums) - 1
        
        while low < high:
            mid = low + (high - low) / 2
            cnt = 0
            for num in nums:
                if num <= mid:
                    cnt += 1
            if cnt <= mid:
                low = mid + 1
            else:
                high = mid
                
        return low
```