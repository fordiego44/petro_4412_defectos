package pe.com.petroperu.configuracion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

//import escf.util.MessageDisplyFilter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RequestTransformingInterceptor extends TransformerObjectSupport
implements ClientInterceptor, InitializingBean {

	private static final Log logger = LogFactory.getLog(RequestTransformingInterceptor.class);

	private Resource requestXslt;

	private Resource responseXslt;

	private Templates requestTemplates;

	private Templates responseTemplates;
	private String att;

	/** Sets the XSLT stylesheet to use for transforming incoming request. */
	public void setRequestXslt(Resource requestXslt) {
	    this.requestXslt = requestXslt;
	}

	/** Sets the XSLT stylesheet to use for transforming outgoing responses. */
	public void setResponseXslt(Resource responseXslt) {
	    this.responseXslt = responseXslt;
	}

	/**
	 * Transforms the request message in the given message context using a provided
	 * stylesheet. Transformation only occurs if the {@code requestXslt} has been
	 * set.
	 *
	 * @param messageContext
	 *            the message context
	 * @return always returns {@code true}
	 * @see #setRequestXslt(org.springframework.core.io.Resource)
	 */
	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		
		logger.debug("Request");
	    if (requestTemplates != null) {
	        WebServiceMessage request = messageContext.getRequest();
	        SaajSoapMessage saajSoapMessage = (SaajSoapMessage) messageContext.getRequest();
	        NodeList nodeList = saajSoapMessage.getSaajMessage().getSOAPPart().getChildNodes();
	        Node node1 = nodeList.item(0);
	        NamedNodeMap nodeAttributes = node1.getAttributes();
	        Node nodeAttribu = nodeAttributes.item(0);
	        Attr el = (Attr) nodeAttribu;
	        int length = nodeAttributes.getLength();
	        att = null;
	        for (int i = 0; i < length; i++) {
	            el = (Attr) nodeAttributes.item(i);
	            if (att == null) {
	                att = el + "";
	            } else {
	                att = att + " " + el;
	            }

	        }

	        Transformer transformer = null;
			try {
				transformer = requestTemplates.newTransformer();
			} catch (TransformerConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				transformMessage(request, transformer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        logger.debug("Request message transformed");
	    }
	    return true;
	}


	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		logger.info("Response");
	    if (responseTemplates != null) {
	        WebServiceMessage response = messageContext.getResponse();
	        
	        SaajSoapMessage saajSoapMessage = (SaajSoapMessage) messageContext.getResponse();
	        NodeList nodeList = saajSoapMessage.getSaajMessage().getSOAPPart().getChildNodes();
	        Node node1 = nodeList.item(0);
	        NamedNodeMap nodeAttributes = node1.getAttributes();
	        Node nodeAttribu = nodeAttributes.item(0);
	        Attr el = (Attr) nodeAttribu;
	        int length = nodeAttributes.getLength();
	        att = null;
	        for (int i = 0; i < length; i++) {
	            el = (Attr) nodeAttributes.item(i);
	            if (att == null) {
	                att = el + "";
	            } else {
	                att = att + " " + el;
	            }

	        }
	        
	        Transformer transformer = null;
			try {
				transformer = responseTemplates.newTransformer();
			} catch (TransformerConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				transformMessage(response, transformer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        logger.debug("Response message transformed");
	    }
	    return true;
	}

	private void transformMessage(WebServiceMessage message, Transformer transformer) throws Exception {
		
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    message.writeTo(buffer);
	    String SOAPMessageRequest = buffer.toString(java.nio.charset.StandardCharsets.UTF_8.name());
	    String bodyMessage = SOAPMessageRequest.split("Body>")[1].replace("</soapenv:", "").trim();
	    
	    bodyMessage = bodyMessage.replaceFirst("soapenv:encodingStyle", att + " soapenv:encodingStyle").replace("\r",
	            "");
	    String[] bodyArr = bodyMessage.split("\n");
	    SaajSoapMessage saajSoapMessage = (SaajSoapMessage) message;
	    
	    
	    SOAPBody body = saajSoapMessage.getSaajMessage().getSOAPBody();
	    String firstTagName = getFirstBodyElement(body);
	    
	    String lastTag = "</" + firstTagName + ">";
	    for (int i = 0; i < bodyArr.length; i++) {
	        String tag = bodyArr[i];
	        if (tag != null && tag.startsWith("</") && tag.contains(firstTagName)) {
	            lastTag = tag;
	            
	        }
	    }

	    bodyMessage = bodyMessage.replace(lastTag, "") + "\n" + lastTag;
	    /*ByteArrayOutputStream out = new ByteArrayOutputStream();
	    saajSoapMessage.writeTo(out);
	    String strMsg = new String(out.toByteArray());*/
	    
	    ByteArrayInputStream is = new ByteArrayInputStream(bodyMessage.getBytes());
	    
	    
	    transform(new StreamSource(is), message.getPayloadResult());

	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    transformer.transform(message.getPayloadSource(), new StreamResult(os));
	    is = new ByteArrayInputStream(os.toByteArray());
	    transform(new StreamSource(is), message.getPayloadResult());

	}


	/** Does nothing by default. Faults are not transformed. */
	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
	    StringWriter stringWriter = new StringWriter();
	    logger.info("handleFault");
	    if (messageContext.getResponse() instanceof SoapMessage) {
	        SaajSoapMessage soapMessage = (SaajSoapMessage)messageContext.getResponse();
	        SoapBody body = soapMessage.getSoapBody();
	        SoapFault fault = body.getFault();
	        String faultstring = fault.getFaultStringOrReason();
	        String faultCode =  fault.getFaultCode().getPrefix()+":"+fault.getFaultCode().getLocalPart();
	        //Messagef  filter = new MessageDisplyFilter();
	        stringWriter.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header /><SOAP-ENV:Body><SOAP-ENV:Fault><faultcode>").append(faultCode).append("</faultcode><faultstring xml:lang=\"en\">");
	        //stringWriter.append(filter.filter(faultstring));
	        stringWriter.append(faultstring);
	        stringWriter.append("</faultstring></SOAP-ENV:Fault></SOAP-ENV:Body></SOAP-ENV:Envelope>");
	        
	        Source source = new StreamSource(new StringReader(stringWriter.toString()));
	        try {
				transform(source, body.getPayloadResult());
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				stringWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	    return true;
	}

	/** Does nothing by default. */
	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
		logger.info("afterCompletion");
		 WebServiceMessage response = messageContext.getResponse();
		 ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		 try {
			response.writeTo(buffer);
			String SOAPMessageRequest = buffer.toString(java.nio.charset.StandardCharsets.UTF_8.name());
			logger.info("Resultado total: " + SOAPMessageRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	    if (requestXslt == null && responseXslt == null) {
	        throw new IllegalArgumentException("Setting either 'requestXslt' or 'responseXslt' is required");
	    }
	    
	    TransformerFactory transformerFactory = getTransformerFactory();//getTransformerFactory();//TransformerFactory.newInstance()
	    XMLReader xmlReader = XMLReaderFactory.createXMLReader();
	    xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
	    if (requestXslt != null) {
	        Assert.isTrue(requestXslt.exists(), "requestXslt \"" + requestXslt + "\" does not exit");
	        if (logger.isInfoEnabled()) {
	            logger.info("Transforming request using " + requestXslt);
	        }
	        Source requestSource = new ResourceSource(xmlReader, requestXslt);
	        requestTemplates = transformerFactory.newTemplates(requestSource);
	    }
	    if (responseXslt != null) {
	        Assert.isTrue(responseXslt.exists(), "responseXslt \"" + responseXslt + "\" does not exit");
	        if (logger.isInfoEnabled()) {
	            logger.info("Transforming response using " + responseXslt);
	        }
	        Source responseSource = new ResourceSource(xmlReader, responseXslt);
	        responseTemplates = transformerFactory.newTemplates(responseSource);
	    }
	}

	public static String getFirstBodyElement(SOAPBody body) {
	    for (Iterator<?> iterator = body.getChildElements(); iterator.hasNext();) {
	        Object child = iterator.next();
	        if (child instanceof SOAPElement) {
	            return ((SOAPElement) child).getTagName();
	        }
	    }
	    return null;
	}
}
