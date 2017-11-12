
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Arrays;
import twitter4j.Status;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hopeful
 */
public class Chat extends ChatBotFormGUI {

    public ArrayList<String> chating(String uText, DBCollection dbcol, ArrayList<String> recover){

        final ArrayList<String> exceptionMessageList = new ArrayList<>(
                Arrays.asList("maalesef anlaşılmadı...", "lütfen tekrarlar mısınız?", "???"));

        final ArrayList<String> greetingMessageList = new ArrayList<>(
                Arrays.asList("merhabalar", "mrb", "slm", "selam", "merhaba", "ooo selammm..",
                        "merhabalar", "mrb", "hoş geldiniz buyrun"));

        final ArrayList<String> msj = new ArrayList<>(
                Arrays.asList("ürün", "ürün seçeceğim", "ürün seç", "ürün sec", "urun sececegim", "urun sec", "urun"));

        final ArrayList<String> msj2 = new ArrayList<>(
                Arrays.asList("gule gule", "bye bye", "bye", "hosca kal", "hoşça kal", "güle güle"));

        ArrayList<String> donut = new ArrayList<>();

        if (msj2.contains(uText)) {//vedalaşma
            donut.add(decideRandom(msj2));

        } else if (greetingMessageList.contains(uText)) {//karşılama
            donut.add(decideRandom(greetingMessageList));

        } else if (msj.contains(uText)) {//ürün seçimi

            Category c = new Category();
            Iterable<DBObject> output = c.getCategoryRecord(dbcol);
            donut = editting("AI: " + "Lütfen kategori seçiniz" + "\n", "-Kategoriler-", output);

        } else if (uText.matches("\\d*")) {// +ZU{0} regular expression'a göre tam sayı girilecek

            String d = null;
            if(!recover.isEmpty())
                d = recover.get(1);//arrayList'teki listenin hangi başlığı içerdiği
            String value = selectedRecords(uText, recover);//Kullanıcı tarafından seçilen ürün-kategori ifadesi

            if (null != value) {
                switch (d) {
                    case "-Kategoriler-": {
                        //kategori seçimine göre Type listesi

                        Types t = new Types();
                        t.categoryName = value;
                        Iterable<DBObject> output = t.getSubRecords(dbcol);
                        donut = editting("Lütfen type seçiniz\n", "-Types-", output);

                        break;
                    }
                    case "-Types-": {
                        //type seçimine göre Brand listele
                        
                        Brand b = new Brand();
                        b.typeName = value;
                        Iterable<DBObject> output = b.getSubBrand(dbcol);
                        donut = editting("Lütfen marka seçiniz\n", "-Markalar-", output);
                        break;
                    }
                    case "-Markalar-": {
                        //brand seçimine göre modelleri listele
                        
                        Model m = new Model();
                        m.brandName = value;
                        Iterable<DBObject> output = m.getSubModel(dbcol);
                        donut = editting("Lütfen model seçiniz\n", "-Modeller-", output);
                        break;
                    }
                    case "-Modeller-":
                        //model seçimine göre twitter yorumlarını analiz et
                        value=value.replaceAll("\\s", "");
                        AnalyseTweets analyse = new AnalyseTweets("#"+value);
                        ArrayList<TWEETS> tweet = analyse.analyse();
                        donut.add("----Twitter üzerinden model Analizi-----\n");
                        donut.add("-------Yorumlar ve polariteleri-------");
                        double avarage = 0;
                        for (int i = 0; i < 10; i++) {
                            donut.add("[ " + (i + 1) + " ]" + tweet.get(i).tweet + "--------->" + tweet.get(i).polarity + "\n");
                            avarage += tweet.get(i).polarity;
                        }
                        donut.add("10 Tweet üzerinden ortalam polarite : " + avarage);
                        break;
                }
            } else {
                donut = recover;
                donut.add("AI: Doğru sıra numarasını giriniz\n");//listelenen ürün indexi girilmediğinde hata msj'ı
            }

        } else {//AI tarafından bilinmeyen bir msj girildiğinde uyarı msj'ı
            donut.add(decideRandom(exceptionMessageList));
        }

        return donut;
    }

    protected String decideRandom(ArrayList<String> messageList) {//chatBot rastgele karşılık msj'ı seç
        
        int decider = (int) (Math.random() * messageList.size());
        String message = "AI: " + messageList.get(decider) + "\n";
        
        return message;
    }

    protected ArrayList<String> editting(String msj, String kat, Iterable<DBObject> output) {

        int sira = 1;
        donut.add(msj);
        donut.add(kat);
        for (DBObject dbObject : output) {
            String[] element = dbObject.toString().split("\"");
            donut.add(sira++ + "-" + element[3]);
        }
        return donut;
    }
    
    protected String selectedRecords(String uText, ArrayList<String> recover) {
        String value = null;
        for (String a : recover) {
            String[] x = a.split("-");
            if (x[0] == null ? uText == null : x[0].equals(uText)) {
                value = x[1];
            }
        }
        return value;
    }
}