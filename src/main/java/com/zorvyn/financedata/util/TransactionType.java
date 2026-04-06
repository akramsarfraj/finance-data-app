package com.zorvyn.financedata.util;

public enum TransactionType {
    INCOME (0,"Income"),
    EXPENSE (1,"Expense");

    private final int value;
    private final String label;

    TransactionType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static String getTransactionLabel(int value){
        for (TransactionType type : TransactionType.values()){
            if(type.value==value){
                return type.label;
            }
        }

        return null;
    }
}
