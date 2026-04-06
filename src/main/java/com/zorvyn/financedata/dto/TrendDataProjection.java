package com.zorvyn.financedata.dto;

public interface TrendDataProjection {

    int getMonthNum();
    int getWeekNum();

    int getDayNum();
    double getTotalIncome();
    double getTotalExpense();
}
