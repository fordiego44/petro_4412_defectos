package pe.com.petroperu.seguridad;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.ad.FConexionDirectorio;
import pe.com.petroperu.ad.model.Rol;
import pe.com.petroperu.ad.model.Usuario;
import pe.com.petroperu.ad.util.Enviroment;
import pe.com.petroperu.ad.util.RolAD;
import pe.com.petroperu.captcha.CaptchaFactory;
import pe.com.petroperu.captcha.ResponseCaptcha;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.UsuarioRol;
import pe.com.petroperu.sistcorr.dao.IRolDAO;
import pe.com.petroperu.sistcorr.dao.IUsuarioDAO;
import pe.com.petroperu.sistcorr.dao.IUsuarioRolDAO;

@Component
@PropertySource({ "classpath:application.properties" })
public class Autenticacion implements AuthenticationProvider, IProcesoVerificacion {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public ConcurrentHashMap<String, CaptchaFactory.Captcha> captchaMap = new ConcurrentHashMap<>();

	@Autowired
	private ISistcorrCliente sistcorrCliente;

	@Autowired
	private MessageSource messageSource;

	@Value("${pe.com.petroperu.sistcorr.security}")
	private boolean validarUsuarioAD;

	@Value("${pe.com.petroperu.sistcorr.produccion}")
	private boolean esSeguridadProduccion;

	@Autowired
	private IUsuarioDAO usuarioDAO;

	@Autowired
	private IRolDAO rolDAO;

	@Autowired
	private IUsuarioRolDAO usuarioRolDAO;

	public boolean validarCaptcha(String idSesion) {
		CaptchaFactory.Captcha _captcha = this.captchaMap.get(idSesion);
		if (_captcha.expired())
			return false;
		return true;
	}

	public boolean validarCaptcha(String captcha, String idSesion) {
		captcha = (captcha == null) ? "" : captcha.toUpperCase();
		CaptchaFactory.Captcha _captcha = this.captchaMap.get(idSesion);
		if (!captcha.equals(_captcha.getValue()))
			return false;
		return true;
	}

	public boolean validarUsuarioAD(String nombreUsuario, String contrasenia, boolean esADProduccion) {
		if (esADProduccion)
			return FConexionDirectorio.validarAcceso(nombreUsuario, contrasenia, Enviroment.PROD);   
		return FConexionDirectorio.validarAcceso(nombreUsuario, contrasenia, Enviroment.QA); 
		//return true;
	}

	public UsuarioPetroperu obtenerInformacion(String nombreUsuario, String contrasenia, boolean esADProduccion) {
		Usuario _usuario = null;
		if (esADProduccion) {
			_usuario = FConexionDirectorio.datosUsuario(nombreUsuario, Enviroment.PROD);
		} else {
			_usuario = FConexionDirectorio.datosUsuario(nombreUsuario, Enviroment.QA);
		}
		if (_usuario == null)
			return null;
		long startTime = Instant.now().toEpochMilli();

		UsuarioPetroperu usuario = new UsuarioPetroperu(startTime, _usuario.getUid(), "***********", true,
				(_usuario.getEmail() == null) ? (_usuario.getUid() + "@petroperu.com.pe") : _usuario.getEmail(),
				_usuario.getNombres(), _usuario.getApellidos());
		usuario.setNombreCompleto(_usuario.getNombreCompleto());
		return usuario;
	}

	public List<Rol> obtenerRoles(String nombreUsuario, String contrasenia, boolean esADProduccion) {
		List<Rol> rolesAD = new ArrayList<>();
		Rol userR = new Rol();
		userR.setDescripcion(RolAD.USUARIO.ROL);
		userR.setRol(RolAD.USUARIO.ROL);
		userR.generarRolAD();
		if (esADProduccion) {
			List<Rol> lista = FConexionDirectorio.datosGrupos(nombreUsuario, Enviroment.PROD);
			if (lista != null) {
				rolesAD = new ArrayList<>();
				rolesAD.addAll(lista);
			}
		} else {
			List<Rol> lista = FConexionDirectorio.datosGrupos(nombreUsuario, Enviroment.QA);
			if (lista != null) {
				rolesAD = new ArrayList<>();
				rolesAD.addAll(lista);
			}
		}
		rolesAD.add(userR);
		return rolesAD;
	}

	public String generarTokenSistcorr(String nombreUsuario, String contrasenia) {
		Respuesta<String> respuesta = this.sistcorrCliente.recuperarToken(nombreUsuario, contrasenia);
		if (!respuesta.estado)
			return "";
		return respuesta.datos.get(0);
	}

	public UsuarioPetroperu actualizarBaseDatos(UsuarioPetroperu usuario, List<pe.com.petroperu.model.Rol> roles) {
		UsuarioPetroperu _usuario = this.usuarioDAO.findOneByUsername(usuario.getUsername());
		if (_usuario == null) {
			usuario.setUsuarioCrea(usuario.getUsername());
			usuario.setFechaCrea(new Date());
		} else {
			usuario.setIdUsuario(_usuario.getIdUsuario());
			usuario.setUsuarioCrea(_usuario.getUsuarioCrea());
			usuario.setFechaCrea(_usuario.getFechaCrea());
			usuario.setUsuarioModifica(usuario.getUsername());
			usuario.setFechaModifica(new Date());
			List<UsuarioRol> _roles = this.usuarioRolDAO.obtenerRoles(usuario.getUsername());
			for (UsuarioRol usuarioRol : _roles) {
				this.usuarioRolDAO.delete(usuarioRol);
			}
		}

		usuario = (UsuarioPetroperu) this.usuarioDAO.save(usuario);
		for (pe.com.petroperu.model.Rol rol : roles) {
			UsuarioRol ur = new UsuarioRol();
			ur.setFechaCrea(new Date());
			ur.setUsuarioCrea(usuario.getUsername());
			ur.setRol(rol);
			ur.setUsuario(usuario);
			this.usuarioRolDAO.save(ur);
		}
		return usuario;
	}

	private UsuarioPetroperu usuarioPruebas(String nombreUsuario, String contrasenia) {
		LOGGER.info("[INICIO] usuarioPruebas");
		long startTime = Instant.now().toEpochMilli();
		UsuarioPetroperu usuario = new UsuarioPetroperu(startTime, nombreUsuario, "*********", true,
				nombreUsuario + "@petroperu.com.pe", "Juan Luis", "Perez Mendoza");

		LOGGER.info("[FIN] usuarioPruebas");

		return usuario;
	}
	
	//INICIO TICKET 9000004403
	private ResponseCaptcha validateRecaptchaGoogle(String recaptchav3, ResourceBundle resource) {
		
		
		ResponseCaptcha captcha= null;
		try{
		
		    String keyPrivate = resource.getString("key.private.recaptchav3");
			String recaptcha_secret = keyPrivate;
			URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
			Map<String,Object> params = new LinkedHashMap<>();
			params.put("secret", recaptcha_secret);
			params.put("response", recaptchav3);
			        
			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String,Object> param : params.entrySet()) {
			    if (postData.length() != 0) postData.append('&');
			    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			    postData.append('=');
			    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);

			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			StringBuilder sb = new StringBuilder();
			for (int c; (c = in.read()) >= 0;){
			    sb.append((char)c);
			}
			       
			String responseGoogle = sb.toString();
			if(responseGoogle != null)
				responseGoogle = responseGoogle.replaceAll("error-codes", "errorCodes");
			System.out.println("Respuesta: " + responseGoogle);
			        
			captcha = new Gson().fromJson(responseGoogle, ResponseCaptcha.class);

		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return captcha;
	}
	//FIN TICKET 9000004403
	
	//INICIO TICKET 9000004873
	public ResponseCaptcha verificarreCATPCHAV2I(String recaptchav3, ResourceBundle resource) {
		
		ResponseCaptcha captcha= null;
		try{		
			String keyPrivate = resource.getString("key.private.recaptchav3");
			String recaptcha_secret = keyPrivate;
			URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
			Map<String,Object> params = new LinkedHashMap<>();
			params.put("secret", recaptcha_secret);
			params.put("response", recaptchav3);
			        
			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String,Object> param : params.entrySet()) {
			    if (postData.length() != 0) postData.append('&');
			    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			    postData.append('=');
			    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);

			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			StringBuilder sb = new StringBuilder();
			for (int c; (c = in.read()) >= 0;){
			    sb.append((char)c);
			}
			       
			String responseGoogle = sb.toString();
			if(responseGoogle != null)
				responseGoogle = responseGoogle.replaceAll("error-codes", "errorCodes");
			this.LOGGER.info("Respuesta: " + responseGoogle);
			        
			captcha = new Gson().fromJson(responseGoogle, ResponseCaptcha.class);
	        
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return captcha;
	}
	//FIN TICKET 9000004873

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LOGGER.info("AUTHENTICATE");
		String nombreUsuario = authentication.getName();
		String constrasenia = authentication.getCredentials().toString();
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes != null)
				? ((ServletRequestAttributes) requestAttributes).getRequest()
				: null;
		//String recaptchav3 = request!=null?(String) request.getParameter("recaptcha_response") : null;//ticket 9000004403
		String recaptchav3 = request!=null?(String) request.getParameter("g-recaptcha-response") : null;//ticket 9000004873
		//String valorCaptcha = (request != null) ? request.getParameter("parametro_3") : null;//COMENTADO ticket 9000004403
		//String randomId = (request != null) ? request.getParameter("parametro_4") : null;//COMENTADO ticket 9000004403
		this.LOGGER.info("[INICIO] autenticando: " + nombreUsuario + " peru" + constrasenia + "petro");
		
		//INICIO TICKET 9000004403
		ResourceBundle resource = ResourceBundle.getBundle("application");
	    //String score = resource.getString("score.recaptchav3");//ticket 9000004873
	    //ResponseCaptcha captVal = this.validateRecaptchaGoogle(recaptchav3, resource);
	    ResponseCaptcha captVal = this.verificarreCATPCHAV2I(recaptchav3, resource);//ticket 9000004873
	    
	    if(captVal == null || !captVal.isSuccess()) {
	    	String msjError = messageSource.getMessage("sistcorr.login.captcha_error", null, new Locale("es", "PE"));
	    	
	    	throw new BadCredentialsException(msjError);
	    }
	    //ticket 9000004873 comment
	    /*if(captVal.getScore() == null || !(captVal.getScore()>Float.parseFloat(score))) {
	    	String msjError = messageSource.getMessage("sistcorr.login.captcha_error", null, new Locale("es", "PE"));
	    	for(String error : captVal.getErrorCodes()) {
	    		if(error != null && error.trim().equalsIgnoreCase("timeout-or-duplicate"))
	    			msjError = "Tiempo para iniciar sesión caducó. Inténtelo de nuevo.";//Captcha expiró
	    	}
	    	throw new BadCredentialsException(msjError);
	    }*/
	    if(nombreUsuario == null || nombreUsuario.trim().equalsIgnoreCase("") || constrasenia == null || constrasenia.trim().equalsIgnoreCase("")) {
	    	LOGGER.error("Login vacio");			
			throw new BadCredentialsException(messageSource.getMessage("sistcorr.login.no_ecnontrado", null, new Locale("es", "PE")));
	    }
	    //FIN TICKET 9000004403
	    
	    //COMENTADO POR EL TICKET 9000004403
		/*
		 * if (!validarCaptcha(randomId)) throw new
		 * BadCredentialsException("Captcha expiró"); if (!validarCaptcha(valorCaptcha,
		 * randomId)) throw new BadCredentialsException(
		 * this.messageSource.getMessage("sistcorr.login.captcha_error", null, new
		 * Locale("es", "PE")));
		 */
		UsuarioPetroperu uExterno = validarUsuarioExterno(nombreUsuario, constrasenia);
		if (uExterno != null) {
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(RolAD.EXTERNO.ROL_SEGURIDAD));
			return (Authentication) new UsernamePasswordAuthenticationToken(uExterno, constrasenia, authorities);
		}
		if (this.validarUsuarioAD) {
			/*if (!validarUsuarioAD(nombreUsuario, constrasenia, this.esSeguridadProduccion))
				throw new BadCredentialsException(
						this.messageSource.getMessage("sistcorr.login.error", null, new Locale("es", "PE")));*/
			UsuarioPetroperu usuario = null;
			usuario = obtenerInformacion(nombreUsuario, constrasenia, this.esSeguridadProduccion);
			if (usuario == null)
				throw new BadCredentialsException(
						this.messageSource.getMessage("sistcorr.login.error", null, new Locale("es", "PE")));
			List<Rol> rolesAD = obtenerRoles(nombreUsuario, constrasenia, this.esSeguridadProduccion);
			if (rolesAD.isEmpty())
				throw new BadCredentialsException(
						this.messageSource.getMessage("sistcorr.login.sin_roles", null, new Locale("es", "PE")));
			String token = generarTokenSistcorr(nombreUsuario, constrasenia);
			if (token.isEmpty())
				throw new BadCredentialsException(this.messageSource.getMessage("sistcorr.api.generar_token.error",
						null, new Locale("es", "PE")));
			List<pe.com.petroperu.model.Rol> roles = new ArrayList<>();
			List<GrantedAuthority> authorities = new ArrayList<>();
			rolesAD.forEach(rolAD -> {
				rolAD.getRolAD();
				pe.com.petroperu.model.Rol _rol = this.rolDAO.findOneByRolSpring(rolAD.getRolAD().ROL_SEGURIDAD);
				roles.add(_rol);
				authorities.add(new SimpleGrantedAuthority((rolAD.getRolAD()).ROL_SEGURIDAD));
			});
			
			usuario.setToken(token);
			usuario.setBandeja(true);
			usuario.setRoles(roles);
			usuario = actualizarBaseDatos(usuario, roles);
			if (usuario == null)
				throw new BadCredentialsException(
						this.messageSource.getMessage("sistcorr.login.error", null, new Locale("es", "PE")));
			return (Authentication) new UsernamePasswordAuthenticationToken(usuario, constrasenia, authorities);
		}
		Rol userR = new Rol();
		userR.setDescripcion(RolAD.USUARIO.ROL);
		userR.setRol(RolAD.USUARIO.ROL);
		userR.generarRolAD();
		Rol ejecutorR = new Rol();
		ejecutorR.setDescripcion(RolAD.EJECUTOR.ROL);
		ejecutorR.setRol(RolAD.EJECUTOR.ROL);
		ejecutorR.generarRolAD();
		Rol jefeR = new Rol();
		jefeR.setDescripcion(RolAD.JEFE.ROL);
		jefeR.setRol(RolAD.JEFE.ROL);
		jefeR.generarRolAD();
		Rol gestorR = new Rol();
		gestorR.setDescripcion(RolAD.GESTOR.ROL);
		gestorR.setRol(RolAD.GESTOR.ROL);
		gestorR.generarRolAD(); 
		// TICKET 9000003944
		Rol administradorR = new Rol();
		administradorR.setDescripcion(RolAD.ADMINISTRADOR.ROL);
		administradorR.setRol(RolAD.ADMINISTRADOR.ROL);
		administradorR.generarRolAD();
		// FIN TICKET
		/*9000004276 - INICIO*/
		Rol receptorR = new Rol();
		receptorR.setDescripcion(RolAD.RECEPTOR.ROL);
		receptorR.setRol(RolAD.RECEPTOR.ROL);
		receptorR.generarRolAD();
		Rol P8CEAdminR = new Rol();
		P8CEAdminR.setDescripcion(RolAD.P8CEADMIN.ROL);
		P8CEAdminR.setRol(RolAD.P8CEADMIN.ROL);
		P8CEAdminR.generarRolAD();
		Rol P8PEAdminR = new Rol();
		P8PEAdminR.setDescripcion(RolAD.P8PEADMIN.ROL);
		P8PEAdminR.setRol(RolAD.P8PEADMIN.ROL);
		P8PEAdminR.generarRolAD();
		/*9000004276 - FIN*/
		// TICKET 9000004961
		Rol fiscalizadorR = new Rol();
		fiscalizadorR.setDescripcion(RolAD.FISCALIZADOR.ROL);
		fiscalizadorR.setRol(RolAD.FISCALIZADOR.ROL);
		fiscalizadorR.generarRolAD();
		// FIN TICKET

		UsuarioPetroperu usuario = null;
		usuario = usuarioPruebas(nombreUsuario, constrasenia);
		List<Rol> rolesAD = new ArrayList<>();
		rolesAD.add(userR);
		rolesAD.add(ejecutorR);
		rolesAD.add(jefeR);
		rolesAD.add(gestorR);
		// TICKET 9000003944
		rolesAD.add(administradorR);
		// FIN TICKET
		/*9000004276 - INICIO*/
		rolesAD.add(receptorR);
		rolesAD.add(P8CEAdminR);
		rolesAD.add(P8PEAdminR);
		/*9000004276 - FIN*/
		// TICKET 9000004961
		rolesAD.add(fiscalizadorR);
		// FIN TICKET
		String token = generarTokenSistcorr(nombreUsuario, constrasenia);
		System.out.println("authenticate 2 token : "+token);
		if (token.isEmpty())
			throw new BadCredentialsException(
					this.messageSource.getMessage("sistcorr.api.generar_token.error", null, new Locale("es", "PE")));
		List<pe.com.petroperu.model.Rol> roles = new ArrayList<>();
		List<GrantedAuthority> authorities = new ArrayList<>();
		LOGGER.info("Cantidad roles AD:" + rolesAD.size());
		rolesAD.forEach(rolAD -> {
			String g = rolAD.getRolAD().ROL_SEGURIDAD;
			LOGGER.info("Rol: " + g);
			pe.com.petroperu.model.Rol _rol = rolDAO.findOneByRolSpring(g);
			roles.add(_rol);
			authorities.add(new SimpleGrantedAuthority((rolAD.getRolAD()).ROL_SEGURIDAD));
		});
		usuario.setToken(token);
		usuario.setBandeja(true);
		LOGGER.info("SETEANDO ROLES 2");
		LOGGER.info("" + roles.size());
		for(pe.com.petroperu.model.Rol ro : roles){
			LOGGER.info("Rol: " + ro.getNombre());
		}
		usuario.setRoles(roles);
		usuario = actualizarBaseDatos(usuario, roles);
		if (usuario == null)
			throw new BadCredentialsException(
					this.messageSource.getMessage("sistcorr.login.error", null, new Locale("es", "PE")));
		return (Authentication) new UsernamePasswordAuthenticationToken(usuario, constrasenia, authorities);
	}

	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public UsuarioPetroperu validarUsuarioExterno(String nombreUsuario, String contrasenia) {
		this.LOGGER.info("[INICIO] validarUsuarioExterno " + nombreUsuario);
		boolean esUsuarioExterno = false;
		List<Object[]> roles = this.rolDAO.listByUsuario(nombreUsuario);
		for (Object[] rol : roles) {
			if (String.valueOf(rol[3].toString()).equals(RolAD.EXTERNO.ROL_SEGURIDAD)) {
				esUsuarioExterno = true;
				break;
			}
		}
		if (esUsuarioExterno) {
			this.LOGGER.info("[FIN] validarUsuarioExterno ");
			return this.usuarioDAO.findOneByUsername(nombreUsuario);
		}
		this.LOGGER.info("[FIN] validarUsuarioExterno ");
		return null;
	}
}
