package com.hbtheme.infigestback.tools;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateUtils {
	
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.FRANCE);

	private DateUtils() {
	}

	public static LocalDate parseStringToDate(String stringDate) {
		if (stringDate == null || stringDate.isEmpty()) {
			return null;
		}
		return LocalDate.parse(stringDate, dateFormatter);
	}
	
	public static boolean isValidDate(String dateStr) {
		try {
            LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            return false;
        }
        return true;
	}

}
