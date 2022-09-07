package com.passenger.service;

import com.passenger.bean.Passenger;

public interface PassengerService {

	public Passenger searchPassengerById(Integer id);
	public Passenger searchPassengerByEmailId(String email);
	public Passenger addNewPassenger(Passenger passenger);
	public Passenger updatePassengerDetails(Passenger passenger);
	public Integer generatePassengerId();
}
