package com.metro.service;

import com.metro.bean.Passenger;

public interface PassengerService {

	public Passenger searchPassengerById(Integer id);
	public Passenger addNewPassenger(Passenger passenger);
	public Passenger updatingPassengerDetails(Passenger passenger);
}
