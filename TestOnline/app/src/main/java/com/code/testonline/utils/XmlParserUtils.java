package com.code.testonline.utils;

import android.util.Log;
import android.util.Xml;

import com.code.testonline.bean.QuestionBean;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * xml解析工具
 */
public class XmlParserUtils {

    private static final String Tag = "XmlParserUtils";

    /**
     * 解析xml
     *
     * @param filePath 下载后保存在手机中的路径
     * @return 题目集合
     */
    public static List<QuestionBean> parser(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) { //文件不存在，返回null
            return null;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            //获取Xml的pull解析器
            XmlPullParser xp = Xml.newPullParser();

            //指定使用什么编码去解析xml文件
            xp.setInput(fis, "utf-8");

            //开始解析
            //需要判断当前解析到什么标签，因为解析到不同的标签需要做不同的操作
            int type = xp.getEventType();

            List<QuestionBean> mList = new ArrayList<>();
            QuestionBean bean = null;

            //循环解析
            while (type != XmlPullParser.END_DOCUMENT) { // XmlPullParser.END_DOCUMENT 文档结束标签
                switch (type) {
                    case XmlPullParser.START_TAG: //开始标签 如：<questions>等
                        if ("questions".equals(xp.getName())) { //遇到<questions>就创建集合
                            mList = new ArrayList<>();
                        } else if ("question".equals(xp.getName())) { //遇到<question>就创建QuestionBean对象
                            bean = new QuestionBean();
                        } else if ("id".equals(xp.getName())) {
                            if (null != bean)
                                bean.id = xp.nextText();//获取当前节点的下一个节点的文本，并把指针移动至当前节点的结束节点
                        } else if ("description".equals(xp.getName())) {
                            if (null != bean) bean.description = xp.nextText();
                        } else if ("answer".equals(xp.getName())) {
                            if (null != bean) bean.answer = xp.nextText();
                        } else if ("choices".equals(xp.getName())) { //遇到<question>就创建选项集合
                            if (null != bean) bean.choices = new ArrayList<>();
                        } else if ("choice_1".equals(xp.getName())) {
                            if (null != bean) bean.choices.add(xp.nextText());
                        } else if ("choice_2".equals(xp.getName())) {
                            if (null != bean) bean.choices.add(xp.nextText());
                        } else if ("choice_3".equals(xp.getName())) {
                            if (null != bean) bean.choices.add(xp.nextText());
                        } else if ("choice_4".equals(xp.getName())) {
                            if (null != bean) bean.choices.add(xp.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG: //结束标签 如：</questions>等
                        if ("question".equals(xp.getName())) { //遇到</question>说明一个题目解析完，放入集合中
                            if (null != bean) mList.add(bean);
                        }
                        break;
                }
                //指针移动至下一个节点，并且获取它的事件类型
                type = xp.next();
            }
            //返回解析结果
            return mList;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Tag, "解析失败");
        }

        return null;
    }
}
