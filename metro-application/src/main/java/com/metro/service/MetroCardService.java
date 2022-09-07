package com.metro.service;

import java.util.ArrayList;

import com.metro.bean.MetroCard;

public interface MetroCardService {
	
	public MetroCard searchMetroCardById(Integer cardId);
	public MetroCard issueNewMetroCard(MetroCard card);
	public Double checkCardBalance(Integer id);
	public Integer AddCardBalance(Integer id, Double balance);
	public MetroCard RefundMetroCard(Integer cardId);
	public ArrayList<MetroCard> getAllCards(Integer passengerId);
	public int deductBalance(Integer cardId, double balance);
	
	
}
