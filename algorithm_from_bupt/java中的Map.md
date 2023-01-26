# java中的Map

**java为数据结构中的映射定义了一个接口java.util.Map;它有四个实现类,分别是HashMap Hashtable LinkedHashMap 和TreeMap.**

Map主要用于存储键值对，根据键得值，不允许键重复，但允许值重复。

***

#### HashMap

HashMap是最常用的Map，根据键的HashCode存储值，允许但只允许1个键为null，允许多个值为null。

* 遍历时，访问顺序随机。
* 不支持线程的同步，并不是线程安全的，如果同一时刻多个线程同时写HashMap，有课能导致数据不一致。
* 如果需要同步，使用Collections的synchronizedMap方法使HashMap具有同步的能力。


#### Hashtable

与HashMap不同的地方是：

* 不允许键或者值为空
* 线程安全，同一时刻只能有一个线程在写，因此有可能速度较慢

#### LinkedHashMap

保存了键插入的顺序，当使用`iterator`遍历时，按插入顺序遍历。

那道“第一个只出现一次的字符”可以用这个数据结构来做。

#### TreeMap

TreeMap实现了一个`SortMap`接口，可以把记录按照键进行排序，默认升序。当使用iterator遍历时，按排好序的顺序进行遍历。

***

### Map的4种遍历方法

```java
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestMap {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "ab");
        map.put(4, "ab");
        map.put(4, "ab");// 和上面相同 ， 会自己筛选
        System.out.println(map.size());
        // 第一种：
        /*
         * Set<Integer> set = map.keySet(); //得到所有key的集合
         * 
         * for (Integer in : set) { String str = map.get(in);
         * System.out.println(in + "     " + str); }
         */
        System.out.println("第一种：通过Map.keySet遍历key和value：");
        for (Integer in : map.keySet()) {
            //map.keySet()返回的是所有key的值
            String str = map.get(in);//得到每个key多对用value的值
            System.out.println(in + "     " + str);
        }
        // 第二种：
        System.out.println("第二种：通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
             Map.Entry<Integer, String> entry = it.next();
               System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
        // 第三种：推荐，尤其是容量大时
        System.out.println("第三种：通过Map.entrySet遍历key和value");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
            //entry.getKey() ;entry.getValue(); entry.setValue();
            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
        }
        // 第四种：
        System.out.println("第四种：通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : map.values()) {
            System.out.println("value= " + v);
        }
    }
}
```
