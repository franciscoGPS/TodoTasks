package com.codef.todotasks.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fcorde on 30/09/16.
 */

public class DateUtil {

    public static String format(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return  sdf.format(date);
    }
}
