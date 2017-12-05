package Statments;

import SicController.Controller;

public class Star {
    public String address ="";
    
    public boolean isValid(String query) {
        address=Integer.toString(Controller.programCounter);
        return query.trim().equals("*");
    }
}
