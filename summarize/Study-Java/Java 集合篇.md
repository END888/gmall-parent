---
title: Java 集合篇
date: 2022-08-08
author: yincy
---



### Java 集合篇

---



#### 1、ArrayList



**构造器**

```java
// 使用默认的构造器创建一个初识容量为0的 Object[]
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
```



**扩容方法**

```java
// 扩容方法（容量的最大值为 int 的最大值：2^31 -1）
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    // 新数组的容量为原来的1.5倍(原来的容量+原来的容量右移一位)
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // 底层使用的是 System.arraycopy()方法
    elementData = Arrays.copyOf(elementData, newCapacity);
}
/* 参数说明：
 *	参数1：源数组
 *	参数2：原数组中的起始位置
 *	参数3：目标数组
 *	参数4：目标数组中的起始位置
 *	参数5：要复制的数组元素的数量
 */
public static native void arraycopy(Object src,  int  srcPos,
                                    Object dest, int destPos,
                                    int length);
```

---



#### 2、Vector



**构造器**



```java
// 创建了一个初识容量为10的 Object[]
public Vector() {
    this(10);
}

public Vector(int initialCapacity) {
    this(initialCapacity, 0);
}

public Vector(int initialCapacity, int capacityIncrement) {
    super();
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    this.elementData = new Object[initialCapacity];
    this.capacityIncrement = capacityIncrement;
}
```





**扩容方法**



```java
// 扩容方法：
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    // 新数组的容量大小为原来的2倍
    int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                     capacityIncrement : oldCapacity);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

---



#### 3、LinkedList

---



#### 4、HashSet

---



#### 5、LinkedHashSet

---



#### 6、HashMap



**内部的存储结构**



###### 1、数组的每个元素类型

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next; // 表示链表的下一个节点
    
    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
```

###### 2、红黑树的结构

```java
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent;
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;
    boolean red;
    TreeNode(int hash, K key, V val, Node<K,V> next) {
        super(hash, key, val, next);
    }
```

###### 3、HashMap 中重要的字段

```java
// 数组默认的初始化长度16
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

// 数组最大容量，2的30次幂，即1073741824
static final int MAXIMUM_CAPACITY = 1 << 30;

// 默认加载因子值
static final float DEFAULT_LOAD_FACTOR = 0.75f;

// 链表转换为红黑树的长度阈值
static final int TREEIFY_THRESHOLD = 8;

// 红黑树转换为链表的长度阈值
static final int UNTREEIFY_THRESHOLD = 6;

// 链表转换为红黑树时，数组容量必须大于等于64
static final int MIN_TREEIFY_CAPACITY = 64;

// HashMap里键值对个数
transient int size;

// 扩容阈值，计算方法为 数组容量*加载因子
int threshold;

// HashMap使用数组存放数据，数组元素类型为Node<K,V>
transient Node<K,V>[] table;

// 加载因子
final float loadFactor;

// 用于快速失败，由于HashMap非线程安全，在对HashMap进行迭代时，如果期间其他线程的参与导致HashMap的结构发生变化了（比如put，remove等操作），直接抛出ConcurrentModificationException异常
transient int modCount;
```

###### 4、put 方法

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
```

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 如果数组(哈希表)为null或者长度为0，则进行数组初始化操作
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 根据key的哈希值计算出数据插入数组的下标位置，公式为(n-1)&hash
    if ((p = tab[i = (n - 1) & hash]) == null)
        // 如果该下标位置还没有元素，则直接创建Node对象，并插入
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // 如果目标位置key已经存在，则直接覆盖
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 如果目标位置key不存在，并且节点为红黑树，则插入红黑树中
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 否则为链表结构，遍历链表，尾部插入
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // 如果链表长度大于等于TREEIFY_THRESHOLD，则考虑转换为红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash); // 转换为红黑树操作，内部还会判断数组长度是否小于MIN_TREEIFY_CAPACITY，如果是的话不转换
                    break;
                }
                // 如果链表中已经存在该key的话，直接覆盖替换
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            // 返回被替换的值
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    // 模数递增
    ++modCount;
    // 当键值对个数大于等于扩容阈值的时候，进行扩容操作
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```



###### 5、get 方法

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```

```java
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    // 判断数组是否为空，数组长度是否小于0，目标索引位置下元素是否为空，是的话直接返回null
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        // 如果目标索引位置元素就是要找的元素，则直接返回
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        // 如果目标索引位置元素的下一个节点不为空
        if ((e = first.next) != null) {
            // 如果类型是红黑树，则从红黑树中查找
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                // 否则就是链表，遍历链表查找目标元素
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```



###### 6、resize 方法

```java
final Node<K,V>[] resize() {
    // 扩容前的数组
    Node<K,V>[] oldTab = table;
    // 扩容前的数组的大小和阈值
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    // 预定义新数组的大小和阈值
    int newCap, newThr = 0;
    if (oldCap > 0) {
        // 超过最大值就不再扩容了
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 扩大容量为当前容量的两倍，但不能超过 MAXIMUM_CAPACITY
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    // 当前数组没有数据，使用初始化的值
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        // 如果初始化的值为 0，则使用默认的初始化容量，默认值为16
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    // 如果新的容量等于 0
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr; 
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    // 开始扩容，将新的容量赋值给 table
    table = newTab;
    // 原数据不为空，将原数据复制到新 table 中
    if (oldTab != null) {
        // 根据容量循环数组，复制非空元素到新 table
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                // 如果链表只有一个，则进行直接赋值
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    // 红黑树相关的操作
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    // 链表复制，JDK 1.8 扩容优化部分
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        // 原索引
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        // 原索引 + oldCap
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 将原索引放到哈希桶中
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    // 将原索引 + oldCap 放到哈希桶中
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

---



#### 7、ArrayList、LinkedList、Vector 的区别



###### 1、ArrayList、Vector 的区别

---



**ArrayList**

1、ArrayList 使用默认的构造器进行创建对象时，会创建一个长度0的 Object[]，当第一次向集合中添加单个元素的时候，会将集合容量扩大到10；

2、ArrayList 每次扩容都是扩大为原来的 1.5 倍；

3、ArrayList 是线程不安全的；

---



**Vector**

1、Vector 使用默认的构造器进行创建对象时，会创建一个初识长度为 10 的 Object[]；

2、Vector 每次扩容都是扩大为原来的 2 倍；

3、Vector 是线程安全的，因为 Vector 向外部提供的方法都加了 synchronized；

---



**共同点**：

1、都继承了AbstractList，都实现了 List 接口；

2、底层都是使用 Object[] 来保存元素

---



###### 2、ArrayList、LinkedList 的区别

1、数据结构不同：ArrayList 使用的是对象数组，而 LinkedList 使用的是双向链表

2、容量大小不同：ArrayList 的容量为 Integer 的最大值，而 LinkedList 没有最大值

3、操作数据的效率不同：

- ArrayList 底层使用的是对象数组，里面的元素在内存中都是连续存储的，并且数组中都为同一类型的元素（指定数组元素类型）支持随机访问（实现了 RandomAccess 接口），所以对读操作的效率很高，但是在进行插入和删除操作时，需要将数组的元素进行移动，所以在修改方面效率低；
- LinkedList 底层使用的是双向链表，每个节点都包含了前驱节点、后继节点还有数据，在进行插入和删除的时候，是通过改变相应的前驱节点和后继节点的指向来实现，所以在修改方面效率高；而在进行获取元素的时候，需要从头节点进行遍历，直到找到要获取的元素的位置为止，所以对元素的获取效率低。

---



#### 8、HashMap、HashSet、HashTable 的区别

---



##### 1、HashMap、HashSet 的区别

1、HashMap 实现了 Map 接口，用来保存一组具有映射关系的 k-v 键值对；HashSet 实现了 Set 接口，用来保存特定的元素，这些元素不会重复，且无序；

2、HashSet 的地城就是使用的 HashMap，比如在向 HashSet 中添加元素时，是调用的 HashMap的put() 方法，key 为要保存的元素，而 value 为一个对象常量，其作用是用来进行占位用的；

3、所以，HashMap 是通过key 来计算 hashCode，而 HashSet 是通过元素来计算 hashCode 的，而对于两个对象而言，hashCode可能相同，所以又会通过 equals() 方法做最终的判断



---



##### 2、HashMap、HashTable 的区别

>**HashMap 采用的数据结构为：数组+链表/红黑树【为了解决哈希冲突，当链表长度大于阈值（默认为8），并且数组长度大于64时将链表转换为红黑树，以减少搜索时间】**



---

**HashMap**：

1. **数据结构不同**：动态数组，每个数组中又维护着一个单向链表或者红黑树
2. **数组类型不同**：Node[] （hash、key、value、next）
3. **初始化容量不同**：16【使用默认构造器时，没有对数组进行初始化 ，当第一次添加的时候，会使用默认初始化容量】
4. **扩容大小不同**：2倍
5. **添加元素方式不同**：
   - 判断 HashMap 数组是否为空，是的话就是初始化数组（由此可见，在创建 HashMap 对象的时候并不会直接初始化数组）
   - 计算 key 在数组中的存放索引
   - 目标索引位置为空的话，直接创建 Node 存储
   - 目标索引位置不为空的话，分下面三种情况：
     - key 相同，覆盖旧值
     - 该节点类型是红黑树，执行红黑树插入操作
     - 该节点类型是链表，遍历到最后一个元素在尾部插入，如果期间遇到 key 相同的，则直接覆盖，如果链表长度大于等于 8，并且数组容量大于等于 64
   - 判断 HashMap 元素个数是否大于等于扩容阈值，是的话，进行扩容操作
6. **是否线程安全**：线程不安全【CurrentHashMap】
7. **空key、value 的支持**：HashMap 中支持 key 为 null，但只能有一个这样的 key；可以有一个或多个键所对应的值为 null
8. **resize**：
   - 获取扩容前的数组、数组的大小和阈值
   - 预定义新数组的大小和阈值
   - 如果原来数组的大小小于 0
     - 超过了最大值就不再扩容了 【Integer 的最大值】
     - 扩大容量为当前容量的两倍
   - 如果当前数组没有元素，使用初始化的值
   - 如果初始化的值为 0，则使用默认的初始化容量，默认值为 16
   - 开始扩容，将原数组变量的应用指向新数组

---

**HashTable**

1. **数据结构不同**：动态数组，每个数组中维护着一个单向链表
2. **数组类型不同**：Entry[] （hash、key、value、next）
3. **初始化容量不同**：11
4. **扩容大小不同**：2n+1
5. **添加元素方式不同**：
   - 根据 key 的 hash 值计算数组的索引位置
   - 如果目标索引位置的元素不为 null 时，对链表进行遍历
     - 判断 key 是否相等（先通过 hash 比较，然后再通过 equals 比较，从而提高比较速度），相等则替换原来的值，并返回新的值
   - 如果该索引位置的元素为 null，又或者遍历完链表后发现没有 key 相等的元素，则执行添加插入操作
     - 判断是否需要进行扩容（判断 count 是否大于或等于扩容阈值【数组容量*加载因子：默认为0.75】）
     - 扩容后，重新计算该 key 在扩容后数组中的下标
     - 采用头插的方式插入，index 位置的节点为新节点的 next 节点，新节点取代 index 位置节点
6. **是否线程安全**：线程安全【synchronized】
7. **空 key、value 的支持**：HashTable 中的 key 和 value 都不能为 null
8. **resize**：
   - 获取原数组、数组大小
   - 设置新容量为原容量的 2n+1 倍
   - 判断新容量是否超过最大容量
     - 如果原容量已经是最大容量的话，就不扩容了
     - 新容量最大值只能是 Integer 的最大值
   - 用新容量创建一个新数组
   - 重新计算下次扩容阈值
   - 将新数组赋值给原数组
   - 遍历数组和链表，进行新数组的赋值
