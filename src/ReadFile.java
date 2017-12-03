import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by aviva on 27/11/2017.
 */
public class ReadFile
{
    String corpusPath;
    ArrayList<String> documents;
    int counter;
    HashSet<String> stopwords;

    public ReadFile(String corpusPath, String stopwordsPath) {
        this.corpusPath = corpusPath;
        stopwords = new HashSet<>();

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
        documents=new ArrayList<>(); //dont forget to clear after text operations
        counter=0;
    }

    public void read() throws FileNotFoundException{
        File corpusFolder = new File(corpusPath);
        File[] listOfFiles = corpusFolder.listFiles();

        for(int i=0; i<3&& i<listOfFiles.length; i++){
            Scanner scanner = new Scanner(new File(listOfFiles[counter].getPath()+"/"+listOfFiles[counter].getName())).useDelimiter("\\A");

            if(scanner.hasNext()) {
                String currentDoc = "";
                currentDoc = scanner.next();

                String[] docs = currentDoc.split("<DOC>");
                for (int j=1; j<docs.length; j++) {
                    documents.add(docs[j]);
                }
                counter++;
            }

        }
        Parse parser=new Parse();
        try {
            parser.parse(documents, stopwords);
            System.out.println("done1");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
