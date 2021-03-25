
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class MakeKeyword {
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private Element docs;
    private Element doc_id;

    public MakeKeyword() {
        docFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            docs = doc.createElement("docs");
            doc.appendChild(docs);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void makeXml(String directory) {
        File dir = new File(directory);
        try {

            FileReader fileReader = new FileReader(dir);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(dir);

            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element)node;
                    String nodeName = e.getNodeName();
                    //System.out.println(nodeName);
                    if(nodeName.equals("doc")) {
                        //System.out.println("doc id = " + e.getAttribute("id"));
                        doc_id = doc.createElement("doc");
                        doc_id.setAttribute("id", e.getAttribute("id"));
                        docs.appendChild(doc_id);
                    }
                    //System.out.println("++++++++++++++++++++++");
                    NodeList children = node.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node tmp = children.item(j);
                        if(tmp.getNodeType() == Node.ELEMENT_NODE){
                            Element e1 = (Element)tmp;
                            String name = e1.getNodeName();
                            //System.out.println(name);
                            if(name.equals("title")){
                                Element title = doc.createElement("title");
                                title.appendChild(doc.createTextNode(e1.getTextContent()));
                                doc_id.appendChild(title);
                                //System.out.println(e1.getTextContent());
                            }
                            else if(name.equals("body")){
                                //System.out.println(e1.getTextContent());
                                String text = this.kwrdExtract(e1.getTextContent());
                                Element body = doc.createElement("body");
                                body.appendChild(doc.createTextNode(text));
                                doc_id.appendChild(body);
                                //System.out.println(text);
                            }
                        }
                    }
                    //System.out.println("----------------------");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new FileOutputStream(new File("./index.xml")));

            transformer.transform(source, result);
        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }

    }

    private String kwrdExtract(String text){
        String line = "";
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(text, true);
        for (int i = 0; i < kl.size(); i++) {
            Keyword kwrd = kl.get(i);
            //System.out.println(kwrd.getString() + "\t" + kwrd.getCnt());
            line = line+kwrd.getString()+":"+kwrd.getCnt() + "#";
        }

        return line;
    }
}
