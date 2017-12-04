import com.sun.xml.internal.xsom.impl.Ref;

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

        String corpusPath="C:/Users/aviva/Desktop/Corpus";
        String stopwordsPath="C:/Users/aviva/Desktop/stopwords";
        File corpusFolder = new File(corpusPath);
        File[] listOfFiles = corpusFolder.listFiles();

        //stop words
        HashSet<String> stopwords = new HashSet<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(stopwordsPath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line!=null){
                stopwords.add(line);
                line=br.readLine();
            }
        }

        catch(Exception e){
            e.printStackTrace();
        }

        //read files from corpus
        ReadFile rf = new ReadFile();
        Parse parser = new Parse();
        Stemmer stemmer = new Stemmer();

        for(int i=0; i<listOfFiles.length; i++) {

            try {
                //read file, return list of docs in file
                String path = listOfFiles[i].getPath()+"/"+listOfFiles[i].getName();
                ArrayList<String> extractDocs = rf.read(path);

                //parse docs in current file
                parser.parse(extractDocs,stopwords);
                ArrayList<ArrayList<String>> parseddocs=parser.getParsedDocs();

                //stem
                //stemmedTerms - for each term we save a list of docs it appears in (with additional properties)
                HashMap<String, HashMap<String,TermInDoc>> stemmedTerms = new HashMap<>();
                //iterate over all docs in file
                for (ArrayList doc:parseddocs) {
                    //stem each word in doc
                    for (int j=1;j<doc.size();j++){
                        stemmer.add(((String)(doc.get(j))).toCharArray(),((String)(doc.get(j))).length());
                        stemmer.stem();
                        String term = stemmer.toString();

                        //if term is new in hashmap
                        if(!stemmedTerms.containsKey(term)){
                            TermInDoc tid = new TermInDoc((String)doc.get(0), 1, false);
                            if(j<100){
                                tid.setInFirst100Terms(true);
                            }
                            (stemmedTerms.get(term)).put((String)doc.get(0),tid);
                        }
                        //term appears in hashmap
                        else{
                            //if doc appears in stemmed term
                            if(stemmedTerms.get(term).containsKey(((String)doc.get(0)))){
                                (stemmedTerms.get(term)).get((String)doc.get(0)).setTf();
                            }
                            //if doc doesnt appear in stemmed term
                            else{
                                stemmedTerms.get(term).put((String)doc.get(0),new TermInDoc((String)doc.get(0), 1, false))
                            }
                        }
                    }
                }

                System.out.println("done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
