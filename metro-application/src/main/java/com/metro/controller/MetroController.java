package com.metro.controller;

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
import com.metro.service.PassengerService;

@Controller
public class MetroController {
	
	@Autowired
	private MetroUserService metroUserService;
	
	@Autowired
	private PassengerService passengerService;
	
	@Autowired
	private MetroCardService metroCardService;
	
	
	@RequestMapping("/")
	public ModelAndView mainPageController() {
		return new ModelAndView("index");
	}
	
	@RequestMapping("/afterLogin")
	public ModelAndView afterLoginController(HttpServletRequest request, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		if(metroUserService.validateUser(userId,password)) {
			modelAndView.setViewName("user");
			Passenger passenger = metroUserService.getPassenger(userId);
			session.setAttribute("userName",passenger.getPassengerName());
			//session.setAttribute("password", password);
			session.setAttribute("userId",userId);
			return modelAndView;
		}
		else {
			modelAndView.setViewName("loginPage");
			modelAndView.addObject("message", "invalid user id or password !!");
			return modelAndView;
		}
	}
	
	@RequestMapping("/afterSignup")
	public ModelAndView afterSignupController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String name = (String)request.getParameter("passengerName");
		String phone = (String)request.getParameter("passengerPhone");
		String age = (String)request.getParameter("passengerAge");
		String email = (String)request.getParameter("passengerEmail");
		String pwd = (String)request.getParameter("passengerPassword");
		Passenger passenger =  new Passenger(0, name, phone, email, Integer.parseInt(age),pwd);
		if(metroUserService.registerUser(passenger)) {
			MetroCard card = new MetroCard(0, passenger.getPassengerId(), 100.0);
			MetroCard issued = metroCardService.issueNewMetroCard(card);
			if(issued!=null) {
				modelAndView.setViewName("signedUp");
				modelAndView.addObject("message","Metro card issued: ");
				modelAndView.addObject("pname",name+" you are signed up successfully !");
				modelAndView.addObject("cardId","Card id is: "+issued.getCardId());
				modelAndView.addObject("balance","Card balance is: "+issued.getBalance());
				return modelAndView;
			}
			else {
				modelAndView.setViewName("signedUp");
				modelAndView.addObject("pname",name+" you are signed up successfully !");
				modelAndView.addObject("message","Unable to issue metro card!, Please try to issue card after login.");
				modelAndView.addObject("cardId","");
				modelAndView.addObject("balance","");
				return modelAndView;
			}
		}
		else {
			modelAndView.setViewName("output");
			modelAndView.addObject("message","Unable to register! please try again.");
			return modelAndView;
		}
	}
	
	@RequestMapping("/logout")
	public ModelAndView logoutController(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		
		ModelAndView modelAndView = new ModelAndView("output");
		modelAndView.addObject("message","Successfully logged out");
		return modelAndView;
		
	}
	
	
	
	@RequestMapping("/dashboard")
	public ModelAndView dashboardController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		HttpSession session = request.getSession();
		String uname = (String)session.getAttribute("userName");
		if(!(uname==null||uname.isEmpty())) {
			modelAndView.setViewName("user");
			//modelAndView.addObject("userName",uname);
			return modelAndView;
		}
		modelAndView.setViewName("output");
		modelAndView.addObject("message","invalid session");
		return modelAndView;
	}
	


}
