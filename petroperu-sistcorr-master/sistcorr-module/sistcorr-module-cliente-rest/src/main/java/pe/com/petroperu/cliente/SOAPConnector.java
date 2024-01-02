package pe.com.petroperu.cliente;

import org.springframework.context.annotation.Bean;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public class SOAPConnector extends WebServiceGatewaySupport{

	@Bean
    public SaajSoapMessageFactory messageFactory() {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setSoapVersion(SoapVersion.SOAP_11);
        messageFactory.afterPropertiesSet();
        return messageFactory;
    }
	
    public Object callWebService(String url, Object request){
    	
    	WebServiceTemplate wst = getWebServiceTemplate();
		wst.setMessageFactory(messageFactory());
		
        return wst.marshalSendAndReceive(url, request);
    }
}
