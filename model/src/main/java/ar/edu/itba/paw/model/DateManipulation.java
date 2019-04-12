package ar.edu.itba.paw.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateManipulation {

    public static Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar stringToCalendar(String string) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
        try {
            Date date = sdf.parse(string);
            cal.setTime(date);
        }
        catch(Exception e) {
            //TODO
        }
        return cal;
    }
}
