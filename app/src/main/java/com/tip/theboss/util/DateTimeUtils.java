package com.tip.theboss.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public class DateTimeUtils {

    private static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    private static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

    public static String toDuration(Date date) {
        try {
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(date); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 8); // adds 8 hour
            // returns new date object, one hour in the future
            Date mDate = cal.getTime();
            Date currentDate = new Date();
            long duration = currentDate.getTime() - mDate.getTime();

            StringBuilder res = new StringBuilder();
            for (int i = 0; i < times.size(); i++) {
                Long current = times.get(i);
                long temp = duration / current;
                if (temp > 0) {
                    res.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                    break;
                }
            }
            if ("".equals(res.toString()))
                return "just a moment ago";
            else
                return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to parse data";
        }
    }

    public static String getLongDateTimeString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(date);
    }

}
