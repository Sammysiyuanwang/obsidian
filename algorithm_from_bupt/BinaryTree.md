# BinaryTree


### 102. Binary Tree Level Order Traversal

> 二叉树的层级遍历

两种方法，一是利用BFS思想，利用队列一层一层遍历

二是用DFS遍历数，只要记住节点在哪一层即可，将level + 1传入下一层递归调用

```java
//BFS
public class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode head = queue.poll();
                level.add(head.val);
                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
            }
            result.add(level);
        }
        return result;
    }
}

//DFS
public class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> lists = new ArrayList<>();
        if (root == null) {
            return lists;
        }
        helper(root, lists, 0);
        return lists;
    }
    public void helper(TreeNode root, List<List<Integer>> lists, int level) {
        if (root == null) return;
        
        List<Integer> list = null;
        if (lists.size() == level) { // 该层不再链表中，因为0层应该有1个
            list = new ArrayList<Integer>();
            lists.add(list);
        } else {
            list = lists.get(level);
        }
        
        list.add(root.val);
        helper(root.left, lists, level + 1);
        helper(root.right, lists, level + 1);
    }
}
```

### 113. Path Sum II

> 找从根节点到叶子节点的路径和等于sum的所有路径

思路：用dfs做，注意的是，不能`target<0`就返回，因为没说全是正数啊，有可能有负数的；还有不要忘记做回溯，dfs完了之后把当前节点删了

```java
public class Solution {
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        List<Integer> path = new ArrayList<>();
        dfs(root, sum, result, path);
        return result;
    }
    private void dfs(TreeNode root, int target, List<List<Integer>> result, List<Integer> path) {
        if (root == null) {
            return;
        }
        path.add(root.val);
        if (root.left == null && root.right == null && root.val == target) {
            result.add(new ArrayList<Integer>(path));
            return;
        }
        if (root.left != null) {
            dfs(root.left, target - root.val, result, path);
            path.remove(path.size() - 1);
        }
        if (root.right != null) {
            dfs(root.right, target - root.val, result, path);
            path.remove(path.size() - 1);
        }
    }
}
```

### 437. Path Sum III

> 不一定从根节点开始叶子节点结束，但一定是从上到下的方向的路径，找等于sum的这些路径的数量


思路：采用正向递归给path赋值，然后记录当前节点的位置（level），从当前节点向上遍历，找等于sum的路径，找到时（i即为路径开头，level为路径结尾）。

```java
public class Solution {
    int ans = 0;
    public int pathSum(TreeNode root, int sum) {
        if (root == null) return 0;
        
        int[] path = new int[depth(root)];
        countSum(root, sum, path, 0);
        return ans;
    }
    private void countSum(TreeNode root, int sum, int[] path, int level) {
        if (root == null) return;
        
        path[level] = root.val;
        int t = 0;
        
        for (int i = level; i >= 0; i--) {
            t += path[i];
            if (t == sum) {
                ans++;
            }
        }
        
        countSum(root.left, sum, path, level + 1);
        countSum(root.right, sum, path, level + 1);
        
        path[level] = Integer.MIN_VALUE;//从路径中移除当前节点，严格的说不需要这么做，但是好习惯
    }
    
    private int depth(TreeNode root) {
        if (root == null) return 0;
        return (1 + Math.max(depth(root.left), depth(root.right)));
    }
}
```