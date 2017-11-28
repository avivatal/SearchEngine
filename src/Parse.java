import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by aviva on 27/11/2017.
 */
public class Parse {

    ArrayList<ArrayList<String>> parsedDocs;

    public Parse() {
        this.parsedDocs = new ArrayList<>();
    }

    public void parse(ArrayList<String> rfDocs){
        for(int i=0; i<rfDocs.size(); i++){
            parsedDocs.add(i, new ArrayList<String>());
            parsedDocs.get(i).add(extractName(rfDocs.get(i)));
            split(extractText(rfDocs.get(i)),i);
        }
    }

    public String extractText(String s){
        s = s.substring(s.indexOf("<TEXT>"), s.indexOf("</TEXT>"));
        return s;
    }

    public String extractName(String s){
        s = s.substring(s.indexOf("<DOCNO>"), s.indexOf("</DOCNO>")).trim();
        return s;
    }

    public void split(String text, int i){
        String[] splited=text.split(" ");

        for(int j=0; j<splited.length; j++){

            splited[j].trim();

            //is number
            if(isNumeric(splited[j])){

                //is number with commas

                //is decimal number
                if(splited[j].contains(".")){
                    BigDecimal bd = new BigDecimal(splited[j]);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    splited[j] = bd.toString();
                }

                //is part of date

                //is percent
                if(splited[j].charAt(splited[j].length()-1)=='%'){

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
