package com.metro.controller;

import java.time.LocalDateTime;
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
import com.metro.bean.Station;
import com.metro.bean.SwipeTransaction;
import com.metro.service.MetroCardService;
import com.metro.service.MetroUserService;
import com.metro.service.StationService;
import com.metro.service.TransactionService;

@Controller
public class CardSwipeController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private MetroUserService metroUserService;
	
	@Autowired
	private MetroCardService metroCardService;
	
	@Autowired
	private StationService stationService;
	
	@RequestMapping("/swipeCard")
	public ModelAndView cardSwipeController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		
		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		//check if already swiped in or not
		List<Station> stationList = stationService.getAllStations();
		Passenger passenger = metroUserService.getPassenger(userId);
		ArrayList<MetroCard> cardList = metroCardService.getAllCards(passenger.getPassengerId());
		String cardId = (String) request.getParameter("cardId1");
		if(!(cardId==null||cardId.isEmpty())) {
			MetroCard card = metroCardService.searchMetroCardById(Integer.parseInt(cardId));
			if(card.getBalance()<20) {
				modelAndView.setViewName("swipe");
				modelAndView.addObject("cardList",cardList);
				modelAndView.addObject("message", "Unable to swipe, balance is less than 20, please recharge");
				return modelAndView;
			}
			SwipeTransaction transaction = transactionService.alreadySwipedIn(Integer.parseInt(cardId));
			if(transaction!=null){
				modelAndView.setViewName("swipeout");
				modelAndView.addObject("stationList",stationList);
				modelAndView.addObject("message", "");
				modelAndView.addObject("cardId",cardId);
				return modelAndView;
			}
			else {
				modelAndView.setViewName("swipein");
				modelAndView.addObject("cardId",cardId);
				modelAndView.addObject("message", "");
				modelAndView.addObject("stationList",stationList);
				return modelAndView;
			}
		}
		modelAndView.setViewName("swipe");
		modelAndView.addObject("stationList",stationList);
		modelAndView.addObject("message", "Unable to swipe..");
		return modelAndView;
	}
	
	@RequestMapping("/swipeOut")
	public ModelAndView swipeOutController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		
		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		
		String cardId = (String) request.getParameter("cardId");
		String destinationStationId = request.getParameter("destinationStationId");
		if(destinationStationId==null) {
			List<Station> stationList = stationService.getAllStations();
			modelAndView.addObject("cardId",cardId);
			modelAndView.addObject("stationList",stationList);
			modelAndView.setViewName("swipeout");
			modelAndView.addObject("message", "Unable to swipe out, invalid station!");
			return modelAndView;
			
		}
		if(!(cardId==null||cardId.isEmpty()||destinationStationId.isEmpty())) {
			if(metroCardService.searchMetroCardById(Integer.parseInt(cardId)).getBalance()<20) {
				List<Station> stationList = stationService.getAllStations();
				modelAndView.addObject("cardId",cardId);
				modelAndView.addObject("stationList",stationList);
				modelAndView.setViewName("swipeout");
				modelAndView.addObject("message", "Unable to swipe out, balance is less than 20");
				return modelAndView;
			}
			SwipeTransaction transaction = transactionService.alreadySwipedIn(Integer.parseInt(cardId));
			if(transaction!=null) {
				transaction.setDestinationStationId(Integer.parseInt(destinationStationId));
				transaction.setSwipeOutTime(LocalDateTime.now());
				SwipeTransaction updated = transactionService.updateTransaction(transaction);
				if(updated!=null) {
					modelAndView.setViewName("swipestatus");
					modelAndView.addObject("message","Successfully swiped out at station: "+stationService.searchMetroStationById(Integer.parseInt(destinationStationId)).getStationName()+" fare charged: "+updated.getFare());
					return modelAndView;
				}
			}
		}

		List<Station> stationList = stationService.getAllStations();
		modelAndView.addObject("cardId",cardId);
		modelAndView.addObject("stationList",stationList);
		modelAndView.setViewName("swipeout");
		modelAndView.addObject("message", "Unable to swipe out");
		return modelAndView;
	}
	
	@RequestMapping("/swipeIn")
	public ModelAndView swipeInController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("userId");
		
		if (userId == null || userId.isEmpty()) {
			modelAndView.setViewName("output");
			modelAndView.addObject("message", "Invalid session..");
			return modelAndView;
		}
		
		String cardId = request.getParameter("cardId");
		String sourceStationId = request.getParameter("sourceStationId");
		if(sourceStationId==null) {
			List<Station> stationList = stationService.getAllStations();
			modelAndView.addObject("cardId",cardId);
			modelAndView.addObject("stationList",stationList);
			modelAndView.setViewName("swipein");
			modelAndView.addObject("message", "Unable to swipe in, invalid station!");
			return modelAndView;
			
		}
		if(!(cardId==null||cardId.isEmpty()||sourceStationId.isEmpty())) {
			if(metroCardService.searchMetroCardById(Integer.parseInt(cardId)).getBalance()<20) {
				List<Station> stationList = stationService.getAllStations();
				modelAndView.addObject("cardId",cardId);
				modelAndView.addObject("stationList",stationList);
				modelAndView.setViewName("swipein");
				modelAndView.addObject("message", "Unable to swipe in, balance is less than 20");
				return modelAndView;
			}
			
			if(transactionService.alreadySwipedIn(Integer.parseInt(cardId))==null) {
				SwipeTransaction transaction = new SwipeTransaction("", Integer.parseInt(cardId), Integer.parseInt(sourceStationId), 0, 0.0,LocalDateTime.now(), null);
				if(transactionService.addNewTransaction(transaction)>0) {
					modelAndView.setViewName("swipestatus");
					modelAndView.addObject("message","Successfully swiped in at station: "+stationService.searchMetroStationById(Integer.parseInt(sourceStationId)).getStationName());
					return modelAndView;
				}
			}
		}
		
		List<Station> stationList = stationService.getAllStations();
		modelAndView.addObject("cardId",cardId);
		modelAndView.addObject("message", "");
		modelAndView.addObject("stationList",stationList);
		modelAndView.setViewName("swipein");
		modelAndView.addObject("message", "Unable to swipe in");
		return modelAndView;
	}
	
	@RequestMapping("/swipe")
	public ModelAndView cardSwipePageController(HttpServletRequest request) {
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
		modelAndView.addObject("message", "");
		modelAndView.setViewName("swipe");
		return modelAndView;
	}
}
