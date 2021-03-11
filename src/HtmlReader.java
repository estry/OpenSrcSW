import java.io.*;

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
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    //xml 생성
                    //0==doc_id, 1 == title, 2 == body
                    if (line.contains("<head>")) {
                        this.makeXml.writeXml(0, Integer.toString(i));
                    }
                    if (line.contains("<title>")) {
                        line.replace("<title>", " ");
                        line.replace("</title>", " ");
                        this.makeXml.writeXml(1, line);
                    } else if (line.contains("<div id=\"content\">")) {
                        line.replace("<p>", " ");
                        line.replace("</p>", " ");
                        this.makeXml.writeXml(2, line);
                    }
                }
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
