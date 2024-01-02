package pe.com.petroperu.util;

import java.io.IOException;
//import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;

//comentado por el ticket 9000004403
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class SessionFilter implements Filter {
	
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	        HttpServletRequest req = (HttpServletRequest) request;
	        HttpServletResponse res = (HttpServletResponse) response;
	        /*Cookie[] allCookies = req.getCookies();
	        if (allCookies != null) {
	        	Cookie session = 
	            Arrays.stream(allCookies).filter(x -> x.getName().equals("JSESSIONID"))
	                    .findFirst().orElse(null);
	 
	            if (session != null) {
	                session.setHttpOnly(true);
	                session.setSecure(false);
	                session.setPath("/");
	                res.addCookie(session);
	                //logger.info("[HTTPONLY] true " + req.getRequestURI());
	            } else {
	            	session = new Cookie("JSESSIONID", "A690742FEFCFC53F24C50F717263C5F7");
	            	session.setHttpOnly(true);
	                session.setSecure(false);
	                session.setPath("/");
	                res.addCookie(session);
	            	//logger.info("[HTTPONLY] no tiene " + req.getRequestURI());
	            }
	        }else {
	        	Cookie session = new Cookie("JSESSIONID", "A690742FEFCFC53F24C50F717263C5F7");
            	session.setHttpOnly(true);
                session.setSecure(false);
                session.setPath("/");
                res.addCookie(session);
            	//logger.info("[HTTPONLY] no tiene " + req.getRequestURI());
	        }*/
	        chain.doFilter(req, res);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}