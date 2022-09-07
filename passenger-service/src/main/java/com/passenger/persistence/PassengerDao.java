package com.passenger.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.passenger.bean.Passenger;
import java.lang.String;
import java.util.List;

@Repository
public interface PassengerDao extends JpaRepository<Passenger,Integer> {

	@Query(nativeQuery=true,value="SELECT p FROM PASSENGER p ORDER BY p.passengerId DESC LIMIT 1")
	public Passenger getLastPassenger();
	
	List<Passenger> findByPassengerEmail(String passengeremail);
	
}
