import java.io.FileNotFoundException;

public class midterm {
    public static void main(String[] args) throws FileNotFoundException {
        genSnippet genSnippet = new genSnippet(args[1], args[3]);
        genSnippet.solution();
    }
}
