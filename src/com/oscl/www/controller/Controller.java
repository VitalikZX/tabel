package com.oscl.www.controller;

import com.oscl.www.model.Model;

/**
 * Created by Vital on 29.01.2017.
 */
public class Controller {//по сути бесполезный для нас клазз(но по MVC нужон)
    private StrFileReader strFileReader;
    private Model model;
    private String[][] resultData;

    public Controller(){
        strFileReader = new StrFileReader();
        model = new Model(strFileReader.getLines());
        resultData = model.getResultData();
    }
    //отдаёт конечные данные тому кто захочет
    public String[][] getResultData(){
        return resultData;
    }
}
