package com.github.vikramhalder.JavaORM.Core.DateTime;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ConvertDT {
    public static String toStr(Date date, String date_formet) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date_formet != null) {
            if(!date_formet.toUpperCase().equals("CURRENT_TIMESTAMP")) {
                df = new SimpleDateFormat(date_formet);
            }
        }
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        return reportDate;
    }

    public static Date toDate(String string, String date_formet) {
        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (date_formet != null)
                formatter1 = new SimpleDateFormat(date_formet);
            Date date = formatter1.parse(string);
            return date;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String HH12(String time, boolean aa) {
        try {
            DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(time));
            if (aa)
                if (Integer.valueOf(time.split(":")[0]) >= 12)
                    return sdf.format(c.getTime()) + " PM";
                else
                    return sdf.format(c.getTime()) + " AM";
            return sdf.format(c.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String timezoneToDT(String timezoon) {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = utcFormat.parse(timezoon);
            DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return pstFormat.format(date);
        } catch (Exception ex) {
            return timezoon;
        }
    }
    public static String timezoneToDT(String timezoon, String format) {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = utcFormat.parse(timezoon);
            DateFormat pstFormat = pstFormat = new SimpleDateFormat(format);
            return pstFormat.format(date);
        } catch (Exception ex) {
            return timezoon;
        }
    }

}
