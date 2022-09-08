package com.metro.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.metro.bean.MetroCard;
import com.metro.bean.Passenger;
import com.metro.service.MetroCardService;
import com.metro.service.MetroUserService;

@Controller
public class CardController {

	@Autowired
	private MetroCardService metroCardService;

	@Autowired
	private MetroUserService metroUserService;

	@RequestMapping("/showCards")
	public ModelAndView showAllCardsController(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		if (userId == null || userId.isEmpty()) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		Passenger passenger = metroUserService.getPassenger(userId);
		ArrayList<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
		if(cardList.isEmpty()) {
			ModelAndView modelAndView = new ModelAndView("cards", "cardList", new ArrayList<MetroCard>());
			modelAndView.addObject("message", "No cards to show");
			return modelAndView;
		}
		ModelAndView modelAndView = new ModelAndView("cards", "cardList", cardList);
		modelAndView.addObject("message", "");
		return modelAndView;
	}

	@RequestMapping("/issueCard")
	public ModelAndView cardIssuePageController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();

		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");

		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		
		modelAndView.setViewName("issuecard");
		return modelAndView;
	}

	@RequestMapping("/issueNow")
	public ModelAndView cardIssueController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();

		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");

		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		String balance = (String) request.getParameter("bal");
		if (!balance.isBlank()) {
			double bal = Double.parseDouble(balance);
			if (bal >=100) {
				Passenger passenger = metroUserService.getPassenger(userId);
				MetroCard metroCard = new MetroCard(0, passenger.getPassengerId(), bal);
				if(metroCardService.issueNewMetroCard(metroCard)!=null) {
					modelAndView.setViewName("cards");
					modelAndView.addObject("message", "Card issued successfully");
					ArrayList<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
					modelAndView.addObject("cardList", cardList);
					return modelAndView;
				}
				else {
					modelAndView.setViewName("issuecard");
					modelAndView.addObject("message", "Unable to issue card");
					return modelAndView;
				}
			}
		}

		modelAndView.setViewName("issuecard");
		modelAndView.addObject("message", "Unable to issue card, balance is less than 100");
		return modelAndView;
	}
	
	@RequestMapping("/recharge")
	public ModelAndView rechargePageController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");

		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		
		Passenger passenger = metroUserService.getPassenger(userId);
		ArrayList<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
		modelAndView.addObject("cardList", cardList);
		modelAndView.setViewName("recharge");
		modelAndView.addObject("message","");
		return modelAndView;
	}
	
	@RequestMapping("/rechargeNow")
	public ModelAndView rechargeCardController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");

		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		String balance = (String) request.getParameter("bal");
		String cardId = (String) request.getParameter("cardId1");
		if(cardId==null) {
			modelAndView.setViewName("recharge");
			modelAndView.addObject("message","Invalid card id");
			modelAndView.addObject("cardList", new ArrayList<MetroCard>());
			return modelAndView;
		}
		if (!balance.isBlank()||cardId.isBlank()) {
			double bal = Double.parseDouble(balance);
			if (bal >0) {
				Integer updated = metroCardService.AddCardBalance(Integer.parseInt(cardId), bal);
				if(updated!=null) {
					Passenger passenger = metroUserService.getPassenger(userId);
					String message = "Balance of Card: "+cardId+" updated";
					modelAndView.setViewName("cards");
					modelAndView.addObject("message",message);
					ArrayList<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
					modelAndView.addObject("cardList", cardList);
					return modelAndView;
					
				}
				
			}
		}
		Passenger passenger = metroUserService.getPassenger(userId);
		ArrayList<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
		modelAndView.addObject("cardList", cardList);
		modelAndView.setViewName("recharge");
		modelAndView.addObject("message","Invalid card, or entered balance is less than 0");
		return modelAndView;
	}

}
