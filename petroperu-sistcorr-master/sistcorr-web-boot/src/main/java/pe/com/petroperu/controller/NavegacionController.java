package pe.com.petroperu.controller;

import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pe.com.petroperu.model.UsuarioPetroperu;

@Controller
public class NavegacionController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = { "", "/", "/inicio**" }, method = RequestMethod.GET)
	public ModelAndView defaultPage(Locale locale) {
		ModelAndView model = new ModelAndView();
		model.addObject("titulo", messageSource.getMessage("sistcorr.titulo", null, locale));
		model.setViewName("sistcorr");
		model.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
		model.addObject("titulo_form", messageSource.getMessage("sistcorr.login.formulario", null, locale));
		return model;
	}

	@RequestMapping(value = { "/.bash_history", "/.nsconfig", "/login.*", "/.login" }, method = RequestMethod.GET)
	public ModelAndView bash_history(Locale locale) {
		ModelAndView model = new ModelAndView();
		model = new ModelAndView("redirect:/404.html");
		return model;
	}

	//@GetMapping(value = "/login")//ticket 9000004403 comentado
	@RequestMapping(value = { "/login" }, method = {RequestMethod.GET, RequestMethod.POST})//ticket 9000004403 add requestMapping get and post
	public ModelAndView loging(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest peticionHttp,
			Locale locale) {
		ModelAndView model = new ModelAndView();
		model.setViewName("login");
		model.addObject("version", messageSource.getMessage("sistcorr.login.version", null, locale));
		model.addObject("titulo_form", messageSource.getMessage("sistcorr.login.formulario", null, locale));
		/*TICKET 9000004394 INI*/
		HttpSession session = peticionHttp.getSession(false);
		if (session != null && error != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
            	LOGGER.info("[ERROR] " + error);
                model.addObject("error", (ex.getMessage() != null)?(ex.getMessage()):(messageSource.getMessage("sistcorr.login.no_ecnontrado", null, locale)));
            }
        }
		/*TICKET 9000004394 FIN*/
		
		/*if (error != null) {
			model.addObject("error", messageSource.getMessage("sistcorr.login.error", null, locale));
		}*/

		if (logout != null) {
			model.addObject("msg", messageSource.getMessage("sistcorr.login.cerrado", null, locale));
		}
		return model;
	}

	@GetMapping(value = "/403")
	public ModelAndView accesoDenegado() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UsuarioPetroperu usuario = (UsuarioPetroperu) auth.getPrincipal();
			model.addObject("usuario", usuario.getNombreCompleto());
		}
		model.setViewName("403");
		return model;
	}

	@GetMapping(value = "/404")
	public ModelAndView noEncontrado() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UsuarioPetroperu usuario = (UsuarioPetroperu) auth.getPrincipal();
			model.addObject("usuario", usuario.getNombreCompleto());
		}
		model.setViewName("404");
		return model;
	}

}
