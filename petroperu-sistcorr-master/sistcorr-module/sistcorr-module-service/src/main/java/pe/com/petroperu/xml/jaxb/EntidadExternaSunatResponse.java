package pe.com.petroperu.xml.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getDatosPrincipalesResponse", namespace = "http://service.consultaruc.registro.servicio2.sunat.gob.pe")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntidadExternaSunatResponse {

	@XmlElement(name = "getDatosPrincipalesReturn")
	protected List<DatosPrincipalesResponseSunat> datosPrincipales;

	public List<DatosPrincipalesResponseSunat> getDatosPrincipales() {
		return datosPrincipales;
	}

	public void setDatosPrincipales(List<DatosPrincipalesResponseSunat> datosPrincipales) {
		this.datosPrincipales = datosPrincipales;
	}

	
}
