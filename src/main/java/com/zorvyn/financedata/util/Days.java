package com.zorvyn.financedata.util;

public enum Days {
    SUNDAY(0, "Sunday"),
    MONDAY(1, "Monday"),
    TUESDAY(2, "Tuesday"),
    WEDNESDAY(3, "Wednesday"),
    THURSDAY(4, "Thursday"),
    FRIDAY(5, "Friday"),
    SATURDAY(6, "Saturday");

    private final int value;
    private final String label;

    Days(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static Days fromValue(int value) {
        for (Days day : Days.values()) {
            if (day.value == value) {
                return day;
            }
        }
        return null;
    }
}
