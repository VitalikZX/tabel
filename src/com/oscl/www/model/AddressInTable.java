package com.oscl.www.model;

import java.util.Locale;

/**
 * Created by Vital on 24.01.2017.
 */
public class AddressInTable {//переводит буквенночисловой формат таблички в читаемый PC цифровой код
    private int iRows;
    private int iColumn;

    private String strRows;
    private String strColumn;

    public AddressInTable(String address){
        separateRowFromColumn(address);
        convertStrAddressToInt();
    }

    public int getiRows() {
        return iRows;
    }

    public int getiColumn() {
        return iColumn;
    }
    //разбивает ссылкоадрес на колумны и ровзы
    private void separateRowFromColumn(String address){
        char[] chrAddress = address.toCharArray();
        int positionStartDigit = 1;
        for (int i = 1; i < chrAddress.length; i++) {
            if(Character.isDigit(chrAddress[i])) {positionStartDigit = i; break;}
        }
        strColumn = address.substring(0, positionStartDigit);
        strRows = address.substring(positionStartDigit, address.length());
    }
    //конвертит букво в цифро
    private void convertStrAddressToInt(){
        //переворачиваем и переводим из 26ричной системы в 10тичную
        //гацкий ловеркейс походу не работает с utf8(файл данных в этой кодировке)
        //можно конечно учитывать цифорками, но не стал так топорно
        //strColumn.toLowerCase();
        char[] charColumn = (new StringBuilder(strColumn).reverse().toString()).toCharArray();
        for (int i = 0; i < charColumn.length; i++) {
            iColumn += (int)((charColumn[i]-96) * Math.pow(26, i)); //за минусом 97 ибо это код буквы а
        }
        iColumn--;
        iRows = Integer.parseInt(strRows)-1; //строка за вычетом 1 ибо в массиве с 0
    }
}
