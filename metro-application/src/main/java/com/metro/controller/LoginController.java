package com.metro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@RequestMapping("/login")
	public ModelAndView loginPageController() {
		return new ModelAndView("loginPage");
	}
	
	@RequestMapping("/signup")
	public ModelAndView signupPageController() {
		return new ModelAndView("signupPage");
	}

}
