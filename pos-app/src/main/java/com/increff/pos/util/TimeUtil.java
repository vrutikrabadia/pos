package com.increff.pos.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static ZonedDateTime getCurrentZonedDateWithoutTime(){
		return setTimeToZero(ZonedDateTime.now());
	}
	public static ZonedDateTime setTimeToZero(ZonedDateTime zdt) {
		return zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

    public static ZonedDateTime formatYyyyMmDd(String date){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");

        return LocalDate.parse(date, dtf).atStartOfDay(ZoneOffset.UTC);
    }
}
