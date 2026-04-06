package com.zorvyn.financedata.util;


public enum Category {

    // Expense
    FOOD(1, "Food"),
    GROCERIES(2, "Groceries"),
    RENT(3, "Rent"),
    TRANSPORT(4, "Transport"),
    HEALTHCARE(5, "Healthcare"),

    // Income
    SALARY(6, "Salary"),
    BONUS(7, "Bonus"),
    INVESTMENT(8, "Investment"),
    INTEREST(9, "Interest"),
    OTHER_INCOME(10, "Other Income");

    private final int value;
    private final String label;

    Category(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static String getCategoryLabelByValue(int value){
        for (Category category : Category.values()){
            if(category.value==value){
                return category.label;
            }
        }

        return null;
    }
}
