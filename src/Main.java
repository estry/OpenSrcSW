public class Main {

    public static void main(String[] args) {
	// write your code here
        MakeXml makeXml = new MakeXml();
        HtmlReader reader = new HtmlReader(makeXml);
        reader.readHtml();
    }
}


