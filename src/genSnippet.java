import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class genSnippet {
    private String query;
    private String filename;

    public genSnippet(String filename, String query) {
        this.filename = filename;
        this.query = query;
    }

    public void solution() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        Scanner sc = new Scanner(fileInputStream, "UTF-8");

        ArrayList<String> lines = new ArrayList<>();

        while (sc.hasNext()) {
            lines.add(sc.nextLine());
        }

        int index = 0;
        int max = 0;
        String[] tkquery = query.split(" ");
        for (int i = 0; i < lines.size(); i++) {
            int count = 0;
            for (int j = 0; j < tkquery.length; j++) {
                if (lines.get(i).contains(tkquery[j])) {
                    count++;
                }
            }
            if (count > max) {
                max = count;
                index = i;
            }
        }
        System.out.println(lines.get(index));

    }
}
