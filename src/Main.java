/**
 * Created by aviva on 27/11/2017.
 */
public class Main {

    public static void main(String[] args){
        ReadFile rf = new ReadFile("C:/Users/aviva/Desktop/Corpus","C:/Users/aviva/Desktop/stopwords");
        try{
            rf.read();
            System.out.println("done");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}
