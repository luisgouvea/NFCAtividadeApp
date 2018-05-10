package com.fiquedeolho.nfcatividadeapp.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {

    public static String formatDate(Date date) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateString = sdfr.format(date);
        } catch (Exception ex) {
            try {
                throw new Exception(ex.getMessage());
            } catch (Exception e) {
                Log.e("##erroConvert##", "Excecao [Convert.formatDate()]");
            }
        }
        return dateString;
    }
}
