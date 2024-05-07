package rrhs.xc.ia;

import rrhs.xc.ia.ui.GUIDriver;

public class Main {

    public static void main(String[] args) {
        new GUIDriver(args);
        
        System.exit(0); //once the javafx thread has closed, exit the application. Kills swing threads that could still be open
    }
    

}
