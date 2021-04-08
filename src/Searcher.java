import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Searcher {
    String file;
    String query;
    HashMap map;
    HashMap<String, ArrayList> hashMap;

    public Searcher(String file, String query) {
        this.file = file;
        this.query = query;
        map = new HashMap();
        hashMap = new HashMap<>();
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

            HashMap hashMap = (HashMap) object;

            ArrayList al = (ArrayList) hashMap.get(key);
            fileInputStream.close();

            return al;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void calcSim() {
        ArrayList innerProduct = this.innerProduct();
        double[] sim = new double[5];

        double[] sqB = this.calcSQB();
        for(int i = 0;i<5;i++){
            sqB[i] = Math.sqrt(sqB[i]);
        }
        double sqA = calcSQA();
        for(int i = 0;i<innerProduct.size();i++) {
            Pair p = (Pair) innerProduct.get(i);
            sim[i] = p.weight / (sqA * sqB[i]);
            sim[i] = Math.round(sim[i]*100.0)/100.0;
        }

        for (double d : sim)
            System.out.println(d);

        ArrayList<Pair> list = new ArrayList<>();
        for (int i = 0; i < sim.length; i++) {
            Pair p = new Pair(i, sim[i]);
            list.add(p);
        }

        Comparator comp = (o1, o2) -> {
            Pair x = (Pair) o1;
            Pair y = (Pair) o2;
            if (x.weight < y.weight)
                return 1;
            else if (x.weight > y.weight)
                return -1;
            else
                return 0;
        };
        list.sort(comp);
        String[] docName = {"떡.html", "라면.html", "아이스크림.html", "초밥.html", "파스타.html"};
        for (int i = 0; i < 3; i++) {
            Pair p = list.get(i);
            if(p.weight != 0)
                System.out.println(docName[p.id]);
        }
    }

    private double[] calcSQB() {
        Iterator<String> iter = map.keySet().iterator();
        double[] sqB = new double[5];
        while (iter.hasNext()) {
            String key = iter.next();
            if (!hashMap.containsKey(key))
                continue;
            else {
                ArrayList<Pair> tmp = (ArrayList) hashMap.get(key);
                for (Pair p : tmp) {
                    double dw = p.weight;
                    sqB[p.id] += Math.pow(dw, 2);
                }
            }
        }
        return sqB;
    }

    private double calcSQA() {
        double ret = 0.0;
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            double kw = (double) map.get(key);
            ret += Math.pow(kw, 2);
        }
        ret = Math.sqrt(ret);
        return ret;
    }

    public ArrayList innerProduct() {
        map = this.kwrdExtract(this.query);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            ArrayList list = readFile(key);
            hashMap.put(key, list);
        }
        // 문서개수만큼 배열 생성, 일단 5개 하드코딩
        double[] similarity = new double[5];
        Iterator<String> iter = map.keySet().iterator();
        for (int i = 0; i < 5; i++) {
            while (iter.hasNext()) {
                String key = iter.next();
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

        ArrayList<Pair> list = new ArrayList<>();
        for (int i = 0; i < similarity.length; i++) {
            Pair p = new Pair(i, similarity[i]);
            list.add(p);
        }

        return list;
    }
}
