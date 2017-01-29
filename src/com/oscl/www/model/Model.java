package com.oscl.www.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GritsenkoVA on 24.01.17.
 */
public class Model {//собственно тут вся работа и происходит
    private List<String> strLinesLoadFromFile;

    private int rows;
    private int columns;

    private String[][] data;
    private String[][] resultData;

    private FormulaHandler formulaHandler;

    public Model(List<String> dataFromFile){
        this.strLinesLoadFromFile = new ArrayList<>(dataFromFile);

        takeSizeOfTable();
        initialStructure();
        dataParse();
        calculationResultTable();
    }
    //отдаёт результат вычисления
    public String[][] getResultData(){
        return resultData;
    }
    //получает значения размеров таблы
    private void takeSizeOfTable(){
        String[] str;
        str = (strLinesLoadFromFile.get(0)).split(" ");
        rows = Integer.parseInt(str[0]);
        columns = Integer.parseInt(str[1]);
    }
    //инициализашка хотя можно было и в конструкторе
    private void initialStructure(){
        data = new String[rows][columns];
        resultData = new String[rows][columns];
    }
    //парсим данные с файла в своизадуманные структуры
    private void dataParse(){
        for (int i=1; i < strLinesLoadFromFile.size(); i++) {
            data[i-1] = strLinesLoadFromFile.get(i).split(" ");
        }
    }
    //опять проверяем не число ли(на отрицательные не проверяем ибо по условию всё с плюсиком
    private boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }
    //если в формуле все числа считае(можно было сделать этот расчёт и формулах где ссылки, но решил так)
    private int numberCalculation(String str){
        int result = 0;
        char[] chr = str.toCharArray();
        String strBuffer="";
        List<Integer> listOfNumber = new ArrayList<>();
        List<Character> listOfSign = new ArrayList<>();
        for (int i = 1; i < chr.length; i++) { //первый символ =, его пропускаем
            if (Character.isDigit(chr[i])) strBuffer += chr[i];
            else {
                listOfNumber.add(Integer.parseInt(strBuffer));
                listOfSign.add(chr[i]);
                strBuffer = "";
            }
        }
        listOfNumber.add(Integer.parseInt(strBuffer));

        result = listOfNumber.get(0);
        for (int i = 0; i < listOfSign.size(); i++) {
            switch (listOfSign.get(i)){
                case '+': result = result + listOfNumber.get(i+1); break;
                case '-': result = result - listOfNumber.get(i+1); break;
                case '*': result = result * listOfNumber.get(i+1); break;
                case '/': result = result / listOfNumber.get(i+1); break;
            }
        }

        return result;
    }
    //расчёт формул с ссылками
    private int referCalculation(){
        for (int i = 0; i < resultData.length; i++) {
            for (int j = 0; j < resultData[i].length; j++) {
                if (resultData[i][j].startsWith("=")) {
                    formulaHandler = new FormulaHandler(resultData, i, j);
                }
            }
        }
        return 0;
    }
    //сильнострашная функция, проверяет что у нас в ячейке и чуть что вызывает numberCalculation
    private String calculation(String str){
        char[] chr = str.toCharArray();
        boolean flagIsCalculate = false;
        for (int i = 1; i < chr.length; i++) {
            if((Character.isDigit(chr[i]))||(chr[i]== '+')||(chr[i]== '-')||(chr[i]== '*')||(chr[i]== '/')){
                flagIsCalculate = true;
            } else {flagIsCalculate = false; break;}
        }

        if (flagIsCalculate) {
            return Integer.toString(numberCalculation(str));
        } else {
            return str;
        }
    }
    //расчитывает конечную табличко с данными
    private void calculationResultTable(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((data[i][j].startsWith("’"))||(isNumber(data[i][j]))||(data[i][j]=="")){
                    resultData[i][j] = data[i][j];
                } else {
                    if (data[i][j].startsWith("=")){

                        resultData[i][j] = calculation(data[i][j]);
                    }
                }
            }
        }
        referCalculation();
    }
}
