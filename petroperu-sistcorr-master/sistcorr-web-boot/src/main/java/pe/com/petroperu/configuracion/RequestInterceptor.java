package pe.com.petroperu.configuracion;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long startTime = Instant.now().toEpochMilli();
		request.setAttribute("startTime", startTime);
		logger.info("Inicio de la petición");
		StringBuilder sb = new StringBuilder();
		sb. append("\t").append(request.getRemoteAddr()).append(" ")
			.append("\t").append(request.getHeader("User-Agent")).append(" ");
		logger.info(sb.toString());
		sb = new StringBuilder();
		sb.append("Petición:\t")
			.append(startTime).append("\t")
			.append(request.getMethod()).append("\t")
			.append(request.getRequestURI().toString());
		logger.info(sb.toString());
		
		if(!"".equals(queryStringParamtemerts(request))) {
			logger.info("Parámetros:\t" + queryStringParamtemerts(request));
		}		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		StringBuilder sb = new StringBuilder();
		logger.info("Fin de la petición");
		super.postHandle(request, response, handler, modelAndView);
	}
	
	private String queryStringParamtemerts(HttpServletRequest request) {
		if(request.getQueryString() != null) {
			return request.getQueryString();
		}
		return "";
	}
}
