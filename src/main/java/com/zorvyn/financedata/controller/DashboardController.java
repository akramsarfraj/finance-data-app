package com.zorvyn.financedata.controller;

import com.zorvyn.financedata.dto.ResponseBody;
import com.zorvyn.financedata.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<ResponseBody> getDashBoardSummary(
            @RequestParam(required = false,defaultValue = "0") int category){
        return dashboardService.getDashboardSummary(category);
    }



    @GetMapping("/dashboard/trends")
    @Operation(
            summary = "Get dashboard trending data",
            description = "Fetch trending data based on filters.\n\n" +
                    "• All filters are optional.\n\n" +
                    "• Monthly Data: Provide 'year' (default is current year).\n" +
                    "• Weekly Data: Provide 'month' (1-12).\n" +
                    "• Daily Data: Provide 'week' (1-5) along with month.\n\n"
    )
    public ResponseEntity<ResponseBody> getTrendingData(
            @RequestParam(required = false,defaultValue = "0")
            @Parameter(
                    description = "Week of the month (1-5). Required for daily trends.",
                    example = "2",
                    schema = @Schema(type = "integer", allowableValues = {"0","1","2","3","4","5"})
            )
            int week,
            @RequestParam(required = false,defaultValue = "0")
            @Parameter(
                    description = "Month (1-12). Required for weekly trends.",
                    example = "4",
                    schema = @Schema(type = "integer", allowableValues = {"0","1","2","3","4","5","6","7","8","9","10","11","12"})
            )
            int month,
            @RequestParam(required = false,defaultValue = "0")
            @Parameter(
                    description = "Year for monthly trends. Defaults to current year if not provided.",
                    example = "2026"
            )
            int year
            ){
        return dashboardService.getTrendingData(month,year,week);
    }

    @GetMapping("/dashboard/categories")
    public ResponseEntity<ResponseBody> getCategoriesList(){
        return dashboardService.getCategoriseList();
    }
}
