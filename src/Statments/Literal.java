package Statments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SicController.Controller;

public class Literal implements IStatement {

    private String hexNum = "(?i)x'([+-]?([a-f]|\\d)+)'";
    private String characters = "(?i)c'(.+)'";
    private String num = "([+-]?\\d+)'";
    private String Query;
    public String address;

    public Literal() {
        this.Query = null;
        this.address = "0000";
    }

    public boolean isValid(String query) {
        query = query.trim();
        Pattern pattern;
        Matcher matcher;
        Query = query;
       /* if(query.equal("*")){
            address=Integer.toHexString(Controller.programCounter);
        }*/
        if (query.toLowerCase().startsWith("x")) {
            query = query.replace("X'", "x'");
            pattern = Pattern.compile(hexNum);
            matcher = pattern.matcher(query);
            if (!matcher.matches())
                return false;
            else {
                if (query.contains("-")) {
                    String substr = query.substring(3, query.length() - 1);
                    Query = "x'" +twosCompHexConverter(substr) + "'";
                } else if (query.contains("+")) {
                    Query = "x'" + query.substring(3, query.length() - 1) + "'";
                }
                return true;
            }
        } else if (query.toLowerCase().startsWith("c")) {
            Query = query.replace("C'", "c'");
            pattern = Pattern.compile(characters);
            matcher = pattern.matcher(query);
            if (!matcher.matches())
                return false;
            else
                return true;
        }else if(query.startsWith("+")||query.startsWith("-")){
            try{
                query=query.substring(1);
                String s=Integer.toHexString(Integer.parseInt(query));
                if(Query.startsWith("-"))s=twosCompHexConverter(s);
                Query=s;
                return true;
            }catch(Exception e){}
        }
        return false;
    }
    public static String twosCompHexConverter(String hex) {
        int length = hex.length() + 1;
        int intVal = Integer.parseInt(hex, 16);
        String sol = Integer.toHexString((-1 * intVal));
        return sol.substring(sol.length() - length);
    }

    public Boolean executePass1() {
        
        if (Controller.LiteralsTable.containsKey(Query))
            return true;
        else if (Controller.unDeclaredSymbols.contains(Query))
            return true;
        else {
            Controller.unDeclaredSymbols.add(Query);
            return true;
        }
    }

    public Boolean executePass2() {
        if (Controller.LiteralsTable.containsKey(Query)) {
            address = Integer.toHexString(Controller.LiteralsTable.get(Query));
            return true;
        }
        return false;

    }

    public static void main(String[] args) {
        String query = "+122";
        System.out.println(new Literal().isValid(query));
        System.out.println(new Literal().isValid("-145"));
        String substr = query.substring(3, query.length() - 1);
        System.out.println(substr);
        System.out.println(twosCompHexConverter(substr));
    }
}
