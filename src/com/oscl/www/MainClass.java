package com.oscl.www;

import com.oscl.www.view.View;


/**
 * Created by GritsenkoVA on 24.01.17.
 */
public class MainClass {
    public static void main(String[] args) {
        /*Можно сделать ввод имени файла с консоли,
        * но наверное то не самоцель
        * потому дефолтный конструктор
        * для чтения файла*/

        View view = new View(); //создаём вьюху и принтим данные
        view.printStringTwoDimensionArray();

    }
}