package com.metro.service;

import org.springframework.stereotype.Service;
import com.metro.bean.Passenger;


@Service
public interface MetroUserService {

	public boolean validateUser(String userID, String password);
	public Passenger getPassenger(String userId);
	public boolean registerUser(Passenger passenger);

}
