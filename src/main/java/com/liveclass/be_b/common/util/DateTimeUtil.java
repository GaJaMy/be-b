package com.liveclass.be_b.common.util;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.YearMonth;

public final class DateTimeUtil {
    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");

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

    public static LocalDateTime toKstLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(KST_ZONE_ID)
                .toLocalDateTime();
    }

    public static OffsetDateTime toKstOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime.atOffset(ZoneOffset.ofHours(9));
    }

    public static void validateDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

    public static void validateYearMonthRange(YearMonth from, YearMonth to) {
        if (from.isAfter(to)) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        }
    }
}
