/**
 * Created by aviva on 27/11/2017.
 */
public class Main {

    public static void main(String[] args){
        ReadFile rf = new ReadFile("C:/Users/aviva/Desktop/test corpus","C:/Users/aviva/Desktop/stopwords");
        try{
            rf.read();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}
