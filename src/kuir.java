import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class kuir {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // write your code here
        if (args[0].equals("-c")) {
            MakeCollection makeXml = new MakeCollection();
            readHtml(makeXml, args[1]);
        }
        if (args[0].equals("-k")) {
            MakeKeyword makeKeyword = new MakeKeyword();
            makeKeyword.makeXml(args[1]);
        }
        // -i ./index.xml
        if (args[0].equals("-i")) {
            Indexer indexer = new Indexer();
            indexer.xmlParser(args[1]);
        }
        // -s ./index.post -q "query"
        if(args[0].equals("-s")){
            //Searcher...
            if(args[2].equals("-q")){
                Searcher searcher = new Searcher(args[1], args[3]);
                searcher.calcSim();
            }
            else {
                System.out.println("invalid argument");
                System.exit(-1);
            }
        }

    }

    public static void readHtml(MakeCollection makeCollection, String directory) {

        File dir = new File(directory);
        File[] fileList = dir.listFiles();

        for (int i = 0; i < fileList.length; i++) {
            try {
                FileReader fileReader = new FileReader(fileList[i]);
                org.jsoup.nodes.Document doc = Jsoup.parse(fileList[i], "UTF-8");

                Elements texts = doc.getElementsByTag("p");

                makeCollection.writeXml(0, Integer.toString(i));
                makeCollection.writeXml(1, doc.title());
                if (texts.hasText()) {
                    List<String> txts = texts.eachText();
                    String line = "";
                    for (String txt : txts) {
                        txt += "\n\t\t  ";
                        line += txt;
                    }
                    makeCollection.writeXml(2, line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


