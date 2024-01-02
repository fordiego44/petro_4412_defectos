package pe.com.petroperu.ad.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfiguracionPROD extends AConfiguracion {
	private static final String SUFIX = "prod";
	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public ConfiguracionPROD() {
		Properties prop = new Properties();
		try {
			InputStream is = ConfiguracionPROD.class.getClassLoader().getResourceAsStream("ad.properties");
			if (is != null) {
				prop.load(is);
			} else {
				throw new FileNotFoundException("property file 'ad.properties' not found in the classpath");
			}
			this.IP_SERVIDOR = prop.getProperty("pe.com.petropeperu.prod.ip");
			this.URL_SERVIDOR = prop.getProperty("pe.com.petropeperu.prod.url");
			this.PUERTO_SERVIDOR = prop.getProperty("pe.com.petropeperu.prod.port");
			this.USUARIO = prop.getProperty("pe.com.petropeperu.admin");
			this.CLAVE = prop.getProperty("pe.com.petropeperu.password");
			this.AUTENTICACION = prop.getProperty("pe.com.petropeperu.autenticacion");
			this.INITIAL_CONTEXT_FACTORY = prop.getProperty("pe.com.petropeperu.contexto");
			String _roles = prop.getProperty("pe.com.petropeperu.roles");
			this.ROLES = _roles.split(",");
		} catch (Exception e) {
			// TICKET 9000003992
			this.LOGGER.error("[ERROR] ConfiguracionPROD " + " This is error : " + e);
			// System.out.println(e.getMessage());
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] ConfiguracionPROD");
	}
}
