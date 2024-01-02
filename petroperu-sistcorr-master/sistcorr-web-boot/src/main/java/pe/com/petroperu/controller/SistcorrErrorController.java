package pe.com.petroperu.controller;

import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SistcorrErrorController implements ErrorController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;
	
	@Override
	public String getErrorPath() {
		return null;
	}
	
	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest request, Locale locale) {
		ModelAndView page;
		LOGGER.info("[ERROR] handleError");
		page = new ModelAndView("error-sistcorr");
		String titulo = "SISTCORR 2.0", statusCode = "ERROR", msjPrincipal;

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			statusCode = status.toString();
			//titulo += " - " + statusCode;
		}
		switch (statusCode) {
		case "401":
			msjPrincipal = messageSource.getMessage("sistcorr.error.401.principal", null, locale);
			break;
		case "403":
			msjPrincipal = messageSource.getMessage("sistcorr.error.403.principal", null, locale);
			break;
		case "404":
			msjPrincipal = messageSource.getMessage("sistcorr.error.404.principal", null, locale);
			break;
		case "405":
			msjPrincipal = messageSource.getMessage("sistcorr.error.405.principal", null, locale);
			break;
		case "500":
			msjPrincipal = messageSource.getMessage("sistcorr.error.500.principal", null, locale);
			break;
		default:
			msjPrincipal = messageSource.getMessage("sistcorr.error.otro.principal", null, locale);
		}

		page.addObject("titulo", titulo);
		page.addObject("statusCode", statusCode);
		page.addObject("msjPrincipal", msjPrincipal);

		return page;
	}
}
