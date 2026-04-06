package com.zorvyn.financedata.dto;


import lombok.Getter;
import lombok.Setter;


public interface DashboardProjection {

    int getType();

    double getTotal();
}
