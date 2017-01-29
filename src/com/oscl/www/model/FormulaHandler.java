package com.oscl.www.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GritsenkoVA on 25.01.17.
 */
public class FormulaHandler {
    private String formula;                                 //исходная формула
    private String buff;
    private String[] addressOfCells;                        //массив ссылок и цифр в текущей формуле

    private int iu; //текущая ровза формулы
    private int ju; //текущая колумна формулы

    private String[][] resultDataTable;
    private List<String> mathOperation = new ArrayList<>(); //список мат операций в текущей формуле

    private List<String> references = new ArrayList<>();    //список ссылок и цифр в текущей формуле
    private String referencesBuffer;

    private AddressInTable adrDataBuffer;
    //Стартовая инициализация и сразу запуск вычислений
    public FormulaHandler(String[][]resultDataTable, int i, int j){
        this.resultDataTable = resultDataTable;
        this.iu = i;
        this.ju = j;
        this.formula = resultDataTable[i][j];
        parseFormula();
        changeOnNumberAndString();
    }
    //распарсивает формулу на два листа(лист матопераций и лист значений)
    private void parseFormula(){
        char[] buffer = formula.toCharArray();
        for (int i = 0; i < buffer.length; i++) {
            if ((buffer[i]=='+')||(buffer[i]=='-')||(buffer[i]=='*')||(buffer[i]=='/')) {
                switch (buffer[i]){
                    case '+': mathOperation.add("+"); break;
                    case '-': mathOperation.add("-"); break;
                    case '*': mathOperation.add("*"); break;
                    case '/': mathOperation.add("/"); break;
                }
                buffer[i] = ' ';
            }
        }
        buff = new String(buffer);
        addressOfCells = buff.split(" ");
        addressOfCells[0] = addressOfCells[0].substring(1, addressOfCells[0].length());
        for (int i = 0; i < addressOfCells.length; i++) {
            references.add(addressOfCells[i]);
        }
    }
    //проверка на кольцевые ссылки
    private boolean isFormulaHaveRingLinks(List<String> haveRings){
        AddressInTable adrtbl;
        List<String> listString = new ArrayList<>();

        for(String str: haveRings){
            if (str.startsWith("’")||(isNumber(str)||(str.startsWith("#")))){
            } else {listString.add(str);}
        }

        for(int i = 0; i < listString.size(); i++){
            adrtbl = new AddressInTable(listString.get(i));
            listString.set(i,(Integer.toString(adrtbl.getiRows()))+Integer.toString(adrtbl.getiColumn()));
        }
        listString.add(0, Integer.toString(iu) + Integer.toString(ju));

        for(int i = 0; i < listString.size()-1; i++) {
            for (int j = i+1; j < listString.size(); j++) {
                if(listString.get(i).equals(listString.get(j))){
                    return true;
                }
            }
        }
        return false;
    }
    //замена в формуле на цифры и тд.. и расчёт(если возможно)
    //жосткодлинная функция, но переписывать не хватило терпения
    private void changeOnNumberAndString(){
        boolean referenceInReferenceFlag = false;
        if(isFormulaHaveRingLinks(references)){resultDataTable[iu][ju] = "#ErrorRingLinks"; return;}

        for (int i =0; i < references.size(); i++) {
            if(isNumber(references.get(i))) continue;
            adrDataBuffer = new AddressInTable(references.get(i)); //получаем объект, содержащий адрес ячейки в цифровом формате

            buff = resultDataTable[adrDataBuffer.getiRows()][adrDataBuffer.getiColumn()];//содержит значение ячейки полученной по ссылке из формулы
            //проверяем чего было в ячейке
            if(isNumber(buff))references.set(i, buff);
            if(buff.startsWith("#")){resultDataTable[iu][ju] = "#ErrorIncorrectData"; break;}
            if(buff.startsWith("’")&(references.size()>1)){resultDataTable[iu][ju] = "#ErrorStingNotNumber"; break;}
            if(buff.startsWith("’")&(references.size()==1)){resultDataTable[iu][ju] = buff; break;}
            if(buff.length() == 0) {resultDataTable[iu][ju] = "#ErrorVoidNotNumber";  break;}
            if(buff.startsWith("=")){//если формула выставляем флаг
                buff = buff.substring(1, buff.length());
                references.set(i, buff);
                referenceInReferenceFlag = true;
            }
            buff = "";
        }

        if (referenceInReferenceFlag == true){    //формируем новую формулу
            referencesBuffer = "=" + references.get(0);
            for (int i = 0; i < mathOperation.size(); i++) {
                referencesBuffer += mathOperation.get(i) + references.get(i+1);
            }
            //записываем новую формулу в конечную таблицу и повторяем все процедуры
            resultDataTable[iu][ju] = referencesBuffer;
            referenceInReferenceFlag = false;
            formula = referencesBuffer;
            mathOperation.clear();
            references.clear();
            parseFormula(); //парсим формулу
            isFormulaHaveRingLinks(references); //проверяем на кольцевые ссылки
            changeOnNumberAndString(); //заменяем значения и считаем(если возможно)
        } else { //если в итоговой формуле все значения номера считаем ячейку
            boolean isReferencesNumber = false;
            for (String str : references) {//проверяем все ли значения - циферки
                if(isNumber(str)) {
                    isReferencesNumber = true;} else {
                    isReferencesNumber = false;
                    break;
                }
            }

            if (isReferencesNumber == true){//считаем
                int result = Integer.parseInt(references.get(0));
                for(int i =0 ; i < mathOperation.size(); i++){
                    switch (mathOperation.get(i)){
                        case "+": result = result + Integer.parseInt(references.get(i+1)); break;
                        case "-": result = result - Integer.parseInt(references.get(i+1)); break;
                        case "*": result = result * Integer.parseInt(references.get(i+1)); break;
                        case "/": result = result / Integer.parseInt(references.get(i+1)); break;
                    }
                }
                isReferencesNumber = false;
                resultDataTable[iu][ju] = Integer.toString(result);
                mathOperation.clear();
                references.clear();
            }
        }
    }
    //проверяет не число ли
    private boolean isNumber(String strNumber){
        char[] chr = strNumber.toCharArray();
        if (chr[0] == '-'){
            for (int i = 1; i < chr.length; i++) {
                if (!Character.isDigit(chr[i])) return false;
            }
        } else {
            for (char e : chr) {
                if (!Character.isDigit(e)) return false;
            }
        }
        return true;
    }
}
