package pe.com.petroperu.ad.service;

import java.util.Hashtable;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.petroperu.ad.util.AConfiguracion;

public class FedoraADService<T extends AConfiguracion> {
	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());
	private T configuracion;

	public FedoraADService(T configuracion) {
		this.configuracion = configuracion;
	}

	public Conexion getConexion() {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] getConexion");
		try {
			Hashtable<String, String> env = new Hashtable<>();
			env.put("java.naming.factory.initial", this.configuracion.getINITIAL_CONTEXT_FACTORY());
			env.put("java.naming.provider.url", this.configuracion.getURL_SERVIDOR());
			env.put("java.naming.security.authentication", this.configuracion.getAUTENTICACION());
			env.put("java.naming.security.principal", this.configuracion.getUSUARIO());
			env.put("java.naming.security.credentials", this.configuracion.getCLAVE());

			DirContext ctx = new InitialDirContext(env);
			Conexion cx = new Conexion(ctx);
			return cx;
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] getConexion " + " This is error : " + e);
			// e.printStackTrace();
			// System.out.println(e.getMessage());
			// TICKET 9000003992
			this.LOGGER.info("[FIN] getConexion");
			return null;
		}
	}

	public Conexion getConexionForGroups() {
		try {
			Hashtable<String, String> env = new Hashtable<>();
			env.put("java.naming.factory.initial", this.configuracion.getINITIAL_CONTEXT_FACTORY());
			env.put("java.naming.provider.url", this.configuracion.getURL_SERVIDOR());
			env.put("java.naming.security.authentication", this.configuracion.getAUTENTICACION());
			env.put("java.naming.security.principal", this.configuracion.getUSUARIO());
			env.put("java.naming.security.credentials", this.configuracion.getCLAVE());
			env.put("java.naming.ldap.attributes.binary", "grupo");

			DirContext ctx = new InitialDirContext(env);
			Conexion cx = new Conexion(ctx);
			return cx;
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] getConexionForGroups " + " This is error : " + e);
//			e.printStackTrace();
//			System.out.println(e.getMessage());
			this.LOGGER.info("[FIN] getConexionForGroups");
			return null;
		}
	}

	public T getConfiguracion() {
		return this.configuracion;
	}
}
