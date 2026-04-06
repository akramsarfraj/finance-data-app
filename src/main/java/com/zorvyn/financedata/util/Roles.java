package com.zorvyn.financedata.util;


public enum Roles {
    ADMIN(1,"ADMIN"),
    ANALYST(2,"ANALYST"),
    USER(3,"USER");

    private int id;
    private String label;

    Roles(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static String getRoleLabel(int id){
        for (Roles type : Roles.values()){
            if(type.id==id){
                return type.label;
            }
        }

        return null;
    }

    public static Roles getRole(int id){
        for (Roles type : Roles.values()){
            if(type.id==id){
                return type;
            }
        }

        return null;
    }
}
