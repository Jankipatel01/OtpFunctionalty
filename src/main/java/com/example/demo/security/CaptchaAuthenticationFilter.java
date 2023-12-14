package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	 private String processUrl;

	    public CaptchaAuthenticationFilter(String defaultFilterProcessesUrl, String failureUrl) {
	        super(defaultFilterProcessesUrl);
	        this.processUrl = defaultFilterProcessesUrl;
	        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
	    }
	
	 @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	        HttpServletRequest req = (HttpServletRequest) request;
	        HttpServletResponse res=(HttpServletResponse)response;
	        System.err.println(req.getServletPath());
        	if(processUrl.equals(req.getServletPath()) && "POST".equalsIgnoreCase(req.getMethod())){
        		 String captcha = req.getParameter("captcha");
     	        String storedCaptcha = (String) req.getSession().getAttribute("captcha");
        		 if (captcha != null && !captcha.equals(storedCaptcha)) {
//     	            throw new ValidationException("CAPTCHA validation failed");
                     unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("Enter Valid Captcha"));
                     if(!res.isCommitted())
                     {
                   	  res.sendRedirect("/home");
                     }
                   return;
     	        }
        	}
	        
	        chain.doFilter(request, response);
	 }

	
	  @Override
	    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
	        return null;
	    }
}
