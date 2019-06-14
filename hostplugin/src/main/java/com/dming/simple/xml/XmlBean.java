package com.dming.simple.xml;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlBean {

    public XmlBean father;
    public XmlBean son;
    public XmlBean elderBrother;

    public List<XmlBean> youngerBrother = new ArrayList<>();
    public HashMap<String, String> attributeMap = new HashMap<>();
    public boolean closeTag = false;
    public String name;
    public String text;

    public XmlBean(String name) {
        this.name = name;
    }

    public void setFather(XmlBean father) {
        this.father = father;
    }

    public void setSon(XmlBean son) {
        this.son = son;
    }

    public void setElderBrother(XmlBean elderBrother) {
        this.elderBrother = elderBrother;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public XmlBean getFather() {
        return father;
    }

    public XmlBean getSon() {
        return son;
    }

    public XmlBean getElderBrother() {
        return elderBrother;
    }

    @NonNull
    public List<XmlBean> getYoungerBrother() {
        return youngerBrother;
    }

    public HashMap<String, String> getAttributeMap() {
        return attributeMap;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
