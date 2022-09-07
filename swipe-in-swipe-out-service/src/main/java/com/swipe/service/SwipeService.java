package com.swipe.service;

import java.util.List;

import com.swipe.bean.MetroCard;
import com.swipe.bean.SwipeTransaction;

public interface SwipeService {
	public List<SwipeTransaction> getAllSwipeTransactionsByCardId(int cardId);
	public int swipeIn(SwipeTransaction transaction);
	public SwipeTransaction alreadySwipedIn(Integer cardId);
	public SwipeTransaction swipeOut(SwipeTransaction transaction);
	public String getTransactionId();
	public MetroCard getMetroCardById(Integer cardId);
}
