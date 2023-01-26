
//链表实现栈
public class Stack<Item> {

	private Node first; // 栈顶元素
	private int N; //栈的大小

	private class Node {
		Item item;   //存储的值
		Node next;   //下一个节点
	}

	private boolean isEmpty() {
		return first == null;  // 或者 N == 0;
	}

	private int size() {
		return N;
	}

	private void push(Item head) {
		Node oldfirst = first;
		first = new Node();
		first.item = head;
		first.next = oldfirst;
		N++;
	}

	private Item pop() {
		Item item = first.item;
		first = first.next;
		N--;
		return item;
	}
}

// 链表实现队列
public class Queue<Item> {

	private Node first; // 队列头的元素
	private Node last; // 队列尾的元素
	private int N; // 队列长度

	private Node {
		Item item;
		Node next;
	}

	private boolean isEmpty() {
		return (N == 0);
	}

	private int size() {
		return N;
	}

	private void enqueue(Item item) {
		New oldlast = last;
		last = new Node();
		last.item = item;
		last.next = null;
		// 需要判断是否为空，是的话把first和last都要指向新节点
		if (isEmpty()) {
			first = last;
		} else {
			oldlast.next = last;
		}
		N++;
	}

	private Item dequeue() {
		Node item = first.item;
		first = first.next;
		// 判空
		if (isEmpty()) {
			last = null;
		}
		N--;
		return item;
	}
}


// 实现priority Queue
public class MaxPQ<Key extends Comparable<Key>> {
	// 使用一个N+1的数组表示大小为N的堆，第一个位置pq[0]始终为空
	// 这样对于节点K，它的父节点的索引就是k/2，它的子节点就是2k和2k+1
	
	private Key[] pq;  //基于堆的完全二叉树，数组实现
	private int N;    // 堆的大小

	public MaxPQ(int maxN) {
		pq = (Key[]) new Comparable[maxN + 1];
	}

	public boolean isEmpty() {
		return (N == 0);
	}

	public int size() {
		return N;
	}

	public void insert(Key v) {
		pq[N++] = v;
		swim(v); // 底部(数组末尾)插入v，将v上浮到合适的位置
	}
	
	//删除最大元素的时候，将数组顶端元素删除并把末尾的放到顶端，减小堆的大小
	//然后让这个元素下沉到合适位置
	public Key delMax() {
		Key max = pq[1];  //从根节点得到最大元素
		swap(1, N--);    // 将其和最后一个个节点交换，并刨除最后一个节点。
		pq[N+1] = null; // 防止对象游离
		sink(1);   // 恢复堆的有序性
		return max;
	}

	// 辅助方法
	private boolean less(int i, int j) {
		return pq[i].compareTo(pq[j]) < 0;
	}

	private void swap(int i, int j) {
		Key t = pq[i];
		pq[i] = pq[j];
		pq[j] = t;
	}

	private void swim(int k) {
		while (k > 1 && less(k/2, k)) {
			swap(k/2, k);
			k = k/2;
		}
	}
	// 下沉的时候要考虑下沉到两个节点中更大的那个节点
	// k的下个节点的下标是2k和2k+1
	private void sink(int k) {
		while (2 * k < N) {
			int j = 2 * k;
			if (j < N && less(j, j+1)) j++;
			if (!less(k, j)) break;
			swap(k, j);
			k = j;
		}
	}
}





