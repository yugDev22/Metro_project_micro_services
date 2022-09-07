package com.passenger.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.passenger.bean.Passenger;
import com.passenger.persistence.PassengerDao;

@Service
public class PassengerServiceImpl implements PassengerService {

	@Autowired
	private PassengerDao passengerDao;
	
	@Override
	public Passenger searchPassengerById(Integer id) {
		return passengerDao.findById(id).orElse(null);
		
	}

	@Override
	public Passenger addNewPassenger(Passenger passenger) {
		try {
			return passengerDao.save(passenger);
		}catch(Exception e) {
			return null;
		}

	}

	@Override
	public Passenger updatePassengerDetails(Passenger passenger) {
		try {
			return passengerDao.save(passenger);
		}catch(Exception e) {
				return null;
		}
	}

	@Override
	public Integer generatePassengerId() {
		Passenger passenger = passengerDao.getLastPassenger();
		if(passenger!=null) {
			return (passenger.getPassengerId()+1);
		}
		return 2001;
	}

	@Override
	public Passenger searchPassengerByEmailId(String email) {
		List<Passenger> passengers = passengerDao.findByPassengerEmail(email);
		if(!passengers.isEmpty()) {
			return passengers.get(0);
		}
		return null;
	}

}
