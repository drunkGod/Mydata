# 一、JAVA基础

## 1.Java知识点

### 1.1 基本/包装类型

•  基本类型和包装类型的区别，涉及自动装箱和拆箱，怎么做的，原理

**区别：**

    1.基本类型存储在栈里，包装类型存储在堆里。因为栈的效率更高，所以保留了基本类型。

    2.包装类型是对象，拥有方法和字段，对象的调用是引用对象的地址。

    3.基本类型是值传递，包装类是引用传递。

    4.向ArrayList，LinkedList中放数据的时候，只能放Object类型的，基本类型放不进去。

​    5.基本类型和包装类型默认值不同，想 int的默认值是0，Integer的默认值为null。

​    6.装箱就是自动将基本数据类型转换为包装器类型，用valueOf()方法实现；

​    7.拆箱就是自动将包装器类型转换为基本数据类型，用xxxValue()方法实现，如intValue()。

​    8.Integer、Short、Byte、Character、Long包装类有缓存机制（cache数组），缓存了[-128, 127]，Double和Float没有.

​    9.整型中，每个类型都有一定的表示范围，但是，在程序中有些计算会导致超出表示范围，即溢出。**溢出的时候并不会抛异常，也没有任何提示。**所以，在程序中，使用同类型的数据进行运算的时候，**一定要注意数据溢出的问题。**大值一定要用BigDecimal来处理。（溢出如：i (2147483647) + j (2147483647) = k (-2)）

​    10.自动拆箱，如果包装类对象为null，那么自动拆箱时就有可能抛出NPE。

```java
Integer total = 99; 
//执行上面那句装箱代码的时候，系统为我们执行了： 
Integer total = Integer.valueOf(99);

int totalprim = total; 
//执行上面那句拆箱代码的时候，系统为我们执行了： 
int totalprim = total.intValue();

//因为缓存机制，所以Integer.valueOf(127)用==比较时返回为true。而new Integer(127)用==比较也是返回false。原理：
public static Integer valueOf(int i) {
    final int offset = 128;
    if (i >= -128 && i <= 127) { // must cache 
        return IntegerCache.cache[i + offset];
    }
    return new Integer(i);
 }

//注意以下代码自动拆装箱会报错，因为当第二，第三位操作数分别为基本类型和对象时，其中的对象就会拆箱为基本类型来操作
Map<String, Boolean> map = new HashMap<>();
Boolean b = map != null ? map.get("test") : false;	//会拆箱：(Boolean)map.get("test").booleanValue()
//改为一下则正常因为不会拆箱了：
Boolean b = (map!=null ? map.get("test") : Boolean.FALSE);
```



### 1.2 String/Builder/Buffer

String是final修饰的不可变类，底层是一个final的char数组。JVM在堆中使用字符串池来存储所有的字符串对象。

StringBuilder是可变字符串类，是线程非安全的，效率相比StringBuffer快一些。

StringBuffer是可变字符串类，是线程安全的，但效率相比StringBuilder慢一些。

String不可变的优点：

- 由于String是不可变类，所以在多线程中使用是安全的，我们不需要做任何其他同步操作。
- String是不可变的，它的值也不能被改变，所以用来存储数据密码很安全。
- 因为java字符串是不可变的，可以在Java运行时节省大量Java堆空间。因为不同的字符串变量可以引用池中的相同的字符串。如果字符串是可变得话，任何一个变量的值改变，就会反射到其他变量，那字符串池也就没有任何意义了。

```
字符串编码的区别：
• GBK的文字编码是双字节来表示的，即不论中、英文字符均使用双字节来表示；
• UTF-8编码则是用以解决国际上字符的一种多字节编码，它对英文使用8位（即一个字节），中文使用24位（三个字节）来编码。
```

**Java字符串常量池**

String有两种赋值方式，

第一种是通过“**字面量**”赋值。

```java
String s1 = "abc"; 
String s2 = "abc"; 
System.out.println(s1==s2);	//true
//字面量结论：采用字面值的方式创建一个字符串时，JVM首先会去字符串池中查找是否存在"abc"这个对象，如果不存在，则在字符串池中创建"abc"这个对象，然后将池中"abc"这个对象的引用地址返回给"abc"对象的引用s1，这样s1会指向池中"abc"这个字符串对象；如果存在，则不创建任何对象，直接将池中"abc"这个对象的地址返回，赋给引用s2。因为s1、s2都是指向同一个字符串池中的"abc"对象，所以结果为true。
```

 第二种是通过**new**关键字声明创建新对象。

```java
String s3 = new String("xyz"); 
String s4 = new String("xyz"); 
System.out.println(s3==s4); //false
//new关键字结论：采用new关键字新建一个字符串对象时，JVM首先在字符串池中查找有没有"xyz"这个字符串对象，如果有，则不在池中再去创建"xyz"这个对象了，直接在堆中创建一个"xyz"字符串对象，然后将堆中的这个"xyz"对象的地址返回赋给引用s3，这样，s3就指向了堆中创建的这个"xyz"字符串对象；如果没有，则首先在字符串池中创建一个"xyz"字符串对象，然后再在堆中创建一个"xyz"字符串对象，然后将堆中这个"xyz"字符串对象的地址返回赋给s3引用，这样，s3指向了堆中创建的这个"xyz"字符串对象。s4则指向了堆中创建的另一个"xyz"字符串对象。s3 、s4是两个指向不同对象的引用，结果当然是false。
```

**Java字符串的intern()方法**

对于**intern()**方法，如果字符串常量池里存在一个和当前字符串对象等价的字符串对象，那么返回字符串常量池里那个对象。如果不存在，把当前字符串对象存进常量池，返回当前字符串对象。使用场景如下：

```java
String test1 = "abc";
String test2 = new String("abc");
String str = test2.intern();
//字符串引用str，如果直接用str=test2，那么str将指向堆中的字符串对象，而如果使用上面的str=test2.intern()，因为test1已经在常量池创建了"abc"对象，所以intern()后会直接返回常量池里的"abc"对象。此时，堆中的字符串对象"abc"在没有其他引用的情况下，可以进行回收，这是intern()真正的好处.
```

另外，面试时可能经常会有一些比较字符串是否==的题目出现，要理解这些问题还要掌握以下两点：。

- 对于两个字符串常量“a”+"b"直接做+运算的，不会把这两个字符串放入常量池中，而是直接把运算后的结果"ab"放入。
- 对于new String("a")+"b"这种用了声明的，会把"a"，“b”都放入常量池，结果“ab”不会直接放入。

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = "Hel" + "lo";
String s4 = "Hel" + new String("lo");
String s5 = new String("Hello");
String s6 = s5.intern();
String s7 = "H";
String s8 = "ello";
String s9 = s7 + s8;
//-------结论--------//
System.out.println(s1 == s2);  // true，因为指向的是同一个地址。s1在创建对象的同时在字符串池中也创建了其引用。
System.out.println(s1 == s3);  // true，JVM优化，直接存结果，也是同一个地址
System.out.println(s1 == s4);  // false，不会优化，指向的不是同一个地址。在编译期无法知道"lo"的地址
System.out.println(s1 == s9);  // false，s7+s8都是变量，不会优化，指向的不是同一个地址。
System.out.println(s4 == s5);  // false，指向的不是同一个地址
System.out.println(s1 == s6);  // true，intern()时会返回已经存在的Hello的地址，所以指向同一个。
```



### 1.3 值传递/引用传递

**值传递/引用传递概念**

- **值传递**

    在方法的调用过程中，实参把它的实际值传递给形参，此传递过程就是将实参的值复制一份传递到函数中，这样如果在函数中对该值（形参的值）进行了操作将不会影响实参的值。因为是直接复制，所以这种方式在传递大量数据时，运行效率会特别低下。

```
基本类型，String是值传递
```

- **引用传递**

    引用传递弥补了值传递的不足，如果传递的数据量很大，直接复过去的话，会占用大量的内存空间，而引用传递就是将对象的地址值传递过去，函数接收的是原始值的首地址值。在方法的执行过程中，形参和实参的内容相同，指向同一块内存地址，也就是说操作的其实都是源数据，所以方法的执行将会影响到实际对象。



**Java只有值传递**

**Java 语言的参数传递只有「按值传递」。**按值传递的精髓是：传递的是存储单元中的内容，而不是存储单元的引用！当一个实例对象作为参数被传递到方法中时，参数的值就是该对象的引用的一个副本。指向同一个对象，对象的内容可以在被调用的方法内改变，但对象的引用(不是引用的副本) 是永远不会改变的。

即Java 的参数传递，不管是基本数据类型还是引用类型的参数，**都是按值传递，没有按引用传递！**

```java
public static void main(String[] args) {
	Object o = new Object();
	System.out.println(o);	//java.lang.Object@1b6d3586
	change(o);	//方法中将o指向另一个对象java.lang.Object@4554617c
	System.out.println(o); //因为是值传递，输出还是java.lang.Object@1b6d3586
}

private static void change(Object o) {
	o = new Object();	//将形参指向java.lang.Object@4554617c
}
```



### 1.4 HashMap 

•  源码级掌握，底层结构，扩容，红黑树，hash冲突

- HashMap存储的是键值对，底层是：数组+链表+红黑树。

- 最重要的是put方法和get方法，put方法主要就是根据key的hashCode和数组长度定位到数组的某个位置。（键值都可以为空，null键是放在数组的首位），如果数组为没有进行初始化，则调用resize()方法进行初始化操作。如果没碰撞就直接存储到那个位置；如果碰撞了，就看元素是否已经存在，如果存在的话就覆盖原来的值并返回旧值；如果元素不存在，就插入到链表或者红黑树，并且如果链表过长的话还会转为红黑树；当插入成功后，如果当前键值对数量超过阈值（最大容量*负载因子），就进行扩容；扩容就是创建一个新的数组，长度为之前的2倍，并把旧数组中的数据迁移到新数组中。迁移过程中，原来table的每个位置的元素，在新的table中，他们要么待在原来的位置，要么移动2的指数的偏移，这也导致了Hashmap不能保证插入数据的顺序性。总结为5步就是：

  1、判断数组是否初始化，没有则进行初始化操作（resize）；

  2、通过hash定位数组的索引坐标；

  3、如果该位置上还是空的，就直接插入那个位置；

  4、如果该位置已经有值了，意味着哈希冲突，就看元素是否已经存在，如果存在的话就用新值覆盖原来的值并返回旧值；如果元素不存在，就插入到链表或者红黑树，并且如果链表过长的话还会转为红黑树；

  5、如果添加成功，就判断是否需要进行扩容。

  **（即1.初始化->2.定位->3.非冲突插入->4.冲突插入->5.扩容）**


- HashMap线程不安全，如两个线程同时插入同一个位置，则会覆盖数据，导致数据丢失。（JDK1.7还可能死循环）
- HashMap要变为线程安全，可以用Collections工具类：Map m = Collections.synchronizedMap(map); 或者用ConcurrentHashMap。并发环境下，ConcurrentHashMap 效率较Collections.synchronizedMap(map)更高。

```
1.确定位置：(hash = key.hashCode()) ^ (hash >>> 16)，索引位置i：tab[i = (n - 1) & hash]；因为hash(key)的值是随机的，无法确定其范围，通过&操作，相当于对hash表的长度取模，能够在保证数据随机均匀的分布在hash表中。
2.JDK1.7是每个元素都重新hash，1.8后不是在原位就是进行2的指数的偏移，定位结点的hash算法简化会带来弊端,Hash冲突加剧，所以有链表树化的操作。转为红黑树，效率能从O(n)提高到O(logn)，用红黑树是因为它添加、删除和查找表现相对较好。
3.默认参数：数组长度16，链表转树8，树转链表6，扩容倍数2，负载因子0.75，最大容量2的31次方，最小树形化阈值64（当哈希表中的容量 > 64时，才允许树形化链表，如果<64，则是先扩容）
4.synchronizedMap加锁：
  final Object mutex;	// 默认this.mutex = this; 或者this.mutex = var2(参数传入);
  public V put(K key, V value) {
      synchronized (mutex) {return m.put(key, value);}
  }
```

**HashMap#put()源码：**

```java
//1.根据key计算它的hash值，用于得到一个在数组的索引。
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
	Node<K,V>[] tab; Node<K,V> p; int n, i;
    //2.如果数组为空，则调用resize()进行初始化
	if ((tab = table) == null || (n = tab.length) == 0)
		n = (tab = resize()).length;
    //3.如果没有哈希碰撞，就直接插入。
	if ((p = tab[i = (n - 1) & hash]) == null)
		tab[i] = newNode(hash, key, value, null);
    //4.如果哈希冲突，
	else {
        //就看元素是否已经存在，如果存在的话就覆盖原来的值并返回旧值；
        //如果元素不存在，就插入到链表或者红黑树，并且如果链表过长的话还会转为红黑树；
        ...
	}
	++modCount;（记录修改次数，用于在forEach中抛出 ConcurrentModificationException）
    //5.实际长度+1，判断是否大于临界值，大于则扩容
	if (++size > threshold)
		resize();
	afterNodeInsertion(evict);（LinkedHashMap用，删除近期最少使用的节点）
	return null;
}
```

另外：

**- LinkHashMap是有序的HashMap**，能够严格按照输入的顺序迭代，且迭代效率高于HashMap。LinkedHashMap采用的hash算法和HashMap相同，但是它重新定义了数组中保存的元素Entry，该Entry除了保存当前对象的引用外，还保存了其上一个元素before和下一个元素after的引用，从而在哈希表的基础上又构成了双向链接列表。这样就能按照插入的顺序遍历原本无序的HashMap了。

```java
LinkedHashMap有两个Entry<K, V>对象head，tail，然后每个Entry又有Entry<K,V> before, after。可以顺序或逆序遍历。
```

**- TreeMap根据其自然顺序**对其所有条目进行升序排序。还提供了firstKey()/lastKey()/subMap()/tailMap()等实用方法，然后也可以传入自己的comparator类进行自定义比较。

```
Comparable可以看做是内部比较器，Comparator可以看做是外部比较器。对于实现了Comparable的类，直接用Arrays.sort()方法可以实现排序，如果没有实现Comparable接口，也可以用自己的比较器来实现:Arrays.sort(users, new MyComparator());同时存在时，自定义的Comparator的排序优先级高。
```

### 1.5 ConcurrentHashMap

•  段锁，如何分段，和hashmap在hash上的区别，性能，等等

- ConcurrentHashMap线程安全，键、值都不允许为空，为空则抛出NPE异常。

- JDK1.7 采用分段锁技术，整个 Hash 表被分成多个段Segment，每个段中会对应一个 Segment 段锁，段与段之间可以并发访问，但是多线程想要操作同一个段是需要获取锁的。所有的 put，get，remove 等方法都是根据键的 hash 值对应到相应的段中，然后尝试获取锁进行访问。即ConcurrentHashMap定位一个元素的过程需要进行两次Hash操作。第一次Hash定位到Segment，第二次Hash定位到元素所在的链表的头部。

- JDK1.8 取消了基于 Segment 的分段锁思想，改用 CAS + synchronized 控制并发操作。底层实现跟1.8 版本的 HashMap一样 ，使用数组+链表+红黑树进行数据存储。

- ConcurrentHashMap的put方法过程很清晰，对当前的table进行**无条件自循环直到put成功**，可以分成以下六步流程来概述：

  1、判断数组是否初始化，没有则**进行初始化操作**（initTable）；

  2、通过**hash定位数组的索引坐标**，是否有Node节点，如果没有则使用CAS进行添加（链表的头节点），添加失败则进入下次循环。

  3、检查到内部正在扩容，就帮助它一块扩容。

  4、如果哈希冲突了，则**使用synchronized锁住**f元素（链表/红黑树的头元素）。如果是Node（链表结构）则执行链表的添加操作；如果是TreeNode（树型结构）则执行树添加操作。

  5、判断链表长度已经达到临界值8（默认值），当节点超过这个值就需要**把链表转换为树结构**。（转换时也会加锁）

  6、如果添加成功就调用addCount() 方法统计size，并且检查是否需要扩容

（即：1.初始化->2.定位->3.非冲突CAS插入->4.帮助扩容->5.冲突synchronized插入->6.扩容）

- get源码中也没有加锁操作。使用tabAt(tab, (n - 1) & h))，确保多线程可见，并且保证获取到是内存中最新的table[i] 元素值。
- 多线程复合操作时是否能保证线程安全？答案是不能，原因： ConcurrentHashMap 使用锁分离(jdk7)/cas(jdk8)方式保证并发环境下，添加/删除操作安全，但这进针对的是单个put 或者 remove方法，如果多个方法配合复合使用，依然需要额外加锁。



**ConcurrentHashMap#put()源码：**

```java
/** Implementation for put and putIfAbsent */
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    //1.计算key的hash值
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        //2.如果当前table还没有初始化先调用initTable方法将tab进行初始化
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        //3.tab中索引为i的位置的元素为null，则直接使用CAS将值插入即可
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null,new Node<K,V>(hash, key, value, null)))
                break;
        }
        //4.如果当前正在扩容，则帮助扩容
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        //5.如果哈希冲突了，锁住元素头部
        else {
            V oldVal = null;
            synchronized (f) {
                //看元素是否已经存在，如果存在的话就覆盖原来的值并返回旧值；
        		//如果元素不存在，就插入到链表或者红黑树，并且如果链表过长的话还会转为红黑树；
            }
        }
    }
    //6.对当前容量大小进行检查，如果超过了临界值（实际大小*加载因子）就需要扩容 
    addCount(1L, binCount);
    return null;
}
```

ConcurrentHashmap重要方法、参数：

```
1、spread(key.hashcode)：(h ^ (h >>> 16)) & HASH_BITS，比HashMap的hash()多了一步& HASH_BITS，HASH_BITS是0x7fffffff，该步是为了消除最高位上的负符号 hash的负在ConcurrentHashMap中有特殊意义表示在扩容或者是树节点。
2、initTable()：用于里面table数组的初始化，值得一提的是table初始化是没有加锁的，当要初始化时会通过CAS操作将sizeCtl置为-1，而sizeCtl由volatile修饰，保证修改对后面线程可见。这之后如果再有线程执行到此方法时检测到sizeCtl为负数，说明已经有线程在给扩容了，这个线程就会调用Thread.yield()让出一次CPU执行时间。
3、casTabAt()：底层使用Unsafe.compareAndSwapObject 原子操作table[i]位置，如果为null，则添加新建的node节点，跳出循环，反之，再循环进入执行添加操作。
```



### 1.6 CAS

**CAS概念**

​    CAS，全称Compare And Swap（比较与交换），是解决多线程并行情况下使用锁造成性能损耗的一种机制。传统的synchronized是悲观锁，而CAS是乐观锁。 CAS通过调用JNI的代码实现的，如compareAndSwapInt就是借助C来调用CPU底层指令实现的。

​    CAS的实现思想 有三个重要参数（V, A, B），V为内存地址、A为预期原值，B为新值。如果内存地址的值与预期原值相匹配，那么将该位置值更新为新值。否则，说明已经被其他线程更新，处理器不做任何操作。我们可以循环CAS读取变量A，循环中尝试修改或放弃操作。

```
Java中的CAS操作都是通过sun包下Unsafe类实现，而Unsafe类中的方法都是native方法。
使用CAS可分为以下三步：
首先，声明共享变量为volatile，保证线程间的数据是可见的；
然后，使用CAS的原子条件更新来实现线程之间的同步；（如 ++i的核心：compareAndSet(current, current+1)）
同时，配合以volatile的读/写和CAS所具有的volatile读和写的内存语义来实现线程之间的通信。
```

**CAS几个缺点**：

- **ABA问题**。因为CAS需要在操作值的时候检查下值有没有发生变化，如果没有发生变化则更新，但是如果一个值原来是A，变成了B，又变成了A，那么使用CAS进行检查时会发现它的值没有发生变化，但是实际上却变化了。ABA问题解决思路就是使用版本号。在变量前面追加上版本号，每次变量更新的时候把版本号加一，那么A-B-A 就会变成1A-2B-3A。


- **循环时间长开销大**。自旋CAS如果长时间不成功，会给CPU带来非常大的执行开销。


- **只能保证一个共享变量的原子操作**。当对一个共享变量执行操作时，我们可以使用循环CAS的方式来保证原子操作，但是对多个共享变量操作时，循环CAS就无法保证操作的原子性。这个问题的解决方法是可以用锁，或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。比如有两个共享变量i＝2,j=a，合并一下ij=2a，然后用CAS来操作ij。从JDK1.5开始提供了AtomicReference类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行CAS操作。



```java
public class ReentrantLock implements Lock, java.io.Serializable {
	private final Sync sync;
    abstract static class Sync extends AbstractQueuedSynchronizer { ... }
}
```



**资源的共享方式分为2种：**

- 独占式(Exclusive)

只有单个线程能够成功获取资源并执行，如ReentrantLock。

- 共享式(Shared)

多个线程可成功获取资源并执行，如Semaphore/CountDownLatch等。

AQS将大部分的同步逻辑均已经实现好，继承的自定义同步器只需要实现state的获取(acquire)和释放(release)的逻辑代码就可以，主要包括下面方法：

- tryAcquire(int)：独占方式。尝试获取资源，成功则返回true，失败则返回false。
- tryRelease(int)：独占方式。尝试释放资源，成功则返回true，失败则返回false。
- tryAcquireShared(int)：共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
- tryReleaseShared(int)：共享方式。尝试释放资源，如果释放后允许唤醒后续等待结点返回true，否则返回false。
- isHeldExclusively()：该线程是否正在独占资源。只有用到condition才需要去实现它。





### 1.7 HashTable

- HashTable是线程安全的，**它的大部分方法如put()、get()都加了synchronized锁，保证方法只被一个线程执行**。当Hashtable的大小增加到一定的时候，性能会急剧下降，因为迭代时需要被锁定很长的时间，建议使用CcHashMap。
- HashTable初始容量为11，负载因子为0.75f，扩容时是2n+1，计算index的方法也和HashMap不同。
- HashTable键、值都**不允许为空**，为空则抛出NPE异常。
- 1.8版本后，HashTable也没有引入红黑树。也就是说扩容是每个元素重新计算其哈希值以获取在新数组中索引下标。

```
1、HashTable取2n+1是因为hash函数用的是标准的求模函数：(hash & 0x7FFFFFFF) % tab.length)，而HashMap是位运算。
2、public synchronized V put(K key, V value) { ... }
3、public synchronized V get(Object key) { ... }
```



### 1.8 ArrayList/LinkedList

•  ArrayList 优势，扩容，什么时候用

**ArrayList**

- 底层是一个数组Object[]，默认容量是10。因为是数组，读取的速度很快。


```java
ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    transient Object[] elementData;	//元素数组
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};	//直接新建时 elementData = {}
    public boolean add(E e) {
	    ensureCapacityInternal(size + 1);  // 保证容量可用。
	    elementData[size++] = e;
	    return true;	//添加成功返回true
    }
}
//RandomAccess 是一个标志接口，表明实现这个这个接口的 List 集合是支持【快速随机访问】策略的。同时，官网还特意说明了，如果是实现了这个接口的 List，那么使用for循环的方式获取数据会优于用迭代器获取数据。
```

- 当数据满时，它会扩容，容量增加0.5倍，扩容调用的是Arrays.copy()方法。


```java
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {	// 如果是新建后的第一次，初始化容量为10。
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);	//DEFAULT_CAPACITY = 10
    }
  	// 容量不足时：-> grow(minCapacity) -> elementData = Arrays.copyOf(elementData, newCapacity);
    ensureExplicitCapacity(minCapacity); 
}
```

- List非线程安全，线程安全的是Vector，或者用Collectionutils.synchronized(list); 可以将list变为线程安全。
- 遍历删除时，会报错，所以可以倒序删除。或者使用迭代器删除。


```java
public E remove(int index) {
	rangeCheck(index);
	modCount++;
	E oldValue = elementData(index);
	int numMoved = size - index - 1;
	if (numMoved > 0)
		System.arraycopy(elementData, index+1, elementData, index, numMoved);
	elementData[--size] = null; // clear to let GC do its work
	return oldValue;
}
```

```
用Arrays.asList(new Integer[]{1, 2, 3}); 可以快速得到一个List，但这个List是Arrays自己的内部类ArrayList，没有实现AbstractList的很多方法，所以Arrays.asList得到的list不支持add/remove；且注意参数不要写成(new int[]{1,2,3})
```

**LinkedList**

- LinkedList的底层是一个链表，所以增删的速度很快。同样的，读取的速度就较低了。

```java
LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, Serializable {
    transient Node<E> first;
    transient Node<E> last;
    ...
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));	//node(index)拿到插入位置处的元素
    }
   
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }
  
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        final Node<E> pred = succ.prev;	//succ为原位置元素，pred为原元素的前元素
        final Node<E> newNode = new Node<>(pred, e, succ);	//新元素的前元素=原位置前元素，后元素=原位置元素
        succ.prev = newNode;  //原位置元素的前元素 = 新元素
        if (pred == null)
            first = newNode;  //原位置前元素的后元素=新元素
        else
            pred.next = newNode;  //原位置前元素的后元素=新元素
        size++;
        modCount++;
    }
}
```

- LinkedList同时实现了Queue接口，所以可以当做队列来用，如用getFirst()/getLast()/peek()/poll()等方法。

**ArrayList/LinkedList对比**

查询较多用ArrayList，增删较多，且特别是中间插入用LinkedList。



### 1.9 继承/多态/泛型

• 继承、多态的概念、原理是什么，感觉这个很容易被问到

**继承的概念**

子类通过extends关键字继承父类，获得父类的属性和方法。如果子类修改了父类的方法，则称之为重写。

```
重写(overwrite)是子类对父类的允许访问的方法的实现过程进行重新编写, 返回值和形参都不改变。需要调用父类时用super.
重载(overloading) 是在一个类里面，方法名字相同，而参数不同。返回类型可以相同也可以不同。
覆写(override)跟重写一样，不过重写是对父类，而覆写是对父接口的方法实现。
```

**继承时执行顺序**

- 父类静态代码块
- 子类静态代码块
- 父类普通代码块
- 父类构造方法
- 子类普通代码块
- 子类构造方法

```
注意，普通代码块在调用无参构造方法/有参构造方法之前都会执行一次。静态代码块只会在加载类的时候执行一次。
```

**访问权限：**

|  访问权限\范围  |  本类  |  同包  | 不同包子类 | 不同包非子类 |
| :-------: | :--: | :--: | :---: | :----: |
|  public   |  √   |  √   |   √   |   √    |
| protected |  √   |  √   |   √   |   x    |
|  default  |  √   |  √   |   x   |   x    |
|  private  |  √   |  x   |   x   |   x    |

​		

**多态的概念**

- 多态就是同一个接口，使用不同的实例而执行不同操作。

**多态的必要条件**

- 继承
- 重写
- 父类引用指向子类对象



**泛型的感念**

泛型，顾名思义，就是**将具体的类型参数化，然后在使用时才传入具体的类型，比如用参数<T>来表示。**

**Java中的泛型，只在编译阶段有效。**在编译过程中，正确检验泛型结果后，会将泛型的相关信息带出，泛型信息不会进入到运行时阶段。

**泛型的使用方式**

泛型有三种使用方式，分别为：泛型类、泛型接口、泛型方法

泛型类：

```java
public class Generic<T>{ ... }
```

泛型接口：

```java
public interface Generator<T> {
    public T next();
}
```

泛型方法：

```java
//普通泛型方法
public <T> String genericMethod(T t) {
  return t.getClass().getName();
}
//静态泛型方法
public static <T> String genericMethod(T t) {
  return t.getClass().getName();
}
```



### 1.10 接口/抽象类

• 接口和抽象类

首先，抽象类和接口都不完整，就是都不能被实例化，他们只有被子类继续或者实现才有意义。

区别是：

- 抽象类可以有构造方法，接口没有构造方法。抽象类除了能有抽象方法外，其他的跟普通的类一样。
- 抽象类中有普通变量和静态变量，接口中只有public static  final 修饰的常量。
- 抽象类中可以有普通方法，抽象方法有没有都行，接口中没有普通方法，只有抽象方法。
- 抽象类中的抽象方法的访问类型修饰符可以是public 或 protected 的，接口中的抽象方法只能是默认的public abstract ，而且接口中public abstract 还可以省略。
- 抽象类只能单继承，接口可以多继承

什么时候该用抽象类而不用接口？

- 如果你的接口中有很多方法，你对它们的实现又比较清晰，可以考虑提供一个抽象类作为默认实现
- 如果你考虑描述一种行为，比如会飞的会跑的，就定义成接口
- 如果你有很多可以默认的实现，并且又需要其他特殊的实现，可以定义为抽象类



### 1.11 反射/注解  

**反射：**

一般是通过Class.forName()方法拿到class对象；

通过class对象的new Instance方法构造出一个反射的对象；

通过class对象的cls.getMethods()、cls.getDeclaredMethods()可以获取该类对应的方法；

通过class对象的cls.getFields()、cls.getDeclaredFields()可以获取该类对应的属性；

通过class对象属性或方法的cls.getDeclaredAnnotations()可以获取上面注解；

```java
Class<?> aClass = Class.forName("com.lcl.edemo.utils.MyReflect");
MyReflect refObj = (MyReflect)aClass.newInstance();
Field selfDefPro = aClass.getDeclaredField("selfDefPro");
selfDefPro.setAccessible(true);
selfDefPro.set(refObj, "newRefName");
Method selfDefMethod = aClass.getDeclaredMethod("selfDefMethod", String.class);
String callRefMethodResult = (String) selfDefMethod.invoke(refObj, "哈哈哈");
System.out.println(callRefMethodResult);

//final属性的反射
public static final String FINAL_NAME;
static {
    FINAL_NAME = "old Value";
}
//如上，如果直接对final属性赋值，则反射无法修改；如果是在static块中赋值的话，则反射可以修改；
Field field = TitleConstant.class.getDeclaredField("FINAL_NAME");
field.setAccessible(true);
// 去除final修饰符
Field modifiers_field = Field.class.getDeclaredField("modifiers");
modifiers_field.setAccessible(true);
modifiers_field.set(field, field.getModifiers() & ~Modifier.FINAL);
field.set(this, "new Value");
```

**注解：**

使用@interface可以生成自己的注解。注解没有绑定功能的话没有任何意义，所以注解一般是跟切面来配合使用，已完成特定功能。

注解+切面使用步骤：

1. 先定义自己的注解

2. 定义自己的切面Aspect，并在该切面中使用@Pointcut注解，指定自定义注解的位置。

3. 然后就可以@Before、@After进行一些逻辑处理。


```java
public @interface MyAnnotation{..}
@Aspect
public class MyAspect { 
  @Pointcut("...MyAnnotation")
  public void myAspect(){}
  @Before("myAspect()")
  public void dobeforeMyAspect() {...}
}
```



### 1.12 多线程

**进程和线程**

- **进程** - 进程是具有一定独立功能的程序（例如QQ.exe），关于某个数据集合上的一次运行活动，进程是系统进行资源分配和调度的一个独立单位。
- **线程** - 线程是进程的一个实体，是CPU调度和分派的基本单位，它是比进程更小的能独立运行的基本单位；线程自己基本上不拥有系统资源，只拥有一点在运行中必不可少的资源(如程序计数器，一组寄存器和栈)，但是它可共享进程所拥有的全部资源。

​        相对进程而言，线程是一个更加接近于执行体的概念，它可以与同进程中的其他线程共享数据，但拥有自己的栈空间，拥有独立的执行序列。

**多线程使用场景**

- 耗时的计算；

- 耗时的IO；

- 需要异步解耦。

**多线程状态/生命周期**

线程有创建，可运行，运行中，阻塞，消亡五种状态。

新建： 继承Thread | 实现Runnable()接口| 实现Callable接口 | ExecutorService

可运行： 新建的线程调用start() | 运行中的线程时间片用完 | 运行中的线程调用yield() | 从阻塞或锁池状态恢复

运行： 操作系统调度选中

阻塞： 等待用户输入 | sleep() | join()

消亡： run() | main() 方法运行结束。

```
Runnable()和Callable接口区别：Callable的run()有返回值，并可以抛出异常。
Callable的返回值是一个FutureTask对象，有isDone()/cancel()/get()/get(time, unit)等实用的方法。
```

```java
public class ThreadTest {

    public static void main(String[] args) throws Exception {
        System.out.println("主线程" + Thread.currentThread().getName() + "开始运行。。");
        //方式1：继承Thread类实现多线程
        Thread th1 = new MyThreadOne();
        th1.start();
        //方式2：实现Runable接口实现多线程
        Thread th2 = new Thread(new MyThreadTwo());
        th2.start();
        //方式3：实现Callbble接口实现多线程
        //3.1 使用FutureTask类作为中间类连接Callable与Thread
        FutureTask<String> result = new FutureTask<String>(new MyThreadThree());
        Thread th3 = new Thread(result);
        th3.start();
        //3.2 使用FutureTask类的get()等待线程执行结束，接收线程运算后的结果，或使用cancel方法取消等待
        Thread.sleep(5000);
        if (!result.isDone()) {
            System.out.println("一段时间后" + th3.getName() + "仍未done,我就cancel");
            result.cancel(true);
        }
        if (!result.isCancelled()) {
            //如果未取消，我则阻塞等待线程执行结果,另get(time,unit)超时会抛出TimeoutException
            String sum = result.get();    
            System.out.println(sum);
        }
        System.out.println("主线程" + Thread.currentThread().getName() + "结束运行。。");
    }
}

class MyThreadOne extends Thread {
    @Override
    public void run() {
        System.out.println("--多线程" + Thread.currentThread().getName() + "开始运行。。");
        System.out.println("--多线程" + Thread.currentThread().getName() + "结束运行。。");
    }
}

class MyThreadTwo implements Runnable {
    public void run() {
        System.out.println("--多线程" + Thread.currentThread().getName() + "开始运行。。");
        System.out.println("--多线程" + Thread.currentThread().getName() + "结束运行。。");
    }
}

class MyThreadThree implements Callable {

    public Object call() throws Exception {
        System.out.println("--多线程" + Thread.currentThread().getName() + "开始运行。。");
        Thread.sleep(3000);
        System.out.println("--多线程" + Thread.currentThread().getName() + "结束运行。。");

        return "--多线程" + Thread.currentThread().getName() + "结束时间 " + new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}
```




### 1.13 Java内存模型

• 内存模型以及分区，需要详细到每个区放什么。

**JMM概念**

​    Java内存模型（JMM）就是一组规则，它规定了多个线程之间的通信方式与通信细节。

​    首先，JMM规定了

1. 所有的变量都存储在主内存中，每个线程都有自己的工作内存。（主内存=堆，每个线程=栈）
2. 线程需要使用到的变量，都会从主内存拷贝一份副本到自己的工作内存中。
3. 线程对变量的读写操作都是在自己的工作内存中进行，而不能直接读写主内存中的变量。
4. 不同线程之间无法直接访问对方工作内存中的变量。
5. 线程间变量值的传递需要通过主内存来完成。

（总结就是：分主存/工作内存；线程从主存拷变量到工存操作，线程间变量不能直接访问，是通过主存传递）

![img](https://img-blog.csdnimg.cn/20191029105807363.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0hlbGxvV29ybGRfSW5fSmF2YQ==,size_16,color_FFFFFF,t_70)



**Happens-Before概念**

​       JMM是围绕着并发编程中的**原子性、可见性、有序性**这三个特征来建立的。JMM使用Happens-Before的概念来阐述操作之间的内存可见性。happens-before就是先行发生原则：**如果一个操作先发生于第二个操作，则第一个操作对第二个操作是可见的，并且一定发生在第二个操作之前。**

先行发生原则的几个重要原则如下：

- 线程内部规则：在同一个线程内，前面操作的执行结果对后面的操作是可见的。

- 同步规则：如果一个操作x与另一个操作y在同步代码块/同步方法中，那么操作x的执行结果对操作y可见。
- 传递规则：如果操作x的执行结果对操作y可见，操作y的执行结果对操作z可见，则操作x的执行结果对操作z可见。
- 对象锁规则：如果线程1解锁了对象锁a，接着线程2锁定了a，那么，线程1解锁a之前的写操作的执行结果都对线程2可见。

- **volatile变量规则：如果线程1写入了volatile变量v，接着线程2读取了v，那么，线程1写入v及之前的写操作的执行结果都对线程2可见。**
- 线程start原则：如果线程t在start()之前进行了一系列操作，接着进行了start()操作，那么线程t在start()之前的所有操作的执行结果对start()之后的所有操作都是可见的。
- 线程join规则：线程t1写入的所有变量，在任意其它线程t2调用t1.join()成功返回后，都对t2可见。


**线程间通信**

​    线程间通信是有两种机制，一个是共享内存，一个是消息传递。

​    **共享内存**是一种隐式的通信方式，就是JMM模型定义的那样，线程之间共享一些公共状态，线程通过读或写等操作来影响这些公共状态，实现与其它线程的通信。

​    **消息传递**则是一种显示的通信方式，就是我们通过wait()、notify()/notifyAll()这种显示的调用的方式发送消息，实现通信。这种显示的通信方式除了wait()、notify()/notifyAll()外，还有thread.join()，CountdownLatch，CyclicBarrier，FutureTask/Callable，condition.await() / condition.signal()等等。目的就是为了更直观更方便地控制线程执行的顺序，达到我们需要的效果。

**注：join()/yield()可以实现一定的执行顺序，而static + synchronized + wait() + notify()可实现线程任意顺序**

```java
//一般说的synchronized用来做多线程同步功能，其实synchronized只是提供多线程互斥，而对象的wait()和notify()方法才提供了线程的同步功能。Jdk1.8 HotSpot把notify()实现为公平的方式（先睡者先唤醒）而非随机。
public class ThreadTest {
    private static int ThreadOrder = 0;    //控制执行顺序，防止start()因重排序而乱序
    private static final Object lock = new Object();	//锁对象
 	
    //private byte[] o = new byte[0]; //一种性能更好的锁写法。说明：零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：生成零长度的byte[]对象只需3条操作码，而Object lock = new Object()则需要7行操作码。
  
    public static void main(String[] args) throws Exception {
        System.out.println("static + synchronized + wait() + notify()实现线程轮流执行：");
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    synchronized (lock) {
                        for (int i = 1; i <= 3; i++) {
                            System.out.println("线程t1获得锁，t1：" + i);
                            Thread.sleep(1000);
                            if (i == 1) {
                                ThreadOrder = 1;
                                lock.wait();    //T1输出1后，T1睡去，让锁给T2
                            }
                            if (i == 2) {
                                lock.notify();  //T1输出2时，T2、T3仍在沉睡，此时T1唤醒T2
                                lock.wait();    //T1唤醒T2后睡去，T2将会获得锁。
                            }
                            if (i == 3) {
                                lock.notify();   //T1输出3时，T2、T3仍在沉睡，T1唤醒T2后结束
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    try {
                        for (int i = 1; i <= 3; i++) {
                            System.out.println("线程t2获得锁，t2：" + i);
                            Thread.sleep(1000);
                            if (i == 1) {
                                ThreadOrder = 2;
                                lock.wait();    //T2输出1后，T2睡去，让锁给T3
                            }
                            if (i == 2) {
                                lock.notify();   //T2输出2时，T1、T3仍在沉睡，T2唤醒T3
                                lock.wait();    //T2唤醒T3后睡去，T3将会获得锁。
                            }
                            if (i == 3) {
                                lock.notify();   //T2输出3时，T3仍在沉睡，T2唤醒T3，T2线程结束
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    try {
                        for (int i = 1; i <= 3; i++) {
                            System.out.println("线程t3获得锁，t3：" + i);
                            Thread.sleep(1000);
                            if (i == 1) {
                                ThreadOrder = 3;
                                lock.notify();  //T3输出1后，唤醒T1
                                lock.wait();    //此时T2、T3睡去，T1会得到锁
                            }
                            if (i == 2) {
                                lock.notify();   //T3输出2时，T1、T2仍在沉睡，T3唤醒T1
                                lock.wait();    //T3唤醒T1后睡去，T1将会获得锁。
                            }
                            if (i == 3) {
                                //T3输出3时，T3结束，三个线程执行结束
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();

        while (ThreadOrder != 1) {
            Thread.sleep(500);
        }
        t2.start();

        while (ThreadOrder != 2) {
            Thread.sleep(500);
        }
        t3.start();
    }

}
/**结果：
static + synchronized + wait() + notify()实现三个线程轮流执行：
线程t1获得锁，t1：1
线程t2获得锁，t2：1
线程t3获得锁，t3：1
线程t1获得锁，t1：2
线程t2获得锁，t2：2
线程t3获得锁，t3：2
线程t1获得锁，t1：3
线程t2获得锁，t2：3
线程t3获得锁，t3：3
**/
```



### 1.14 synchronized 

•	synchronized 的实现原理以及锁优化？

​	在JMM模型里面，我们知道多个线程会共享同一个主内存上的数据，因为多个线程都能够对主内存上的数据进行读或写操作，所以就会存在一个数据如何同步的问题。因为同时读数据不会产生冲突，所以要解决同步问题，实际上就是要解决多个线程之间同时写数据会冲突的问题。对于这个问题，java就提供了这个Synchronized关键字，synchronized就可以保证在并发情况下，同一时刻只有一个线程执行某个方法或某段代码。只有一个线程执行写操作的话，自然就没有冲突的问题了。

​	synchronized的用法也很简单，它可以修饰普通方法、静态方法和代码块。

​	当一个线程想调用synchronized修饰的方法或代码块时，它必须要获取对应的锁才能够执行。比如多个线程同时调用一个同步方法，那么这多个线程就会去抢调用这个同步方法的实例对象对应的锁，抢到这个锁的线程就可以执行这个方法，没抢到的线程就会进入一个同步队列，等到抢到锁的线程释放锁（执行结束|出现异常|yeid()|wait()等）后，同步队列中的线程就继续争抢对象锁。如此循环，这就保证了同一时刻只有一个线程在执行这段代码。也是通过这种方式实现同步和线程安全，也保证了线程之间的可见性和原子性。

```java
对于synchronized要明白两个问题，①锁的对象是谁；②谁持有了锁。只有竞争相同的锁对象时才会进行排队，否则不影响。

问题1：一个类里定义两个synchronized方法，起两个线程，同一个锁对象，a线程访问1方法，b线程访问2方法会怎么样？
答：同一个锁对象时，如果a线程先获得锁，那么就是a线程执行1方法结束后，释放锁给b线程，b线程才能执行2方法。
  
问题2：在synchronized同步方法调用另一个synchronized同步方法，持锁对象、持锁情况如何？
public synchronized void methodA(int a, int b) {};

public synchronized void methodB(int a）{
    methodA(a, 0);
}
答：
以上有三种情况：
（1）假设方法A和B是在同一个类Test中的两个方法。
Test t = new Test(); 
t.methodB();
这个时候，当前线程中methodB方法被调用时，因为加了synchronized ，需要先获得一个锁，这个锁的对象是当前对象t，也就是当前的这个Test类的实例，而获得锁的东西是线程，也就是说当前线程拿到了t的锁（而不是B方法获得锁），这个时候B方法内调用methodA，因为A也加了synchronized，也需要获得一个锁，因为A和B都是Test类中的方法，所以当前线程要获得的锁的对象也是t。由于当前线程在执行B方法时已经持有了t对象的锁，因此这时候调用methodA是没有任何影响的，相当于方法A上没有加synchronized。
加在非static方法上的synchronized方法是和synchronized（this）块等价的，均为对象锁，即对this加锁。获得当前对象锁的线程，可以继续获得当前对象锁，JVM负责跟踪对象被加锁的次数。线程运行B方法，此时如果this锁可以用，线程获得该锁，线程给对象加锁，计数器变成1，然后B方法调用A方法，由于是对同一个对象同一个线程，线程可以继续获得锁，计数器变为2，表示this被加锁2次。A方法完毕后，线程释放锁，计数器变为1，此时对象锁对其他线程依然是不可获得的。B方法完毕后，线程继续释放锁，此时计数器变为0，表示锁被完全释放，其他线程可以获得对象锁。

（2）假设现在有两个Test类
//线程1中：
Test t1 = new Test(); 
t1.methodB(); //此时当前线程持有了t1对象的锁
//线程2中：
Test t2 = new Test(); 
t2.methodB(); //此时当前线程也持有了t2对象的锁
当前线程持有了两把锁，锁的对象分别是两个不同的Test类的实例t1和t2，互相没有影响。

（3）假设在多线程环境下，两个线程都可以访问Test t=new Test();
此时假设thread1里调用t.methodB();同时thread2里调用t.methodB()
这时假设thread1先抢到t对象的锁,那么thread2需要等待thread1释放t对象的锁才可以执行B方法。
结果像这样：
thread1获得t的锁->thread1执行methodB->thread1执行methodA->释放t的锁；
thread2获得t的锁->thread2执行methodB->thread2执行methodA->释放t的锁。
```





### 1.15 volatile 

• volatile 的实现原理？

volatile也是java中的一个关键字，加入volatile关键字时，编译后的底层代码会多出一个lock前缀指令。这个lock前缀指令实际上相当于一个内存屏障，内存屏障会提供3个功能：

1. 防止重排序：它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；

2. 立即写主存：它会强制将对缓存的修改操作立即写入主存；

3. 另其他缓存无效：如果是写操作，它会导致其他CPU中对应的缓存行无效。其它线程因为缓存失效所以会重读主内存拿到它修改后的值。

也就是说volatile能保证线程之间的可见性与原子性。值得注意的是它并不能保证复合操作的原子性。如自增自减操作。

```java
//典型的双重校验锁实例（DCL）用volatile修饰：
public class SingleTon {
    private static volatile SingleTon instance = null;	//volatile来确保instance初始完成。

    private SingleTon(){}

    public static SingleTon getInstance() {
        if(instance == null){	//第一个校验，是为了代码提高性能，使初始化后不用再去竞争锁。
            synchronized(SinleTon.class){
                if(instance == null){	//第二个校验是为了防止重复创建实例
                    instance = new SingleTon();
                }  
            }
        }
        return instance ;
    }
}
/**
分析：
    第一次校验：由于单例模式只需要创建一次实例，如果后面再次调用getInstance方法时，则直接返回之前创建的实例，因此大部分时间不需要执行同步方法里面的代码，大大提高了性能。如果不加第一次校验的话，那跟上面的懒汉模式没什么区别，每次都要去竞争锁。
    第二次校验：如果没有第二次校验，假设线程T1执行了第一次校验后，判断为null，这时T2也获取了CPU执行权，也执行了第一次校验，判断也为null。接下来T2获得锁，创建实例。这时T1又获得CPU执行权，由于之前已经进行了第一次校验，结果为null（不会再次判断），获得锁后，直接创建实例。结果就会导致创建多个实例。所以需要在同步代码里面进行第二次校验，如果实例为空，才进行创建。
    需要注意的是，private static volatile SingleTon instance=null;需要加volatile关键字，否则会出现错误。问题的原因在于JVM指令重排优化的存在。因为instance = new Singleton() 这句话可以分为三步：
    1. 为 instance 分配内存空间；
    2. 初始化 instance；
    3. 将 instance 指向分配的内存空间。
    在某个线程创建单例对象时，在构造方法被调用之前，就为该对象分配了内存空间并将对象的字段设置为默认值。此时就可以将分配的内存地址赋值给instance字段了，然而该对象可能还没有初始化。若紧接着另外一个线程来调用getInstance，取到的就是初始化一半的对象，程序就会出错。
**/
```



### 1.16 Lock

•	synchronized 和 lock 有什么区别？

​	Lock是一个接口。Jdk1.5后出现的。我们了解到如果一个代码块被synchronized修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，那么如果这个获取锁的线程由于要等待IO或者其他原因（比如调用sleep方法）被阻塞了，但是又没有释放锁，其它线程也只能继续等待，而什么也做不了。这样就很影响了程序的效率。而Lock就可以通过tryLock()方法做到让等待的线程只等待一定的时间，如果一定时间内没有获得锁就可以继续等或者可以做其他事情。Lock也可以通过lock.lockInterruptibly()方法直接中断线程的等待过程。

​	还有一种情况是，当有多个线程读写文件时，读操作和写操作会发生冲突现象，写操作和写操作会发生冲突现象，但是读操作和读操作不会发生冲突现象。但是采用synchronized关键字来实现同步的话，就会导致一个问题：如果多个线程都只是进行读操作，当一个线程在进行读操作时，其他线程也只能等待无法进行读操作。而Lock能够做到多个线程都进行读操作而不会发生冲突。

​	另外，通过Lock可以通过tryLock()方法知道线程有没有成功获取到锁。这个是synchronized无法办到的。

```
synchronized和Lock区别：
1. synchronized是java的关键字，而ReentrantLock是一个类。都能通过锁来控制同步。Synchronized的锁更重量级一些，Lock类的锁更轻量级。
2. 然后synchronized会自动释放锁，Lock需要主动调用unlock()方法释放锁。特别是异常时，如果没有主动unlock()释放锁，很可能造成死锁，所以unlock一般都是放在finally块中执行。
3. 然后synchronized没有获得锁时会一直等待，不能中断，相当于是阻塞式的。Lock可以让等待锁的线程主动中断。
4. 然后synchronized不能知道当前线程有没有获得锁，而lock通过tryLock()可以知道有没有获得锁。
5. 还有Lock可以提高多个线程进行读操作的效率。如实现一个读写锁ReadWriteLock。 

由于ReentrantLock是重入锁，所以可以反复得到相同的一把锁，它有一个与锁相关的获取计数器，如果拥有锁的某个线程再次得到锁，那么获取计数器就加1，然后锁需要被释放两次才能获得真正释放(重入锁)。
```



### 1.17 线程池

•	线程池，这个还是很重要的，在生产中用的挺多，四个线程池类型，其参数，参数的理解很重要，corepoolSize怎么设置，maxpoolsize怎么设置，keep-alive各种的，和美团面试官探讨过阻塞队列在生产中的设置，他说他一般设置为0，防止用户阻塞

#### 1.17.1 线程池概念

​	在没用线程池之前，我们是需要使用线程就去创建一个，实现起来也很简单，但是问题如果并发的线程数量很多，而且每个线程执行的时间又比较短的话，系统就会很频繁地创建，切换和销毁线程，这都是很耗时间的，会降低系统的效率。线程池的主要目的是为了达到一个资源复用的效果。

```
使用Java线程池的好处：
1. 重用存在的线程，减少对象创建、消亡的开销，提升性能。
2. 可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞。
3. 提供定时执行、定期执行、单线程、并发数控制等功能。
```

#### 1.17.2 线程池类型

Java中常用的线程池有四种：

newFixedThreadPool，newSingleThreadPool，newCachedThreadPool，ScheduledThreadPool

每种线程池有不同的适用场景：

**newFixedThreadPool**

概念：创建一个指定工作线程数量的线程池，这样就能控制最大并发数。然后池中的线程数小于核心线程数时，每提交一个任务，线程池就会创建一个工作线程去执行这个任务。然后如果池中的线程数超过核心线程数了，那么这些新提交的任务就会被放到池队列中。然后如果你还要继续提交任务，把池队列也给放满了，就是把池队列的21亿个位置都放满了。那么这时候就判断，如果池中的线程数量小于线程池的最大线程数量，线程池的最大线程数也是21亿，那就继续创建线程去执行。如果池中的线程数量等于线程池的最大线程数量了，就会抛出异常，拒绝任务，至于如何拒绝处理新增的任务，取决于线程池的饱和策略RejectedExecutionHandler了。

适用：对于需要保证所有提交的任务都要被执行的情况，它的性能好很多

```
corePoolSize：
线程池的基本大小，即在没有任务需要执行的时候线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程。这里需要注意的是：在刚刚创建ThreadPoolExecutor的时候，线程并不会立即启动，而是要等到有任务提交时才会启动，除非调用了prestartCoreThread/prestartAllCoreThreads事先启动核心线程。再考虑到keepAliveTime和allowCoreThreadTimeOut超时参数的影响，所以没有任务需要执行的时候，线程池的大小不一定是corePoolSize。

maximumPoolSize：
线程池中允许的最大线程数，线程池中的当前线程数目不会超过该值。如果队列中任务已满，并且当前线程个数小于maximumPoolSize，那么会创建新的线程来执行任务。这里值得一提的是largestPoolSize，该变量记录了线程池在整个生命周期中曾经出现的最大线程个数。为什么说是曾经呢？因为线程池创建之后，可以调用setMaximumPoolSize()改变运行的最大线程的数目。

poolSize：
线程池中当前线程的数量，当该值为0的时候，意味着没有任何线程，线程池会终止；同一时刻，poolSize不会超过maximumPoolSize。
```

线程池数量经验：

- IO密集型 = 2Ncpu（常出现于线程中：数据库数据交互、文件上传下载、网络数据传输等等）
- 计算密集型 = Ncpu、或(N+1)cpu（常出现于线程中：复杂算法）



**newSingleThreadPool**

概念：创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。与其他等效的 newFixedThreadPool(1)不同，可保证无需重新配置此方法所返回的执行程序即可使用其他的线程。

适用：一个任务一个任务执行的场景

**ScheduledThreadPool**

概念：创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。池中保存的线程数，即使线程是空闲的也包括在内。

适用：周期性执行任务的场景

**newCachedThreadPool**

概念：创建一个可缓存线程池，调用 execute() 执行任务时它可以重用之前构造的并且还可用的线程，然后如果没有可用的线程的话，它就会池里面添加一个新的线程去执行任务。默认这个线程池能存在Integer.MAX_VALUE个线程，然后默认会终止和从从缓存中移除那些60s都没有被使用的线程。就是说，长时间保持这个空闲的线程池也不会占用资源。

适用：适合执行大量短暂异步的程序，或者希望提交的任务尽快分配线程执行的情况。





### 1.18 JUC常用类

#### 1.18.1 AQS

**AQS概念**

​    所谓AQS，指的是AbstractQueuedSynchronizer，它提供了一种实现阻塞锁和一系列依赖FIFO等待队列的同步器的框架，ReentrantLock、Semaphore、CountDownLatch、CyclicBarrier等并发类均是基于AQS来实现的，具体用法是通过继承AQS实现其模板方法，然后将子类作为同步组件的内部类。

AQS基本框架如下图所示：

![img](https://upload-images.jianshu.io/upload_images/10431632-7d2aa48b9b217bbe.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/794/format/webp)

​    AQS维护了一个FIFO线程等待队列，多线程竞争下state被阻塞时会进入此队列。和维护了一个volatile修饰的变量**state**，该属性是一个int值，表示对象的当前状态（如0表示lock，1表示unlock）。AQS提供了三个protected final的方法来改变state的值，分别是：getState、setState(int)、compareAndSetState(int, int)。根据修饰符，它们是不可以被子类重写的，但可以在子类中进行调用，这也就意味着子类可以根据自己的逻辑来决定如何使用state值。

AQS的子类应当被定义为内部类，作为内部的helper对象。事实上，这也是juc种锁的做法，如ReentrantLock，便是通过内部的Sync对象来继承AQS的。



#### 1.18.2 Cyclicbarrier 

字面意思循环栅栏，通过它可以实现**让一组线程等待至某个状态之后再全部同时执行**。叫做循环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。 

它的两个构造方法：

```java
public CyclicBarrier(int parties)
public CyclicBarrier(int parties, Runnable barrierAction)
```

第一个参数，表示那个一起执行的线程个数。
第二个参数，表示线程都处于barrier时，一起执行之前，先执行的一个线程。

类似于[人满发车]，当等车的人够20个时，就发出一辆长途汽车，让这20人走。等到又够20个人时，就会又发出一辆车

```java
System.out.println(name +"到达集合点");
// 通过在线程中调用await()来阻塞，直到各线程就位
cyclicBarrier.await();	
System.out.println(name +"开始旅行啦～～");
```

#### 1.18.3 Countdownlatch

CountDownLatch是一个同步工具类，它允许一个或多个线程一直等待，直到其他线程执行完后再执行。例如，应用程序的主线程希望在负责启动框架服务的线程已经启动所有框架服务之后执行。

它是使用一个计数器进行实现。计数器初始值为线程的数量。当每一个线程完成自己任务后，计数器的值就会减一。当计数器的值为0时，表示所有的线程都已经完成了任务，然后在CountDownLatch上等待的线程就可以恢复执行任务。

然后CountDownLatch是一次性的，计数器的值只能在构造方法中初始化一次，之后没有任何机制再次对其设置值，当CountDownLatch使用完毕后，它不能再次被使用。

如要使A线程等待B线程执行完了再执行。则：

```java
//在A线程中使用await()
count.await();
//在B线程执行完后，使用countDown()：
count.countDown();
```

• cyclicbarrier 和countdownlatch的区别，个人理解 赛马和点火箭

#### 1.18.4 Semaphore

• Java 的信号灯？

Semaphore翻译成字面意思为 信号量，Semaphore可以控制并行执行的线程个数，就是可以在Semaphore实例化的时候就指定线程的最大并行数。然后每个线程可以通过 acquire() 方法获取一个许可，如果没有获得线程就会等待，获得许可的线程执行完之后，使用release() 释放一个许可。然后处于等待状态的线程就可以继续拿到这个许可了。


#### 1.18.5 ThreadLocal

- static能不能修饰threadLocal，为什么，这道题我当时一听到其实挺懵逼的

ThreadLocal概念

ThreadLocal类的目的是为每个线程单独维护一个变量的值，避免线程间对同一变量的竞争访问，适用于一个变量在每个线程中需要有自己独立的值的场合。例如以下代码，当多线程同时访问类A的setID和getID方法时，每个线程的getID方法会返回自己setID()时设置的值。

```java
public class A {
  private static ThreadLocal threadLocalID = new ThreadLocal();
  int setID(int id) {
      threadLocalID.set(id);
  }
  int getID() {
      return threadLocalID.get();
  }
}
```

那么ThreadLocal类型的成员变量threadLocalID为什么设置为static的呢？

Java 中每个线程都有与之关联的Thread对象，Thread对象中有一个ThreadLocal.ThreadLocalMap类型的成员变量，该变量是一个Hash表， 所以每个线程都单独维护这样一个Hash表，当ThreadLocal类型对象调用set方法时，即上面的threadLocalID.set(id)，这个set方法会使用当前线程维护的Hash表，把自己作为key, id作为value插入到Hash表中。由于每个线程维护的Hash表是独立的，因此在不同的Hash表中，key值即使相同也是没问题的。

如果把threadLocalID声明为非静态，则在类A的每个实例中都会产生一个新对象，这是毫无意义的，只是增加了内存消耗。





### 1.19 IO/NIO

NIO即异步IO，一般指网络IO，采用多路复用机制，就是一个master线程负责分配连接，其它worker线程负责处理连接。而不是传统的一个连接对应一个线程。



•	怎么实现所有线程在等待某个事件的发生才会去执行？
•	如何线程安全的实现一个计数器
•	快速失败(fail-fast)和安全失败(fail-safe)的区别是什么

如Map的iterator 方法返回的迭代器是fail-fastl的。


•	线程回调，这块 被问过让我设计一个RPC，怎么实现，其实用到了回调这块的东西
•	乐观锁和悲观锁的使用场景
•	悲观锁的常见实现方式：lock synchronized retreentlock
•	乐观锁：CAS MVCC
•	读写锁的实现方式，16位int的前八位和后八位分别作为读锁和写锁的标志位
•	死锁的条件，怎么解除死锁，怎么观测死锁。
•	希望大家能够好好看一下反射的原理，怎么确定类，怎么调方法
•	RPC框架，同步异步，响应时间，这些都被问到过，还让设计过
•	同步，异步，阻塞，非阻塞 在深信服的面试中遇到过，最好再找一些应用场景加以理解4



## 2.JVM

### JVM概念

JVM就是Java 虚拟机。主要功能就是将class字节码文件解释成机器码让计算机能识别运行。我们说Java程序是"一次编译，导出运行"的原因，就是只要在不同的操作系统上安装不同的虚拟机后，就能识别我们的class字节码文件，从而转换为不同平台的机器码来执行。

具体细节就是，当我们的程序运行时，JVM首先要通过类加载子系统(类加载器)加载所需要的类的字节码，class文件被jvm装载以后，最后由执行引擎会配合一些本地方法最终完成class文件的执行。然后在执行的过程中，需要一段内存空间来存储数据，JVM可以对这段内存空间进行分配和释放，这段内存空间我们称为运行时数据区。

JVM主要就是包含了**类加载子系统、执行引擎、运行时数据区**这三个内容。还有垃圾回收器和本地方法接口等模块。



### 2.1 类加载子系统

#### 2.1.1 类加载过程

类加载子系统概念：在JAVA虚拟机中，存在着多个类装载器，称为类加载子系统。也就是说实际上是类装载器把字节码文件加载到JVM中的。我们先不说类加载器，先说类加载器加载类的过程。类的生命周期是从被加载到虚拟机内存中开始，到卸载出内存结束。对应的过程有 **加载----验证----准备----解析-----初始化----使用-----卸载** 共七个阶段。从加载到初始化都属于类加载的过程。下面说一下这五个过程的作用。 （验证+准备+解析 = 连接）

**加载：**通过一个类的全限定名查找此类的二进制字节流，并将这些静态数据转换成方法区中的运行时数据结构，而且在Java堆中也创建一个java.lang.Class类的对象，这样便可以通过该对象访问方法区中的这些数据。

**验证：**验证Class文件的字节流中包含信息符合当前虚拟机要求，并且不会危害虚拟机自身安全。

**准备：**为类变量在方法区分配内存，和给类变量赋予初始值。

**解析：**将常量池内类、字段、方法等的符号引用替换为直接引用的过程**。**

**初始化：**执行静态变量的初始化，包括静态变量的赋值和静态初始化块的执行。

**使用**：类加载到JVM后，就会为其中的对象分配内存空间。一个对象需要多大的内存空间在类加载完成后就确定了。

#### 2.1.2 类加载器

上面知道了类加载的过程，也知道是通过类加载器来进行类加载的。现在说一下类加载器。

JVM预定义的三种类型类加载器，当一个 JVM启动的时候，Java缺省开始使用如下三种类型类装入器：

　**启动（Bootstrap）类加载器**：启动类装入器负责将jre/lib/rt.jar下一些核心类库加载到内存中。由C++实现，属于虚拟机实现的一部分，它并没有继承java.lang.ClassLoader类。开发者不可以直接使用。

　**扩展（Extension）类加载器**：扩展类加载器责将jre/lib/ext下的一些扩展性的类库加载到内存中。开发者可以直接使用。

　**系统（System）类加载器**：系统类加载器负责将系统类classpath路径下的类库加载到内存中。是程序默认类加载器。开发者可以直接使用，也叫 Application ClassLoader。

　自定义类加载器（custom class loader）：除了系统提供的类加载器以外，开发人员可以通过继承 java.lang.ClassLoader类的方式实现自己的类加载器，以满足一些特殊的需求。

```
用 class对象的getClassLoader()可以得到类加载器。
自定义类加载器的方法：通过继承java.lang.ClassLoader，然后重写findClass()方法实现自定义的类加载器。
```

```
加载顺序： 父类静态代码块->子类静态代码块->父类普通代码块->父类构造方法->子类普通代码块->子类构造方法
```

#### 2.1.3 双亲委派机制

JVM在加载类时默认采用的是双亲委派机制。通俗的讲，就是某个特定的类加载器在接到加载类的请求时，首先将加载任务委托给父类加载器，依次递归，如果父类加载器可以完成类加载任务，就成功返回；只有父类加载器无法完成此加载任务时，才自己去加载，这样即使相同类名称的类如果由不同的类加载器加载，也认为是两个不同的类。

**这种机制能够让越基础的类越由顶层加载器加载，这样就能保证类的一致性， 防止内存中出现多份同样的字节码。同时也保证了内库的安全性，像保证了像Object、String等基本类最终是由最顶端的Bootstrap ClassLoader进行加载，保证了Java类型体系中最基础的行为。**



### 2.2 Java对象

•	对象内容，对象创建方法，对象的内存分配，对象的访问定位，双亲委派模型机制

#### **2.2.1 对象结构**

- 对象头：由两部分组成，第一部分存储对象自身的运行时数据：哈希码、GC分代年龄、锁标识状态、线程持有的锁、偏向线程ID（一般占32/64 bit）。第二部分是指针类型，指向对象的类元数据类型（即对象代表哪个类）。如果是数组对象，则对象头中还有一部分用来记录数组长度。
- 实例数据：用来存储对象真正的有效信息，包括父类继承下来的和自己定义的。这是我们定义和关心的。
- 对齐填充：jvm要求对象起始地址必须是8字节的整数倍（8字节对齐）

#### **2.2.2 对象创建过程**

1.    当JVM遇到new指令时，首先JVM需要在运行时常量池查找这个类对应的类符号引用，并且检查这个符号代表的类是否被加载，解析和初始化过，如果没有找到就需要把这个类加载到运行时常量池。 

      ```
      （1.7的运行时常量池在方法区，1.8后在堆）
      ```

2.    根据类的相关信息，为这个要创建的对象分配一定大小的内存。

3.    JVM将对象的内存空间除对象头外初始化为0，这就是为什么JAVA代码中的全局变量可以不用初始化也可以使用的原因。此外，JVM还会为对象设置对象头。如设置对象所属的类，对象哈希码，锁标识状态，GC分代年龄等。

```
Instance instance = new Instance();
1.首先jvm要为这个对象实例分配内存空间
2.初始化这个对象
3.将instance指向内存地址
（双重校验锁中，2、3如果重排序了就导致返回未初始化完成的空对象，所以要加上 volatile关键字。）
```

#### **2.2.3 对象分配过程**

1. 编译器通过逃逸分析，确定对象是在栈上分配还是在堆上分配。如果是堆上分配则继续尝试。

2. 尝试TLAB上能够直接分配对象则直接在TLAB上分配，如果现有的TLAB不足以存放当前对象则重新申请一个TLAB，并再次尝试存放当前对象。如果还放不下则继续尝试。

3. 尝试对象能否直接进入年老代，如果能够进入则直接在年老代分配。否则再继续尝试。

4. 最后选择是年轻代的Eden。（具体分配方式：指针碰撞 / 空闲列表）

   ```
   栈上分配
   JVM允许将线程私有的对象（没有逃逸的）打散分配在栈上（标量替换），而不是分配在堆上。然后对象可以在函数调用结束后自行销毁，垃圾收集系统的压力将会小很多，从而提高系统性能。

   TLAB
   TLAB（Thread Local Allocation Buffer 线程本地分配缓冲区）指JVM在新生代Eden区中为每个线程开辟的私有区域。默认占用Eden Space的1%。因为TLAB是私有的，没有锁的开销，所以Java程序中很多不存在线程共享的小对象，通常是在TLAB上优先分配，这些小对象也适合被快速地回收。
   ```



#### 2.2.4 对象访问方式

建立对象就是为了使用对象，我们的Java程序通过栈上的 reference 数据来操作堆上的具体对象。对象的访问方式有虚拟机实现而定，目前主流的访问方式有使用句柄池和直接指针两种。

**句柄池**：在Java堆中分出一块内存进行存储句柄池，这样的话，在栈中存储的是句柄的地址。Java 堆中将会划分出一块内存来作为句柄池，reference 中存储的就是对象的句柄地址，而句柄中包含了对象实例数据与类型数据各自的具体地址信息。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190417214133394.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvZGVtYWxl,size_16,color_FFFFFF,t_70)



**直接指针**：Java栈直接与对象进行访问，如果使用直接指针访问，那么 Java 堆对象的布局中就必须考虑如何放置访问类型数据的相关信息，而 reference 中存储的直接就是对象的地址。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190417214138689.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvZGVtYWxl,size_16,color_FFFFFF,t_70)

#### **2.2.5 new Object()时**

如Student s=new Student();  的作用就是：

1.加载Student.class文件进内存。
2.在栈内存为s开辟空间
3.在堆内存为学生对象开辟空间
4.对学生对象的成员变量进行默认初始化
5.对学生对象的成员变量进行显示初始化
6.通过构造方法对学生对象进行成员变量赋值
7.学生对象初始化完毕，把对象变量赋值给s变量             



### 2.3 运行时数据区

•	JVM 内存分哪几个区，每个区的作用是什么?

​	当把需要执行的类加载进JVM后，在程序执行的过程中，需要一段内存空间来存储数据，JVM可以对这段内存空间进行分配和释放，这段内存空间我们称为运行时数据区。JVM规范中运行时数据区分为五个区域： **程序计数器，虚拟机栈，本地方法栈，堆，方法区**。下面说一下这五个区域的作用。

```
• 程序计数器：指向当前线程正在执行的字节码指令的地址(行号)，保证在线程切换后也能继续从原来的位置执行下去。分支、循环、跳转、异常处理等基础功能也需要依赖这个计数器来完成
该区域中，JVM规范没有规定任何OutOfMemoryError情况。

• 虚拟机栈：存放当前线程运行的方法所需要的数据、指令、返回值和返回地址。基本单位是栈帧。
当线程调用一个方法时，jvm就会压入一个新的栈帧到这个线程的栈中。栈帧中包括一些局部变量表、操作数栈、动态链接方法、返回值、返回地址等信息。（通过 -Xss 参数设置）
如果方法的嵌套调用层次太多(如递归调用)，栈的深度大于虚拟机所允许的深度，会产生StackOverflowError溢出异常。
如果java程序启动一个新线程时没有足够的空间分配，即扩展时无法申请到足够的内存，则抛出OutOfMemoryError异常。

• 本地方法栈：与虚拟机栈类似，但它是为虚拟机用到的本地方法服务。一般情况下，我们无需关心此区域。
虚拟机通过调用本地方法接口，实现了Java和其他语言的通信(主要是C&C++)

• 堆：由所有线程共享，用于存放实例对象。堆内存就是JVM管理的内存中最大的一块，堆中分为新生代和老年代，是垃圾回收的主要区域。  （通过-Xms -Xmx参数设置）

• 方法区：存储已经被虚拟机加载的类信息、常量、静态变量、静态方法、运行时常量池、即时编译器编译后的代码等
类信息如类的完整有效名称，类的修饰符等等。 （通过-XX:PermSize -XX:MetaspaceSize设置）
```



### 2.4 Java堆

• 堆里面的分区：Eden，survival （from+ to），年老代，各自的特点。

​	Java堆中有一个分代的概念。为什么要分代呢？因为我们的对象都是存在堆中的，堆的容量是有限的，所以要把堆中的一些没用的对象进行回收。那什么回收什么样的对象，什么时候回收多久回收，怎么高效地回收都是需要考虑的问题。JVM的开发人员就提出了分代的思想。分代后，把不同生命周期的对象放在不同代上，不同代上采用适合这个代的回收算法，这样就能比较高效地回收资源、控制内存了。

**堆中分为年轻代和年老代**。

其中年轻代中又分为Eden区和两个大小相等的Survivor区。Java 中的大部分对象都是朝生夕灭，所以大部分新创建的对象都会被分配到Eden区，当Eden区当在Eden申请空间失败时，就会出发Minor GC，然后将Eden和其中一个Survivor区上还存活的对象复制到另外一个Survivor区中，然后一次性清除Eden代和这个Survivor区，也就是说两个Survivor中总有一个是空的。

然后Survivor 区中的对象每经过一次 Minor GC年龄就会 + 1，当对象的年龄达到某个值时 ( 默认是 15 岁)，这些对象就会成为年老代。或者Survivor空间中相同年龄的对象大小总和大于Survivor空间的一半，则年龄大于或等于该年龄的对象就可以进入年老代。然后一些比较大的对象则是直接进入到年老代。但是年老代的内存也不是无限的啊，万一从年轻代过来的对象年老代也装不下呢，所以JVM中还有一个空间分配担保，就是在发生Minor GC之前，虚拟机会检查年老代中的最大的可用连续内存空间如果大于之前每次晋升年老代对象的平均大小或者新生代中存活对象的总大小，则Minor GC是安全的，年老代还装得下，触发一次Minor GC就行；否则说明年老代也装不下了，这时候就会进行一次Full GC。

![img](https://img-blog.csdn.net/2018090721215783?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0hlbGxvV29ybGRfSW5fSmF2YQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



### 2.5 Java方法区

​	方法区用于存储已经被虚拟机加载的类信息、常量、静态变量、静态方法、运行时常量池、即时编译器编译后的代码等。

​	Java8前方法区通过永久代实现，Java8后元空间（metaSpaces）取代了永久代。元空间并不在虚拟机中，而是使用本地内存。元空间就不存在永久代内存溢出的问题，并且不再需要调整和监控永久代的内存空间，提高了内存利用率。

​	和堆的垃圾回收效率相比，方法区的回收效率实在太低，但是此部分内存区域也是可以被回收的。方法区的垃圾回收主要有两种，分别是对`废弃常量`的回收和对`无用类`的回收。



```
方法区中的类需要同时满足以下三个条件才能被标记为无用的类：
1.Java堆中不存在该类的任何实例对象；
2.加载该类的类加载器已经被回收；
3.该类对应的java.lang.Class对象不在任何地方被引用，且无法在任何地方通过反射访问该类的方法。
当满足上述三个条件的类才可以被回收，但是并不是一定会被回收，需要参数进行控制，例如HotSpot虚拟机提供了-Xnoclassgc参数进行控制是否回收。
```

### 2.6 Java垃圾回收

• GC 的两种判定方法：引用计数法、可达性分析法

#### 2.6.1 GC 的判定方法

可达性分析法	：只要你无法与 GC Root 建立直接或间接的连接，系统就会判定你为可回收对象。

在 Java 语言中，可作为 GC Root 的对象包括以下4种：

- 虚拟机栈中引用的对象
- 方法区中类静态属性引用的对象
- 方法区中常量引用的对象
- 本地方法栈的Native 方法中引用的对象

#### 2.6.2 GC的回收算法

• GC 的三种收集方法：标记清除、标记整理、复制算法的原理与特点，分别用在什么地方，如果让你优化收集方法，有什么思路？

有标记清除、标记整理、复制算法。

```
- 标记清除算法：标记清除算法（Mark-Sweep）是最基础的收集算法，其他收集算法都是基于这种思想。标记清除算法分为“标记”和“清除”两个阶段：首先标记出需要回收的对象，标记完成之后统一清除被标记的对象。这种算法实现简单，运行高效，但清除之后会产生大量不连续的内存碎片。

- 标记整理算法：标记整理算法（Mark-Compact）标记操作和“标记-清除”算法一致，后续操作不只是直接清理对象，而是在清理无用对象完成后让所有存活的对象都向一端移动，并更新引用其对象的指针。这种算法能充分利用内存且不会产生内存碎片。但在标记-清除的基础上还需进行对象的移动，成本相对较高。

- 复制算法：复制算法（Copying）将可用内存容量划分为大小相等的两块，每次只使用其中的一块。当这一块用完之后，就将还存活的对象复制到另外一块上面，然后在把已使用过的内存空间一次清理掉。复制算法实现简单，运行高效，不会产生碎片。
但是每次相当于只能使用内存的一半，不能充分使用内存。
```

#### 2.6.3 GC收集器

• GC 收集器有哪些？CMS 收集器与 G1 收集器的特点

**有 Serial 、ParNew、Parallel Scavenge、Serial Old、Parallel Old、CMS、G1 七种垃圾收集器。**

新生代收集器：Serial 、ParNew、Parallel Scavenge；

年老代收集器：Serial Old、Parallel Old、CMS属于。

属于整个堆（新生+老生）收集器：G1。

**Jdk1.8 默认垃圾收集器Parallel Scavenge（新生代）+ Serial Old（年老代），**

**Jdk1.9 默认垃圾收集器G1 （未来趋势）**

```
Parallel Scavenge：ParallelScavenge收集器作用于新生代，它采用复制算法，是并行的多线程收集器，但是用户仍处于等待状态。目标则是达到一个可控制的吞吐量。所谓吞吐量就是CPU用于运行用户代码的时间与CPU总消耗时间的比值，即吞吐量 = 运行用户代码时间 /（运行用户代码时间 + 垃圾收集时间），虚拟机总共运行了100分钟，其中垃圾收集花掉1分钟，那吞吐量就是99%。而高吞吐量为目标，就是减少垃圾收集时间，让用户代码获得更长的运行时间。主要适合在后台运算而不是太多交互的任务，比如需要与用户交互的程序，良好的响应速度能提升用户的体验。

Serial Old：Serial Old是Serial的年老代版本，也是年老代的默认收集器，它是一个单线程的，使用标记整理算法，工作过程中也会stop-the-world。

CMS：CMS收集器（Concurrent Mark Sweep）是一种以获取最短回收停顿时间为目标的收集器。目前很大一部分的Java应用集中在互联网站或者B/S系统的服务端上，这类应用尤其重视服务器的响应速度，希望系统停顿时间最短，以给用户带来较好的体验。CMS收集器就非常符合这类应用的需求。CMS收集器主要优点：并发收集，低停顿。但CMS有三个明显的缺点：（1）CMS收集器对CPU资源非常敏感。CPU个数少于4个时，CMS对于用户程序的影响就可能变得很大；（2）CMS是基于“标记-清除”算法实现的收集器，会产生垃圾碎片；（3）CMS收集器无法处理浮动垃圾 ；

G1：G1 GC是Jdk7的新特性之一，未来计划替代CMS。G1将堆空间划分成了大小相等互相独立的heap区块。每块区域既有可能属于Old区、也有可能是Young区。 G1在全局标记阶段（global marking phase）并发执行, 以确定堆内存中哪些对象是存活的。标记阶段完成后，G1就可以知道哪些heap区哪里垃圾最多。它会首先回收这些区，通常会得到大量的自由空间. 这就是这种垃圾收集方法叫做Garbage-First（垃圾优先）的原因：第一时间处理垃圾最多的区块。
```



### 2.7 查询Java Cpu过高

1. 查找进程：top

   查看进程占用资源情况，明显看出java的两个进程22714，12406占用过高cpu.

2. 查找线程

   使用top -H -p <pid>查看线程占用情况

3. 查找java的堆栈信息

   将线程id转换成十六进制
   \#printf %x 15664
   \#3d30

4. 然后再使用jstack查询线程的堆栈信息

   语法：jstack <pid> | grep -a 线程id（十六进制）
   jstack <pid> | grep -a 3d30

这样就找出了有问题的代码了，剩下的就是分析原因和修改代码了。

```
即 top进程 -> top -H -p 线程 -> printf 线程16进制 -> jstack <pid> | grep -a 线程id 来查看
```



# 二、数据库

## 1.MySQL

### 1.1 事务

#### 1.1.1 事务四大特性

• 事务四大特性（ACID）

**原子性（Atomic）** ：指事务中的操作要么都发生，要么都不发生。  （如转帐扣款加款的两条语句，要么执行，要么不执行）

**一致性（Consistency）**：指如果在执行事务之前数据库是一致的，那么在执行事务之后数据库也还是一致的。（即不会出现修改丢失、脏读、不可重复读、幻读等情况）

**隔离性（Isolation）**：多个事务并发访问时，事务之间是隔离的，一个事务不影响其它事务的运行效果。 （此处有隔离级别的说法）

**持久性（Durability）**：事务提交后，对数据的修改时永久的。及时系统发生故障，也能在不会丢失对数据的修改。一般通过执行前写入日志的方式保证持久性，即使系统崩溃，也能在重启的过程中，通过日志恢复崩溃时处于执行状态的事物。z



#### 1.1.2 事务隔离级别

• 事务的并发？事务隔离级别，每个级别会引发什么问题，MySQL默认是哪个级别？

**读未提交**（Read Uncommitted）：最低的隔离级别，什么都不需要做，一个事务可以读到另一个事务未提交的结果。即可能出现脏读，不可重复读，幻读。

**读提交**（Read Committed）：只有在事务提交后，其更新结果才会被其他事务看见。可以解决脏读问题。但是还有不可重复读和幻读问题。

**可重复读**（Repeated Read）：在一个事务中，对于同一份数据的读取结果总是相同的，无论是否有其他事务对这份数据进行操作，以及这个事务是否提交。可以解决脏读、不可重复读，但是无法解决幻读问题。这是Mysql中默认的隔离级别。

**串行化**（Serialization）：事务串行化执行，隔离级别最高，读写分开，牺牲了系统的并发性。可以解决并发事务的所有问题。

```
脏读：又称无效数据的读出，是指在数据库访问中，事务T1将某一值修改，然后事务T2读取该值，此后T1因为某种原因撤销对该值的修改，这就导致了T2所读取到的数据是无效的。

不可重复读：是指在数据库访问中，一个事务范围内两个相同的查询却返回了不同数据。这是由于查询时系统中其他事务修改的提交而引起的。比如事务T1读取某一数据，事务T2读取并修改了该数据，T1为了对读取值进行检验而再次读取该数据，便得到了不同的结果。不可重复度因为当执行SELECT 操作时没有获得读锁(read locks)或者SELECT操作执行完后马上释放了读锁。

幻读：是指当事务不是独立执行时发生的一种现象，例如第一个事务对一个表中的数据进行了修改，比如这种修改涉及到表中的“全部数据行”。同时，第二个事务也修改这个表中的数据，这种修改是向表中插入“一行新数据”。那么，以后就会发生操作第一个事务的用户发现表中还有没有修改的数据行，就好象发生了幻觉一样。幻读主要由于另一个事务插入导致。
```



### 1.2 存储引擎

• MySQL常见的三种存储引擎（InnoDB、MyISAM、MEMORY）的区别？

MySQL中的存储引擎有很多种，可以通过“SHOW ENGINES”语句来查看。下面重点关注InnoDB、MyISAM、MEMORY这三种

#### 1.2.1 区别

不同的存储引擎决定了MySQL数据库中的表可以用不同的方式来存储。我们可以根据数据的特点来选择不同的存储引擎。

**INNODB** 

支持事务，支持外键，行锁，支持主键自增。查表总行数时，全表扫描。缺点是读写效率较差，占用的数据空间相对较大。

**MYISAM** 

MyISAM的优势在于占用空间小，处理速度快。缺点是不支持事务，不支持外键，不支持行锁。插入数据时，锁定整个表，查表总行数时，不需要全表扫描。

**MEMORY**

MEMORY是MySQL中一类特殊的存储引擎。它使用存储在内存中的内容来创建表，而且**数据全部放在内存中**。这些特性与前面的两个很不同。这样有利于数据的快速处理，提高整个表的效率。值得注意的是，服务器需要有足够的内存来维持MEMORY存储引擎的表的使用。如果不需要了，可以释放内存，甚至删除不需要的表。MEMORY默认使用哈希索引。MEMORY用到的很少，因为它是把数据存到内存中，如果内存出现异常就会影响数据。如果重启或者关机，所有数据都会消失。因此，基于MEMORY的表的生命周期很短，一般是一次性的。

#### 1.2.2 选择：

**InnoDB**：支持事务处理，支持外键，支持崩溃修复能力和并发控制。如果需要**对事务的完整性要求比较高**（比如银行），**要求实现并发控制**（比如售票），那选择InnoDB有很大的优势。如果需要**频繁的更新、删除**操作的数据库，也可以选择InnoDB，因为支持事务的提交（commit）和回滚（rollback）。 

**MyISAM**：插入数据快，空间和内存使用比较低。如果表主要是**用于插入新记录和读出记录**，那么选择MyISAM能实现处理高效率。如果系统读多，写少。对原子性要求低。那么MyISAM最好的选择。

**MEMORY**：所有的数据都在内存中，数据的处理速度快，但是安全性不高。如果需要**很快的读写速度**，对数据的安全性要求较低，可以选择MEMOEY。它对表的大小有要求，不能建立太大的表。所以，这类数据库只使用在相对较小的数据库表。

```
注意，同一个数据库也可以使用多种存储引擎的表。如果一个表要求比较高的事务处理，可以选择InnoDB。这个数据库中可以将查询要求比较高的表选择MyISAM存储。如果该数据库需要一个用于查询的临时表，可以选择MEMORY存储引擎。
```



### 1.3 mysql锁

• mysql都有什么锁，死锁判定原理和具体场景，死锁怎么解决？
• mysql锁，行锁，表锁 ，什么时候发生锁，怎么锁，原理
• 有哪些锁（乐观锁悲观锁），select 时怎么加排它锁？

#### 1.3.1 mysql锁类型

MySQL数据库存在多种数据存储引擎，每种存储引擎所针对的应用场景特点都不太一样，为了满足各自特定应用场景的需求，每种存储引擎的锁定机制都是为各自所面对的特定场景而优化设计。MySQL各存储引擎使用了三种类型（级别）的锁定机制：表级锁定，行级锁定和页级锁定。

**表级锁**：开销小，加锁快；不会出现死锁；锁定粒度大，发生锁冲突的概率最高，并发度最低。 
**行级锁**：开销大，加锁慢；会出现死锁；锁定粒度最小，发生锁冲突的概率最低，并发度也最高。 
**页面锁**：开销和加锁时间界于表锁和行锁之间；会出现死锁；锁定粒度界于表锁和行锁之间，并发度一般。

#### 1.3.2 mysql锁使用

**MYISAM锁**

MyISAM存储引擎，只支持表锁。MyISAM在执行查询语句（SELECT）前，会自动给涉及的所有表加读锁，在执行更新操作 （UPDATE、DELETE、INSERT等）前，会自动给涉及的表加写锁。即MyISAM中读和写是串行的。

**InnoDB锁**

InnoDB存储引擎，它支持行锁，也支持表锁（Lock table .. read/write），默认情况下是采用行锁。InnoDB实现了以下两种类型的行锁。对于UPDATE、DELETE和INSERT语句，InnoDB会自动给涉及数据集加排他锁；

- 共享锁（s）：又称读锁。允许一个事务去读一行，阻止其他事务获得相同数据集的排他锁。若事务T对数据对象A加上S锁，则事务T可以读A但不能修改A，其他事务只能再对A加S锁，而不能加X锁，直到T释放A上的S锁。这保证了其他事务可以读A，但在T释放A上的S锁之前不能对A做任何修改。

- 排他锁（Ｘ）：又称写锁。允许获取排他锁的事务更新数据，阻止其他事务取得相同的数据集共享读锁和排他写锁。若事务T对数据对象A加上X锁，事务T可以读A也可以修改A，其他事务不能再对A加任何锁，直到T释放A上的锁。


```sql
使用方式：
对于普通SELECT语句，InnoDB不会加任何锁；事务可以通过以下语句显式给记录集加共享锁或排他锁。
共享锁（S）： SELECT * FROM table_name WHERE ... LOCK IN SHARE MODE
排他锁（X）： SELECT * FROM table_name WHERE ... FOR UPDATE
```

注意：

- 行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁。这些锁统称为悲观锁(Pessimistic Lock)
- 加过排他锁x的数据行在其他事务中是不能修改数据的，也不能通过for update和lock in share mode锁的方式查询数据，但可以直接通过select …from…查询数据，因为普通查询没有任何锁机制。
- 间隙锁：当我们用范围条件而不是相等条件检索数据，并请求共享或排他锁时，InnoDB会给符合条件的已有数据记录的 索引项加锁；对于键值在条件范围内但并不存在的记录，叫做“间隙（GAP)”，InnoDB也会对这个“间隙”加锁，这种锁机制就是所谓的间隙锁 （Next-Key锁）
- InnoDB的行锁是基于索引实现的，如果不通过索引访问数据，InnoDB会使用表锁。 



### 1.4 索引

• 索引类型？
• 索引为什么要用B+树，B+树/B-树区别？B+Tree索引/Hash索引区别？
• 聚集、非聚集索引区别？

#### 1.4.1 索引分类

Mysql目前主要有以下几种索引类型：全文索引（FULLTEXT），哈希索引（HASH），B+树索引（BTREE），空间数据索引（RTREE）。其中：

- **B+树索引（BTREE）**：

  是mysql默认的InnoDb引擎使用的索引，也是我们最常见的索引，可以分为**普通索引、唯一索引、组合索引。**

  普通索引：仅加速查询最基本的索引，没有任何限制，是我们大多数情况下使用到的索引。

  唯一索引：与普通索引类型，不同的是：加速查询 + 列值唯一（可以有null）

  组合索引：将几个列作为一条索引进行检索，使用最左匹配原则。

```sql
CREATE INDEX  index_name on user_info(name);
CREATE UNIQUE INDEX unindex_name on user_info(name);
```

- **全文索引（FULLTEXT）**

  仅可以适用于MyISAM引擎的数据表；仅可作用于CHAR、VARCHAR、TEXT数据类型的列。它的出现是为了解决WHERE name LIKE '%word%' 这类针对文本的模糊查询效率较低的问题。

- **哈希索引（HASH）**：

  是Memory引擎上的索引。HASH索引可以一次定位，不需要像树形索引那样逐层查找,因此具有极高的效率。但是，这种高效是有条件的，即只在“=”和“in”条件下高效，且不支持范围查询、排序及组合索引。另外hash索引中的hash码的计算可能存在hash冲突。当出现hash冲突的时候，存储引擎必须遍历整个链表中的所有行指针，逐行比较，直到找到所有的符合条件的行。

- **空间数据索引（ RTREE）:**

  RTREE在MySQL很少使用，仅支持geometry数据类型，支持该类型的存储引擎只有MyISAM、BDb、InnoDb、NDb、Archive几种。相对于BTREE，RTREE的优势在于范围查找。



**聚合索引、非聚合索引区别：**

**聚集索引：**

​	当给表上了主键，那么表在磁盘上的存储结构就由整齐排列的结构转变成了树状结构，也就是「平衡树」结构，换句话说，就是整个表就变成了一个索引。没错， 再说一遍， 整个表变成了一个索引，也就是所谓的「聚集索引」。 这就是为什么一个表只能有一个主键， 一个表只能有一个「聚集索引」，因为主键的作用就是把「表」的数据格式转换成「索引（平衡树）」的格式放置。

**非聚集索引：**

​	非聚集索引和聚集索引一样， 同样是采用平衡树作为索引的数据结构。索引树结构中各节点的值来自于表中的索引字段， 假如给user表的name字段加上索引 ， 那么索引就是由name字段中的值构成，在数据改变时， DBMS需要一直维护索引结构的正确性。如果给表中多个字段加上索引 ， 那么就会出现多个独立的索引结构，每个索引（非聚集索引）互相之间不存在关联。

​	**二者区别在于， 通过聚集索引可以查到需要查找的数据， 而通过非聚集索引可以查到记录对应的主键值 ， 再使用主键的值通过聚集索引查找到需要的数据。**



#### 1.4.2 索引创建与使用

**什么时候应该创建索引：**

1. 表的主键、外键必须有索引；
2. 数据量较大的表应该有索引；

3. 经常用做连接查询的字段，需要有索引；

4. 经常出现在Where子句中的字段，应该建立索引；

5. 经常用到排序的列上，因为索引已经排序。（单order by）

6. 经常用在范围内搜索的列上创建索引，因为索引已经排序了，其指定的范围是连续的。（between）


```
order by：mysql查询只使用一个索引，因此如果where子句中已经使用了索引的话，那么order by中的列是不会使用索引的。
group by ：也会用上索引
```

**什么时候会用不上索引：**

1. 如果条件中有or，即使其中有条件带索引也不会使用索引（or可以用union代替）；
2. 对于多列索引，不是使用的第一部分，则不会使用索引（即不符合最左前缀）；且复合索引中只要有一列含有NULL值，那么这一列对于此复合索引就是无效的；
3. like查询是以%开头时不会使用索引（MyISAM的全文索引可以%word%）；
4. 如果列类型是字符串，那一定要在条件中将数据使用引号引用起来,否则不使用索引。
5. 索引列参与计算也不会使用索引。
6. NOT IN 、<>、!= 操作也不会使用索引，但<,<=，=，>,>=,BETWEEN,IN是可以用到索引的

```
通过USE INDEX、IGNORE INDEX、FORCE INDEX可以使用、忽略、强制使用索引
```

#### 1.4.3 索引原理

​	索引的目的在于提高查询效率，原理是通过不断的缩小想要获得数据的范围来筛选出最终想要的结果，同时把随机的事件变成顺序的事件，也就是我们总是通过同一种查找方式来锁定数据。但是由于通过磁盘IO读取数据耗费的时间比较多，所以我们希望**每次查找数据时把磁盘IO次数控制在一个很小的数量级，最好是常数数量级**。那么我们就想到如果一个高度可控的多路搜索树是否能满足需求呢？就这样，b+树应运而生。

![b+树](https://awps-assets.meituan.net/mit-x/blog-images-bundle-2014/7af22798.jpg)

​	如上图，是一颗b+树，关于b+树这里只说一些重点，浅蓝色的块我们称之为一个磁盘块，可以看到每个磁盘块包含几个数据项（深蓝色所示）和指针（黄色所示），如磁盘块1包含数据项17和35，包含指针P1、P2、P3，P1表示小于17的磁盘块，P2表示在17和35之间的磁盘块，P3表示大于35的磁盘块。真实的数据存在于叶子节点即3、5、9、10、13、15、28、29、36、60、75、79、90、99。非叶子节点不存储真实的数据，只存储指引搜索方向的数据项，如17、35并不真实存在于数据表中。

**b+树的查找过程**

​	如图所示，如果要查找数据项29，那么首先会把磁盘块1由磁盘加载到内存，此时发生一次IO，在内存中用二分查找确定29在17和35之间，锁定磁盘块1的P2指针，内存时间因为非常短（相比磁盘的IO）可以忽略不计，通过磁盘块1的P2指针的磁盘地址把磁盘块3由磁盘加载到内存，发生第二次IO，29在26和30之间，锁定磁盘块3的P2指针，通过指针加载磁盘块8到内存，发生第三次IO，同时内存中做二分查找找到29，结束查询，总计三次IO。真实的情况是，3层的b+树可以表示上百万的数据，如果上百万的数据查找只需要三次IO，性能提高将是巨大的，如果没有索引，每个数据项都要发生一次IO，那么总共需要百万次的IO，显然成本非常非常高。

**b+树性质**

1. 通过上面的分析，我们知道IO次数取决于b+树的高度h，假设当前数据表的数据为N，每个磁盘块的数据项的数量是m，则有h=㏒(m+1)N，当数据量N一定的情况下，m越大，h越小；而m = 磁盘块的大小 / 数据项的大小，磁盘块的大小也就是一个数据页的大小，是固定的，如果**数据项占的空间越小，数据项的数量越多，树的高度越低**。这就是为什么每个数据项，即索引字段要尽量的小，比如int占4字节，要比bigint8字节少一半。这也是为什么b+树要求把真实的数据放到叶子节点而不是内层节点，一旦放到内层节点，磁盘块的数据项会大幅度下降，导致树增高。当数据项等于1时将会退化成线性表。

2. 当b+树的数据项是复合的数据结构，比如(name,age,sex)的时候，b+数是按照从左到右的顺序来建立搜索树的，比如当(张三,20,F)这样的数据来检索的时候，b+树会优先比较name来确定下一步的所搜方向，如果name相同再依次比较age和sex，最后得到检索的数据；但当(20,F)这样的没有name的数据来的时候，b+树就不知道下一步该查哪个节点，因为建立搜索树的时候name就是第一个比较因子，必须要先根据name来搜索才能知道下一步去哪里查询。比如当(张三,F)这样的数据来检索时，b+树可以用name来指定搜索方向，但下一个字段age的缺失，所以只能把名字等于张三的数据都找到，然后再匹配性别是F的数据了， 这个是非常重要的性质，即索引的最左匹配特性。


#### 1.4.5 b+树、b树

二者的特点如下：

**b树：**

1. 关键字集合分布在整颗树中；
2. 任何一个关键字出现且只出现在一个结点中；
3. 搜索有可能在非叶子结点结束；
4. 其搜索性能等价于在关键字全集内做一次二分查找；；

**b+树：**

b+树，是b树的一种变体，查询性能更好。m阶的b+树的特征：

1. 有n棵子树的非叶子结点中含有n个关键字（b树是n-1个），这些关键字不保存数据，只用来索引，所有数据都保存在叶子节点（b树是每个关键字都保存数据）。
2. 所有的叶子结点中包含了全部关键字的信息，及指向含这些关键字记录的指针，且叶子结点本身依关键字的大小自小而大顺序链接。
3. 所有的非叶子结点可以看成是索引部分，结点中仅含其子树中的最大（或最小）关键字。
4. 通常在b+树上有两个头指针，一个指向根结点，一个指向关键字最小的叶子结点。
5. 同一个数字会在不同节点中重复出现。

**b+树对比b树的查询优势：**

1. b+树的中间节点不保存数据，所以磁盘页能容纳更多节点元素，更“矮胖”；
2. b+树查询必须查找到叶子节点，b树只要匹配到即可不用管元素位置，因此b+树查找更稳定（并不慢）；
3. 对于范围查找来说，b+树只需遍历叶子节点链表即可，b树却需要重复地中序遍历；（基于范围查询是很普遍的）

```
关于二分查找和二叉树的理解：
（1）二分查找：即折半查找，优点是比较次数少，查找速度快，平均性能好；其缺点是要求待查表为有序表，且插入删除困难
（2）二叉查找树，它或者是一棵空树，或者若它的左子树不空，则左子树上所有结点的值均小于它的根结点的值；
若它的右子树不空，则右子树上所有结点的值均大于它的根结点的值； 它的左、右子树也分别为二叉排序树。
（3）平衡二叉树：避免数据全部在一端时的情况，强制转成平衡的。
```



### 1.5 sql优化

• MySQL慢查询怎么解决？使用explain优化sql和索引？
• 查询语句不同元素（where、jion、limit、group by、having等等）执行先后顺序

#### 1.5.1 explain介绍

explain后得到的结果主要有：id、select_type、table、type、possible_keys、key、key_len、rows、Extra。

**需要强调rows是explain的核心指标，绝大部分rows小的语句执行一定很快。所以优化语句基本上都是在优化rows。**

**另一个explain的重要指标是type，一般要求达到range以上，最好能达到ref。**

其它重点列的简单解释如下:

- select_type：表示 SELECT 的类型，常见的取值有 

  • SIMPLE：简单表,即不使用表连接或者子查询、
  • PRIMARY：主查询，即外层的查询、
  ​• UNION：UNION 中的第二个或者后面的查询语句、
  ​• SUBQUERY：子查询中的第一个 SELECT。

- table：输出结果集的表。

- **rows：扫描行的数量。**

- **type：表示表的连接类型，性能由好到差的连接类型为**

  • system（表中仅有一行,即常量表）
  • const（单表中最多有一个匹配行,例如 primary key 或者 unique index）
  • eq_ref（对于前面的每一行,在此表中只查询一条记录,简单来说,就是多表连接中使用 primary key 或者 unique index)
  • ref（与 eq_ref 类似,区别在于不是使用 primary key 或者 unique index,而是使用普通的索引）
  • ref_or_null（与 ref 类似,区别在于条件中包含对 NULL 的查询）
  • index_merge（索引合并优化）
  • unique_subquery（in的后面是一个查询主键字段的子查询）
  • index_subquery（与 unique_subquery 类似,区别在于 in 的后面是查询非唯一索引字段的子查询）
  • range （单表中的范围查询）
  • index（对于前面的每一行,都通过查询索引来得到数据）
  • all（对于前面的每一行,都通过全表扫描来得到数据）

- possible_keys：表示查询时,可能使用的索引。

- key：表示实际使用的索引。

- key_len：索引字段的长度。

- Extra：执行情况的说明和描述。

#### 1.5.2 慢查询优化

1. 先运行看看是否真的很慢，注意设置SQL_NO_CACHE
2. where条件单表查，锁定最小返回记录表。这句话的意思是把查询语句的where都应用到表中返回的记录数最小的表开始查起，单表每个字段分别查询，看哪个字段的区分度最高。
3. explain查看执行计划，是否与1预期一致（从锁定记录较少的表开始查询）
4. order by limit 形式的sql语句让排序的表优先查
5. 了解业务方使用场景
6. 加索引时参照建索引的几大原则
7. 观察结果，不符合预期继续从1分析

```
可以参考：https://tech.meituan.com/2014/06/30/mysql-index.html
```

### 1.6 数据库崩溃恢复

• 数据库崩溃时事务的恢复机制（REDO日志和UNDO日志）？

InnoDB拥有内部恢复机制，假如数据库崩溃了，InnoDB通过从最后一个时间戳开始运行日志文件，来尝试修复数据库。大多数情况下会修复成功，而且整个过程是透明的。即InnoDB实现了一套完善的崩溃恢复机制，保证在任何状态下（包括在崩溃恢复状态下）数据库挂了，都能正常恢复，这个是与文件系统最大的差别。

**崩溃恢复**：用户修改了数据，并且收到了成功的消息，然而对数据库来说，可能这个时候修改后的数据还没有落盘，如果这时候数据库挂了，重启后，数据库需要从日志中把这些修改后的数据给捞出来，重新写入磁盘，保证用户的数据不丢。这个从日志中捞数据的过程就是崩溃恢复的主要任务，也可以成为数据库前滚。当然，在崩溃恢复中还需要回滚没有提交的事务，提交没有提交成功的事务。**由于回滚操作需要undo日志的支持，undo日志的完整性和可靠性需要redo日志来保证，所以崩溃恢复先做redo前滚，然后做undo回滚**。

```
redo日志：现代数据库都需要写redo日志，例如修改一条数据，首先写redo日志，然后再写数据。在写完redo日志后，就直接给客户端返回成功。

undo日志：数据库还提供类似撤销的功能，当你发现修改错一些数据时，可以使用rollback指令回滚之前的操作。这个功能需要undo日志来支持。

innodb_fast_shutdown：这个默认值=1，表示在MySQL关闭的时候，仅仅把日志和数据刷盘。= 0。这个表示在MySQL关闭的时候，执行slow shutdown，不但包括日志的刷盘，数据页的刷盘，还包括数据的清理(purge)，ibuf的合并，buffer pool dump以及lazy table drop操作。 = 2时表示关闭的时候，仅仅日志刷盘，其他什么都不做，就好像MySQL crash了一样。 这个参数值越大，MySQL关闭的速度越快，但是启动速度越慢，相当于把关闭时候需要做的工作挪到了崩溃恢复上。
```

恢复方法：

```
一旦确定MySQL因为InnoDB表损坏无法启动时，就可以按照以下5步进行修复：
1.编辑/etc/my.cnf文件，加入如下行:
innodb_force_recovery = 4
2.这时就可以重新启动数据库了，在innodb_force_recovery配置的作用，所有的插入与更新操作将被忽略;
3.导出所有的数据表;
4.关闭数据库并删除所有数据表文件及目录，再运行 mysql_install_db来创建MySQL默认数据表;
5.在/etc/my.cnf中删除innodb_force_recovery这一行，再启动MySQL（这时MySQL正常启动）;
6.从第3步备份的文件中恢复所有的数据。

innodb_force_recovery可以设置为1-6,大的数字包含前面所有数字的影响。当设置参数值大于0后，可以对表进行select,create,drop操作,但insert,update或者delete这类操作是不允许的。 
1(SRV_FORCE_IGNORE_CORRUPT):忽略检查到的corrupt页。
2(SRV_FORCE_NO_BACKGROUND):阻止主线程的运行，如主线程需要执行full purge操作，会导致crash。
3(SRV_FORCE_NO_TRX_UNDO):不执行事务回滚操作。
4(SRV_FORCE_NO_IBUF_MERGE):不执行插入缓冲的合并操作。
5(SRV_FORCE_NO_UNDO_LOG_SCAN):不查看重做日志，InnoDB存储引擎会将未提交的事务视为已提交。
6(SRV_FORCE_NO_LOG_REDO):不执行前滚的操作。
```



### 1.7 主从复制

• 数据库的读写分离如何实现？
• 主从复制，主从复制分析的 7 个问题、主从复制

**原理：**

复制过程中一个服务器充当主服务器，而一个或多个其它服务器充当从服务器。mysql复制基于主服务器在二进制日志（binlog）中跟踪所有对数据库的更改(更新、删除等等)。因此，要进行复制，必须在主服务器上启用二进制日志。每个从服务器从主服务器接收主服务器已经记录到的二进制日志，获取日志信息更新。通过设置在Master上的binlog，使其处于打开状态；Slave通过一个I/O线程从Master上读取binlog，然后传输到Slave的中继日志（relaylog）中，然后使用SQL线程读取中继日志，并应用到自身数据库中，从而实现主从数据同步功能。

```
即mysql主从复制需要三个线程：
1. 主：binlog线程——记录下所有改变了数据库数据的语句，放进master上的binlog中；
2. 从：io线程——在使用start slave 之后，负责从master上拉取 binlog 内容，放进 自己的relay log中；
3. 从：sql执行线程——执行relay log中的语句；
```



### 1.8 读写分离

方式1：通过MyCAT 实现读写分离

方式2：通过MaxScale实现读写分离 



### 1.9 分库分表

• 数据库优化，最左原则啊，水平分表，垂直分表

**基础概念**

- **分表**，能够解决单表数据量过大带来的查询效率下降的问题；
- **分库**，面对高并发的读写访问，当数据库master服务器无法承载写操作压力时，不管如何扩展slave服务器，此时都没有意义。此时，则需要通过数据分库策略，提高数据库并发访问能力。
- **优点**，分库、分表技术优化了数据存储方式，有效减小数据库服务器的负担、缩短查询响应时间。


- **数据分库、分表存储场景条件**
  - 关系型数据库
  - 主从架构(master-slave)
  - 单表数据量在百万、千万级别
  - 数据库面临极高的并发访问
- 分库、分表实现策略
  - 关键字取模，实现对数据访问进行路由


•	什么是临时表，临时表什么时候删除?
•	非关系型数据库和关系型数据库区别，优势比较？
•	什么是 内连接、外连接、交叉连接、笛卡尔积等？
•	varchar和char的使用场景？
•	mysql 高并发环境解决方案？




# 三、框架

## 1.Spring相关

### 1.1 Spring功能模块

**Spring有七大功能模块，分别是Spring Core，AOP，ORM，DAO，MVC，WEB，Context。 **

**1.Spring Core ：**Core模块是Spring的核心类库，Spring的所有功能都依赖于该类库，Core主要实现IOC功能，Sprign的所有功能都是借助IOC实现的。

**2.AOP： **AOP模块是Spring的AOP库，提供了AOP（拦截器）机制，并提供常用的拦截器，供用户自定义和配置。 

**3.ORM：**Spring 的ORM模块提供对常用的ORM框架的管理和辅助支持，Spring支持常用的Hibernate，ibtas，jdao等框架的支持，Spring本身并不对ORM进行实现，仅对常见的ORM框架进行封装，并对其进行管理。

**4.DAO模块：**Spring 提供对JDBC的支持，对JDBC进行封装，允许JDBC使用Spring资源，并能统一管理JDBC事物，并不对JDBC进行实现。

**5.WEB模块：**WEB模块提供对常见框架如Struts1，Struts 2，JSF的支持，Spring能够管理这些框架，将Spring的资源注入给框架，也能在这些框架的前后插入拦截器。 

**6.Context模块：**Context模块提供框架式的Bean访问方式，其他程序可以通过Context访问Spring的Bean资源，相当于资源注入。 

**7.MVC模块：**WEB MVC模块为Spring提供了一套轻量级的MVC实现。


### 1.2 IOC

• Spring IOC 的理解，其初始化过程？
• 如果存在A依赖B，B依赖A，那么是怎么加到IOC中去的。（用setter注入）
• 如果要你实现Spring IOC，你会注意哪些问题？

**IOC概念**

**IOC（Inversion of Control），意为控制反转，指的就是获得依赖对象的过程被反转了。**

系统是由各个对象构成的，各个对象之间通过相互合作，最终实现系统地业务逻辑。对象之间的耦合关系是无法避免的，也是必要的，但是我们要尽可能的减少他们之间的耦合度，就是我们常说的”高内聚、低耦合”。在没有使用IOC容器之前，对象A依赖于对象B，自己必须主动去创建对象B或者使用已经创建的对象B。无论是创建还是使用对象B，控制权都在自己手上。引入IOC容器之后，对象A与对象B之间失去了直接联系，当对象A运行到需要对象B的时候，IOC容器会主动创建一个对象B注入到对象A需要的地方。对象A获得依赖对象B的过程，由主动行为变为了被动行为，控制权颠倒过来了，这就是“控制反转”这个名称的由来。

**IOC实现原理**

实现IOC的方法：依赖注入。

理解DI的关键是：“谁依赖谁，为什么需要依赖，谁注入谁，注入了什么”，那我们来深入分析一下：

　　●谁依赖于谁：当然是应用程序依赖于IoC容器；

　　●为什么需要依赖：应用程序需要IoC容器来提供对象需要的外部资源；

　　●谁注入谁：很明显是IoC容器注入应用程序某个对象，应用程序依赖的对象；

　　●注入了什么：就是注入某个对象所需要的外部资源（包括对象、资源、常量数据）。

所谓依赖注入（DI），就是由IOC容器在运行期间，动态地将某种依赖关系注入到程序之中。

Spring通过DI（依赖注入）实现IOC（控制反转），常用的注入方式主要有三种：

1. 构造方法注入
2. setter注入
3. 基于注解的注入

```
Spring支持setter注入和构造器注入。（在xml中<bean>中指定constructor-arg方式或property，或者autowired）
```

IoC 不是一种技术，只是一种思想，它实际用到的是Java的“反射”技术。

依赖注入（DI）和控制反转（IOC）只是从不同的角度的描述的同一件事情，就是指通过引入IOC容器，利用依赖关系注入的方式，实现对象之间的解耦。

**IOC缺点**

- 生成对象的步骤变得有些复杂，会感觉系统变得不太直观。
- 由于IOC容器生成对象是通过反射方式，在运行效率上有一定的损耗。
- 具体到IOC框架产品来讲，需要进行一些额外的配制工作，客观上也可能加大一些工作成本。
- 如果团队成员对于IOC框架产品缺乏深入的理解，那么会影响到整个项目，所以这也是一个隐性的风险。

**IoC容器的初始化过程**

- 第一个过程是：BeanDefinition的Resource定位
- 第二个过程是：BeanDefinition的载入和解析
- 第三个过程是：BeanDefinitionI在IoC容器中的注册

**容器初始化步骤图：**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190901215207616.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0ZyYW1lX1g=,size_16,color_FFFFFF,t_70)

一个被@Component等组件修饰的类，就会被交给spring进行管理，这也就是bean对象的创建；bean对象都会放在IOC容器里进行配置和存放bean，因为IOC里面有两个Map， 上面的Map意思为存放bean的配置信息（工厂原材料），下面的Map存放的为bean 的实例信息，（工厂中的成品对象），下面的实例对象创建之后会被放到BeanFactory里面，当外界调用时，就会在BeanFactory里面拿，如果没有，beanFactory就会在上图的map里面拿配置信息，配置的实例就又会存放到下图的map中，然后在传给BeanFactory；（创建过程可参考：1.4.2 Bean加载过程）


### 1.3 AOP

• 如果要你实现Spring AOP，请问怎么实现？
• Spring AOP 和 AspectJ AOP 区别

**AOP概念**

AOP，即面向切面编程（Aspect Oriented Programming），AOP的核心思想就是“将应用程序中的业务逻辑跟系统支持的通用服务进行分离”，应用对象只需要专注于完成业务逻辑，它们并不负责（甚至是意识）其它的系统级关注点，例如日志或事务支持。

Spring用代理类包裹切面，把他们织入到Spring管理的bean中。也就是说代理类伪装成目标类，它会截取对目标类中方法的调用，让调用者对目标类的调用都先变成调用伪装类，伪装类中就先执行了切面，再把调用转发给真正的目标bean。

**AOP实现方式：**

通过代理的方式实现。即Jdk动态代理和CGLib动态代理。

- Jdk动态代理

  JDK动态代理是面向接口的代理模式。如果要被代理的对象是个实现类，Spring默认会使用JDK动态代理。原理是代理类实现`InvocationHandler`接口，并通过`InvocationHandler`的 `invoke()`方法反射来调用目标类中的代码来实现AOP。

- CGLib动态代理

  CGLib动态代理是面向类的代理模式。如果要被代理的对象不是实现类，那么Spring会选择使用CGLIB动态代理。原理是Spring在运行期间通过 CGlib继承要被动态代理的类，重写父类的方法，实现AOP。也因为CGLIB是通过继承的方式做的动态代理，因此如果某个类被标记为final，那么它是无法使用CGLIB做动态代理的。

```
关于两者之间的性能的话，在以前的JDK版本中，JDK动态代理性能并不是很高，高版本中二者就差不多了。
对于singleton的代理对象，因为无需频繁的创建代理对象，所以比较适合采用CGLib动态代理，反之，则比较适用JDK动态代理。
```

**Spring AOP 和 AspectJ AOP 区别**

Spring AOP 属于运行时增强，而 AspectJ 是编译时增强。 Spring AOP 基于代理，而 AspectJ 基于字节码操作。

Spring AOP 已经集成了 AspectJ ，AspectJ 应该算的上是 Java 生态系统中最完整的 AOP 框架了。AspectJ 相比于 Spring AOP 功能更加强大，但是 Spring AOP 相对来说更简单。

如果我们的切面比较少，那么两者性能差异不大。但是，当切面太多的话，最好选择 AspectJ ，它比Spring AOP 快很多。

```
Join point 就是菜单上的选项，Pointcut就是你选的菜。Join point 你只是你切面中可以切的那些方法，一旦你选择了要切哪些方法，那就是Pointcut。
advice就是你作用到pointcut上的方式，你可以使用befor, after 或者around。
advisor就是作用在具体对象上的ponitcut和advice，把ponitcut和advice合起来就是advisor。切到哪个具体方法上面，是使用的befor、after 还是around，就这个就叫advisor.
```

### 1.4 BeanFactory

• beanFactory的理解，怎么加载bean
• 基于注解的形式，是怎么实现的， 你知道其原理吗，说一下

#### **1.4.1 相关理解：**

- BeanFactory管理着IOC容器里面所有的Bean的实例，包含了Bean的实例化、状态变更、销毁过程的整个生命周期。
- BeanFactory默认单例，且采用延迟初始化策略（lazy-load）。
- BeanFactory是接口，具体实现靠其子类如 DefaultListableBeanFactory、ApplicationContext、XmlBeanFactory等。
- BeanFactory的对象依赖注入的三种方式：构造方法注入、setter注入、基于注解的注入

#### **1.4.2 Bean生命周期**

• Spring Bean 的生命周期，如何被管理的？
• Spring Bean 的加载过程是怎样的？

其生命周期详细描述如下：

- spring对bean实例化；
- spring将值和bean的引用注入到bean对应的属性中；
- 如果bean实现了BeanNameAware接口，spring将bean的id传递给setBeanName()方法，该方法的作用就是**设置bean的id到BeanFactory中方便创建该bean，同时让bean知道自己在BeanFactory配置中的名字**；
- 如果bean实现了BeanFactoryAware接口，spring将调用setBeanFactroy()方法，将BeanFactory容器实例传入，**便于bean够获取配置他们的BeanFactory的引用**；
- 如果bean实现了ApplicationContextAware接口，spring将调用setApplicationContext()方法，将bean所在应用上下文的引用传进来，**便于bean获取它所在的Spring容器**；
- 如果bean实现了BeanPostProcessor接口，spring将调用它的postProcessBeforeInitialization()方法，该方法在bean初始化之前调用；
- 如果bean实现了InitializingBean接口，spring将调用它的afterPropertiesSet()方法，当bean的所用属性被设置完成之后调用该方法；
- 如果bean实现了BeanPostProcessor接口，spring将调用它的postProcessAfterInitialization()方法，在bean初始化完成之后调用；
- 如果bean实现了DisposableBean接口，spring将调用它的destory()方法，在容器关闭时；

```
步骤：实例化->注入属性->setBeanName()->setBeanClassLoader()->setBeanFactory()-> setApplicationContext()-> postProcessBeforeInitialization() -> afterPropertiesSet()-> postProcessAfterInitialization() -> destory()
```

Spring 允许 Bean 在初始化完成后以及销毁前执行特定的操作。下面是常用的三种指定特定操作的方法：

	• 通过实现InitializingBean/DisposableBean 接口来定制初始化之后/销毁之前的操作方法； 
	• 通过<bean> 元素的init-method/destroy-method属性指定初始化之后 /销毁之前调用的操作方法；
	• 在指定方法上加上@PostConstruct或@PreDestroy注解来制定该方法是在初始化之后还是销毁之前调用
Bean在实例化的过程中：Constructor > @PostConstruct >InitializingBean > init-method
Bean在销毁的过程中：@PreDestroy > DisposableBean > destroy-method
注意scope为singleton的bean的destroy方法则是在容器关闭时执行，而scope为prototype的bean是不会执行destroy方法的。
• 对于关闭Spring IoC容器，web应用可通过ContextLoaderListener的contextDestroyed()实现。非web应用可通过AbstractApplicationContext的registerShutdownHook()和destroy()实现，等于在JVM里注册了关闭钩子shutdown hook。

#### 1.4.3 FactoryBean

BeanFactory：工厂，用于生成任意bean。

FactoryBean：通过FactoryBean的工厂接口，用户可以通过实现该接口定制实例化的bean。

```java
//按照传统的方式，则需要在<bean>中提供大量的配置信息，配置的灵活度受限，这时候采用编码的方式可能更简单一点。
public interface FactoryBean<T> {
    @Nullable
    T getObject() throws Exception;
    @Nullable
    Class<?> getObjectType();
    default boolean isSingleton() {return true;}
}
//使用时：
public class StudentFactoryBean implements FactoryBean<Student> { 
	@Override
    public Student getObject() throws Exception {
        Student student = new Student();
        student.setName(studentInfo.split(",")[0]);
        return student;
    }
  	@Override
    public Class<?> getObjectType() {
        return Student.class;
    }
}
<bean id="student" class="top.mcwebsite.bean.StudentFactoryBean" p:studentInfo="张三,计科1901,男" />
```



#### 1.4.4 Scope

作用域（scope）：用于确定Spring创建Bean的实例个数。

类别有以下几种：

- singleTon：默认值，在Spring IOC容器中仅存在一个Bean实例，Bean以单例方式存在。
- prototype：每次从容器中调用Bean时，都会返回一个新的实例。（每次getBean()等于new XxxBean()）
- request：每次HTP请求都会创建一个新的Bean，该作用域仅适用于 Webapplication Context环境
- session：同一个HTTP Session共享一个Bean，不同Session使用不同Bean，仅适用于 Webapplication Context环境
- globalsession：一般用于 Portlet应用环境，该作用域仅适用于Webapplication Context环境

```
• Spring 通过 ConcurrentHashMap 实现单例注册表的特殊方式实现单例模式。Spring管理的Bean对象默认是单例模式，当多线程操作Bean对象时就会出现线程安全问题；因为在多线程中线程改变了bean对象的可变成员变量时，其他线程就无法访问该bean对象的初始状态，这样就造成数据错乱。所以需要用线程同步来处理这个问题。如使用ThreadLocal/syn、或者改为prototype。
• scope为prototype的bean，容器会将创建好的对象实例返回给请求方，之后，容器就不再拥有其引用，请求方需要自己负责当前对象后继生命周期的管理工作，包括该对象的销毁。
• scope为singleton的bean的destroy方法则是在容器关闭时执行，而scope为prototype的bean是不会执行destroy方法的。
```

#### 1.4.5 Bean依赖

• 依赖冲突，有碰到过吗，你是怎么解决的~

• Spring 循环注入的原理？

对于普通依赖，在创建javaBean的时候，首先需要先实例化其依赖的beanDependency，然后再实例化其自身。

对于循环依赖，就要分情况了，Spring 只解决单例模式下的循环依赖，对于原型模式的循环依赖则是抛出 BeanCurrentlyInCreationException 异常。

```
• Spring是先调用构造函数进行实例化，然后填充属性，再接着进行其他附加操作和初始化，是通过这样来解决循环依赖。也就是说Spring在实例化一个bean的时候，是首先递归的实例化其所依赖的所有bean，直到某个bean没有依赖其他bean，此时就会将该实例返回，然后反递归的将获取到的bean设置为各个上层bean的属性的。
• 上述是基于setter注入，如果是基于构造函数的注入，如果有循环依赖，Spring也是不能够解决的。
```



### 1.5 ApplicationContext

• BeanFactory 和 ApplicationContext区别？

**相关理解：**

ApplicationContext继承了BeanFactory接口，拥有BeanFactory的全部功能，它默认每次容器启动时初始化所有对象。另外它又扩展了很多高级特性：

- MessageSource，提供国际化的消息访问 
- 资源访问，如URL和文件。
- 事件传播 
- 载入多个上下文 ，使得每一个上下文都专注于一个特定的层次，比如应用的web层; 

**几乎所有的应用场合我们都直接使用ApplicationContext 而非底层的BeanFactory。**

```
创建ApplicationContext的方法： 
1. 从类路径下加载配置文件：ClassPathXmlApplicationContext ("applicationContext.xml");
2. 从硬盘绝对路径下加载配置文件：FileSystemXmlApplicationContext(“d:/xxx/yyy/xxx”);
```

```java
//BeanFactory提供的6个方法
boolean containsBean(String beanName);
Object getBean(String);
Object getBean(String, Class);
class getType(String name);
boolean isSingleton(String);
String[] getAliases(String name);
```



### 1.6 自动装配

• spring中的自动装配方式

**装配方式：**

基于xml配置文件中的autowire来实现的spring自动装配，主要有以下五种具体的实现方式：

- no —— 默认情况，装配方式为手动装配，即通过ref手动设定

- byName —— 根据属性名称自动装配

- byType——按照数据类型进行自动装配

- constructor ——通过构造函数参数的byType方式。

- autodetect —— 如果找到默认的构造函数，使用“自动装配用构造”; 否则，使用“按类型自动装配”。（已经遗弃了）

默认情况下，Spring是不进行自动装配的。我们可以在xml中，设置beans标签的default-autowire属性为byName，byType等，来设置所有bean都进行自动装配。常用的注解@Autowired默认是按照byType进行注入的。

```
如：<bean id="userServiceImpl" class="com.service.impl.UserServiceImpl" autowire="byXXXX"></bean>  
若autowire="byType"意思是通过 class="com.service.impl.UserServiceImpl"来查找UserDaoImpl下所有的对象；
若autowire="byName"意思是通过 id="userDao"来查找Bean中的userDao对象；
好处：大幅度减少Spring配置 
坏处：依赖不能明确管理，可能会有多个bean同时符合注入规则，没有清晰的依赖关系。
```

**@Autowired 与@Resource的区别：**

1. @Autowired与@Resource都可以用来装配bean. 都可以写在字段上,或写在setter方法上。
2. @Autowired默认按类型装配（这个注解是属业spring的），默认情况下必须要求依赖对象必须存在，如果要允许null值，可以设置它的required属性为false，如：@Autowired(required=false) ，如果我们想使用名称装配可以结合@Qualifier注解进行使用。
3. @Resource（这个注解属于J2EE的），默认按照名称进行装配，名称可以通过name属性进行指定，如果没有指定name属性，当注解写在字段上时，默认取字段名进行安装名称查找，如果注解写在setter方法上默认取属性名进行装配。当找不到与名称匹配的bean时才按照类型进行装配。但是需要注意的是，如果name属性一旦指定，就只会按照名称进行装配。



### 1.7 Spring事务

•	Spring 是如何管理事务的，事务管理机制？
•	Spring 的不同事务传播行为有哪些，干什么用的？

什么是事务：对数据的操作，要么一起成功，要么一起失败。

Spring事务的主要内容：事务特性（4种：ACID），隔离级别（5种：defalut，读提交...），传播行为（7种：required...）

#### 1.7.1 事务管理机制

**Spring并不直接管理事务，而是提供了多种事务管理器，他们将事务管理的职责委托给JDBC，Hibernate，JPA/JTA等持久化框架的事务来实现。**（Jdbc面向sql，JPA/JTA等是直接操作对象来实现持久化）

Spring事务管理器的接口是`PlatformTransactionManager`，通过这个接口，Spring为各个平台提供对应的事务管理器，然后具体的实现就是各个平台自己的事情了。例如JDBC的持久化就是通过`DataSourceTransactionManager`来实现。实际上，DataSourceTransactionManager是通过调用java.sql.Connection来管理事务，而后者是通过DataSource获取到的。通过调用连接的commit()方法来提交事务，同样，事务失败则通过调用rollback()方法进行回滚。

`DataSourceTransactionManager`继承`AbstractPlatformTransactionManager`，后者实现`PlatformTransactionManager`

![这里写图片描述](http://img.blog.csdn.net/20160324011156424)

```java
@Bean(name = "midTransactionManager")
@Primary
public DataSourceTransactionManager getTransactionManager(@Qualifier("midDatasource") DataSource dataSource) {
	return new DataSourceTransactionManager(dataSource);
}
```
一个完整的配置mybatis多数据源的例子：

```java
@Configuration
// 这里的basePackages只能扫描mapper，如果把servie也扫进去，重复生成实例，报错
@MapperScan(basePackages = {"com.digitalchina.manage.mid.mapper*" }, sqlSessionTemplateRef = "midSqlSessionTemplate")
public class MidDbConfig {

	// 配置配置数据源
	@Bean(name = "midDatasource")
	@Primary
	public DataSource getDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	// 配置SessionFactory 交给spring 容器管理，可以用Mybatis或MybatisPlus的工厂
	@Bean(name = "midSqlSessionFactory")
	@Primary
	public SqlSessionFactory getSqlSessionFactory(@Qualifier("midDatasource") DataSource dataSource,
			@Autowired Environment env) throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = MyBatisPlusGlobalConfig.createSqlSessionFactory(env);
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
				.getResources("classpath*:com/digitalchina/manage/*/mapper/xml/*Mapper.xml"));
		return sqlSessionFactory.getObject();
	}
	
	// 配置SqlSessionTemplate，是Mybatis-Spring的关键。可通过SqlSessionFactory作为构造方法的参数来创建
	@Bean(name = "midSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate getSqlSessionTemplate(
			@Qualifier("midSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	// 配置事务管理器
	@Bean(name = "midTransactionManager")
	@Primary
	public DataSourceTransactionManager getTransactionManager(@Qualifier("midDatasource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
```

#### 1.7.2 事务属性

事务管理器接口`PlatformTransactionManager`通过`getTransaction(TransactionDefinition definition)`方法来得到事务，这个方法里面的参数是`TransactionDefinition`类，这个类就定义了一些基本的事务属性。这些事务属性就包括了五点：**传播行为、隔离级别、事务超时、是否只读、回滚规则。**

**1.7.2.1 传播行为**-propagation

事务的第一个方面是传播行为（propagation behavior），指的就是当一个事务方法被另一个事务方法调用时，这个事务方法应该如何进行。 例如：methodA事务方法调用methodB事务方法时，methodB是继续在调用者methodA的事务中运行呢，还是为自己开启一个新事务运行，这就是由methodB的事务传播行为决定的。Spring定义了七种传播行为：

- PROPAGATION_REQUIRED：表示当前方法必须运行在事务中。如果当前事务存在，方法将会在该事务中运行。否则，会启动一个新的事务，这是默认的传播行为。
- PROPAGATION_SUPPORTS：表示当前方法不需要事务上下文，但当前存在事务的话，那么该方法会在这个事务中运行
- PROPAGATION_MANDATORY：表示该方法必须在事务中运行，如果当前事务不存在，则会抛出一个异常
- PROPAGATION_REQUIRED_NEW：表示当前方法必须运行在它自己的事务中。一个新的事务将被启动。如果存在当前事务，在该方法执行期间，当前事务会被挂起。若使用JTATransactionManager的话，则需要访问TransactionManager
- PROPAGATION_NOT_SUPPORTED：表示该方法不应该运行在事务中。如果存在当前事务，在该方法运行期间，当前事务将被挂起。如果使用JTATransactionManager的话，则需要访问TransactionManager
- PROPAGATION_NEVER：表示当前方法不应该运行在事务上下文中。如果当前正有一个事务在运行，则会抛出异常
- PROPAGATION_NESTED：表示如果当前已经存在一个事务，那么该方法将会在嵌套事务中运行。嵌套的事务可以独立于当前事务进行单独地提交或回滚。如果当前事务不存在，那么其行为与PROPAGATION_REQUIRED一样。注意各厂商对这种传播行为的支持是有所差异的。（嵌套时父事务会建立一个回滚点，叫save point，父事务会回滚到进入子事务前建立的save point，然后尝试其他的事务或者其他的业务逻辑，父事务之前的操作不会受到影响，更不会自动回滚。如果子事务回滚，父事务会回滚到进入子事务前建立的save point，然后尝试其他的事务或者其他的业务逻辑，父事务之前的操作不会受到影响，更不会自动回滚。如果父事务回滚，会发生什么？如果父事务回滚，子事务也会跟着回滚）

**1.7.2.2 隔离级别**-isolation

DEFAULT这是一个PlatfromTransactionManager默认的隔离级别，使用数据库默认的事务隔离级别；
读未提交（read uncommited）：脏读，不可重复读，虚读都有可能发生
读提交（read commited）：避免脏读。但是不可重复读和虚读都有可能发生；
可重复读（repeatable read）：避免脏读和不可重复读，但是虚读有可能发生；
串行化的（serializable）：避免以上所有读问题。

```
MySQL默认：可重复读，Oracle默认：读提交
```

**1.7.2.3 事务超时**-timeout
事物的超时指的是设置一个时间，当执行时间超过这个时间后，抛异常并回滚。是通过设置一个截至时间来实现的。

**1.7.2.4 是否只读**-readOnly

spring 事物的只读属性是通过设置，java.sql.Connection 的 readOnly 属性来实现的。**当设置了只读属性后，数据库会对只读事物进行一些优化**，如不启用回滚段，不启用回滚日志等。同时值得注意的是，设置了只读属性后，如果进行写操作会报错。

**1.7.2.4 回滚规则**-rollbackFor

回滚规则只得是spring 事物遇到哪种异常会回滚。默认只回滚RuntimeException和Error。

类似rollbackFor的属性还有rollbackForClassName，noRollbackFor，noRollbackForClassName 。都是类似的作用。

#### 1.7.3 事务使用

spring中使用事务有两种方式，一种是编程式事务，一种是声明式事务。

- 编程式事务推荐使用TransactionTemplate，实现TransactionCallback接口，需要编码实现；
- 声明式事务只需要在函数增加注解@Transactional，无需任何配置，代码入侵较小，使用AOP原理，推荐使用声明式事务，在应用启动类上记得加上@EnableTransactionManagement注解哟。

```java
//声明式编程实例：
@Transactional(value = "midTransactionManager", rollbackFor = Exception.class, propagation = ...)
public void testTransaction(){...}

//编程式事务实例：
@Autowired
DataSourceTransactionManager transactionManager;
public void testTransaction() {
  DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
  definition.setName("testTransaction");
  definition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
  //设置状态点
  TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
  try{
    // do something
    // 手动提交
    transactionManager.commit(transactionStatus);
  } catch (Exception e) {
    //手动回滚
    transactionManager.rollback(transactionStatus);
  }
}
```



### 1.8 Spring设计模式

•  Spring 中用到了那些设计模式？

主要用到了①工厂模式（BeanFactory）、②单例模式（SingleTon）、③代理模式（JDK/CGlib）、④观察者模式（Listener）、⑤模版方法模式（JdbcTemplate）、⑥委派模式（DispatcherHandler）、⑦适配器模式（HandlerAdaptor）；

其它还有⑧策略模式（Resource）、⑨装饰者模式（Datasource）、⑩ 建造者模式（AbstractApplicationContext）

**工厂模式：**

​	Spring的IOC容器的实现，就是一个大的工厂模式的实现。BeanFactory工厂管理着Spring容器里面所有的Bean的实例，包含了Bean的实例化、状态变更、销毁过程的整个生命周期。如BeanFactory。

```
好处：当我们用到的类现在需要更改了，就直接在工厂中把这个类改一下然后所有调用到这个类的地方都不用动
```

**单例模式：**

​	保证一个类仅有一个实例，并提供一个访问它的全局访问点。Spring下默认的bean均为singleton。Spring对单例的底层实现没有用到饿汉式单例和懒汉式单例，spring中的单例是通过单例注册表实现的，就是双重校验锁。如SingleTon。

```java
private final Map<String, Object> singletonObjects = new ConcurrentHashMap(256);
private final Map<String, Object> earlySingletonObjects = new HashMap(16);
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap(16);
/**单例的获取顺序是singletonObjects —> earlySingletonObjects —> singletonFactories 这样的三级层次。
Spring中创建单例的过程还是很绕的，最终是将我们需要的对象放在map中，下次需要的时候就直接从map中获取。
Spring解决相互引用的方式称为三级缓存的方式，即整个流程涉及到三个变量（都是Map）：
singletonObjects：一级缓存，存储beanName（key）和bean实例（value）之间的关系，这里存储的bean实例是已经完全创建完成的bean实例
earlySingletonObjects：二级缓存，也是存储beanName和bean实例之间的关系，注意和singletonObjects的区别，这里存储的bean实例是没有创建完成的bean实例，即该bean还在创建过程中，为了解决循环引用的问题，将未创建完全的bean缓存起来。
singletonFactories：三级缓存，用于保存beanName和bean工厂之间的关系。当三级缓存创建bean成功后，会将bean放入二级缓存，并将beanName对应的beanFactory从singletonFactories中移除。*/
```

**代理模式：**

​	静态代理：在编译器就完成，就是一个接口，然后一个实现类和一个代理类同时实现这个接口。代理类里面，就包含了实现类的具体逻辑，还有它作为代理需要做的一些逻辑。

​	动态代理：在运行期才完成，Spring实现AOP功能的原理就使用代理模式（1、JDK动态代理。2、CGLib字节码生成技术代理。）对类进行方法级别的切面增强，即，生成被代理类的代理类， 并在代理类的方法前，设置拦截器，通过执行拦截器中的内容增强了代理方法的功能，实现的面向切面编程。如JdkDynamicAopProxy和Cglib2AopProxy。

```
JDK，CGLib。如果类是final，则只能使用JDK动态代理。
```

**观察者模式：**

​	定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
Spring中观察者模式常用的地方是listener的实现。如ApplicationListener。

**装饰者模式：**

​	动态的给一个对象添加一些额外的职责。装饰者设计模式可以动态地给对象增加些额外的属性或行为。相比于使用继承，装饰者模式更加灵活。Spring 中配置DataSource的时候，DataSource可能是不同的数据库和数据源。我们能否根据客户的需求在少修改原有类的代码下切换不同的数据源？这个时候据需要用到装饰者模式。

```
代理模式和装饰者模式都是在自身对象中保存[对被代理/被装饰者]对象的引用。但装饰者模式注重对对象功能的扩展，它不关心外界如何调用，只注重对对象功能的加强，装饰后还是对象本身。而代理模式注重对对象某一功能的流程把控和辅助，重心是为了借用对象的功能完成某一流程，而非对象功能如何。
```

**模板方法模式：**

​	定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。Template Method使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。如JdbcTemplate、RedisTemplate等。

**策略模式：**

​	Spring 框架的资源访问接口就是基于策略设计模式实现的。Spring框架本身大量使用了Resource接口来访问底层资源。Resource接口本身没有提供访问任何底层资源的实现逻辑，针对不同的额底层资源，Spring将会提供不同的Resource实现类，不同的实现类负责不同的资源访问类型；如UrlResource、ClassPathResource、FileSystemResource

定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换。Spring框架本身大量使用了Resource接口来访问底层资源。Resource接口本身没有提供访问任何底层资源的实现逻辑，针对不同的额底层资源，Spring将会提供不同的Resource实现类，不同的实现类负责不同的资源访问类型；如UrlResource、ClassPathResource、FileSystemResource

等，再如Spring中在实例化对象的时候用到Strategy模式。如SimpleInstantiationStrategy

**委派模式：**

​	Spring 中委派模式确实是用得比较多的一种模式，这个模式不关心过程，只关心结果。Spring MVC框架中的DispatcherServlet其实就用了委派模式，也有人称委派模式是代理模式和策略模式的组合。

**适配器模式：**

​	适配器设计模式将一个接口转换成客户希望的另一个接口，适配器模式使得接口不兼容的那些类可以一起工作。在Spring MVC中，DispatcherServlet根据请求信息调用HandlerMapping，解析请求对应的Handler，解析到对应的Handler（也就是我们常说的Controller控制器）后，开始由HandlerAdapter适配器处理。为什么要在Spring MVC中使用适配器模式？Spring MVC中的Controller种类众多不同类型的Controller通过不同的方法来对请求进行处理，有利于代码的维护拓展。

**建造者模式：**

​	Spring容器的启动过程，实际上就是Bean的实例化过程。在整个过程中，AbstractApplicationContext就是充当了Builder模式中的Director的角色，分别操作不同的Builder来实现不同类型Bean的实例化与注册过程。



## 2.SpringMvc相关

### 2.1 SpringMvc运行流程

第一步：发起请求到前端控制器(DispatcherServlet)

第二步：前端控制器请求HandlerMapping查找 Handler （可以根据xml配置、注解进行查找）

第三步：处理器映射器HandlerMapping向前端控制器返回Handler，HandlerMapping会把请求映射为HandlerExecutionChain对象（包含一个Handler处理器（页面控制器）对象，多个HandlerInterceptor拦截器对象），通过这种策略模式，很容易添加新的映射策略

第四步：前端控制器调用处理器适配器去执行Handler

第五步：处理器适配器HandlerAdapter将会根据适配的结果去执行Handler

第六步：Handler执行完成给适配器返回ModelAndView

第七步：处理器适配器向前端控制器返回ModelAndView （ModelAndView是springmvc框架的一个底层对象，包括 Model和view）

第八步：前端控制器请求视图解析器去进行视图解析 （根据逻辑视图名解析成真正的视图(jsp)），通过这种策略很容易更换其他视图技术，只需要更改视图解析器即可

第九步：视图解析器向前端控制器返回View

第十步：前端控制器进行视图渲染 （视图渲染将模型数据(在ModelAndView对象中)填充到request域）

第十一步：前端控制器向用户响应结果

**springMVC中的几个组件：**

DispatcherServlet：前端控制器，接收请求，响应结果，相当于电脑的CPU。

HandlerMapping：处理器映射器，根据URL去查找处理器。

Handler：处理器，需要程序员去写代码处理逻辑的，即我们的Controller。

HandlerAdapter：处理器适配器，会把处理器包装成适配器，这样就可以支持多种类型的处理器，类比笔记本的适配器（适配器模式的应用）

ViewResovler：视图解析器，进行视图解析，对返回的字符串，进行处理，可以解析成对应的页面



> • 为什么有了springmvc还要在项目中使用spring？
>
> Spring是IOC和AOP的容器框架，SpringMVC是基于Spring功能之上添加的Web框架，想用SpringMVC必先依赖Spring



### 2.2 DispatcherServlet

• DispatcherServlet的工作原理

```java
DispatcherServlet extends FrameworkServlet  extends HttpServletBean extends HttpServlet
```

DispatcherServlet是一个派发器，即对任意一个web请求都会根据一定的规则派发到对应的处理器上处理，并最终将结果返回。它实现了Request to Handler的路由，而我们只需要实现处理器的逻辑，大大简化了开发代码复杂度和耦合度。

- DispatcherServlet的初始化


- DispatcherServlet对web请求的处理

**DispatcherServlet的初始化**

​	DispatcherServlet最终继承的是HttpServlet，也就是说它同样满足Servlet的工作原理。DispatcherServlet类初始化时主要完成了一件事，就是从用户定义的application context中加载自定义的bean或者从默认文件中加载各种bean，用于初始化HandlerMapping，HandlerAdapter，HandlerExceptionResolver等bean对象。初始化时默认会先从已经初始化好的context中根据默认的beanName调用getBean(String name, Class<T> requiredType)方法获取，如果没有则设置为null。

**DispatcherServlet对web请求的处理**
​	任何一个请求都会有对应的一个servlet来处理，是通过调用其service(ServletRequest req, ServletResponse res)方法来实现的，这个方法实现了对一个请求的具体响应处理逻辑。HttpServlet 提供了 doGet()、doPost() 等方法，DispatcherServlet 中这些方法是在其父类 FrameworkServlet 中实现的。对于DispatcherServlet，Dispatch(request, response)方法包含了真正处理一个请求的核心逻辑。执行步骤包含以下几步：

- 根据已初始化的handlerMappings和request，获取该请求对应的HandlerExecutionChain对象；
- 依次调用HandlerExecutionChain维护的interceptors， 执行handlerInterceptor的preHandle()方法。这里需要强调的是，如果某个interceptor.preHandle()处理失败，就直接短路，所以handler是不会被执行的，而是通过exceptionResolver处理异常并返回给客户端；
- 如果interceptor都执行成功，通过handlerAdaptors获取到该请求对应的HandlerAdaptor, 并执行handler；
- 执行完毕后，执行interceptor.postHandle()方法，如果需要返回view, 则配置ModelAndView, 并渲染view对象。



### 2.3 SpringMvc对象包装

• 如何自动包装对象：HandlerMethodArgumentResolver、HandlerMethodReturnValueHandler

#### **方法中的参数解析**

在Handler（Controller）中进行方法参数值解析，是通过**HandlerMethodArgumentResolver**接口。

准确是说是通过它具体的实现类，它们针对不同的注解实现不同的解析绑定功能：

1. **RequestParamMethodArgumentResolver**

   支持带有@RequestParam注解的参数或带有MultipartFile类型的参数。

   ```
   注意：如果controller的方法没有任何参数的注解，但有参数对象而且参数对象的类型是简单类型，则会默认调用RequestParamMethodArgumentResolver类进行解析，从httpServletRequest中获取parameter，然后调用参数对象的set方法进行注入。
   ```

2. RequestParamMapMethodArgumentResolver

    支持带有@RequestParam注解的参数 && @RequestParam注解的属性value存在 && 参数类型是实现Map接口的属性

3. PathVariableMethodArgumentResolver

   支持带有@PathVariable注解的参数 且如果参数实现了Map接口，@PathVariable注解需带有value属性

4. MatrixVariableMethodArgumentResolver

   支持带有@MatrixVariable注解的参数 且如果参数实现了Map接口，@MatrixVariable注解需带有value属性 

5. RequestResponseBodyMethodProcessor

   解析绑定带有@RequestBody的方法参数和带有@ResponseBody的方法返回值

6. ServletRequestMethodArgumentResolver

   参数类型是实现或继承或是WebRequest、ServletRequest、MultipartRequest、HttpSession、Principal、Locale、TimeZone、InputStream、Reader、HttpMethod这些类。

7. ServletResponseMethodArgumentResolver

    参数类型是实现或继承或是ServletResponse、OutputStream、Writer这些类

8. RedirectAttributesMethodArgumentResolver

    参数是实现了RedirectAttributes接口的类

9. HttpEntityMethodProcessor

    **参数类型是HttpEntity**

```
@RequestBody是作用在形参列表上，用于将前台发送过来固定格式的数据【xml 格式或者 json等】封装为对应的 JavaBean 对象，封装时使用到的一个对象是系统默认配置的 HttpMessageConverter进行解析，然后封装到形参上。GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交。使用@RequestBody注解时，是用于接收Content-Type为application/json类型的请求,数据类型是JSON：{"aaa":"111","bbb":"222"}，不使用@RequestBody注解时，可接收Content-Type为application/x-www-form-urlencoded类型的请求所提交的数据，数据格式：aaa=111&bbb=222  ,form表单提交以及jQuery的.post()方法所发送的请求就是这种类型。
@RequestParam(xxx)在参数前的话，前端必须有对应的xxx名字才行(不管是否有值，可通过required属性来调节是否必须传)
@ResponseBody 表示该方法的返回结果直接写入 HTTP response body 中
可由MappingJackson2HttpMessageConverter来处理@ResponseBody返回的对象，对返回的json做进一步处理（类型、格式等）
```

#### 方法中的返回值解析

**方法返回值处理接口HandlerMethodReturnValueHandler**

下面是一些具体的实现类，它们针对不同的注解或返回值类型实现不同的功能：

1. **ModelAndViewMethodReturnValueHandler**

   **返回值类型是ModelAndView或其子类**


2. ModelMethodProcessor

   返回值类型是Model或其子类


3. ViewMethodReturnValueHandler

   返回值类型是View或其子类 


4. HttpHeadersReturnValueHandler

   返回值类型是HttpHeaders或其子类  


5. ModelAttributeMethodProcessor

   返回值有@ModelAttribute注解


6. ViewNameMethodReturnValueHandler

   返回值是void或String，并且返回值没有注解

7. **RequestResponseBodyMethodProcessor**

   **返回值有@ResponseBody注解**



```
• 如何保证 Controller 并发的安全？
首先，在使用Spring开发项目时默认情况下Controller、Service、DAO都是单例模式。正因为单例所以不是线程安全的，所以Controller不要使用非静态的成员变量，否则会发生数据逻辑混乱。单例是不安全的，会导致属性重复使用。
解决方案
1、不要在controller中定义成员变量。
2、万一必须要定义一个非静态成员变量时候，则通过注解@Scope(“prototype”)，将其设置为多例模式。
3、在Controller中使用ThreadLocal变量
```


• 怎么让mapper 和xml对应

```java
@Bean(name = "midSqlSessionFactory")
	public SqlSessionFactory getSqlSessionFactory(@Qualifier("midDatasource") DataSource dataSource,
			@Autowired Environment env) throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = MyBatisPlusGlobalConfig.createSqlSessionFactory(env);
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setTypeHandlersPackage("com.digitalchina.event.utils");
		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
				.getResources("classpath*:com/digitalchina/event//midwarn/mapper/xml/*Mapper.xml"));
		return sqlSessionFactory.getObject();
	}
```



## 3.SpringBoot相关

### 3.1 spring/boot区别

简而言之，Spring框架为开发Java应用程序提供了全面的基础架构支持。它包含了很多简化开发的功能，如依赖注入和开箱即用的模块，如：Spring JDBC、Spring MVC、Spring Security、Spring AOP、Spring ORM、Spring Test等，这些模块可以大大缩短应用程序的开发时间。但是Spring还是存在一个问题，就是一些配置还是很复杂的。Spring Boot基本上是Spring框架的扩展，它消除了设置Spring应用程序所需的复杂例行配置。它的目标和Spring的目标是一致的，就是能够实现更快、更高效的开发。

SpringBoot最大的特点如下：

- 简化配置：约定大于配置，不再需要配置xml文件，SpringBoot第三方库几乎可以是零配置的开箱即用。
- 简化开发：比如我们要创建一个 web 项目，我们只需要在 pom 文件中添加如下一个 starter-web 依赖即可。
- 简化测试：Spring Boot 内置了多种强大的测试框架（junit、springTest、AssertJ..），满足我们日常项目的测试需求。
- 简化部署：Spring Boot 内嵌了 tomcat，让我们打包和部署都变得很容易。
- 简化监控：比如我们要创建实现对web项目的监控，我们只需要在 pom 文件中添加如下一个 starter-actuator 依赖即可

```
微服务是未来发展的趋势，项目会从传统架构慢慢转向微服务架构，因为微服务可以使不同的团队专注于更小范围的工作职责、使用独立的技术、更安全更频繁地部署。Spring Boot也是官方大力推荐的技术，可以看出Spring Boot是未来发展的一个大趋势。
```



### 3.2 starter种类

1. spring-boot-starter-web：web开发
2. spring-boot-starter-test：测试
3. spring-boot-starter-data-jdbc：数据库
4. spring-boot-starter-security：安全
5. spring-boot-starter-aop：切面
6. spring-boot-starter-data-redis：redis相关
7. spring-boot-starter-cache：缓存
8. spring-boot-starter-data-mongodb：缓存
9. spring-boot-starter-thymeleaf：模版引擎
10. spring-boot-starter-freemarker：模版引擎
11. spring-boot-starter-data-elasticsearch：搜索引擎
12. spring-boot-starter-activemq：消息队列（activemq）
13. spring-boot-starter-amqp：消息队列（rabbitmq）
14. spring-boot-starter-actuator：监控，可以看到应用的容器中所有 bean 信息、health信息、env信息等。

### 3.3 starter原理

stater机制帮我们完成了项目起步所需要的的相关jar包。

如在mybatis-spring-boot-starter 中，并没有任何源码，只有一个pom文件，它的作用就是帮我们引入了相关jar包。最重要的mybatis-spring-boot-autoconfigure类，里面通过@Configuration&、@Bean两个配置类，自动帮我们生成了SqlSessionFactory、SqlSessionTemplate这些Mybatis的重要实例并交给spring容器管理，从而完成bean的自动注册。自动配置是有依赖条件的，常见的条件依赖注解有：@ConditionalOnBean（仅在当前上下文中存在某个bean时，才会实例化这个Bean）、@ConditionalOnMissingBean（仅在当前上下文中不存在某个bean时，才会实例化这个Bean）等，所以要完成Mybatis的自动配置，需要在类路径中存在SqlSessionFactory.class、SqlSessionFactoryBean.class这两个类，**需要存在DataSource这个bean且这个bean完成自动注册。**spring-boot-autoconfigure-2.0.4.RELEASE.jar这个包，自动配置这个包帮们引入了jdbc、kafka、logging、mail、mongo等包。很多包需要我们引入相应jar后自动配置才生效。像引入了mybatis的jar包后，DataSource这个自动配置才生效，才能够自动配置SqlSessionFactory和SqlSessionFactoryBean这些类。autoconfigure包里面，就有DataSourceAutoConfiguration这个类，在DataSourceAutoConfiguration类里面，使用了@EnableConfigurationProperties这个注解，且使用了注解@ConfigurationProperties指定了配置文件的前缀为spring.datasource，这两个注解一起作用能够把yml或者properties配置文件转化为bean。

```
@SpringBootApplication包含@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan；
  - @Configuration的作用上面我们已经知道了，被注解的类将成为一个bean配置类。
  - @ComponentScan的作用就是自动扫描并加载符合条件的组件，比如@Component和@Repository等，最终将这些bean定义加载到spring容器中。
  - @EnableAutoConfiguration 这个注解的功能很重要，借助@Import的支持，收集和注册依赖包中相关的bean定义。
```

```
spring-boot-starter-xxx是官方提供的starter，xxx-spring-boot-starter是第三方提供的starter。
```

**总结**

我们可以将自动配置的关键几步以及相应的注解总结如下：

1、@Configuration&与@Bean->基于java代码的bean配置

2、@Conditional->设置自动配置条件依赖

3、@EnableConfigurationProperties与@ConfigurationProperties->读取配置文件转换为bean。

4、@EnableAutoConfiguration、@AutoConfigurationPackage 与@Import->实现bean发现与加载。

```
bean加载
如果要让一个普通类交给Spring容器管理，通常有以下方法：
1、使用 @Configuration与@Bean 注解
2、使用@Controller @Service @Repository @Component 注解标注该类，然后启用@ComponentScan自动扫描
3、使用@Import 方法
```



> 微服务的缺点：
>
> 1）开发人员要处理分布式系统的复杂性，
> 2）多服务运维难度增加，
> 3）系统部署依赖，
> 4）服务间通信成本增加，
> 5）数据一致性、系统集成测试、性能检测等等



## 4.SpringCloud相关

### 4.1 SpringCloud五大组件

• springcloud组件

Spring Cloud，提供了搭建分布式系统及微服务常用的工具，如配置管理、服务发现、断路器、智能路由、微代理、控制总线、一次性token、全局锁、选主、分布式会话和集群状态等，满足了构建微服务所需的所有解决方案。

Spring Cloud的五大组件如下：

1. 服务发现—— Eureka / Zookeeper
2. 分布式配置——Spring Cloud Config
3. 服务网关—— Zuul / Gateway
4. 客服端负载均衡—— Ribbon/Feign
5. 断路器—— Hystrix



### 4.2 Eureka

**Eruka概述**

​	微服务架构的关键原则之一就是 “服务发现”，消费者从注册中心发现服务，生产者向注册中心注册服务，任何一个服务既可以是消费者，也可以是生产者，注册中心自身也不除外。**有了服务的注册和发现，只需要使用服务的标识符，就可以访问到服务。**而不需要修改服务调用的配置文件了。

​	Eureka分为服务端和客户端，服务端作为服务注册中心提供服务注册功能，让客户端注册服务到注册中心。实现注册中心时只需要在项目的启动类EurekaServerApplication上使用@EnableEurekaServer注解即可（一般要禁用自己去注册）。注册中心启动后，每一个实例注册（使用@EnableEurekaClient）之后需要向注册中心发送心跳，当client向server注册时，它会提供一些元数据，例如主机和端口，URL，主页等。Eureka server 从每个client实例接收心跳消息。 如果心跳超时，则通常将该实例从注册server中删除。

**为什么要用注册中心：**

​	微服务应用和机器越来越多，调用方需要知道接口的网络地址，如果靠配置文件的方式去控制网络地址，对于动态新增机器，维护带来很大问题。主流的注册中心：Eureka、zookeeper、consul、etcd 等。

​	通过注册中心，可以根据服务名字调用服务的ip地址，可以实现动态微服务调用效果，它不会因为更换电脑而出错，Eureka只是维护了服务生产者、注册中心、服务消费者三者之间的关系，真正的服务消费者调用服务生产者提供的数据是通过Spring Cloud Ribbon或者Feign来实现的。

**Eureka/Zookeeper比较**

- Eureka取CAP的AP，注重可用性，Zookeeper取CAP的CP注重一致性，对于注册中心而言，可用性更重要。
- Zookeeper在选举期间注册服务瘫痪，虽然服务最终会恢复，但选举期间不可用。
- eureka的自我保护机制，会导致一个结果就是不会再从注册列表移除因长时间没收到心跳而过期的服务。依然能接受新服务的注册和查询请求，但不会被同步到其他节点。不会服务瘫痪。
- Zookeeper有Leader和Follower角色，Eureka各个节点平等。
- Zookeeper采用过半数存活原则，Eureka采用自我保护机制解决分区问题。
- Eureka本质是一个工程，Zookeeper只是一个进程。

```
当Eureka Server 节点在短时间内丢失了过多实例的连接时（比如网络故障或频繁启动关闭客户端）节点会进入自我保护模式，保护注册信息，不再删除注册数据，故障恢复时，自动退出自我保护模式。
```

```
Spring WebFlux是Spring Framework 5.0中引入的新的反应式Web框架。 与Spring MVC不同，它不需要Servlet API，完全异步和非阻塞。它使用的是事件驱动模型，对不同的事件做不同的处理。
WebFlux有两个东西：Mono 和 Flux 适用于两个场景，即： 
 Mono：实现发布者，并返回 0 或 1 个元素，即单对象。 
 Flux：实现发布者，并返回 N 个元素，即 List 列表对象。 
 有人会问，这为啥不直接返回对象，比如返回 City/Long/List。 
 原因是，直接使用 Flux 和 Mono 是非阻塞写法，相当于回调方式。 
 利用函数式可以减少了回调，因此会看不到相关接口。这恰恰是 WebFlux 的好处：集合了非阻塞 + 异步
```



### 4.3 SpringCloud Config

​	微服务面临的配置问题：系统中有大量服务，每个服务都需要必要的配置信息才能运行。所以一套集中式的，动态的配置管理设施是必不可少的。SpringCloud提供了ConfigServer来解决这个问题。SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为**各个不同微服务应用**的所有环境提供了一个**中心化的外部配置**。SpringCloud Config分为服务端和客户端两部分。服务端：也称为**分布式配置中心，它是一个独立的微服务应用**，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口。客户端：通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息。配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理，并可以通过git客户端来方便地管理和访问配置内容。

```properties
#服务端
spring:
  application:
    name: service-config
  profiles:
    active: native,dev
  cloud:
    config:
      server:
        native:
          searchLocations: classpath:/config
#客户端
spring:
  application:
    name: myclient
  profiles:
    active: dev
  cloud:
    # 配置服务器的地址
    config:
      discovery:
        enabled: true
        service-id: service-config
      # 可以使用之前的版本。默认值可以是git label, branch name or commit id。多个Label可以使用逗号分隔
      #label: 
      # true: 如果访问配置中心失败，则停止启动服务
      fail-fast: true
```



### 4.4 Zuul/Gateway

**Zuul**

​	功能（代理+路由+过滤），**zuul包含了对请求的路由和过滤两个最主要的功能**：其中路由功能负责将外部请求转发到具体的微服务实例上，是实现外部访问统一入口的基础；过滤器功能则负责对请求的过程进行干预，是实现请求校验，服务聚合的功能的基础。zuul与Eureka整合，将它自身注册为Eureka服务治理下的应用，同时从Eureka中获取其他微服务的信息，即以后的微服务都是通过zuul跳转后获得的。

**Gateway**

​	Spring Cloud Gateway是Spring Cloud官方推出的第二代网关框架，取代Zuul网关。除了网关常见的功能路由转发、权限校验外，还具有**限流控制、负载均衡**等作用。且gateway很好的支持异步，而zuul仅支持同步。

**区别：**

​	两者均是web网关，处理的是http请求。gateway对比zuul多依赖了spring-webflux，在spring的支持下，功能更强大，内部实现了限流、负载均衡等，扩展性也更强，但同时也限制了仅适合于Spring Cloud套件，而zuul则可以扩展至其他微服务框架中，其内部没有实现限流、负载均衡等。gateway很好的支持异步，而zuul仅支持同步



### 4.5 Ribbon/Feign

**Ribbon**

Ribbon是一个基于 HTTP 和 TCP 客户端的负载均衡器

它可以在客户端配置 ribbonServerList（服务端列表），然后轮询请求以实现均衡负载（@LoadBalanced注解实现）

它在联合 Eureka 使用时，ribbonServerList 会从 Eureka 注册中心获取服务端列表，同时将职责委托给 Eureka 来确定服务端是否已经启动。

**Feign**

Feign默认集成了Ribbon。

同时它还支持Hystrix和它的Fallback;

Feign是一个使用起来更加方便的 HTTP客户端，它用起來就好像调用本地方法一樣，完全感觉不到是在调用远程方法。

```
如果是用Ribbon，调用是如：restTemplate.getForEntity("http://COMPUTE-SERVICE/add?a=10&b=20", String.class).getBody()这样的语句，这样进行服务间调用并非不可以，只是我们在服务化的过程中，希望跨服务调用能够看起来像本地调用，这也是我理解的Feign的使用场景。使用Feign时只需要：@FeignClient(value = "xxx")即可。
```

**区别**

在Spring cloud 中服务之间通过restful方式调用有两种方式  ① restTemplate+Ribbon，②Feign。

Ribbon和Feign说到底都是直接调用接口，主要是用来解决服务之间相互调用的问题。然后使用Feign方式的话就更优雅更简单，就像本地函数调用一样。

```
ribbon是对服务之间调用做负载，是服务之间的负载均衡，zuul是可以对外部请求做负载均衡。 
```

### 4.6 Hystrix

由于在微服务系统中微服务与微服务之间会由很多相互的调用，如果调用时某个微服务节点故障，就会调用失败，而熔断器的作用就是当出现远程调用失败的时候提供一种机制来保证程序的正常运行而不会卡死在某一次调用，类似Java程序中的try-catch结构，而只有当异常发生的时候才会进入catch的代码块。它是通过注解@HystrixCommand(fallbackMethod = "fallback") 来实现的，在falllback方法中实现相应的服务降级逻辑。

```java
//Feign中使用Hystrix
@FeignClient(value = "hello-service",fallback = HelloServiceFallback.class)
//Feign中不适用Hystrix
@FeignClient(name = "eureka-client-demo", configuration = DisableHystrix.class)
```

断路器状态：

- 全开：一段时间内，达到一定的次数无法调用，并且多次监测没有恢复的迹象，断路器完全打开，那么下次请求就不会请求到该服务
- 半开：短时间内，有恢复迹象，断路器会将部分请求发给该服务，正常调用时，断路器关闭
- 关闭：当服务一直处于正常状态，能正常调用

### 4.7 SpringCloud其它组件

消息队列：rabbitmq/kafka/activemq

分布式锁：redis实现和consul实现

日志统计：logback+ELK

本地缓存：guava cache

链路跟踪：zipkin、brave

安全鉴权：auth2、openId connect

自动化构建与部署：gitlab + jenkins + docker + k8s



## 5.Mybatis相关

### 5.1 mybatis原理



### 5.2 **Dao映射mapper原理？**

​	Dao接口即Mapper接口。接口的全限名，就是映射文件中的namespace的值；接口的方法名，就是映射文件中Mapper的Statement的id值；接口方法内的参数，就是传递给sql的参数。当调用接口方法时，接口全限名+方法名拼接字符串作为key值，可唯一定位一个MapperStatement。在Mybatis中，每一个<select>、<insert>、<update>、<delete>标签，都会被解析为一个MapperStatement对象。

​	Mapper接口里的方法，是不能重载的，因为是使用 全限名+方法名 的保存和寻找策略。Mapper 接口的工作原理是JDK动态代理，Mybatis运行时会使用JDK动态代理为Mapper接口生成代理对象proxy，代理对象会拦截接口方法，转而执行MapperStatement所代表的sql，然后将sql执行结果返回。



###5.3 #{}和${}的区别是什么？

\#{}是预编译处理，${}是字符串替换。

Mybatis在处理#{}时，会将sql中的#{}替换为?号，调用PreparedStatement的set方法来赋值；

Mybatis在处理${}时，就是把${}替换成变量的值。

使用#{}可以有效的防止SQL注入，提高系统安全性。



### 5.4 Mybatis的一级、二级缓存

①、一级缓存指的就是sqlsession，在sqlsession中有一个数据区域，是map结构，这个区域就是一级缓存区域。一级缓存中的key是由sql语句、条件、statement等信息组成一个唯一值。一级缓存中的value，就是查询出的结果对象。如果查完后，增删改操作，清空缓存。不同的sqlSession之间的缓存数据区域（HashMap）是互相不影响的。

②、二级缓存是mapper级别的缓存，多个SqlSession去操作同一个Mapper的sql语句，多个SqlSession可以共用二级缓存，二级缓存是跨SqlSession的。可以通过<cache/>;来开启二级缓存。另外可以使用useCache="false"来不适用缓存，建议避免使用二级缓存。如针对一个表的某些操作不在他独立的namespace下进行，如在UserMapper.xml中做了刷新缓存的操作，但在XXXMapper.xml中缓存仍然有效，这样可能会有问题。所以所用缓存的话可以用redis。

```java
public interface SqlSession extends Closeable {
    <T> T selectOne(String var1);
	<E> List<E> selectList(String var1);
  	int insert(String var1);
  	...
}
```



### 5.5 Mybatis的插件运行原理

答：Mybatis仅可以编写针对ParameterHandler、ResultSetHandler、StatementHandler、Executor这4种接口的插件，Mybatis使用JDK的动态代理，为需要拦截的接口生成代理对象以实现接口方法拦截功能，每当执行这4种接口对象的方法时，就会进入拦截方法，具体就是InvocationHandler的invoke()方法，当然，只会拦截那些你指定需要拦截的方法。

编写插件：实现Mybatis的Interceptor接口并复写intercept()方法，然后在给插件编写注解，指定要拦截哪一个接口的哪些方法即可，记住，别忘了在配置文件中配置你编写的插件。
