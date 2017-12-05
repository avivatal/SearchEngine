import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Controller {

    HashSet<String> stopwords = new HashSet<String>();
    Parser parser;
    Stemmer stemmer;
    Indexer indexer;

    Controller(String stopwordsPath) {
         parser = new Parser();
         stemmer = new Stemmer();
         indexer = new Indexer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(stopwordsPath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                stopwords.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void control(ArrayList<String> documents) {


        //parse docs in current file
        parser.parse(documents, stopwords);
        HashMap<String, ArrayList<String>> parseddocs = parser.getParsedDocs();

             /*   //stem
                //stemmedTerms - for each term we save a map of docs and properties of term in that doc
                HashMap<String, HashMap<String,TermInDoc>> stemmedTerms = new HashMap<>();
                //iterate over all docs in file
                for (ArrayList doc:parseddocs.values()) {
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
                            HashMap<String, TermInDoc> map = new HashMap<>();
                            map.put((String)doc.get(0), tid);
                            stemmedTerms.put(term,map);
                        }
                        //term appears in hashmap
                        else{
                            //if doc appears in stemmed term
                            if(stemmedTerms.get(term).containsKey(((String)doc.get(0)))){
                                (stemmedTerms.get(term)).get((String)doc.get(0)).setTf();
                            }
                            //if doc doesnt appear in stemmed term, create new TermInDoc entry
                            else{
                                stemmedTerms.get(term).put((String)doc.get(0),new TermInDoc((String)doc.get(0), 1, false));
                                if(j<100){
                                    stemmedTerms.get(term).get((String)doc.get(0)).setInFirst100Terms(true);
                                }
                            }
                        }
                    }
                }

                //indexer
                indexer.index(stemmedTerms);

*/

        System.out.println("done");

    }
}
