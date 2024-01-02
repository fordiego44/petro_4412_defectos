package pe.com.petroperu.ad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.petroperu.ad.util.ConfiguracionQA;
import pe.com.petroperu.ad.util.Enviroment;

public class MainDEV {
	// TICKET 9000003992
	final static Logger LOGGER = LoggerFactory.getLogger("MainDEV");

	public static void main(String[] args) {
		// TICKET 9000003992
		LOGGER.info("[INICIO] main");

		LOGGER.info("DEV");
		LOGGER.info((new ConfiguracionQA()).toString());
		LOGGER.info("-----------------------------------------");
		LOGGER.info("" + FConexionDirectorio.datosUsuario("amurillo", Enviroment.QA));
		LOGGER.info("-----------------------------------------");
		LOGGER.info("" + FConexionDirectorio.validarAcceso("amurillo", "c0rrespon", Enviroment.QA));
		LOGGER.info("-----------------------------------------");
		LOGGER.info("" + FConexionDirectorio.datosGrupos("amurillo", Enviroment.QA));
		LOGGER.info("FIN");

		LOGGER.info("[FIN] main");

		// System.out.println("DEV");
		// System.out.println((new ConfiguracionQA()).toString());
		// System.out.println("-----------------------------------------");
		// System.out.println(FConexionDirectorio.datosUsuario("amurillo", Enviroment.QA));
		// System.out.println("-----------------------------------------");
		// System.out.println(FConexionDirectorio.validarAcceso("amurillo", "c0rrespon", Enviroment.QA));
		// System.out.println("-----------------------------------------");
		// System.out.println(FConexionDirectorio.datosGrupos("amurillo", Enviroment.QA));
		// System.out.println("FIN");
  	}
}
