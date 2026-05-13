package com.liveclass.be_b.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static LocalDateTime startOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1).atStartOfDay();
    }

    public static LocalDateTime endExclusiveOfMonth(YearMonth yearMonth) {
        return yearMonth.plusMonths(1).atDay(1).atStartOfDay();
    }

    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    public static LocalDateTime endExclusiveOfDay(LocalDate date) {
        return date.plusDays(1).atStartOfDay();
    }
}
