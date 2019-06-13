package com.dming.simple.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import com.dming.simple.SPlugin;
import com.dming.simple.utils.DLog;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecPlugin {

    public static final String RECEIVER_NAME = BroadcastReceiverDispatch.class.getName();
    public static String sPackageName;
    public final static String PLUGIN_RECEIVER_FLAG = "pluginReceiver";
    private static List<SubBroadcastReceiver> mBroadcastReceiverList = new ArrayList<>();

    public static void addSubBroadcastReceiver(SubBroadcastReceiver subBroadcastReceiver) {
        mBroadcastReceiverList.add(subBroadcastReceiver);
    }

    public static void clearBroadcastReceiverList(Context context) {
        unRegisterBroadcastReceiver(context);
        mBroadcastReceiverList.clear();
    }

    public static void registerBroadcastReceiver(Context context) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.registerReceiver(subReceiver.getReceiver(), subReceiver.getFilter() != null ?
                    subReceiver.getFilter() : new IntentFilter());
            DLog.i("subReceiver>" + subReceiver.getReceiver().getClass().getSimpleName());
        }
    }

    public static void unRegisterBroadcastReceiver(Context context) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            context.unregisterReceiver(subReceiver.getReceiver());
        }
    }

    public static void dealPluginReceiver(Context context, PackageInfo pInfo, Resources resource) {
        ActivityInfo[] receivers = pInfo.receivers;
        sPackageName = pInfo.packageName;
        clearBroadcastReceiverList(context);
        parseAndroidManifest(resource);
        for (ActivityInfo receiverInfo : receivers) {
            DLog.i("SPlugin ActivityInfo receivers>" + receiverInfo.name + " " + receiverInfo.flags);
            try {
                Class receiver = SPlugin.getInstance().getClassLoader().loadClass(receiverInfo.name);
                IntentFilter intentFilter = new IntentFilter();
                addSubBroadcastReceiver(new SubBroadcastReceiver((BroadcastReceiver) receiver.newInstance(), intentFilter));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        registerBroadcastReceiver(context);
    }

    static List<HashMap<String, String>> sRootList = new ArrayList<>();
    static HashMap<String, String> sNodeMap = null;
    //节点名称
    private static String RECEIVER = "receiver";
    private static String ACTIVITY = "activity";
    //当前解析的节点标记
    private static String currentTag;
    //当前解析的节点值
    private static String currentValue;

    private static void parseAndroidManifest(Resources resource) {
        try {
            final XmlResourceParser xml = resource.getAssets().openXmlResourceParser("AndroidManifest.xml");
            try {
                int eventType = xml.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            DLog.i("START_DOCUMENT: "+xml.getName());
                            break;
                        case XmlPullParser.START_TAG:
                            DLog.i("START_TAG: "+xml.getName());
//                            String sName = xml.getName();
//                            if (sName.equals(RECEIVER) || sName.equals(ACTIVITY)) {
//                                sNodeMap = new HashMap<>();
//                            }
//                            if (sNodeMap != null) {
//                                for (int i = xml.getAttributeCount() - 1; i >= 0; i--) {
//                                    sNodeMap.put(xml.getAttributeName(i), xml.getAttributeValue(i));
//                                }
//                            }
//                            // 当前的解析的节点名称
//                            currentTag = sName;
                            break;
                        case XmlPullParser.TEXT:
                            DLog.i("TEXT: "+xml.getText());
//                            if (sNodeMap != null && currentTag != null) {
//                                currentValue = xml.getText();
//                                if (currentValue != null && !currentValue.equals("")
//                                        && !currentValue.equals("\n")) {
//                                    sNodeMap.put(currentTag, currentValue);
//                                }
//                            }
//                            currentTag = null;
//                            currentValue = null;
                            break;
                        case XmlPullParser.END_TAG:
                            DLog.i("END_TAG: "+xml.getName());
//                            String eName = xml.getName();
//                            if (eName.equals(RECEIVER) || eName.equals(ACTIVITY)) {
//                                sRootList.add(sNodeMap);
//                                sNodeMap = null;
//                            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        DLog.i("success!");
    }
//    private static void parseAndroidManifest(Resources resource) {
//        try {
//            final XmlResourceParser xml = resource.getAssets().openXmlResourceParser("AndroidManifest.xml");
//            try {
//                int eventType = xml.getEventType();   //先获取当前解析器光标在哪
//                while (eventType != XmlPullParser.END_DOCUMENT) {    //如果还没到文档的结束标志，那么就继续往下处理
//                    switch (eventType) {
//                        case XmlPullParser.START_DOCUMENT:
//                            break;
//                        case XmlPullParser.START_TAG:
//                            if ("receiver".equals(xml.getName())) {
//                                while (eventType != XmlPullParser.END_DOCUMENT) {
//                                    if (eventType == XmlPullParser.START_TAG) {
//                                        String name = xml.getName();
////                                        if("action".equals(action)){
//                                        DLog.d("name-> " + name);
//                                        for (int i = xml.getAttributeCount() - 1; i >= 0; i--) {
//                                            DLog.d(xml.getAttributeName(i) + ": " + xml.getAttributeValue(i));
//                                        }
//                                    }
//                                    eventType = xml.nextToken();
//                                }
//                            }
//                            break;
//                        case XmlPullParser.TEXT:
//                            DLog.d("Text:" + xml.getText());
//                            break;
//                        case XmlPullParser.END_TAG:
//                            break;
//                        default:
//                            break;
//                    }
//                    eventType = xml.next();   //将当前解析器光标往下一步移
//                }
//            } catch (XmlPullParserException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            int eventType = xml.getEventType();
////            while (eventType != XmlPullParser.END_DOCUMENT) {
////                if (eventType == XmlPullParser.START_TAG) {
////                    String name = xml.getName();
////                    if("action".equals(name)){
////                        DLog.d( "name> "+name);
////                        for (int i = xml.getAttributeCount() - 1; i >= 0; i--) {
////                            DLog.d(xml.getAttributeName(i) + ": " + xml.getAttributeValue(i));
////                        }
////                    }
////                }
////                eventType = xml.nextToken();
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void dispatchReceiver(Context context, Intent intent) {
        for (SubBroadcastReceiver subReceiver : mBroadcastReceiverList) {
            DLog.i("subReceiver.getReceiver Name " + subReceiver.getReceiver().getClass().getSimpleName());
            String recName = intent.getStringExtra(PLUGIN_RECEIVER_FLAG);
            if (recName != null &&
                    subReceiver.getReceiver().getClass().getSimpleName().equals(recName)) {
                subReceiver.getReceiver().onReceive(context, intent);
            }
        }
    }

}
