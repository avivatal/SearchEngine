
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by aviva on 27/11/2017.
 */
public class Main {

    public static void main(String[] args){

        String corpusPath="C:/Users/talshemt/Downloads/corpus";
        String stopwordsPath="C:/Users/talshemt/Downloads/stop_words.txt";

        //read files from corpus
        try{
        ReadFile rf = new ReadFile(stopwordsPath);
        rf.read(corpusPath);}
        catch (Exception e){
            e.printStackTrace();}



        System.out.println("done");
    }

}
