package com.zorvyn.financedata.repository;

import com.zorvyn.financedata.dto.DashboardProjection;
import com.zorvyn.financedata.dto.TrendDataProjection;
import com.zorvyn.financedata.model.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions,Long> {


    @Query(value = "select t from  Transactions t  " +
            " where ( :category is null or t.category=:category ) " +
//            " and (:date is null or t.date=:date ) " +
            " and ( :type is null or t.type=:type ) "
    )
    Page<Transactions> findAllByDateTypeOrCategory(Pageable pageable,
                                                   @Param("type") Integer type,
                                                   @Param("category")  Integer category);

    @Query("select t from  Transactions t order by t.createdAt desc limit 5 ")
    List<Transactions> getRecentFiveTransactionRecord();

    @Query("select t.type as type , sum(t.amount) as total  " +
            "  from  Transactions t " +
            " where ( :category=0 or t.category=:category ) " +
            " group by t.type " )
    List<DashboardProjection> getSummaryByCategory(@Param("category") int category);



    /*
    *  here first filter per year data and then grouping
    * the record base on month(1-12), and then calculating the totalIncome and expense
    * */
    @Query(value = """
        select EXTRACT(MONTH FROM t.date) AS monthNum,
        SUM(case when t.type=0 then t.amount else 0 end ) as totalIncome,
        SUM(case when t.type=1 then t.amount else 0 end ) as totalExpense
        from Transactions t 
        where EXTRACT(YEAR FROM t.date) = :year 
        group by EXTRACT(MONTH FROM t.date)
        ORDER BY monthNum
      """, nativeQuery = true)
    List<TrendDataProjection> getTrendingDataMonthlyBaseOnYear(@Param("year")int year);

    /*
     *  here first filter per year and month, then grouping
     * the record base on week(1-5)  using to_char function ,
     *  and then calculating the totalIncome and expense
     * */
    @Query(value = """
        select to_char(date, 'W') AS weekNum,
        SUM(case when t.type=0 then t.amount else 0 end ) as totalIncome,
        SUM(case when t.type=1 then t.amount else 0 end ) as totalExpense
        from Transactions t 
        where EXTRACT(YEAR FROM t.date) = :year 
        and (:month=0 or EXTRACT(MONTH FROM t.date) =:month)
        group by to_char(date, 'W')
        ORDER BY weekNum
      """, nativeQuery = true)
    List<TrendDataProjection> getTrendingDataWeeklyBaseOnMonth(@Param("month")int month,
                                                                 @Param("year")int year);


    /*
     *  here first filter per year,month,week .Then grouping
     * the record base on days(0(sun)-6(sat)) using extract(DOW FROM ? ) function ,
     *  and then calculating the totalIncome and expense
     * */
    @Query(value = """
       select extract(DOW FROM t.date) AS dayNum,
        SUM(case when t.type=0 then t.amount else 0 end ) as totalIncome,
        SUM(case when t.type=1 then t.amount else 0 end ) as totalExpense
        from Transactions t 
        where EXTRACT(YEAR FROM t.date) = :year 
        and (:month=0 or EXTRACT(MONTH FROM t.date) =:month)
        and  to_char(date, 'W')=:week
        group by extract(DOW FROM t.date)
        ORDER BY dayNum
      """, nativeQuery = true)
    List<TrendDataProjection> getTrendingDataDailyBaseOnWeek(@Param("week") String week,
                                                             @Param("month")int month,
                                                             @Param("year")int year);
}
