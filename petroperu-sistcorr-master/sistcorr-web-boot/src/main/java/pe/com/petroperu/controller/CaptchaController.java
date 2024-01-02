package pe.com.petroperu.controller;

import java.time.Instant;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import pe.com.petroperu.captcha.CaptchaFactory;
import pe.com.petroperu.controller.CaptchaController;
import pe.com.petroperu.seguridad.Autenticacion;
import pe.com.petroperu.util.CaptchaResponse;
import pe.com.petroperu.util.SistcorrRespuesta;

@RestController
public class CaptchaController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Autenticacion autenticacion;
	
	/*@PostMapping({ "/j_spring_security_logout" })
	public ModelAndView cerrarSession(Locale locale){
		SecurityContextHolder.getContext().setAuthentication(null);
		
		return new ModelAndView("redirect:login?logout");
	}*/

	@RequestMapping(value = { "/.captcha", "/captcha.*" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ResponseEntity<String> errors() {
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}

	@RequestMapping(value = { "/captcha" }, method = { RequestMethod.GET }, produces = { "application/json" })
	public ResponseEntity<SistcorrRespuesta> generarCaptcha(Locale locale) {
		this.LOGGER.info("[INICIO] generarCaptcha");
		SistcorrRespuesta<CaptchaResponse> respuesta = new SistcorrRespuesta<>();
		Long startTime = Long.valueOf(Instant.now().toEpochMilli());
		try {
			String id = startTime.toString();
			String nuevoId = "";
			int tamanio = id.length();
			char[] toArray = id.toCharArray();
			int contador = 0;
			for (char c : toArray) {
				if (contador == tamanio / 2) {
					nuevoId = nuevoId + c + "-";
				} else {
					nuevoId = nuevoId + c + "";
				}
				contador++;
			}
			this.autenticacion.captchaMap.put(nuevoId, (new CaptchaFactory()).createCaptcha());
			CaptchaFactory.Captcha captcha = (CaptchaFactory.Captcha) this.autenticacion.captchaMap.get(nuevoId);
			if (captcha == null) {
				throw new Exception(this.messageSource.getMessage("sistcorr.captcha.generar_error", null, locale));
			}
			CaptchaResponse response = new CaptchaResponse(captcha.getBase64Img(), nuevoId);
			respuesta.estado = true;
			respuesta.datos.add(response);
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.captcha.generar_success", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
}
