import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by aviva on 27/11/2017.
 */
public class Parse {

    ArrayList<ArrayList<String>> parsedDocs;
    HashMap<String, String > months;
    ArrayList<String> whitespaces;
    HashSet<String> stopwords;

    public Parse() {
        this.parsedDocs = new ArrayList<>();
        whitespaces =  new ArrayList<>(Arrays.asList(".",",",":","\"", ";","(",")","[","]","{","}","?","!","<",">"));
        months=new HashMap<String, String>();
        months.put("Jan", "01");
        months.put("Feb","02");
        months.put("Mar","03");
        months.put("Apr","04");
        months.put("May","05");
        months.put("Jun","06");
        months.put("Jul","07");
        months.put("Aug","08");
        months.put("Sep","09");
        months.put("Oct","10");
        months.put("Nov","11");
        months.put("Dec","12");
        months.put("January", "01");
        months.put("February","02");
        months.put("March","03");
        months.put("April","04");
        months.put("June","06");
        months.put("July","07");
        months.put("August","08");
        months.put("September","09");
        months.put("October","10");
        months.put("November","11");
        months.put("December","12");
        months.put("JAN", "01");
        months.put("FEB","02");
        months.put("MAR","03");
        months.put("APR","04");
        months.put("MAY","05");
        months.put("JUN","06");
        months.put("JUL","07");
        months.put("AUG","08");
        months.put("SEP","09");
        months.put("OCT","10");
        months.put("NOV","11");
        months.put("DEC","12");
        months.put("JANUARY", "01");
        months.put("FEBRUARY","02");
        months.put("MARCH","03");
        months.put("APRIL","04");
        months.put("JUNE","06");
        months.put("JULY","07");
        months.put("AUGUST","08");
        months.put("SEPTEMBER","09");
        months.put("OCTOBER","10");
        months.put("NOVEMBER","11");
        months.put("DECEMBER","12");
    }

    public void parse(ArrayList<String> rfDocs, HashSet<String> stopwords){
        this.stopwords=stopwords;
        for(int i=0; i<rfDocs.size(); i++){
            parsedDocs.add(i, new ArrayList<String>());
            parsedDocs.get(i).add(extractName(rfDocs.get(i)));
            split(extractText(rfDocs.get(i)),i);
        }
    }

    public String extractText(String s){
        s = s.substring(s.indexOf("<TEXT>")+6, s.indexOf("</TEXT>"));
        return s;
    }

    public String extractName(String s){
        s = s.substring(s.indexOf("<DOCNO>")+7, s.indexOf("</DOCNO>")).trim();
        return s;
    }

    public void split(String text, int i) {
        String[] splited = text.split(" |\\-|\\\n");

        for (int j = 0; j < splited.length; j++) {

            if (!(splited[j].equals("") || splited[j].equals(" "))) {

                splited[j].trim();

                //is date
                if (months.containsKey(splited[j])) {

                    if (isNumeric(splited[j + 1])) {
                        //MONTH DD -> DD/MM
                        if (Double.parseDouble(splited[j + 1]) > 0 && Double.parseDouble(splited[j + 1]) < 32) {
                            if (splited[j + 1].length() == 1) {
                                splited[j + 1] = "0" + splited[j + 1];
                            }
                            parsedDocs.get(i).add(splited[j + 1] + "/" + months.get(splited[j]));
                            j++;
                        }
                        //MONTH YYYY -> MM/YYYY
                        if (splited.length>j+1 &&((isNumeric(splited[j + 1]) && (splited[j + 1].length() == 4)) || (splited[j+1].length()==5 && whitespaces.contains(splited[j+1].substring(splited[j+1].length()-1))))) {
                            if(splited[j+1].length()==5 && whitespaces.contains(splited[j+1].substring(splited[j+1].length()-1))){
                                splited[j+1]=splited[j+1].substring(0,splited[j+1].length()-1);
                            }
                            parsedDocs.get(i).add(months.get(splited[j]) + "/" + splited[j + 1]);
                            j++;
                        }
                    }
                    //MONTH DD, YYYY -> DD/MM/YYYY
                    else if (splited[j + 1].charAt(splited[j + 1].length() - 1) == ',') {
                        //is next string DD,
                        if (isNumeric(splited[j + 1].substring(0, splited[j + 1].length() - 1))) {
                            //is next string a number between 1 to 31
                            if (Integer.parseInt(splited[j + 1].substring(0, splited[j + 1].length() - 1)) > 0 && Integer.parseInt(splited[j + 1].substring(0, splited[j + 1].length() - 1)) < 32) {
                                //remove comma
                                splited[j + 1] = splited[j + 1].substring(0, splited[j + 1].length() - 1);
                                //add zero if D and not DD
                                if (splited[j + 1].length() == 1) {
                                    splited[j + 1] = "0" + splited[j + 1];
                                }
                                //is next next string a year
                                if (isNumeric(splited[j + 2]) && splited[j + 2].length() == 4) {
                                    //save as date
                                    parsedDocs.get(i).add(splited[j + 1] + "/" + months.get(splited[j]) + "/" + splited[j + 2]);
                                    j += 2;
                                }
                            }
                        }
                    }
                    //CHECK IF "MONTH DD*" (NO YEAR, whitespace after DD) -> DD/MM
                    else if (whitespaces.contains(splited[j + 1].substring(splited[j + 1].length() - 1))) {
                        if (isNumeric(splited[j + 1].substring(0, splited[j + 1].length() - 1))) {
                            //is next string a number between 1 to 31
                            if (Integer.parseInt(splited[j + 1].substring(0, splited[j + 1].length() - 1)) > 0 && Integer.parseInt(splited[j + 1].substring(0, splited[j + 1].length() - 1)) < 32) {
                                //remove whitespace
                                splited[j + 1] = splited[j + 1].substring(0, splited[j + 1].length() - 1);
                                //add zero if D and not DD
                                if (splited[j + 1].length() == 1) {
                                    splited[j + 1] = "0" + splited[j + 1];
                                }
                                parsedDocs.get(i).add(splited[j + 1] + "/" + months.get(splited[j]));
                                j++;
                            }
                        }
                    }
                } else if (months.containsKey(splited[j + 1])) {

                    //DDth MONTH YYYY -> DD/MM/YYYY
                    if (splited[j].substring(splited[j].length() - 2).equals("th")) {
                        if (isNumeric(splited[j].substring(0, splited[j].length() - 2))) {
                            //is next string a number between 1 to 31
                            if (Integer.parseInt(splited[j].substring(0, splited[j].length() - 2)) > 0 && Integer.parseInt(splited[j].substring(0, splited[j].length() - 2)) < 32) {
                                //remove 'th'
                                splited[j] = splited[j].substring(0, splited[j].length() - 2);

                            }
                        }
                    }
                    if (isNumeric(splited[j])) {

                        if (Integer.parseInt(splited[j]) > 0 && Integer.parseInt(splited[j]) < 32) {
                            //add zero if D and not DD
                            if (splited[j].length() == 1) {
                                splited[j] = "0" + splited[j + 1];
                            }
                            //check for year
                            if (splited.length>j+2 &&((isNumeric(splited[j + 2]) && (splited[j + 2].length() == 4)) || (splited[j+2].length()==5 && whitespaces.contains(splited[j+2].substring(splited[j+2].length()-1))))) {
                                if(splited[j+2].length()==5 && whitespaces.contains(splited[j+2].substring(splited[j+2].length()-1))){
                                    splited[j+2]=splited[j+2].substring(0,splited[j+2].length()-1);
                                }
                                //save as date - DD MONTH YYYY -> DD/MM/YYYY
                                parsedDocs.get(i).add(splited[j] + "/" + months.get(splited[j + 1]) + "/" + splited[j + 2]);
                                j += 2;
                            } else if (isNumeric(splited[j + 2]) && splited[j + 2].length() == 2) {
                                //save as date - DD MONTH YY -> DD/MM/YYYY
                                parsedDocs.get(i).add(splited[j] + "/" + months.get(splited[j + 1]) + "/" + "19" + splited[j + 2]);
                                j += 2;
                            } else {
                                //save as date - DD MONTH -> DD/MM
                                parsedDocs.get(i).add(splited[j] + "/" + months.get(splited[j + 1]));
                                j++;
                            }
                        }
                    }
                    else{
                        if(whitespaces.contains(splited[j].substring(0,1))){
                            splited[j]=splited[j].substring(1);
                        }
                        if(whitespaces.contains(splited[j].substring(splited[j].length()-1))){
                            splited[j]=splited[j].substring(0,splited[j].length()-1);
                        }
                        if(!stopwords.contains(splited[j])){

                            parsedDocs.get(i).add(splited[j].toLowerCase());
                        }
                    }
                }

                //capital lettered expressions
                else if ((splited[j].length()>1 && Character.isUpperCase(splited[j].charAt(0))) || (splited[j].length()>2 && whitespaces.contains(splited[j].substring(0,1)) && Character.isUpperCase(splited[j].charAt(1)))) {
                    String expression = splited[j].toLowerCase();
                    if (whitespaces.contains(splited[j].substring(0,1))) {
                        expression = expression.substring(1);
                    }

                    //check next word
                    boolean bool = true;
                    if (whitespaces.contains(splited[j].substring(splited[j].length() - 1))) {
                        bool = false; //there is a whitespace after current word
                    }
                    int index = j;

                    while (bool) {
                        if (splited.length > index + 1 && Character.isUpperCase(splited[index + 1].charAt(0)) && !months.containsKey(splited[index+1])) {
                            expression +=" "+ splited[index + 1].toLowerCase();
                            index++;
                        } else if (splited.length > index + 2 && splited[index+1].equals("of")) {
                            int counter=index;
                            while(splited[counter+2].equals(" ") || splited[counter+2].equals("")){
                                counter++;
                            }
                            if (Character.isUpperCase(splited[counter + 2].charAt(0))) {
                                expression +=" "+ splited[index + 1].toLowerCase();
                                index=counter+1;
                            }
                        } else {
                            if(splited[index+1].equals(" ")){
                                index++;
                            }
                            else{
                                bool = false;
                            }
                        }
                        if (splited.length > index + 1 && whitespaces.contains(splited[index + 1].substring(splited[index + 1].length() - 1))) {
                            bool = false;
                        }
                    }
                    if (whitespaces.contains(expression.substring(expression.length() - 1))) {
                        expression = expression.substring(0, expression.length() - 1);
                    }
                    if(!stopwords.contains(expression)) {
                        parsedDocs.get(i).add(expression);
                    }
                    if (index != j) {

                        for (String s : expression.split(" ")) {
                            if(!stopwords.contains(s)) {
                                parsedDocs.get(i).add(s);
                            }
                        }
                        j = index;
                    }
                }

                //is number
                else if (isNumeric(splited[j])) {

                    //is number with commas

                    //is decimal number
                    if (splited[j].contains(".")) {
                        BigDecimal bd = new BigDecimal(splited[j]);
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        splited[j] = bd.toString();
                        if(splited[j].charAt(splited[j].length()-1) == '0'){
                            splited[j]=splited[j].substring(0,splited[j].length()-1);
                        }
                    }



                    //is percent (word)
                    if (splited[j + 1].equals("percent") || splited[j + 1].equals("percentage")) {
                        parsedDocs.get(i).add(splited[j] + " percent");
                        j++;
                    }
                    else{
                        parsedDocs.get(i).add(splited[j]);
                    }


                }
                //is percent
                else if (splited[j].charAt(splited[j].length() - 1) == '%') {
                    //is decimal
                    if (splited[j].contains(".")) {
                        BigDecimal bd = new BigDecimal(splited[j].substring(0, splited[j].length() - 1));
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        splited[j] = bd.toString();
                        if(splited[j].charAt(splited[j].length()-1) == '0'){
                            splited[j]=splited[j].substring(0,splited[j].length()-1);
                        }
                        parsedDocs.get(i).add(splited[j] + " percent");
                    } else {
                        parsedDocs.get(i).add(splited[j].substring(0, splited[j].length() - 1) + " percent");
                    }

                }

                //number with commas
                else if (splited[j].contains(",")) {
                    String tempNoCommas = splited[j];
                    if (isNumeric(tempNoCommas.replaceAll(",", ""))) {
                        //decimal
                        if (tempNoCommas.contains(".")) {
                            BigDecimal bd = new BigDecimal(tempNoCommas);
                            bd = bd.setScale(2, RoundingMode.HALF_UP);
                            tempNoCommas = bd.toString();
                        }
                        parsedDocs.get(i).add(tempNoCommas);
                    }
                }
                else if(!splited[j].equals("")){
                    if(whitespaces.contains(splited[j].substring(0,1))){
                        splited[j]=splited[j].substring(1);
                    }
                    if(whitespaces.contains(splited[j].substring(splited[j].length()-1))){
                        splited[j]=splited[j].substring(0,splited[j].length()-1);
                    }
                    if(!stopwords.contains(splited[j])){

                        parsedDocs.get(i).add(splited[j].toLowerCase());
                    }
                }
            }
        }
    }



    public static boolean isNumeric(String str)
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
}
