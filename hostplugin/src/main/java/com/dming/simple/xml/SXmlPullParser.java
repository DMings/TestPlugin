package com.dming.simple.xml;

import android.content.res.XmlResourceParser;
import com.dming.simple.utils.DLog;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SXmlPullParser {

    public static XmlBean parse(XmlResourceParser xml) {
        XmlBean rootXmlBean = new XmlBean(null);
        XmlBean curXmlBean;
        try {
            int eventType = xml.getEventType();
            curXmlBean = rootXmlBean;
            while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case org.xmlpull.v1.XmlPullParser.START_DOCUMENT:
//                        DLog.i("START_DOCUMENT: " + xml.getName());
                        break;
                    case org.xmlpull.v1.XmlPullParser.START_TAG:
//                        DLog.i("START_TAG: " + xml.getName());
                        if (curXmlBean.name != null) { //二次进入
                            if (curXmlBean.closeTag) { // 完整标签分析完成，下一个兄弟
                                XmlBean xmlBean = new XmlBean(xml.getName());
                                if (curXmlBean.elderBrother == null) { // 当前是大兄长,小弟进来不会是null
                                    xmlBean.elderBrother = curXmlBean;
                                } else {
                                    xmlBean.elderBrother = curXmlBean.elderBrother; // 传递大兄长个下一个小弟
                                }
                                xmlBean.elderBrother.youngerBrother.add(xmlBean);
                                curXmlBean = xmlBean;
                            } else { // 下一个儿子
                                XmlBean xmlBean = new XmlBean(xml.getName());
                                curXmlBean.son = xmlBean;
                                xmlBean.father = curXmlBean;
                                curXmlBean = xmlBean;
                            }
                        } else {
                            curXmlBean.name = xml.getName(); // 首次进入
                        }
                        // 获取全部属性
                        for (int i = xml.getAttributeCount() - 1; i >= 0; i--) {
//                            DLog.d(xml.getAttributeName(i) + ": " + xml.getAttributeValue(i));
                            curXmlBean.attributeMap.put(xml.getAttributeName(i), xml.getAttributeValue(i));
                        }
                        break;
                    case org.xmlpull.v1.XmlPullParser.TEXT:
//                        DLog.i("TEXT: " + xml.getText());
                        curXmlBean.text = xml.getText();
                        break;
                    case org.xmlpull.v1.XmlPullParser.END_TAG:
//                        DLog.i("END_TAG: " + xml.getName());
                        if (curXmlBean.father != null) { // 找回爸爸
                            if (curXmlBean.father.name.equals(xml.getName())) {//当前标签是爸爸标签
                                curXmlBean = curXmlBean.father;
//                                DLog.i("father: " + curXmlBean.name);
                            }
                        } else if (curXmlBean.elderBrother != null) { // 自己是小弟，只能通过大兄弟找爸爸
                            if (curXmlBean.elderBrother.father != null) { // 找回爸爸
                                if (curXmlBean.elderBrother.father.name.equals(xml.getName())) {//当前标签是爸爸标签
                                    curXmlBean = curXmlBean.elderBrother.father;
//                                    DLog.i("->father: " + curXmlBean.name);
                                }
                            }
                        }
                        curXmlBean.closeTag = true;
                        break;
                    default:
                        break;
                }
                eventType = xml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootXmlBean;
    }

}
