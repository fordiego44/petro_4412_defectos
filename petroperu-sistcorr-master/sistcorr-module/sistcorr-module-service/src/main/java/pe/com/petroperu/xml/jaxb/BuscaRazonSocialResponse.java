package pe.com.petroperu.xml.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BuscaRazonSocialResponse {

	@XmlElement(name = "buscaRazonSocialReturn")
	protected List<DatosPrincipalesResponseSunatRZ> datosPrincipalesResponseSunatRZ;

	public List<DatosPrincipalesResponseSunatRZ> getDatosPrincipalesResponseSunatRZ() {
		return datosPrincipalesResponseSunatRZ;
	}

	public void setDatosPrincipalesResponseSunatRZ(List<DatosPrincipalesResponseSunatRZ> datosPrincipalesResponseSunatRZ) {
		this.datosPrincipalesResponseSunatRZ = datosPrincipalesResponseSunatRZ;
	}
	
}
