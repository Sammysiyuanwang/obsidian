# Dynamic Programming

### 70. Climbing Stairs

> 爬楼梯问题，一次能爬一级或者两级，问n阶台阶有几种走法

思路，关键点在于，n阶台阶的走法，是（n-1)级台阶的走法加上(n-2)级台阶的走法。因此可以使用递归。但是递归通常会溢出，那么使用动态规划思想，记录下每级台阶的走法。


```java
//逆向走法
public class Solution {
    public int climbStairs(int n) {
        // write your code here
        int[] map = new int[n+1];
        Arrays.fill(map, Integer.MIN_VALUE);
        return helper(n, map);
    }
    private int helper(int n, int[] map) {
        if (n < 0) return 0;
        if (n == 0) {
            return 1;
        } else if (map[n] > -1) {
            return map[n];
        } else {
            map[n] = helper(n-1, map) + helper(n-2, map);
        }
        return map[n];
    }
}
//正向走法：
public class Solution {
    public int climbStairs(int n) {
        if (n == 0) return 1;
        int[] f = new int[n+1];
        f[0] = 1;
        f[1] = 1;
        
        for (int i = 2; i < n + 1; i++) {
            f[i] = f[i-1] + f[i-2];
        }
        return f[n];
    }
}
```

### 64. Minimum Path Sum

> Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which minimizes the sum of all numbers along its path.

> Note: You can only move either down or right at any point in time.


思路，用一个二维矩阵记录到该点的最短路径，因为只能向右或想下走，因此递推公式为：
`D[m][n] = min(D[m-1][n], D[m][n-1]) + value[m][n]`.即左边和上边的min加上该点的值。

```java
//该方法使用了extra space，也可以不使用直接在原矩阵上修改
public class Solution {
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        
        int[][] D = new int[m][n];
        D[0][0] = grid[0][0];
        for (int j = 1; j < n; j++) {
            D[0][j] = D[0][j-1] + grid[0][j];
        }
        for (int i = 1; i < m; i++) {
            D[i][0] = D[i-1][0] + grid[i][0];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                D[i][j] = Math.min(D[i-1][j], D[i][j-1]) + grid[i][j];
            }
        }
        return D[m-1][n-1];
    }
}
```

### 62. Unique Paths

> 找从左上角走到右下角的不同的路径有多少条


思路：利用动态规划，每一个点的路径数等于左边点的路径数加上边点的路径数。其中最左边一列和最上边一行路径数都为1。递推公式为`D[m][n] = D[m-1][n] + D[m][n-1]`。
开二维数组记录即可。

```java
public class Solution {
    public int uniquePaths(int m, int n) {
        int[][] D = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j== 0) {
                    D[i][j] = 1;
                } else {
                    D[i][j] = D[i-1][j] + D[i][j-1];
                }
            }
        }
        return D[m-1][n-1];
    }
}
```
优化：上面的方法用了`O(m*n)`的space，可以优化到`O(min(m, n))`的space，考虑D[m][n]只有D[m-1][n]和D[m][n-1]有关，D[m][n-1]可以看成上一时刻的D[m][n]，因此用一维数组的空间即可。

```java
public class Solution {
    public int uniquePaths(int m, int n) {
        int[] res = new int[n];
        res[0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n; j++) {
                res[j] = res[j - 1] + res[j];
            }
        }
        return res[n-1];
    }
}
```

### 63. Unique Paths II

> 在grid里有障碍导致不能通过

思路一样，就是遇到障碍的时候把该点的路径置0即可，但是需要注意一点是j不能从1开始遍历了，因为也有可能在`j=0`的时候有障碍，所以都需要遍历。

```java
public class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        
        int[] D = new int[n];
        D[0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    D[j] = 0;
                } else if (j > 0) {
                    D[j] = D[j] + D[j-1];
                }
            }
        }
        return D[n-1];
    }
}
```

### 300. Longest Increasing Subsequence

> 找一个序列中的最长子序列，返回它的长度。

思路：O(n2)解法是，第n个数的最长子序列的长度应该为前面每一个比它小的数的最长子序列长度+1。这就是递推公式，外层大循环为第几个数，内层循环找n前面每一个比n小的数的最长子序列的长度，找到最长的+1。然后记得更新全局变量即可。

```java
public class Solution {
    public int lengthOfLIS(int[] nums) {
        if (nums == null) return 0;
        int longest = 0;
        int[] dp = new int[nums.length];
        
        for (int i = 0; i < nums.length; i++) {
            // 找到dp[0]到dp[i-1]中最大的升序序列长度且nums[j]<nums[i]
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j]);
                }
            }
            dp[i]++;
            longest = Math.max(longest, dp[i]);
        }
        return longest;
    }
}
```