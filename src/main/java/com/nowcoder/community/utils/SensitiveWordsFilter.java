package com.nowcoder.community.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveWordsFilter {

    private static Logger logger = LoggerFactory.getLogger(SensitiveWordsFilter.class);

    //替换符
    private static final String REPLACEMENT = "***";
    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyWord;
            while((keyWord = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyWord(keyWord);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败：" + e.getMessage());
        }
    }


    //讲一个敏感词添加到前缀树中
    private void addKeyWord (String keyWord) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyWord.length(); i++) {
            char c = keyWord.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            //指向子节点，进入下一轮循环
            tempNode = subNode;

            //设置结束标识
            if (i == keyWord.length()-1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return     过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            //跳过符号
            if (isSymbol(c)) {
                //如果指针1处于根节点，就将此符号计入结果，让指针2继续走
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头还是中间，指针3都向下走一步
                position++;
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                //发现敏感词，将begin~position的字符串替换掉
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                //重新指向根节点
                tempNode = rootNode;
            } else {
                //检查下一个字符
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c) {
        //0x2E80~0x9FFF 是东亚文字范围
        return CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c >0x9FFF);
    }
    //前缀树
    private class TrieNode {
        //关键词结束标识
        private boolean isKeyWordEnd = false;

        //子节点(key是下级字符，value是下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点方法
        public void addSubNode(Character character, TrieNode node) {
            subNodes.put(character, node);
        }

        //获取子节点方法
        public TrieNode getSubNode(Character character) {
            return subNodes.get(character);
        }
    }
}
