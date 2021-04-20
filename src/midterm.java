// 제출한 url로 접속하시면 master branch 로 연결됩니다.
// 컴파일 할 때
// javac -cp ./lib/* *.java -encoding UTF-8
// 기존 컴파일하는 방식 그대로 이용했습니다.
// 기존 실습파일도 같이 브랜치에 푸시되어있습니다.
import java.io.FileNotFoundException;

public class midterm {
    public static void main(String[] args) throws FileNotFoundException {
        genSnippet genSnippet = new genSnippet(args[1], args[3]);
        genSnippet.solution();
    }
}
