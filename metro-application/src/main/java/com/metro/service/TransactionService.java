package com.metro.service;

import java.util.List;

import com.metro.bean.SwipeTransaction;

public interface TransactionService {

	public List<SwipeTransaction> getAllTransactionsByCardId(int cardId);
	public int addNewTransaction(SwipeTransaction transaction);
	public SwipeTransaction alreadySwipedIn(Integer cardId);
	public SwipeTransaction updateTransaction(SwipeTransaction transaction);
}
