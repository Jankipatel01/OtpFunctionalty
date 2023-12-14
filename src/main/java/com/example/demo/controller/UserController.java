package com.example.demo.controller;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Model.Authority;
import com.example.demo.Model.User;
import com.example.demo.Repository.AuthorityRepo;
import com.example.demo.Repository.UserRepo;
import com.google.gson.Gson;

@Controller
public class UserController {
	@Autowired
	PasswordEncoder bcript;
	
	@Autowired
	public UserRepo urepo;
	@GetMapping("/")
	String home() {		
		System.out.println("hello");
		return "redirect:/home";
	}	
	
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("registrationForm", new User());
		model.addAttribute("loginForm", new User());
		return "home";
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {

		return "dashboard";
	}
	
	@GetMapping("/superadminlanding")
	public String superadminlanding(Model model) {

		return "dashboard";
	}
	@GetMapping("/subadminlanding")
	public String subadminlanding(Model model) {

		return "dashboard";
	}
	

	@PostMapping("/login")
	public String login(Model model, @ModelAttribute("loginForm") User userForm) {
		System.out.println("in login post============");
		
		return	"redirect:/dashboard";
		//return "dashboard";
	}
	
	@Autowired
	AuthorityRepo aurepo;
	
	
	@PostMapping("/registration")
	public String registersave(Model model, @ModelAttribute("registrationForm") User userForm) {	
		
		Authority authority = new Authority();
		authority.setAuthority(userForm.getAuthority());
		authority.setUsername(userForm.getUsername());
		
		
		List<Authority> userAuthorities = new ArrayList<>();
	    userAuthorities.add(authority);

	    // Set the user's authorities
	    userForm.setAuthorities1(userAuthorities);
		
		
		userForm.setPassword(bcript.encode(userForm.getPassword()));
		urepo.save(userForm);				
		return "home";
	}

	@GetMapping("/captcha")
	protected void captcha(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Progma", "no-cache");
		response.setDateHeader("Max-Age", 0);

		String captcha = generateCaptcha(5);

		int width = 160, height = 50;
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.OPAQUE);
		Graphics graphics = bufferedImage.createGraphics();
		graphics.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));
		graphics.setColor(new Color(255, 255, 255));
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(new Color(0, 0, 0));
		graphics.drawString(captcha, 30, 35);

		HttpSession session = request.getSession(true);
		session.setAttribute("captcha", captcha);

		OutputStream outputStream = response.getOutputStream();
		ImageIO.write(bufferedImage, "jpeg", outputStream);
		outputStream.close();
	}
	
	private String generateCaptcha(int captchaLength) {
		String captcha = "123456789ABCDEFGHJKMNPQRSTUVWXYZ";

		StringBuffer captchaBuffer = new StringBuffer();
		Random random = new Random();

		while (captchaBuffer.length() < captchaLength) {
			int index = (int) (random.nextFloat() * captcha.length());
			captchaBuffer.append(captcha.substring(index, index + 1));
		}
		return captchaBuffer.toString();
	}
	
	

	//otp code start 
	 
	  @PostMapping("otp")
	    @ResponseBody
	    public String otp(@RequestBody Object loginForm) {
	        try {
	        	JSONObject obj = new JSONObject(new Gson().toJson(loginForm));
	            String username = obj.getString("username");
	            String password = obj.getString("password");

	            User user = urepo.findFirstByUsername(username);
	            if (user != null && bcript.matches(password,user.getPassword())) {
	            	
	            	String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
	            	user.setOtp(otp);
	            	urepo.save(user);
	            	 
	            	 System.out.println("OTP IS GENERATED SUCCESSFULLY :"+otp);
	               
	                return "Authentication successful. OTP sent.";
	                
	            } else {
	                // Authentication failed
	                return "Authentication failed. Invalid username or password.";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Something went wrong.";
	        }
	    }
	//otp code end
}
