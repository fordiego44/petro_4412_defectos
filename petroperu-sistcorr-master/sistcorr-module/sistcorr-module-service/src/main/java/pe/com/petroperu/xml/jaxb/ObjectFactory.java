package pe.com.petroperu.xml.jaxb;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	public ObjectFactory() {
    }

	public EntidadExternaSunatPorRucRequest createEntidadExternaSunatPorRucRequest() {
        return new EntidadExternaSunatPorRucRequest();
    }
	
	public EntidadExternaSunatPorRZRequest createEntidadExternaSunatPorRZRequest() {
        return new EntidadExternaSunatPorRZRequest();
    }
	
	public EntidadExternaSunatResponse createEntidadExternaSunatResponse() {
        return new EntidadExternaSunatResponse();
    }
	
	public EntidadExternaRZSunatResponse createEntidadExternaRZSunatResponse() {
        return new EntidadExternaRZSunatResponse();
    }
}