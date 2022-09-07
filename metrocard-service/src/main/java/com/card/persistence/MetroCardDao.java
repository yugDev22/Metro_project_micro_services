package com.card.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.card.bean.MetroCard;
import java.lang.Integer;
import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface MetroCardDao extends JpaRepository<MetroCard, Integer> {

	
	@Query(nativeQuery=true,value="SELECT * FROM MetroCard ORDER BY cardId DESC LIMIT 1")
	public MetroCard getLastCard();
	
	List<MetroCard> findByPassengerId(Integer passengerid);
	
	@Query(nativeQuery=true,value="UPDATE MetroCard c SET c.balance=:bal WHERE c.cardId=:cId")
	@Modifying
	@Transactional
	public int updateMetroCard(@Param("bal")double balance,@Param("cId")int cardId);
}
