package com.code.testonline.bean;

import java.util.List;

/**
 * 问题数据类
 */
public class QuestionBean {

    public String id; //id
    public String description; //题目描述
    public String answer; //答案 A，B，C，D中的一个

    public List<String> choices; //选项，有四个
}
