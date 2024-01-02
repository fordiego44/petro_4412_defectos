package pe.com.petroperu.service.impl;

import java.util.ArrayList;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.SOAPConnector;
import pe.com.petroperu.service.IDestinatarioEntidadExternoService;
import pe.com.petroperu.xml.jaxb.EntidadExternaRZSunatResponse;
import pe.com.petroperu.xml.jaxb.EntidadExternaSunatPorRZRequest;
import pe.com.petroperu.xml.jaxb.EntidadExternaSunatPorRucRequest;
import pe.com.petroperu.xml.jaxb.EntidadExternaSunatResponse;
import pe.com.petroperu.xml.jaxb.ObjectFactory;

@Service
@PropertySource({ "classpath:application.properties" })
public class DestinatarioEntidadExternoServiceImpl implements IDestinatarioEntidadExternoService {

	@Value("${url.servicio.entidades.externas.sunat}")
	private String apiUrlSunatRucRazonSocial;
	
	@Autowired
	@Qualifier("soapConnectorSunat")
	private SOAPConnector soapConnectorSunat;
	
	@Autowired
	private MessageSource messageSource;
	
	private static final ObjectFactory WS_CLIENT_FACTORY = new ObjectFactory();
	
	@Override
	public Respuesta<Object> getEntidadesExternasNacionalesSunat(String rucRazonSocial,
			String tipoFiltro, Locale locale) {
		// TODO Auto-generated method stub
		
		Respuesta<Object> respuesta = new Respuesta<>();
		
		try {
			if(tipoFiltro != null && tipoFiltro.equalsIgnoreCase("RUC")) {
				EntidadExternaSunatPorRucRequest request = WS_CLIENT_FACTORY.createEntidadExternaSunatPorRucRequest();
				request.setNumruc(rucRazonSocial);
				EntidadExternaSunatResponse response =(EntidadExternaSunatResponse) soapConnectorSunat.callWebService(apiUrlSunatRucRazonSocial, request);
				if((response.getDatosPrincipales() != null && 
						response.getDatosPrincipales().size() > 0 &&
						response.getDatosPrincipales().get(0).getDdp_numruc() != null && 
						response.getDatosPrincipales().get(0).getDdp_numruc().equalsIgnoreCase(""))) {
					
					response.setDatosPrincipales(new ArrayList<>());
				}
		        respuesta.datos.add(response);
			}else {
				EntidadExternaSunatPorRZRequest request = WS_CLIENT_FACTORY.createEntidadExternaSunatPorRZRequest();
				request.setNumruc(rucRazonSocial);
				EntidadExternaRZSunatResponse response =(EntidadExternaRZSunatResponse) soapConnectorSunat.callWebService(apiUrlSunatRucRazonSocial, request);
				if((response.getBuscaRazonSocialReturnPrincipal() != null && 
						response.getBuscaRazonSocialReturnPrincipal().getDatosPrincipalesResponseSunatRZ() != null &&
							response.getBuscaRazonSocialReturnPrincipal().getDatosPrincipalesResponseSunatRZ().size() > 0 && 
						response.getBuscaRazonSocialReturnPrincipal().getDatosPrincipalesResponseSunatRZ().get(0).getDdp_nombre().equalsIgnoreCase(""))) {
					
					response.getBuscaRazonSocialReturnPrincipal().setDatosPrincipalesResponseSunatRZ(new ArrayList<>());
				}
				respuesta.datos.add(response);
			}
			
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.buscar.entidades.externas.sunat.exito", null, locale);
			respuesta.estado = true;
			
		}catch (Exception e) {
			// TODO: handle exception
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.buscar.entidades.externas.sunat.error", null, locale);
			respuesta.estado = false;
		}
        
        return respuesta;
	}

}
