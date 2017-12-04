import javafx.collections.transformation.SortedList;

import java.io.PrintWriter;
import java.util.*;

public class Indexer {

    HashMap<String,TermInDictionairy> dictionairy;
    int numberOfTempPostingFiles=1;

    public Indexer() {
        dictionairy = new HashMap<>();
    }

    public void index(HashMap<String, HashMap<String,TermInDoc>> stemmedTerms){

        for (Map.Entry<String,HashMap<String,TermInDoc>>  entry : stemmedTerms.entrySet()) {

            //if word doesnt appear in dictionairy - add it
            if(!dictionairy.containsKey(entry.getKey())){
                dictionairy.put(entry.getKey(),new TermInDictionairy());
            }
            //word is in dictionairy - update idf
            else{
                dictionairy.get(entry.getKey()).setIdf();
            }
        }

        //temp posting file

        //sort stemmedTerms map
        SortedSet<String> sortedKeys = new TreeSet<String>(stemmedTerms.keySet());

        try {
            //create file for temp posting file
            PrintWriter writer = new PrintWriter(numberOfTempPostingFiles + ".txt", "UTF-8");
            numberOfTempPostingFiles++;

            //write each posting entry to file
            for (String key : sortedKeys) {
                //key = term, value = <docID, TermInDoc>
                HashMap<String, TermInDoc> value = stemmedTerms.get(key);
                //string to write to file
                String line = key+":";
                for (TermInDoc termInDoc : value.values()) {
                    line+=" "+termInDoc.toString();
                }
                writer.println(line);
            }
        }
        catch (Exception e){ e.printStackTrace();}
    }
}
