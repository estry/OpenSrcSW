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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MakeXml {
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private Element docs;
    private Element doc_id;
/*    private Element title;
    private Element body;*/

    public MakeXml(){

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
    public void writeXml(int type, String content){
        switch (type){
            case 0:
                doc_id = doc.createElement("doc");
                docs.appendChild(doc_id);
                doc_id.setAttribute("id",content);
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
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");

        DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new FileOutputStream(new File("./data/collection.xml")));

            transformer.transform(source,result);
        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }

}
