package com.github.vikramhalder.JavaORM.Core.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDT {
    public static String toStr(String format){
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);
        java.util.Date now = new java.util.Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    public static Date toDate(String format){
        Date now = new java.util.Date();
        return now;
    }
}
