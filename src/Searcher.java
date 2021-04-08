import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class Searcher {
    public void calcSim1() {

    }

    String file;
    String query;

    public Searcher(String file, String query) {
        this.file = file;
        this.query = query;
    }

    //query 형태소 분석
    private HashMap kwrdExtract(String text) {
        HashMap<String, Double> map = new HashMap<>();
        String line = "";
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(text, true);
        for (Keyword kwrd : kl) {
            map.put(kwrd.getString(), 1.0);
        }
        return map;
    }

    //read index.post
    private ArrayList readFile(String key) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Object object = objectInputStream.readObject();
            objectInputStream.close();

            //System.out.println("읽어온 객체 type ->" + object.getClass());

            HashMap hashMap = (HashMap) object;
            /*Iterator<String> it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                ArrayList list = (ArrayList) hashMap.get(key);
                System.out.print(key + " -> ");
                for (Object p : list) {
                    Pair pa = (Pair) p;
                    System.out.print(pa.id + " " + pa.weight + " ");
                }
                System.out.println();
            }*/
            ArrayList al = (ArrayList) hashMap.get(key);
            fileInputStream.close();

            return al;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //

    public void innerProduct() {
        HashMap<String, ArrayList> hashMap = new HashMap<>();
        HashMap map = this.kwrdExtract(this.query);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            ArrayList list = readFile(key);
            hashMap.put(key, list);
        }
        // 문서개수만큼 배열 생성 일단 5개 하드코딩
        double[] similarity = new double[5];
        Iterator<String> iter = map.keySet().iterator();
        for (int i = 0; i < 5; i++) {
            while (iter.hasNext()) {
                String key = iter.next();
                System.out.println(key);
                if (!hashMap.containsKey(key))
                    continue;
                else {
                    double kw = (double) map.get(key);
                    ArrayList<Pair> tmp = (ArrayList) hashMap.get(key);
                    for (Pair p : tmp) {
                        double dw = p.weight;
                        similarity[p.id] += kw * dw;
                    }
                }
            }
        }
        //Collections.sort(similarity, Collections.reverseOrder());
        for (double d : similarity)
            System.out.println(d);
        ArrayList<Pair> list = new ArrayList<>();
        for (int i = 0; i < similarity.length; i++) {
            Pair p = new Pair(i, similarity[i]);
            list.add(p);
        }

        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Pair x = (Pair) o1;
                Pair y = (Pair) o2;
                if(x.weight<y.weight)
                    return 1;
                else if(x.weight>y.weight)
                    return -1;
                else
                    return 0;

            }
        };

        Collections.sort(list,comp);

        String[] docName = {"떡.html", "라면.html", "아이스크림.html", "초밥.html", "파스타.html"};

        for(int i = 0;i<3;i++){
            Pair p = list.get(i);
            System.out.println(docName[p.id]);
        }

    }
}
