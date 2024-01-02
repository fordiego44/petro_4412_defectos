package pe.com.petroperu.configuracion;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import pe.com.petroperu.model.UsuarioPetroperu;

@Component
public class CustomRequestInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long startTime = Instant.now().toEpochMilli();
		request.setAttribute("startTime", startTime);
		logger.info("Inicio de la petición");
		StringBuilder sb = new StringBuilder();
		UsuarioPetroperu user = obtenerUsuario();
		sb.append("Cliente:\t")
			.append(user == null ? "": user.getUsername())
			.append("\t").append(request.getRemoteAddr()).append(" ")
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
	
	private String requestBody(HttpServletRequest request) {
		try {
			if(request.getInputStream() != null) {
				return IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
			}else {
				return "Request body could not be read because it's empty.";
			}
		} catch (Exception e) {
			logger.trace("Could not obtain the saml request body from the http request", e);
			return "Could not obtain the saml request body from the http request";
		}
	}
	
	private UsuarioPetroperu obtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		return null;
	}

}
