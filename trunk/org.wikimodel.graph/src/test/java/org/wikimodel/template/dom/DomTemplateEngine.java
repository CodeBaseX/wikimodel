package org.wikimodel.template.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.wikimodel.template.IXmlTemplateConst;
import org.wikimodel.template.IXmlTemplateNodeManager;
import org.wikimodel.template.XmlTemplateEngine;

public class DomTemplateEngine extends XmlTemplateEngine<Node> {

    @Override
    protected XmlDataSelector newDataSelector() {
        return new XmlDataSelector() {
            @Override
            protected Object selectChildData(Node node, Object data, String path) {
                Object result = null;
                if (data instanceof Map<?, ?>) {
                    Map<?, ?> map = (Map<?, ?>) data;
                    result = map.get(path);
                } else if (data instanceof Node) {
                    Node n = (Node) data;
                    short type = n.getNodeType();
                    if (type == Node.ELEMENT_NODE) {
                        Element e = (Element) data;
                        NodeList children = e.getElementsByTagName(path);
                        if (children != null) {
                            int len = children.getLength();
                            if (len == 1) {
                                result = children.item(0);
                            } else if (len > 1) {
                                List<Node> list = new ArrayList<Node>();
                                for (int i = 0; i < len; i++) {
                                    Node item = children.item(i);
                                    list.add(item);
                                }
                                result = list;
                            }
                        }
                        if (result == null) {
                            String attr = getAttribute(e, path);
                            if (attr != null && !"".equals(attr)) {
                                result = attr;
                            }
                        }
                    } else if (type == Node.TEXT_NODE) {
                        result = n.getTextContent();
                    }
                }
                return result;
            }
        };
    }

    @Override
    protected IXmlTemplateNodeManager<Node> newNodeManager() {
        return new IXmlTemplateNodeManager<Node>() {

            public String getAttribute(Node node, String attrName) {
                if (!isElement(node))
                    return null;
                Element element = (Element) node;
                return element.getAttribute(attrName);
            }

            public String getElementName(Node node) {
                return node.getNodeName();
            }

            public Map<String, String> getElementParams(Node node) {
                if (!isElement(node))
                    return null;
                Element element = (Element) node;
                Map<String, String> params = new HashMap<String, String>();
                NamedNodeMap atts = element.getAttributes();
                for (int i = 0; i < atts.getLength(); i++) {
                    Attr n = (Attr) atts.item(i);
                    String key = n.getName();
                    if (key.startsWith(IXmlTemplateConst._PREFIX))
                        continue;
                    String value = n.getValue();
                    params.put(key, value);
                }
                return params;
            }

            public Node getFirstChild(Node node) {
                return node.getFirstChild();
            }

            public Node getNextSibling(Node node) {
                return node.getNextSibling();
            }

            public String getTextContent(Node node) {
                return node.getTextContent();
            }

            public boolean isElement(Node node) {
                return node.getNodeType() == Node.ELEMENT_NODE;
            }

        };
    }

    private void print(Node node, StringBuffer buf, boolean appendNode) {
        if (node instanceof Text) {
            buf.append(node.getTextContent());
        } else if (node instanceof Element) {
            Element e = (Element) node;
            if (appendNode) {
                String name = e.getNodeName();
                buf.append("<").append(name);
                NamedNodeMap attributes = e.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    buf.append(" ");
                    Attr attr = (Attr) attributes.item(i);
                    String attName = attr.getName();
                    String attValue = attr.getValue();
                    buf.append(attName);
                    buf.append("='");
                    buf.append(attValue);
                    buf.append("'");
                }
                buf.append(">");
            }
            NodeList children = e.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                print(child, buf, true);
            }
            if (appendNode) {
                String name = e.getNodeName();
                buf.append("</").append(name).append(">");
            }
        }
    }

    @Override
    protected String toString(Object data) {
        if (data instanceof Element) {
            Element e = (Element) data;
            try {
                StringBuffer buf = new StringBuffer();
                print(e, buf, false);
                return buf.toString();
            } catch (Exception e1) {
                return null;
            }
        } else if (data instanceof Node) {
            Node n = (Node) data;
            return n.getTextContent();
        }
        return super.toString(data);
    }
}
