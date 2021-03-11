import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class HtmlReader {
    private MakeXml makeXml;

    public HtmlReader() {
    }

    public HtmlReader(MakeXml makeXml) {
        this.makeXml = makeXml;
    }

    public void readHtml() {
        File dir = new File("./html_data");

        File[] fileList = dir.listFiles();

        for (int i = 0; i < fileList.length; i++) {
            try {
                FileReader fileReader = new FileReader(fileList[i]);
                Document doc = Jsoup.parse(fileList[i], "UTF-8");

                //System.out.println(doc.title());

                Elements texts = doc.getElementsByTag("p");
                //System.out.println(texts);

                makeXml.writeXml(0, Integer.toString(i));
                makeXml.writeXml(1, doc.title());
                if (texts.hasText()) {
                    List<String> txts = texts.eachText();
                    String line = "";
                    for (String txt : txts) {
                        txt += "\n\t\t  ";
                        //System.out.print(txt);
                        line += txt;
                    }
                    makeXml.writeXml(2, line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
