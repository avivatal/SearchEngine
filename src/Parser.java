import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Parser {
    HashMap<String, ArrayList<String>> parsedDocs;
    HashMap<String, String > months;
    HashSet<String> whitespaces;
    HashSet<String> stopwords;

    public Parser() {
        this.parsedDocs = new HashMap<>();
        whitespaces=new HashSet<>();
        whitespaces.addAll(Arrays.asList(".",",","'","/"));
        months=new HashMap<String, String>();
        months.put("Jan", "01"); months.put("Feb","02");months.put("Mar","03");months.put("Apr","04");months.put("May","05");months.put("Jun","06");months.put("Jul","07");months.put("Aug","08");months.put("Sep","09");months.put("Oct","10");months.put("Nov","11");months.put("Dec","12");
        months.put("January", "01");months.put("February","02");months.put("March","03");months.put("April","04");months.put("June","06");months.put("July","07");months.put("August","08");months.put("September","09");months.put("October","10");months.put("November","11");months.put("December","12");
        months.put("JAN", "01");months.put("FEB","02");months.put("MAR","03");months.put("APR","04");months.put("MAY","05");months.put("JUN","06");months.put("JUL","07");months.put("AUG","08");months.put("SEP","09");months.put("OCT","10");months.put("NOV","11");months.put("DEC","12");
        months.put("JANUARY", "01");months.put("FEBRUARY","02");months.put("MARCH","03");months.put("APRIL","04");months.put("JUNE","06");months.put("JULY","07");months.put("AUGUST","08");months.put("SEPTEMBER","09");months.put("OCTOBER","10");months.put("NOVEMBER","11");months.put("DECEMBER","12");
    }

    public HashMap<String, ArrayList<String>> getParsedDocs() {
        return parsedDocs;
    }

    public void parse(ArrayList<String> rfDocs, HashSet<String> stopwords){
        this.stopwords=stopwords;
        for(int i=0; i<rfDocs.size(); i++){
            String docName=extractName(rfDocs.get(i));
            parsedDocs.put(docName, new ArrayList<String>());
            parsedDocs.get(docName).add(docName);
            split(extractText(rfDocs.get(i)),docName);
        }
    }

    public String extractText(String s){
        if(s.length()>6) {
            int start = s.indexOf("<TEXT>");
            int end = s.indexOf("</TEXT>");
            if (start != -1 && end != -1) {
                s = s.substring(start + 6, end);
            }
        }
        return s;
    }

    public String extractName(String s){
        s = s.substring(s.indexOf("<DOCNO>")+7, s.indexOf("</DOCNO>")).trim();
        return s;
    }

    public void split(String text, String i) {

        String[] splited = text.split("\\-+|\\s+|\\\n+|\\(+|\\)+|\\;+|\\:+|\\?+|\\!+|\\<+|\\>+|\\}+|\\{+|\\]+|\\[+|\\*+|\\++|\\|+|\\\"+|\\=+|\\\\+");

        int splitedlen=splited.length;

        for (int j = 0; j < splitedlen; j++) {

            //avoid parsing empty strings
            if (!(splited[j].equals("") || splited[j].equals(" ")) && splited[j].length()>0) {
                splited[j].trim();

                int splitedj = splited[j].length();
                int splitedj1=0;
                int splitedj2=0;
                if(splitedlen>j+1){
                    splitedj1 = splited[j+1].length();}
                if(splitedlen>j+2){
                    splitedj2 = splited[j+2].length();}

                //**************CAPITAL LETTER EXPRESSIONS********************
                //if the first letter is in uppercase, or the first char is a whitespace and the second char is uppercase
                if (((splitedj>1 && Character.isUpperCase(splited[j].charAt(0))) || (splitedj>2 && whitespaces.contains(splited[j].substring(0,1)) && Character.isUpperCase(splited[j].charAt(1)))) && !months.containsKey(splited[j])) {

                    //if first char is whitespace - remove it
                    if (whitespaces.contains(splited[j].substring(0,1))) {
                        splited[j] = splited[j].substring(1);
                        splitedj--;
                    }

                    //check next word
                    boolean bool = true;
                    if (whitespaces.contains(splited[j].substring(splitedj - 1))) {
                        bool = false; //there is a whitespace after current word- end of expression
                    }
                    splited[j]=cleanWhiteSpaces(splited[j]);
                    splitedj=splited[j].length();
                    String expression = splited[j].toLowerCase();
                    int index = j;

                    while (bool) {
                        //if next word starts with uppercase and isnt a month
                        if (splitedlen > index + 1 && splited[index + 1].length()>0 && Character.isUpperCase(splited[index + 1].charAt(0)) && !months.containsKey(splited[index+1])) {

                            if (splitedlen > index + 1 && whitespaces.contains(splited[index + 1].substring(splited[index + 1].length() - 1))) {
                                bool = false;
                                splited[index+1]=cleanWhiteSpaces(splited[index + 1]);
                                if(splitedlen>j+1){
                                    splitedj1 = splited[j+1].length();}
                                if(splitedlen>j+2){
                                    splitedj2 = splited[j+2].length();}
                                expression +=" "+ splited[index + 1].toLowerCase().substring(0,splited[index + 1].length()-1);
                                index++;
                            }
                            else{
                                splited[index+1]=cleanWhiteSpaces(splited[index + 1]);
                                if(splitedlen>j+1){
                                    splitedj1 = splited[j+1].length();}
                                if(splitedlen>j+2){
                                    splitedj2 = splited[j+2].length();}
                                expression +=" "+ splited[index + 1].toLowerCase();
                                index++;
                            }
                        }
                        //if next word is "of" check next word
                        else if (splitedlen > index + 2 && splited[index+1].equals("of")) {
                            int counter=index;
                            while(splited[counter+2].equals(" ") || splited[counter+2].equals("")){
                                counter++;
                            }
                            if (Character.isUpperCase(splited[counter + 2].charAt(0))) {
                                splited[index+1]=cleanWhiteSpaces(splited[index + 1]);
                                if(splitedlen>j+1){
                                    splitedj1 = splited[j+1].length();}
                                if(splitedlen>j+2){
                                    splitedj2 = splited[j+2].length();}
                                expression +=" "+ splited[index + 1].toLowerCase();
                                index=counter+1;
                            }
                            else{ bool=false;} //next word after 'of' isnt upper case
                        }
                        //next word is space or not uppercase
                        else {
                            if(splitedlen>index+1 && splited[index+1].equals(" ")){
                                index++;
                            }
                            else{
                                bool = false;
                            }
                        }

                    }

                    //save expression
                    if(!stopwords.contains(expression)) {
                        parsedDocs.get(i).add(expression);
                    }
                    //save each word in expression
                    if (index != j) {
                        for (String s : expression.split(" ")) {
                            if(!stopwords.contains(s)) {
                                parsedDocs.get(i).add(s);
                            }
                        }
                        j = index;
                    }
                }
                //********END CAPITAL LETTERS CHECK***********

                else{
                    //remove dots, commas and apostrophes
                    splited[j]=cleanWhiteSpaces(splited[j]);
                    splited[j].trim();
                    splitedj=splited[j].length();

                    //**********CHECK IF DATE*****************

                    //current string is a month
                    String tmp="";
                    if(splitedlen>j+1){
                        tmp = cleanWhiteSpaces(splited[j+1]);}
                    if (months.containsKey(splited[j])) {
                        //check if next string is day or year
                        if (splitedlen>j+1 && isNumeric(tmp)) {
                            splited[j+1]=tmp;
                            splitedj1 = splited[j+1].length();

                            //MONTH DD -> DD/MM *OR* MONTH DD YYYY -> DD/MM/YYYY
                            if (Double.parseDouble(splited[j + 1]) > 0 && Double.parseDouble(splited[j + 1]) < 32) {
                                if (splitedj1 == 1) {
                                    splited[j + 1] = "0" + splited[j + 1];
                                }

                                //is next next string a year
                                String temp="";
                                if(splitedlen>j+2){
                                    temp = cleanWhiteSpaces(splited[j+2]);}
                                if (splitedlen>j+2 && isNumeric(temp) && temp.length() == 4) {
                                    //save as date
                                    splited[j+2]=temp;
                                    splitedj2 = splited[j+2].length();
                                    parsedDocs.get(i).add(splited[j + 1] + "/" + months.get(splited[j]) + "/" + splited[j + 2]);
                                    j += 2;
                                }
                                //no year, only month and day
                                else {
                                    parsedDocs.get(i).add(splited[j + 1] + "/" + months.get(splited[j]));
                                    j++;
                                }
                            }

                            //MONTH YYYY -> MM/YYYY
                            else if (splitedlen>j+1 &&((isNumeric(splited[j + 1]) && (splitedj1 == 4)))) {
                                parsedDocs.get(i).add(months.get(splited[j]) + "/" + splited[j + 1]);
                                j++;
                            }
                        }

                    }
                    //if NEXT string is month
                    else if (splitedlen> j+1 && months.containsKey(tmp)) {

                        splited[j+1]=tmp; //cleaned

                        //DDth MONTH YYYY -> DD/MM/YYYY
                        if (splitedj>2 && splited[j].substring(splitedj - 2).equals("th")) { //if current string ends with 'th'
                            if (isNumeric(splited[j].substring(0, splitedj - 2))) { //check if its numeric without the 'th'
                                splited[j]=splited[j].substring(0, splitedj - 2); //remove the 'th'
                                splitedj-=2;
                            }
                        }
                        if (isNumeric(splited[j])) {
                            if (!splited[j].contains(".") && Integer.parseInt(splited[j]) > 0 && Integer.parseInt(splited[j]) < 32) { //check if day
                                //add zero if D and not DD
                                if (splitedj == 1) {
                                    splited[j] = "0" + splited[j];
                                }
                                //check for year
                                String temp="";
                                if(splitedlen>j+2){
                                    temp = cleanWhiteSpaces(splited[j+2]);}
                                if (splitedlen>j+2 && isNumeric(temp) && temp.length() == 4) {
                                    splited[j+2]=temp;
                                    splitedj2=splited[j+2].length();
                                    //save as date - DD MONTH YYYY -> DD/MM/YYYY
                                    parsedDocs.get(i).add(splited[j] + "/" + months.get(splited[j + 1]) + "/" + splited[j + 2]);
                                    j += 2;
                                }
                                //if year is written in short (YY)
                                else if (splitedlen>j+2 && isNumeric(temp) && temp.length() == 2) {
                                    //save as date - DD MONTH YY -> DD/MM/YYYY
                                    splited[j+2]=temp;
                                    parsedDocs.get(i).add(splited[j] + "/" + months.get(splited[j + 1]) + "/" + "19" + splited[j + 2]);
                                    j += 2;
                                }
                                else {
                                    //save as date - DD MONTH -> DD/MM
                                    parsedDocs.get(i).add(splited[j] + "/" + months.get(splited[j + 1]));
                                    j++;
                                }
                            }
                        }
                        else{ //current word isnt connected to a date, save it
                            if(!stopwords.contains(splited[j])){
                                parsedDocs.get(i).add(splited[j].toLowerCase());
                            }
                        }
                    }
                    //***********END DATES CHECK*****************

                    //***********NUMBERS***************
                    else if (isNumeric(splited[j])) {

                        //is decimal number
                        if (splited[j].contains(".")) {
                            try {
                                BigDecimal bd = new BigDecimal(splited[j]);
                                bd = bd.setScale(2, RoundingMode.HALF_UP);
                                splited[j] = bd.toString();
                                splitedj=splited[j].length();
                            }
                            catch (NumberFormatException e){
                                e.printStackTrace();
                                System.out.println("PROBLEM: "+splited[j]); //////////delete
                            }
                            //if second decimal digit is zero, remove it
                            try {
                                if (splited[j].endsWith("0") && splitedj > 1) {
                                    splited[j] = splited[j].substring(0, splitedj - 1);
                                    splitedj--;
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                System.out.println(splited[j] + " " + splitedj);
                            }
                        }

                        //is percent (word)
                        if (splitedlen>j+1 && (splited[j + 1].equals("percent") || splited[j + 1].equals("percentage"))) {
                            parsedDocs.get(i).add(splited[j] + " percent");
                            j++;
                        }
                        //number without percent
                        else{
                            parsedDocs.get(i).add(splited[j]);
                        }
                    }
                    //is percent
                    else if ((splitedj>1 && splited[j].endsWith("%"))) {
                        splited[j]=splited[j].substring(0, splitedj - 1); //remove '%'
                        splitedj--;
                        //is decimal
                        if (splited[j].contains(".")) {
                            try {
                                BigDecimal bd = new BigDecimal(splited[j]);
                                bd = bd.setScale(2, RoundingMode.HALF_UP);
                                splited[j] = bd.toString();
                                splitedj=splited[j].length();
                            }
                            catch (NumberFormatException e){
                                e.printStackTrace();
                                System.out.println("PROBLEM: "+splited[j]); //delete!
                            }
                            //ends with zero - remove it
                            if(splited[j].endsWith("0")){
                                splited[j]=splited[j].substring(0,splitedj-1);
                                splitedj--;
                            }
                            //save as percent
                            parsedDocs.get(i).add(splited[j] + " percent");
                        }
                        else { //not a decimal number
                            parsedDocs.get(i).add(splited[j] + " percent");
                        }
                    }

                    //number with commas
                    else if (splited[j].contains(",")) {
                        String tempNoCommas = splited[j];
                        tempNoCommas=tempNoCommas.replaceAll(",", "");
                        if (isNumeric(tempNoCommas)) {
                            //decimal
                            if (tempNoCommas.contains(".")) {
                                //   System.out.println(tempNoCommas);
                                BigDecimal bd = new BigDecimal(tempNoCommas);
                                bd = bd.setScale(2, RoundingMode.HALF_UP);
                                tempNoCommas = bd.toString();
                            }
                            parsedDocs.get(i).add(tempNoCommas);
                        }
                    }
                    else{ //regular word - save it

                        if(!stopwords.contains(splited[j])) {
                            parsedDocs.get(i).add(splited[j].toLowerCase());
                        }
                    }

                }
            }


        }
    }






    public boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private String cleanWhiteSpaces(String s){
        if(s.length()>0){
            //clean from start
            char current = s.charAt(0);
            while (s.length()>1 && whitespaces.contains(current+"")) {
                s = s.substring(1);
                current = s.charAt(0);
            }
            //clean from end
            if(s.length()>0){
                current = s.charAt(s.length() - 1);}
            while (s.length()>1 && whitespaces.contains(current+"")) {
                s = s.substring(0,s.length()-1);
                current = s.charAt(s.length() - 1);
            }

            //remove apostrophe
            if(s.endsWith("'s") || s.endsWith("'S")){
                s=s.substring(0,s.length()-2);
            }
            if(isNumeric(s) ) {
                if (s.endsWith("f") || s.endsWith("d") || s.endsWith("D") || s.endsWith("F")) {
                    s = s.substring(0, s.length() - 1);
                }
                if (s.endsWith(".d") || s.endsWith(".D")) {
                    s = s.substring(0, s.length() - 2);
                }

            }}
        return s;

    }

}
