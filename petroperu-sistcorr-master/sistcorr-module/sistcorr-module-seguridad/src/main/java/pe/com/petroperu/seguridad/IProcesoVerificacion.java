package pe.com.petroperu.seguridad;

import java.util.List;

import pe.com.petroperu.ad.model.Rol;
import pe.com.petroperu.model.UsuarioPetroperu;

public interface IProcesoVerificacion {
	
	/**
	 * Valida si captcha ha expirado
	 * @param idSesion
	 * @return ok: true, error: false
	 */
	boolean validarCaptcha(String idSesion);
	
	/**
	 * Valida si captcha coincide
	 * @param captcha
	 * @param idSesion
	 * @return ok: true, error: false
	 */
	boolean validarCaptcha(String captcha, String idSesion);
	
	/**
	 * Valida usuario en Petroperu AD
	 * @param nombreUsuario
	 * @param contrasenia
	 * @param esADProduccion
	 * @return ok: true, error: false
	 */
	boolean validarUsuarioAD(String nombreUsuario, String contrasenia, boolean esADProduccion);
	
	/**
	 * Obtene información del usuario
	 * @param nombreUsuario
	 * @param contrasenia
	 * @param esSeguridad
	 * @return
	 */
	UsuarioPetroperu obtenerInformacion(String nombreUsuario, String contrasenia, boolean esADProduccion);
	
	
	/**
	 * Obtener Roles del usuario
	 * @param nombreUsuario
	 * @param contrasenia
	 * @param esADProduccion
	 * @return
	 */
	List<Rol> obtenerRoles(String nombreUsuario, String contrasenia, boolean esADProduccion);
	
	
	/**
	 * Generar token
	 * @param nombreUsuario
	 * @param contrasenia
	 * @return
	 */
	String generarTokenSistcorr(String nombreUsuario, String contrasenia);
	
	/**
	 * Actualiza usuario en la base de datos de sistcorr movile
	 * @param usuario
	 * @return
	 */
	UsuarioPetroperu actualizarBaseDatos(UsuarioPetroperu usuario, List<pe.com.petroperu.model.Rol> roles);
	
	
	/**
	 * Validar si es un usuario externo
	 * @param nombreUsuario
	 * @param contrasenia
	 * @return
	 */
	UsuarioPetroperu validarUsuarioExterno(String nombreUsuario, String contrasenia);

}
