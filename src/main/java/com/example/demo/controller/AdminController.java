package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	
	
	@GetMapping("/")
	String login() {		
		System.out.println("hello");
		return "Hello world";
	}

}
