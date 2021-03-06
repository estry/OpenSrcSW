
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

public class MakeCollection {
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private Element docs;
    private Element doc_id;

    public MakeCollection() {
        try {
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            docs = doc.createElement("docs");
            doc.appendChild(docs);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void writeXml(int type, String content) {
        switch (type) {
            case 0:
                doc_id = doc.createElement("doc");
                doc_id.setAttribute("id", content);
                docs.appendChild(doc_id);
                break;
            case 1:
                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(content));
                doc_id.appendChild(title);
                break;
            case 2:
                Element body = doc.createElement("body");
                body.appendChild(doc.createTextNode(content));
                doc_id.appendChild(body);
                break;
        }
        try {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new FileOutputStream(new File("collection.xml")));

            transformer.transform(source, result);
        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }


}


