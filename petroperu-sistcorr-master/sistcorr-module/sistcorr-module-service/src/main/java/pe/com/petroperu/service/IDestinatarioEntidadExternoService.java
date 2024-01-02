package pe.com.petroperu.service;

import java.util.Locale;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.xml.jaxb.EntidadExternaSunatResponse;

public interface IDestinatarioEntidadExternoService {

	/**
	 * getEntidadesExternasNacionalesSunat
	 * @param rucRazonSocial
	 * @param tipoFiltro
	 * @return
	 */
	Respuesta<Object> getEntidadesExternasNacionalesSunat(String rucRazonSocial, String tipoFiltro, Locale locale);
}
