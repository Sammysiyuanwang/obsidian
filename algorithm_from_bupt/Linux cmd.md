# Linux shell cmd

总结一下linux shell中常用的命令：


### shell 基本命令

#### 拷贝命令：cp

* `cp [option] source destination`

#### 移动命令：mv

* `mv [option] source destination`
* 可以用来将文件改名或者移动到别处

#### 创建文件夹和删除空文件夹：mkdir , rmdir

* `mkdir [option] dir-name`
* `rmdir [option] dir-name` 必须是空文件夹

#### 改变工作目录：cd

#### 列出目录：ls

#### 改变权限: su

* `su [option] 使用者账号`

#### 显示进程命令：PS

* `ps [option]`

#### kill 进程： kill

* `kill [option] PID`

#### 搜索命令：grep

* `grep 字符串`

例：查看所有CMD里是java的进程：

* 使用BSD格式显示所有进程 `ps -aux | grep java`
* 使用标准格式显示所有进程 `ps -ef | grep java`

#### 显示命令：echo

* `echo 字符串` 在显示器上显示一段文字

#### 清楚屏幕信息：clear

### shell基本知识

#### 系统变量：

* $0: 该程序名字
* $n: 该程序的第n个参数值
* $*: 该程序所有参数
* $#: 该程序参数的个数
* $$: 该程序PID

#### 从键盘输入变量值：

* read命令： `read var1, var2, ..., varn`

#### 比较：

* `-eq`：相等 equal
* `-ne`：不等 not equal
* `-gt`: 大于 greater than
* `-ge`: 大于等于 greater equal
* `-lt`: 小于 less than
* `-le`: 小于等于 less equal

#### 双引号及单引号：

`$echo "$HOME $PATH"`  -- 显示变量值

`/home/hbwork opt/kde/bin:/usr/local/bin:`

 `$echo '$HOME $PATH'`  -- 显示单引号里的内容
 
`$HOME $PATH`

### if-else语句：

#### 1. if...else

```
if [ expression ]
then
	statement to be executed if expression is true
fi
```

注意:expression和方括号之间必须有空格，分号是不同语句之间的分割符，如果一行只有一个语句，则不需加分号，见下例区别：

```
!/bin/zsh

echo "please print a"
read a
echo "please print b"
read b

if [ $a -eq $b ];then
    echo "a is equal to b"
fi

if [ $a -ne $b ]
then
    echo "a is not equal to b"
fi
```

#### 2. if...else...fi

```
if [ expression ]
then
   Statement(s) to be executed if expression is true
else
   Statement(s) to be executed if expression is not true
fi
```

#### 3. if...elif...if

```
if [ expression 1 ]
then
   Statement(s) to be executed if expression 1 is true
elif [ expression 2 ]
then
   Statement(s) to be executed if expression 2 is true
elif [ expression 3 ]
then
   Statement(s) to be executed if expression 3 is true
else
   Statement(s) to be executed if no expression is true
fi
```
#### 4. 另外：

`test`命令用于检查某个条件是否成立，与方括号类似

因此if...else语句可以写成一行以命令行的方式运行：
`if test $[2*3] -eq $[1+5]; then echo 'The two numbers are equal!'; fi;`

或者：

```
num1=$[2*3]
num2=$[1+5]
if test $[num1] -eq $[num2]
then
    echo 'The two numbers are equal!'
else
    echo 'The two numbers are not equal!'
fi
```

### for loop

```
for 变量 in 列表
do
    command1
    command2
    ...
    commandN
done
```

列表是你想遍历的一组值组成的序列，每个值通过**空格**分隔。

in列表是可选的，如果不用它，for循环使用命令行的位置参数。

```
for loop in 1 2 3 4 5
do
    echo "The value is: $loop"
done
```

### while loop

```
while command
do
   Statement(s) to be executed if command is true
done
```

命令执行完毕，控制返回循环顶部，从头开始直至测试条件为假

例：leetcode 195 tenth line: 输出文件的第10行

```
cnt=0
while read line && [ $cnt -le 10 ]; do
  let 'cnt = cnt + 1'
  if [ $cnt -eq 10 ]; then
    echo $line
    exit 0
  fi
done < file.txt
```
let 为做运算

最后`<`为重定向，将file.txt传给while