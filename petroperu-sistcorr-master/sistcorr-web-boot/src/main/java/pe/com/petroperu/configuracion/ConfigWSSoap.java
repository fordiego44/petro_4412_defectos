package pe.com.petroperu.configuracion;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import pe.com.petroperu.cliente.SOAPConnector;

@Configuration
@PropertySource({ "classpath:application.properties" })
public class ConfigWSSoap {

	@Value("${url.servicio.entidades.externas.sunat}")
	private String apiUrlSunatRucRazonSocial;

	@Value("${sistcorr.petroperu.server.proxy}")
	private String serverProxy;

	@Value("${sistcorr.petroperu.server.proxy.port}")
	private Integer portProxy;

	@Value("${sistcorr.petroperu.server.proxy.username}")
	private String userProxy;

	@Value("${sistcorr.petroperu.server.proxy.password}")
	private String passProxy;
	
	@Value("${sistcorr.petroperu.server.proxy.flag.connect}")
	private Integer esConnectProxy;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this is the package name specified in the <generatePackage> specified in
		// pom.xml
		// marshaller.setContextPath("com.example.howtodoinjava.schemas.school");
		marshaller.setContextPath("pe.com.petroperu.xml.jaxb");
		return marshaller;
	}

	@Bean
	public RequestTransformingInterceptor transformingInterceptor() {
		RequestTransformingInterceptor transformingInterceptor = new RequestTransformingInterceptor();
		Resource xslt = new ClassPathResource("/styleTransfXmlSOAP/multiRef.xslt", getClass());
		// transformingInterceptor.setRequestXslt(xslt);
		transformingInterceptor.setResponseXslt(xslt);
		try {
			transformingInterceptor.afterPropertiesSet();
		} catch (Exception e) {
			// log.error(e, e);
		}
		return transformingInterceptor;
	}

	@Bean(name="soapConnectorSunat")
	public SOAPConnector soapConnectorSunat(Jaxb2Marshaller marshaller) throws Exception {

		CloseableHttpClient cHttpClient = (esConnectProxy == 1)?this.getConfigHttpClientConnectProxy():this.getConfigHttpClient();
		
		HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(cHttpClient);
		ClientInterceptor[] interceptors = { transformingInterceptor() };
		SOAPConnector client = new SOAPConnector();

		client.setDefaultUri(apiUrlSunatRucRazonSocial);
		client.setMarshaller(marshaller);
		client.setInterceptors(interceptors);
		client.setMessageSender(messageSender);
		client.setUnmarshaller(marshaller);

		return client;
	}

	private CloseableHttpClient getConfigHttpClientConnectProxy() {

		// Creating the CredentialsProvider object
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		// Setting the credentials
		credsProvider.setCredentials(new AuthScope(serverProxy, portProxy),
				new UsernamePasswordCredentials(userProxy, passProxy));
		// Creating the HttpClientBuilder
		HttpClientBuilder clientbuilder = HttpClients.custom();

		// Setting the credentials
		clientbuilder = clientbuilder.setDefaultCredentialsProvider(credsProvider);
		HttpHost proxyHost = new HttpHost(serverProxy, portProxy);

		// Setting the proxy
		RequestConfig.Builder reqconfigconbuilder = RequestConfig.custom();
		reqconfigconbuilder = reqconfigconbuilder.setProxy(proxyHost);

		RequestConfig config = reqconfigconbuilder.build();

		// Building the CloseableHttpClient object
		CloseableHttpClient httpclient = clientbuilder
				.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor())
				.setDefaultRequestConfig(config).build();
		
		return httpclient;
	}
	
	private CloseableHttpClient getConfigHttpClient() {

		// Creating the HttpClientBuilder
		HttpClientBuilder clientbuilder = HttpClients.custom();
		// Setting the proxy
		RequestConfig.Builder reqconfigconbuilder = RequestConfig.custom();
		
		RequestConfig config = reqconfigconbuilder.build();

		// Building the CloseableHttpClient object
		CloseableHttpClient httpclient = clientbuilder
				.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor())
				.setDefaultRequestConfig(config).build();
		
		return httpclient;
	}
}
