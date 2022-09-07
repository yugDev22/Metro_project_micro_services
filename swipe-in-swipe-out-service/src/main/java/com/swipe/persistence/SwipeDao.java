package com.swipe.persistence;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swipe.bean.SwipeTransaction;

import java.lang.Integer;
import java.time.LocalDateTime;

@Repository
public interface SwipeDao extends JpaRepository<SwipeTransaction, String> {

	public List<SwipeTransaction> findByCardId(Integer cardId);

	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="INSERT INTO SwipeTransaction (transactionId,cardId,boardingStationId,swipeInTime) values(:transactionId,:cardId,:boardingStationId,:swipeInTime)")
	public int addSwipeInTransaction(@Param("transactionId") String transactionId,
			@Param("cardId") Integer cardId, 
			@Param("boardingStationId") Integer boardingStationId,
			@Param("swipeInTime") LocalDateTime swipeInTime);
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="UPDATE SwipeTransaction SET destinationStationId=:dest, fare=:fare, swipeOutTime=:time WHERE transactionId=:tid")
	public int addSwipeOutTransaction(@Param("dest") Integer destinationStationId,@Param("fare") Double fare,@Param("time") LocalDateTime swipeOutTime,@Param("tid") String transactionId);
	
	@Query(nativeQuery=true,value="SELECT * FROM SwipeTransaction ORDER BY transactionId DESC LIMIT 1")
	public SwipeTransaction getLastTransaction();
	
	@Query(nativeQuery=true,value="SELECT * FROM SwipeTransaction WHERE cardId=:id AND destinationStationId is null")
	public SwipeTransaction getSwipedInTransaction(@Param("id") Integer cardId);

}
