20221030

[岛屿问题通用解法
](https://leetcode.cn/problems/number-of-islands/solutions/211211/dao-yu-lei-wen-ti-de-tong-yong-jie-fa-dfs-bian-li-/)



### 1. 岛屿个数

<https://leetcode.cn/problems/number-of-islands/description/>

> 给你一个由 `'1'`（陆地）和 `'0'`（水）组成的的二维网格，请你计算网格中岛屿的数量。
> 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
> 此外，你可以假设该网格的四条边均被水包围。

解法：
```java
class Solution {
    public int numIslands(char[][] grid) {
        int res = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j ++) {
                if (grid[i][j] == '1') {
                    dfs(grid, i, j);
                    res++;
                }
            }
        }
        return res;
    }

    private void dfs(char[][] grid, int r, int c) {
        // 退出条件，当超出网格范围时退出
        // WARNING: 这里大于等于 length 而不是大于
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length) {
            return;
        }

        // 当遇到visited或者海洋时退出
        if (grid[r][c] != '1') {
            return;
        }

        // 核心代码，把遍历过的节点改为2，上面for循环的时候会直接跳过，不会重复遍历造成死循环
        grid[r][c] = '2';
        dfs(grid, r-1, c);
        dfs(grid, r+1, c);
        dfs(grid, r, c-1);
        dfs(grid, r, c+1);
    }
}
```


### 2. 岛屿周长

<https://leetcode.cn/problems/island-perimeter/description/>

定义跟上题类似，但是只有一个岛屿，求这个陆地的周长。

解法：还是使用dfs实现，需要想明白的一点就是，从陆地跨到海洋，就是经过一条边，这条边就是长度为1的，把所有跨出去到海洋或者出界的边加起来，也就是周长。

```java
class Solution {
    public int islandPerimeter(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    return dfs(grid, i, j);
                }
            }
        }
        return 0;
    }

    private int dfs(int[][] grid, int r, int c) {
	    // 从陆地遍历过来，如果是出界或者到了海洋，那这就是一条边
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] == 0) {
            return 1;
        }

		// 访问到陆地不增加周长，因为是内部的边
        if (grid[r][c] != 1) {
            return 0;
        }

        grid[r][c] = 2;
        return dfs(grid, r-1, c) +
            dfs(grid, r+1, c) +
            dfs(grid, r, c-1) +
            dfs(grid, r, c+1);
    }
}
```

### 3. 最大岛屿的面积

求最大岛屿的面积

解法同上，经过陆地加一，经过海洋返回即可

```java
class Solution {
    public int maxAreaOfIsland(int[][] grid) {
        int res = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    int area = dfs(grid, i, j);
                    if (area > res) {
                        res = area;
                    }
                }
            }
        }
        return res;
    }

    private int dfs(int[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length) {
            return 0;
        }

        if (grid[r][c] != 1) {
            return 0;
        }

        grid[r][c] = 2;
        return 1 +
            dfs(grid, r-1, c) +
            dfs(grid, r+1, c) +
            dfs(grid, r, c-1) + 
            dfs(grid, r, c+1);
    }
}
```

### 4. 最大人工岛

<https://leetcode.cn/problems/making-a-large-island/description/>

>给你一个大小为 `n x n` 二进制矩阵 `grid` 。**最多** 只能将一格 `0` 变成 `1` 。
>返回执行此操作后，`grid` 中最大的岛屿面积是多少？
>**岛屿** 由一组上、下、左、右四个方向相连的 `1` 形成。

解法：

此题是上面最大面积题目的延伸，首先做的是使用dfs遍历每个岛屿，算出每个岛屿的面积。然后遍历每个海洋地块的四条边，把连着陆地的边面积加起来（再加1）就是最大人工岛的面积。

这里需要注意的是：
- 不同的陆地，使用不同的index来标记，然后拿一个map存储不同index岛的面积。不能直接把面积标记在岛上，因为有可能出现海洋的两个边接壤的是同一块陆地的情况。
- 第一次写出错的地方：需要考虑没有海洋的情况，因此要把陆地最大面积和人工岛最大面积再比较下，确保这种corner case没问题

```java
class Solution {
    public int largestIsland(int[][] grid) {
        int index = 2;
        int maxArea = 0;   // 人工岛最大地块
        int originMax = 0; // 原始陆地最大地块
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    int area = dfs(grid, i, j, index);
                    originMax = Math.max(area, originMax);
                    map.put(index, area);
                    index++;
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
	                // 遍历四个临近地块是否是陆地，用set去重同一块陆地
                    Set<Integer> islands = new HashSet<>();
                    if (getLand(grid, i-1, j) > 1) {
                        islands.add(getLand(grid, i-1, j));
                    }
                    if (getLand(grid, i+1, j) > 1) {
                        islands.add(getLand(grid, i+1, j));
                    }
                    if (getLand(grid, i, j+1) > 1) {
                        islands.add(getLand(grid, i, j+1));
                    }
                    if (getLand(grid, i, j-1) > 1) {
                        islands.add(getLand(grid, i, j-1));
                    }
                    int area = 0;
                    for (Integer x: islands) {
                        area += map.getOrDefault(x, 0);
                    }
                    if (maxArea < area) {
                        maxArea = area;
                    }
                }
            }
        }
        // 防止没有海洋的极端情况出现
        return maxArea + 1 > originMax ? maxArea + 1 : originMax;
    }

    private int dfs(int[][] grid, int r, int c, int index) {
        if (!inArea(grid, r, c)) {
            return 0;
        }

        if (grid[r][c] != 1) {
            return 0;
        }

        grid[r][c] = index;
        return 1 +
            dfs(grid, r-1, c, index) +
            dfs(grid, r+1, c, index) +
            dfs(grid, r, c-1, index) + 
            dfs(grid, r, c+1, index);
    }

    private int getLand(int[][] grid, int i, int j) {
        if (inArea(grid, i, j) && grid[i][j] > 1) {
            return grid[i][j];
        }
        return 0;
    }

    private boolean inArea(int[][] grid, int i, int j) {
        return i >= 0 && j >= 0 && i < grid.length && j < grid[0].length;
    }
}
```