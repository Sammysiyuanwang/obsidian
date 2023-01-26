
### 题目

<https://leetcode.cn/problems/lru-cache/?favorite=2cktkvj>

>请你设计并实现一个满足  [LRU (最近最少使用) 缓存](https://baike.baidu.com/item/LRU) 约束的数据结构。
>实现 `LRUCache` 类：
>-   `LRUCache(int capacity)` 以 **正整数** 作为容量 `capacity` 初始化 LRU 缓存
>-    `int get(int key)` 如果关键字 `key` 存在于缓存中，则返回关键字的值，否则返回 `-1` 。
>-    `void put(int key, int value)` 如果关键字 `key` 已经存在，则变更其数据值 `value` ；如果不存在，则向缓存中插入该组 `key-value` 。如果插入操作导致关键字数量超过 `capacity` ，则应该 **逐出** 最久未使用的关键字。
>-  函数 `get` 和 `put` 必须以 `O(1)` 的平均时间复杂度运行。



```java
class LRUCache {
    class DNode{
        int key;
        int value;
        DNode prev;
        DNode next;
        public DNode() {}
        public DNode(int key, int value) {this.key = key; this.value = value;}
    }

    // 记得初始化
    private int capacity;
    private Map<Integer, DNode> map = new HashMap<Integer, DNode>();
    private DNode head = new DNode();
    private DNode tail = new DNode();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        if (map.containsKey(key)) {
            DNode res = map.get(key);
            moveToHead(res);
            return res.value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            // 如果有的话，拿出来改value，并且移到头部
            DNode cur = map.get(key);
            cur.value = value;
            moveToHead(cur);
        } else {
            // 没有的话，创建一个并移到头部。如果map的大小超过capacity，那么删掉最后一个节点
            DNode newNode = new DNode(key, value);
            insertNode(newNode);
            map.put(key, newNode);
            if (map.size() > capacity) {
                removeNode();
            }
        }
    }

    // 双向链表，要把 prev 和 next都处理好。
    private void moveToHead(DNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        insertNode(node);
    }

    private void insertNode(DNode node) {
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev = head;
    }

    private void removeNode() {
        DNode pr = tail.prev;
        DNode prpr = tail.prev.prev;
        prpr.next = tail;
        tail.prev = prpr;
        map.remove(pr.key);
    }
}
```
