package pe.com.petroperu.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "buscaRazonSocialResponse", namespace = "http://service.consultaruc.registro.servicio2.sunat.gob.pe")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntidadExternaRZSunatResponse {

	@XmlElement(name = "buscaRazonSocialReturn")
	protected BuscaRazonSocialResponse buscaRazonSocialReturnPrincipal;

	public BuscaRazonSocialResponse getBuscaRazonSocialReturnPrincipal() {
		return buscaRazonSocialReturnPrincipal;
	}

	public void setBuscaRazonSocialReturnPrincipal(BuscaRazonSocialResponse buscaRazonSocialReturnPrincipal) {
		this.buscaRazonSocialReturnPrincipal = buscaRazonSocialReturnPrincipal;
	}
	
}
