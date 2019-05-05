package com.android.ledgerbook.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {
    private static final int PAISE_PER_RUPEE = 100;
    private static final Format INDIAN_NUMBER_FORMAT = new DecimalFormat("##,##,##0");
    private static final String COMMON_DATE_FORMAT = "dd-MMM-yy";
    private static final Locale DATE_LOCALE = Locale.ENGLISH;
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Formats an amount in Rupees according to the Indian numbering system.
     *
     * @param rupee the amount in Rupees
     * @return A formatted string in Rupees
     * @see <a href="https://en.wikipedia.org/wiki/Indian_numbering_system">Indian_numbering_system</a>
     */
    public static String formatRupee(long rupee) {
        return INDIAN_NUMBER_FORMAT.format(rupee);
    }

    /**
     * Formats an amount in Paise according to the Indian numbering system.
     *
     * @param paise the amount in Paise
     * @return A formatted string in Rupees
     * @see <a href="https://en.wikipedia.org/wiki/Indian_numbering_system">Indian_numbering_system</a>
     */
    public static String formatPaise(long paise) {
        return formatRupee(paise / PAISE_PER_RUPEE);
    }

    /**
     * Format given date using given pattern
     *
     * @param date   Date
     * @param format Date format
     * @return A formatted date
     */
    public static String formatDate(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format, DATE_LOCALE);
        return df.format(date);
    }

    public static String toCamelCase(String text) {
        String[] parts = text.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return sb.toString();
    }
}
