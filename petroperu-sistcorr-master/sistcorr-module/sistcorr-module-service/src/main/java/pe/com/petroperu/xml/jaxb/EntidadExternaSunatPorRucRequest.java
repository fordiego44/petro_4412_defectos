package pe.com.petroperu.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getDatosPrincipales",namespace="http://service.consultaruc.registro.servicio2.sunat.gob.pe")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntidadExternaSunatPorRucRequest{

	@XmlElement(name="numruc")
    private String numruc;

	public String getNumruc() {
		return numruc;
	}

	public void setNumruc(String numruc) {
		this.numruc = numruc;
	}
}
