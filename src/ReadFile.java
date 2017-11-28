import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by aviva on 27/11/2017.
 */
public class ReadFile
{
    String corpusPath;
    ArrayList<String> documents;
    int counter;

    public ReadFile(String corpusPath) {
        this.corpusPath = corpusPath;
        documents=new ArrayList<>(); //dont forget to clear after text operations
        counter=0;
    }

    public void read() throws FileNotFoundException{
        File corpusFolder = new File(corpusPath);
        File[] listOfFiles = corpusFolder.listFiles();

        for(int i=0; i<3; i++){
            String currentDoc=new Scanner(new File(listOfFiles[counter].getPath()+"/"+listOfFiles[counter].getName())).useDelimiter("\\A").next();
            String[] docs = currentDoc.split("<DOC>");
            for (int j=1; j<docs.length; j++) {
                documents.add(docs[j]);
            }
            counter++;
        }
    }

}
