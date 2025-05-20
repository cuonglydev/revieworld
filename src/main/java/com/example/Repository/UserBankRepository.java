package com.example.Repository;

import com.example.Entity.UserBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBankRepository extends JpaRepository<UserBank, Integer> {
	 List<UserBank> findAllByUserId(int userId);
//    @Query("SELECT b FROM UserBank b WHERE b.user.id = :userId")
//    List<UserBank> findByUserId(@Param("userId") int userId);
//
//    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM UserBank b WHERE b.user.id = :userId AND b.accountNumber = :accountNumber")
//    boolean existsByUserIdAndAccountNumber(@Param("userId") int userId, @Param("accountNumber") String accountNumber);
//
//    @Query("SELECT b FROM UserBank b WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
//    List<UserBank> findByUserIdOrderByCreatedAtDesc(@Param("userId") int userId);
    
   
} 