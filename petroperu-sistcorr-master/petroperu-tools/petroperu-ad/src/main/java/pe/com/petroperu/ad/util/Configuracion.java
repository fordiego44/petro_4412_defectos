package pe.com.petroperu.ad.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuracion {
	// TICKET 9000003992
	static Logger LOGGER = LoggerFactory.getLogger("Configuracion");

	private static final String FILE_NAME = "ad.properties";

	public static String IP_SERVIDOR_PROD;
	public static String URL_SERVIDOR_PROD;
	public static String PUERTO_SERVIDOR_PROD;

	public static String IP_SERVIDOR_QA;
	public static String URL_SERVIDOR_QA;
	public static String PUERTO_SERVIDOR_QA;

	public static String USUARIO;
	public static String CLAVE;
	public static String AUTENTICACION;
	public static String INITIAL_CONTEXT_FACTORY;
	public static String[] ROLES;

	public static final String PAQUETE_BASE = "pe.com.petropeperu";

	static {
		Properties prop = new Properties();
		try {
			InputStream is = Configuracion.class.getClassLoader().getResourceAsStream(FILE_NAME);
			if (is != null) {
				prop.load(is);
			} else {
				throw new FileNotFoundException("property file '" + FILE_NAME + "' not found in the classpath");
			}
			IP_SERVIDOR_PROD = prop.getProperty(PAQUETE_BASE + ".prod.ip");
			URL_SERVIDOR_PROD = prop.getProperty(PAQUETE_BASE + ".prod.url");
			PUERTO_SERVIDOR_PROD = prop.getProperty(PAQUETE_BASE + ".prod.port");

			IP_SERVIDOR_QA = prop.getProperty(PAQUETE_BASE + ".qa.ip");
			URL_SERVIDOR_QA = prop.getProperty(PAQUETE_BASE + ".qa.url");
			PUERTO_SERVIDOR_QA = prop.getProperty(PAQUETE_BASE + ".qa.port");

			USUARIO = prop.getProperty(PAQUETE_BASE + ".admin");
			CLAVE = prop.getProperty(PAQUETE_BASE + ".password");
			AUTENTICACION = prop.getProperty(PAQUETE_BASE + ".autenticacion");
			INITIAL_CONTEXT_FACTORY = prop.getProperty(PAQUETE_BASE + ".contexto");
			String _roles = prop.getProperty(PAQUETE_BASE + ".roles");
			ROLES = _roles.split(",");
		} catch (Exception e) {
			// TICKET 9000003992
			LOGGER.error("[ERROR] Configuracion " + " This is error : " + e);
			// System.out.println(e.getMessage());
		}

	}

}
