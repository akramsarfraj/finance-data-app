package com.zorvyn.financedata.repository;


import com.zorvyn.financedata.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    @Query(" select u from User u " +
            " where (:search is null or u.userName like %:search% )" +
            " and ( :isDelete is null or u.isDelete=:isDelete ) " +
            " and ( :isActive is null  or u.isActive=:isActive) ")
    Page<User> findAllByActiveAndDeleteFilter(Pageable pageable,
                                             @Param("isDelete") Boolean isDelete,
                                              @Param("isActive")  Boolean isActive,
                                              @Param("search") String search );



    @Query("select u from User u where u.email=:email and u.isDelete=false ")
    Optional<User> findByEmailAndDelete(@Param("email") String email);

    Optional<User> findByEmail(String email);


}
