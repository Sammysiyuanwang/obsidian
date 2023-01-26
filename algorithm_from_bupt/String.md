# String 

string类题的刷题总结：

### 3. Longest Substring Without Repeating Characters

题意：找最长的没有重复字符的子串

解法：使用一个字典存储字符和其索引（可用HashMap，我用的数组），然后两根指针i, j，`i`做字符串的遍历，`j`从i开始向后遍历，不断把后面的字符存入字典，当遇到字典中存过的字符时，j停止循环，记下此时长度，然后把i更新到之前出现过的重复字符的索引位置。即`eabcdbc`若开始i为`e`，当j指向第二个b时停止，此时长度为`j-i`，i更新到第一个b。需要**注意**的点就是考虑到所有字符都不同的情况，跳出循环时，j已经是s的长度之外的索引了，因此`s.charAt(j)`会报错，需要妥善处理这个边界，这时候i没得更新，直接更新成j，跳出大循环即可。

code:

```java
public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int i = 0;
        int ans = 1;
        while(i < s.length() - 1) {
            int[] char_set = new int[256];
            Arrays.fill(char_set, -1);
            char_set[(int)s.charAt(i)] = i;
            int j = i + 1;
            while (j < s.length() && char_set[(int)s.charAt(j)] == -1) {
                char_set[(int)s.charAt(j)] = j;
                j++;
            }
            int length = j - i;
            if (j == s.length()) {
                //这里，j已经大于s的索引了，直接i更新成j在大循环中跳出即可
                i = j;
            } else {
                i = char_set[(int)s.charAt(j)] + 1;
            }
            //System.out.print(j + "," + length + "\n");
            ans = Math.max(ans, length);
        }
        return ans;
    }
```





### 5. Longest Palindromic Substring

题意：找一个字符串的最长回文串并输出

解法：遍历字符串的每一个字符，从该字符两根指针分别往前和往后遍历，找最长的回文串。需要**注意**的一点为要妥善处理字符相同的情况，如果中间是`'aaaa'`那么应当从第一个`a`的前面一个和最后一个`a`的后面一个开始双指针对比，因为`aaaa`相同的在中间不论几个，一定是回文串

code:

```java
public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        if (s.length() == 1) {
            return s;
        }
        String longest = "";
        for (int i = 0; i < s.length() - 1; i++) {
            int j = i - 1;
            int k = i + 1;
            //寻找i位置有几个相同字符
            while (j >= 0 && s.charAt(j) == s.charAt(i)) {
                j--;
            }
            while (k <= s.length() - 1 && s.charAt(k) == s.charAt(i)) {
                k++;
            }
            while (j >= 0 && k <= s.length() - 1 && s.charAt(j) == s.charAt(k)) {
                j--;
                k++;
            }
            String tmp = s.substring(j + 1, k);
            if (tmp.length() > longest.length()) {
                longest = tmp;
            }
        }
        return longest;
    }
```

###  8. String to Integer (atoi)

题意：实现atoi函数，字符串转整数

解法：此题关键在于case的覆盖，共有4种情况：

- 判断空串
- 开头是空格的串，以及中间出现不是数字的字符
- 前面有正负号
- 数值越界的处理

code：

```java
public int myAtoi(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int flag = 1;
        int number = 0;
        int index = 0;
        //处理开头的空格
        while (str.charAt(index) == ' ') {
            index++;
        }
        //处理正负号
        if (str.charAt(index) == '-') {
            flag = -1;
            index++;
        } else if (str.charAt(index) == '+') {
            index++;
        }
        while (index < str.length()) {
            int s = str.charAt(index) - '0';
            //处理非数字字符
            if (s < 0 || s > 9) break;
            //处理数值越界问题
            if (number > Integer.MAX_VALUE / 10 || (number == Integer.MAX_VALUE /10 && s > 7)) {
                if (flag == 1) {
                    return Integer.MAX_VALUE;
                } else {
                    return Integer.MIN_VALUE;
                }
            }
            number = number * 10 + s;
            index++;
        }
        //记得把符号乘上
        number *= flag;
        return number;
    }
```