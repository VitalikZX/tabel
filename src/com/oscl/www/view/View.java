package com.oscl.www.view;

import com.oscl.www.controller.Controller;

/**
 * Created by Vital on 29.01.2017.
 */
public class View {//тоже жостконужный класс(но пусть будет)
    private Controller controller;
    private String[][] resultData;

    public View(){
        controller = new Controller();
        resultData = controller.getResultData();
    }

    public void printStringTwoDimensionArray(){
        for(int i = 0; i < resultData.length; i++){
            for(String prtStr: resultData[i]){
                System.out.print(prtStr + " ");
            }
            System.out.println();
        }
    }
}
