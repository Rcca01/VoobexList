package br.com.voobex.todolist.Util;

import java.text.SimpleDateFormat;

/**
 * Created by SnowFlake on 31.01.2016.
 * Helps to work with date and time.
 */
public class Utils {
    public static String getDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(date);
    }

    public static String getTime(long time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(time);
    }
    //returns full date
    public static String getFullDate(long date) {
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
        return fullDateFormat.format(date);
    }
}
