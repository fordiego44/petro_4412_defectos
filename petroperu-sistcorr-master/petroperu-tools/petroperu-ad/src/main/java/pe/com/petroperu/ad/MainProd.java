package pe.com.petroperu.ad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.petroperu.ad.util.ConfiguracionPROD;
import pe.com.petroperu.ad.util.Enviroment;

public class MainProd {
	// TICKET 9000003992
	final static Logger LOGGER = LoggerFactory.getLogger("MainProd");

	public static void main(String[] args) {
		// TICKET 9000003992
		LOGGER.info("[INICIO] main");

		LOGGER.info("PROD");
		LOGGER.info((new ConfiguracionPROD()).toString());
		LOGGER.info("---------------------------------------------------------------");
		LOGGER.info("" + FConexionDirectorio.validarAcceso("amurillo", "c0rrespon", Enviroment.PROD));
		LOGGER.info("" + FConexionDirectorio.datosUsuario("elugo", Enviroment.PROD));
		LOGGER.info("" + FConexionDirectorio.datosGrupos("elugo", Enviroment.PROD));
		LOGGER.info("PROD");

		// TICKET 9000003992
		LOGGER.info("[FIN] main");

		// System.out.println("PROD");
		// System.out.println((new ConfiguracionPROD()).toString());
		// System.out.println("---------------------------------------------------------------");
		// System.out.println(FConexionDirectorio.validarAcceso("amurillo", "c0rrespon", Enviroment.PROD));
		// System.out.println(FConexionDirectorio.datosUsuario("elugo", Enviroment.PROD));
		// System.out.println(FConexionDirectorio.datosGrupos("elugo", Enviroment.PROD));
		// System.out.println("PROD");
	}
}
