package com.dming.simple;

import android.content.res.AXmlResourceParser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.Assert.*;
import static test.AXMLPrinter.getManifestXMLFromAPK;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final float[] RADIX_MULTS = new float[]{0.00390625F, 3.051758E-5F, 1.192093E-7F, 4.656613E-10F};
    private static final String[] DIMENSION_UNITS = new String[]{"px", "dip", "sp", "pt", "in", "mm", "", ""};
    private static final String[] FRACTION_UNITS = new String[]{"%", "%p", "", "", "", "", "", ""};

    @Test
    public void addition_isCorrect() {
        File manifestFile = new File("./TestManifest.xml");//设定为当前文件夹
        try{
            System.out.println(manifestFile.exists());//获取标准的路径
        }catch(Exception e){}
        System.out.println(getManifestXMLFromAPK(manifestFile));
    }

    public static String getManifestXMLFromAPK(File manifestFile) {
        StringBuilder xmlSb = new StringBuilder(100);
        try {
            InputStream inputStream = new FileInputStream(manifestFile);
            AXmlResourceParser parser = new AXmlResourceParser();
            parser.open(inputStream);
            StringBuilder sb = new StringBuilder(10);
            String var7 = "\t";
            while(true) {
                int type;
                while((type = parser.next()) != 1) {
                    switch(type) {
                        case 0:
                            log(xmlSb, "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        case 1:
                        default:
                            break;
                        case 2:
                            log(false, xmlSb, "%s<%s%s", sb, getNamespacePrefix(parser.getPrefix()), parser.getName());
                            sb.append("\t");
                            int namespaceCountBefore = parser.getNamespaceCount(parser.getDepth() - 1);
                            int namespaceCount = parser.getNamespaceCount(parser.getDepth());

                            int i;
                            for(i = namespaceCountBefore; i != namespaceCount; ++i) {
                                log(xmlSb, "%sxmlns:%s=\"%s\"", i == namespaceCountBefore ? "  " : sb, parser.getNamespacePrefix(i), parser.getNamespaceUri(i));
                            }

                            i = 0;

                            for(int size = parser.getAttributeCount(); i != size; ++i) {
                                log(false, xmlSb, "%s%s%s=\"%s\"", " ", getNamespacePrefix(parser.getAttributePrefix(i)), parser.getAttributeName(i), getAttributeValue(parser, i));
                            }

                            log(xmlSb, ">");
                            break;
                        case 3:
                            sb.setLength(sb.length() - "\t".length());
                            log(xmlSb, "%s</%s%s>", sb, getNamespacePrefix(parser.getPrefix()), parser.getName());
                            break;
                        case 4:
                            log(xmlSb, "%s%s", sb, parser.getText());
                    }
                }

                parser.close();
                break;
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        return xmlSb.toString();
    }
    private static String getNamespacePrefix(String prefix) {
        return prefix != null && prefix.length() != 0 ? prefix + ":" : "";
    }

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == 3) {
            return parser.getAttributeValue(index);
        } else if (type == 2) {
            return String.format("?%s%08X", getPackage(data), data);
        } else if (type == 1) {
            return String.format("@%s%08X", getPackage(data), data);
        } else if (type == 4) {
            return String.valueOf(Float.intBitsToFloat(data));
        } else if (type == 17) {
            return String.format("0x%08X", data);
        } else if (type == 18) {
            return data != 0 ? "true" : "false";
        } else if (type == 5) {
            return Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & 15];
        } else if (type == 6) {
            return Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & 15];
        } else if (type >= 28 && type <= 31) {
            return String.format("#%08X", data);
        } else {
            return type >= 16 && type <= 31 ? String.valueOf(data) : String.format("<0x%X, type 0x%02X>", data, type);
        }
    }

    private static String getPackage(int id) {
        return id >>> 24 == 1 ? "android:" : "";
    }

    private static void log(StringBuilder xmlSb, String format, Object... arguments) {
        log(true, xmlSb, format, arguments);
    }

    private static void log(boolean newLine, StringBuilder xmlSb, String format, Object... arguments) {
        xmlSb.append(String.format(format, arguments));
        if (newLine) {
            xmlSb.append("\n");
        }

    }

    public static float complexToFloat(int complex) {
        return (float)(complex & -256) * RADIX_MULTS[complex >> 4 & 3];
    }
}