package li.zhuoran.survey.utils;

import org.joda.time.DateTime;
import java.util.Date;

public class DateGenerator {
    public static int DAYS_PER_YEAR = 365;

    public static Date daysAgo(int days) {
        return new DateTime(new Date()).minusDays(days).toDate();
    }

    public static Date yearsAgo(int years) {
        return new DateTime(new java.util.Date()).minusYears(years).toDate();
    }
}
