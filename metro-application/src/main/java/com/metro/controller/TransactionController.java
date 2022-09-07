package com.metro.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.metro.bean.MetroCard;
import com.metro.bean.Passenger;
import com.metro.bean.SwipeTransaction;
import com.metro.service.MetroCardService;
import com.metro.service.MetroUserService;
import com.metro.service.TransactionService;

@Controller
public class TransactionController {

	@Autowired
	private MetroCardService metroCardService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private MetroUserService metroUserService;

	@RequestMapping("/showTransactions")
	public ModelAndView allTransactionController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		Passenger passenger = metroUserService.getPassenger(userId);
		List<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
		List<SwipeTransaction> transactionList = new ArrayList<SwipeTransaction>();
		modelAndView.setViewName("mytransactions");
		modelAndView.addObject("transactionList",transactionList);
		modelAndView.addObject("cardList",cardList);
		return modelAndView;
	}
	
	@RequestMapping("/showMyTransactions")
	public ModelAndView myTransactionController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		String cardId = (String) request.getParameter("cardId1");
		Passenger passenger = metroUserService.getPassenger(userId);
		List<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
		List<SwipeTransaction> transactionList = transactionService.getAllTransactionsByCardId(Integer.parseInt(cardId));
		if(transactionList==null||transactionList.isEmpty()) {
			transactionList = new ArrayList<SwipeTransaction>();
			transactionList.add(new SwipeTransaction(null, null, null, null, null, null, null));
		}
		modelAndView.setViewName("mytransactions");
		modelAndView.addObject("cardList",cardList);
		modelAndView.addObject("transactionList",transactionList);
		return modelAndView;
	}
}
