package com.zsm.commonexample.fileoperator;

import com.zsm.commonexample.main.Main;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;
import java.util.Set;


/**
 * sax解析是一个节点一个节点得往下读，读到后面的节点，就把前面的释放掉，所以不会存在耗费大量内存的情况。
 * Sax解析parse时需要实现一个 DefaultHandler 或者 HandlerBase 的子类，并覆盖节点操作方法
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/30 23:00.
 * @Modified By:
 */
public class XmlSaxUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void readXml(String filePath)
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(filePath, new MyHandler());
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }
}


class MyHandler extends DefaultHandler
{
    private String currentNodeName = null;

    private Map<String, String> map = null;

    /**
     * 开始解析文档
     */
    @Override
    public void startDocument()
        throws SAXException
    {
        map = new HashedMap();
    }

    /**
     * 开始解析节点并且读取节点的属性
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        currentNodeName = qName;
        for (int i = 0, len = attributes.getLength(); i < len; i++)
        {
            map.put(attributes.getQName(i), attributes.getValue(i));
            System.out.println(attributes.getQName(i) + " " + attributes.getValue(i));
        }
    }

    /**
     * 读取节点里的内容
     */
    @Override
    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
        String value = new String(ch, start, length);
        map.put(currentNodeName, value);
        System.out.println(currentNodeName + " " + value);
    }

    /**
     * 结束读取这一个节点
     */
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {

    }

    /**
     * 结束文档的解析
     */
    @Override
    public void endDocument()
        throws SAXException
    {
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> entry : set)
        {
            System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
        }
    }
}
