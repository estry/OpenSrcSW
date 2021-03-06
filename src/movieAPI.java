import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class movieAPI {
    public static void main(String[] args) throws IOException, ParseException {
        String filename = "./out/production/SimpleIR/apikeys.txt";
        FileInputStream fileInputStream = new FileInputStream(filename);
        Scanner fsc = new Scanner(fileInputStream, StandardCharsets.UTF_8);
        String clientId = fsc.nextLine();
        String clientSecret = fsc.nextLine();

        Scanner sc = new Scanner(System.in);
        System.out.print("검색어를 입력하세요: ");
        String input = sc.nextLine();
        String text = URLEncoder.encode(input, StandardCharsets.UTF_8);
        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + text;

        URL url = new URL(apiURL);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Naver-Client-Id", clientId);
        con.setRequestProperty("x-Naver-Client-Secret", clientSecret);

        int responseCode = con.getResponseCode();

        BufferedReader br;
        if(responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        else{
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(String.valueOf(response));
        JSONArray infoArray = (JSONArray) jsonObject.get("items");

        for(int i = 0;i<infoArray.size();i++) {
            System.out.println("=item" +i+"============================================");
            JSONObject itemObject = (JSONObject) infoArray.get(i);
            System.out.println("title:\t"+itemObject.get("title"));
            System.out.println("subtitle:\t"+itemObject.get("subtitle"));
            System.out.println("director:\t"+itemObject.get("director"));
            System.out.println("actor:\t"+itemObject.get("actor"));
            System.out.println("userRating:\t"+itemObject.get("userRating"));
        }
    }
}
