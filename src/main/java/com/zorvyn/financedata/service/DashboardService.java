package com.zorvyn.financedata.service;

import com.zorvyn.financedata.dto.DashboardProjection;
import com.zorvyn.financedata.dto.ResponseBody;
import com.zorvyn.financedata.dto.TrendDataProjection;
import com.zorvyn.financedata.repository.TransactionRepository;
import com.zorvyn.financedata.util.Category;
import com.zorvyn.financedata.util.Days;
import com.zorvyn.financedata.util.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    private TransactionRepository transactionRepository;


    public ResponseEntity<ResponseBody> getDashboardSummary(int category){
        List<DashboardProjection> summary = transactionRepository.getSummaryByCategory(category);

        Map<String,Object> data = new HashMap<>();

        double income = 0.0;
        double expense = 0.0;

        for(DashboardProjection d : summary){
            if(d.getType()== TransactionType.INCOME.getValue()) {

                income = d.getTotal();
            }else {

                expense = d.getTotal();
            }
        }

        data.put("total-income", income);
        data.put("total-expense",expense);
        data.put("net-balance",income - expense);
        data.put("category",category==1? "Default": Category.getCategoryLabelByValue(category) );

        // send recent top 5 activity
        data.put("recent-activity",transactionRepository.getRecentFiveTransactionRecord());

        ResponseBody body = new ResponseBody<>();
        body.setData(data);
        body.setCurrentTimestamp(new Date());
        body.setStatus(HttpStatus.OK.getReasonPhrase());
        body.setMessage("dashboard summary fetch.");

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public ResponseEntity<ResponseBody> getTrendingData(int month, int year, int week){
        year = year==0? LocalDate.now().getYear() : year;
        List<TrendDataProjection> trends;

        if(month==0) {
            log.info("Getting monthly trending data base on year");
            trends = transactionRepository
                    .getTrendingDataMonthlyBaseOnYear( year);
        }else if(month!=0 && week!=0) {

            log.info("Getting daily trending data base on week");

            trends = transactionRepository
                    .getTrendingDataDailyBaseOnWeek(String.valueOf(week),month,year);
        }else{
            log.info("Getting weekly trending data base on month");
            trends = transactionRepository
                    .getTrendingDataWeeklyBaseOnMonth(month,year);
        }


        List<Map<String,Object>> trendingData = new ArrayList<>();

        for(TrendDataProjection d : trends){

            Map<String,Object> data = new HashMap<>();

            data.put("total-income", d.getTotalIncome());
            data.put("total-expense",d.getTotalExpense());
            data.put("profit", d.getTotalIncome()- d.getTotalExpense());
            if(month==0) {
                data.put("month", d.getMonthNum());
            } else if (month!=0 && week!=0) {
                data.put("day",d.getDayNum());
                data.put("dayLabel", Days.fromValue(d.getDayNum()).getLabel());
            } else {
                data.put("week",d.getWeekNum());
            }


            trendingData.add(data);

        }

        Map<String,Object> resBody = new HashMap<>();
        resBody.put("year",year);
        resBody.put("trendData",trendingData);

        if(month==0) {
            resBody.put("monthValue", IntStream.rangeClosed(1, 12)
                    .boxed()
                    .collect(Collectors.toList()));
        }else if (month!=0 && week!=0) {
            resBody.put("month",month);
            resBody.put("week",week);
            resBody.put("days",Arrays.stream(Days.values())
                    .map(m->m.getValue())
                    .collect(Collectors.toList())
            );
            resBody.put("daysLabel",Arrays
                    .stream(Days.values())
                    .map(m->m.getLabel())
                    .collect(Collectors.toList())
            );

        }else {
            resBody.put("month",month);
            resBody.put("weekValue",IntStream.rangeClosed(1,5)
                    .boxed().collect(Collectors.toList()));
        }


        ResponseBody body = new ResponseBody<>();
        body.setData(resBody);
        body.setCurrentTimestamp(new Date());
        body.setStatus(HttpStatus.OK.getReasonPhrase());
        body.setMessage("trending data fetch for year = "+year);

        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

    public ResponseEntity<ResponseBody> getCategoriseList() {
         Map<Integer,String> map = Arrays.stream(Category.values())
                .collect(Collectors.toMap(Category::getValue,Category::getLabel));
        ResponseBody body = new ResponseBody<>();
        body.setData(map);
        body.setCurrentTimestamp(new Date());
        body.setStatus(HttpStatus.OK.getReasonPhrase());
        body.setMessage("categories list.");

       return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
