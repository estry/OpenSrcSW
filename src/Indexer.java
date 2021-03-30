import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Indexer {
    String[] body;
    int len;

    public Indexer() {

    }

    public void xmlParser(String filename) throws IOException {
        File dir = new File(filename);
        File d = new File("./data");
        File[] fileList = d.listFiles();
        assert fileList != null;
        len = fileList.length;
        body = new String[len];

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(dir);

            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String idx = "";
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    String nodeName = e.getNodeName();
                    if (nodeName.equals("doc")) {
                        idx = e.getAttribute("id");
                    }
                    NodeList children = node.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node tmp = children.item(j);
                        if (tmp.getNodeType() == Node.ELEMENT_NODE) {
                            Element e1 = (Element) tmp;
                            String name = e1.getNodeName();
                            if (name.equals("body")) {
                                int index = Integer.parseInt(idx);
                                body[index] = e1.getTextContent();
                            }
                        }
                    }
                }
            }
            makeIndex();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public void makeIndex() {
        // w
        HashMap<String, ArrayList<Pair>> map = new HashMap<>();
        //df
        HashMap<String, Integer> dic = new HashMap<>();
        for (String value : body) {
            String[] pa = value.split("#");
            for (String s : pa) {
                String[] tmp = s.split(":");
                if (!dic.containsKey(tmp[0])) {
                    dic.put(tmp[0], 1);
                } else {
                    int t = dic.get(tmp[0]);
                    dic.replace(tmp[0], 1 + t);
                }
            }
        }

        //tf-idf calc
        for (int i = 0; i < body.length; i++) {
            String[] pa = body[i].split("#");
            for (String s : pa) {
                String[] tmp = s.split(":");
                int tf = Integer.parseInt(tmp[1]);
                int df = dic.get(tmp[0]);
                double w = Math.round(tf * Math.log(5.0 / (double) df) * 1000.0) / 1000.0;

                Pair p = new Pair(i, w);
                if (!map.containsKey(tmp[0])) {
                    ArrayList<Pair> list = new ArrayList<>();
                    list.add(p);
                    map.put(tmp[0], list);
                } else {
                    ArrayList<Pair> list = map.get(tmp[0]);
                    list.add(p);
                    map.replace(tmp[0], list);
                }
            }
        }

        makeFile(map);
        readFile();
    }

    public void makeFile(HashMap<String, ArrayList<Pair>> map) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./index.post");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(map);
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("./index.post");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Object object = objectInputStream.readObject();
            objectInputStream.close();

            System.out.println("읽어온 객체 type ->" + object.getClass());

            HashMap hashMap = (HashMap) object;
            Iterator<String> it = hashMap.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next();
                ArrayList list = (ArrayList) hashMap.get(key);
                System.out.print(key + " -> ");
                for (Object p : list) {
                    Pair pa = (Pair) p;
                    System.out.print(pa.id + " " + pa.weight + " ");
                }
                System.out.println();
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }
}

class Pair implements Serializable {
    public int id;
    public double weight;


    public Pair(int id, double weight) {
        this.id = id;
        this.weight = weight;
    }
}
