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

    public ArrayList<String> read(String path) throws FileNotFoundException{

        ArrayList<String> documents=new ArrayList<>();
        Scanner scanner = new Scanner(new File(path)).useDelimiter("\\A");

        if(scanner.hasNext()) {
            String currentDoc = "";
            currentDoc = scanner.next();

            String[] docs = currentDoc.split("<DOC>");
            for (int j=1; j<docs.length; j++) {
                documents.add(docs[j]);
            }
        }
        return documents;

    }

}



