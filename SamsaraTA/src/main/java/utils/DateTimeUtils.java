package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils extends LoggerUtils {

    public static void wait (int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            log.warn("InterruptedException in Thread.sleep(). Message: " + e.getMessage());
        }
    }

    public static Date getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static Date getDateTime(long milliseconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        return cal.getTime();
    }

    public static String getFormattedDateTime(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String getFormattedCurrentDateTime(String pattern) {
        Date date = getCurrentDateTime();
        return getFormattedDateTime(date, pattern);
    }

    public static String getDateTimeStamp() {
        return getFormattedCurrentDateTime("yyMMddHHmmss");
    }

    public static Date getBrowserDateTime(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String sBrowserDateTime = (String) js.executeScript("var browserDateTime = new Date().getTime(); return Intl.DateTimeFormat('en-GB', {dateStyle: 'full', timeStyle: 'long'}).format(browserDateTime);")
        sBrowserDateTime = sBrowserDateTime.replace(" at ", " ");
        String sPattern = "EEEE, dd MMMM yyyy HH:mm:ss z";
        return getParsedDateTime(sBrowserDateTime, sPattern);
    }

    public static String getBrowserTimeZone(WebDriver driver) {
        Date date = getBrowserDateTime(driver);
        return getFormattedDateTime(date, "z");
    }

    public static boolean compareDateTimes(Date date1, Date date2, int threshold) {
        long diff = (date2.getTime() - date1.getTime()) / 1000;
        return Math.abs(diff) <= threshold;
    }
}
