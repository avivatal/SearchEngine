import javafx.collections.transformation.SortedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Indexer {

    HashMap<String,TermInDictionairy> dictionairy;
    int numberOfTempPostingFiles=0;

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
            numberOfTempPostingFiles++;
            PrintWriter writer = new PrintWriter(numberOfTempPostingFiles+".txt" , "UTF-8");


            //write each posting entry to file
            for (String key : sortedKeys) {
                //key = term, value = <docID, TermInDoc>
                HashMap<String, TermInDoc> value = stemmedTerms.get(key);
                //string to write to file
                StringBuilder line = new StringBuilder();
                line.append(key+": ");
                for (TermInDoc termInDoc : value.values()) {
                    line.append(" "+termInDoc.toString());
                }
                writer.println(line.toString());
                writer.flush();
            }
        }
        catch (Exception e){ e.printStackTrace();}
    }

    private String mergeSameTerm(String first, String second){
        first+= second.substring(second.indexOf(": ")+1);
        return first;
    }

    public void mergeTempPostings(){

        int start=1;

        //number of temp posting files is uneven
        if(numberOfTempPostingFiles%2 ==1)
        {
            start=2;
            //merge first 2 files
            merge(1,2,numberOfTempPostingFiles+1);
            File file = new File("C:/Users/talshemt/IdeaProjects/SearchEngine/"+numberOfTempPostingFiles+1+".txt");
            File file2 = new File("C:/Users/talshemt/IdeaProjects/SearchEngine/"+2+".txt");
            file.renameTo(file2);
        }

        int end=numberOfTempPostingFiles;
        int iterations = (int)(Math.ceil(Math.log10(numberOfTempPostingFiles)/Math.log10(2))+1); //height of merging tree is ceil(log2(#files))+1

        //merge levels iterations
        for(int i=0; i<iterations; i++){
            int tmp = merge(start,end,end+1);
            start=end+1;
            end=tmp;
            //last merge - merge into alphabetical files
            if(end-start==1){
                mergeAlphabetic(start, end);
            }
        }

    }

    public int merge(int startIndex, int endIndex, int nextcounter) {

        int currentIndex = startIndex; //index of sorted files
        int counter = nextcounter; //index of new file
        try {
            while (currentIndex < endIndex) {



                //CREATE NEW FILE
                PrintWriter writer = new PrintWriter((counter++)+".txt", "UTF-8");

                //OPEN FILES TO MERGE
                File file1=new File("C:/Users/talshemt/IdeaProjects/SearchEngine/"+currentIndex+".txt");
                File file2=new File("C:/Users/talshemt/IdeaProjects/SearchEngine/"+(currentIndex+1)+".txt");
                FileReader fileReader1 = new FileReader(file1);
                FileReader fileReader2 = new FileReader(file2);
                BufferedReader reader1=new BufferedReader(fileReader1);
                BufferedReader reader2=new BufferedReader(fileReader2);

                //READ LINE FROM EACH FILE
                String line1 = reader1.readLine();
                String line2 = reader2.readLine();
                try {
                    while (line1 != null && line2 != null) {
                        if (line1.length() > 0 && line2.length() > 0 && line1.contains(":") && line2.contains(":")) {
                            if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) < 0) {
                                writer.println(line1);
                                line1 = reader1.readLine();
                            } else if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) > 0) {
                                writer.println(line2);
                                line2 = reader2.readLine();
                            } else if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) == 0) {
                                writer.println(mergeSameTerm(line1, line2));
                                line1 = reader1.readLine();
                                line2 = reader2.readLine();
                            }
                        }
                        writer.flush();
                    }
                }catch (StringIndexOutOfBoundsException e) {
                    System.out.println(line1);
                    System.out.printf(line2);
                }

                //IF REACHED THE END OF ONLY ONE OF THE FILES - WRITE THE REST OF THE NEXT FILE
                while(line1!=null){
                    writer.println(line1);
                    line1=reader1.readLine();
                }

                while(line2!=null){
                    writer.println(line2);
                    line2=reader1.readLine();
                }

                //DELETE OLD FILES AND CLOSE READERS
                fileReader1.close();
                fileReader2.close();
                file1.delete();
                file2.delete();
                writer.close();
                reader1.close();
                reader2.close();

                currentIndex+=2;
            }
        }catch(Exception e){
            e.printStackTrace();
            return counter;
        }
        return counter;
    }

    public void mergeAlphabetic(int start, int end){

        try {
            char current = 'a';
            //CREATE NEW FILE for non-letters (symbols and numbers)
            PrintWriter writer = new PrintWriter("nonLetters.txt", "UTF-8");

            //OPEN FILES TO MERGE
            File file1=new File("C:/Users/talshemt/IdeaProjects/SearchEngine/"+start+".txt");
            File file2=new File("C:/Users/talshemt/IdeaProjects/SearchEngine/"+end+".txt");
            FileReader fileReader1 = new FileReader(file1);
            FileReader fileReader2 = new FileReader(file2);
            BufferedReader reader1=new BufferedReader(fileReader1);
            BufferedReader reader2=new BufferedReader(fileReader2);

            //READ LINE FROM EACH FILE
            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            //iterate only over non letters
            while(line1!=null && line2!=null && ((line1.charAt(0)<97)|| line1.charAt(0)>122) && ((line2.charAt(0)<97)|| line2.charAt(0)>122)) {
                if(line1.length()>0 && line2.length()>0 && line1.contains(":") && line2.contains(":")) {
                    if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) < 0) {
                        writer.println(line1);
                        line1 = reader1.readLine();
                    } else if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) > 0) {
                        writer.println(line2);
                        line2 = reader2.readLine();
                    } else if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) == 0) {
                        writer.println(mergeSameTerm(line1, line2));
                        line1 = reader1.readLine();
                        line2 = reader2.readLine();
                    }
                }
                writer.flush();
            }

            //IF REACHED THE END OF THE NON LETTERS IN ONLY ONE OF THE FILES - WRITE THE REST OF THE NEXT FILE UNTIL LETTER
            while(line1.charAt(0)<97|| line1.charAt(0)>122){ //first file still contains non-letters
                writer.println(line1);
                line1=reader1.readLine();
            }

            while(line2.charAt(0)<97|| line2.charAt(0)>122){
                writer.println(line2);
                line2=reader1.readLine();
            }

            //MERGE THE FILES - EACH LETTER IN DIFFERENT FILE
            char currentChar = 'a';
            while(currentChar<123){
                PrintWriter letterWriter = new PrintWriter(currentChar+".txt", "UTF-8");
                while(line1!=null && line2!=null && line1.charAt(0)==currentChar && line2.charAt(0)==currentChar) {
                    if(line1.length()>0 && line2.length()>0 && line1.contains(":") && line2.contains(":")) {
                        if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) < 0) {
                            writer.println(line1);
                            line1 = reader1.readLine();
                        } else if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) > 0) {
                            writer.println(line2);
                            line2 = reader2.readLine();
                        } else if (line1.substring(0, line1.indexOf(":")).compareTo(line2.substring(0, line2.indexOf(":"))) == 0) {
                            writer.println(mergeSameTerm(line1, line2));
                            line1 = reader1.readLine();
                            line2 = reader2.readLine();
                        }
                    }
                    writer.flush();
                }

                //IF REACHED THE END OF THE NON LETTERS IN ONLY ONE OF THE FILES - WRITE THE REST OF THE NEXT FILE UNTIL LETTER
                while(line1.charAt(0)==currentChar){ //first file still contains old letter
                    writer.println(line1);
                    line1=reader1.readLine();
                }

                while(line2.charAt(0)==currentChar){
                    writer.println(line2);
                    line2=reader1.readLine();
                }
                currentChar++;
            }

            //DELETE OLD FILES AND CLOSE READERS
            fileReader1.close();
            fileReader2.close();
            file1.delete();
            file2.delete();
            writer.close();
            reader1.close();
            reader2.close();



        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
