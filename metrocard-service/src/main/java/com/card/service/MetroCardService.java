package com.card.service;

import java.util.ArrayList;
import java.util.List;

import com.card.bean.MetroCard;

public interface MetroCardService {
	
	public MetroCard searchMetroCardById(Integer cardId);
	public MetroCard issueNewMetroCard(MetroCard card);
	public Double checkCardBalance(Integer id);
	public Integer addCardBalance(Integer id, Double balance);
	public MetroCard refundMetroCard(Integer cardId);
	public Integer getCardId();
	public List<MetroCard> getAllCards(Integer passengerId);
	public int deductBalance(Integer cardId, double balance);

}
