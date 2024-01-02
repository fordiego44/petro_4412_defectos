package pe.com.petroperu;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
//import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
//import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;


@SpringBootApplication
@ComponentScan(basePackages = { "pe.com.petroperu"})
@PropertySource({ "classpath:application.properties" })
@EnableScheduling//TICKET 9000004411
public class SistcorrApplication  extends SpringBootServletInitializer  {
	
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(SistcorrApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}
	
	private static Class<SistcorrApplication> applicationClass = SistcorrApplication.class;
	
	
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es", "PE"));
		return localeResolver;
	}
	
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding(StandardCharsets.ISO_8859_1.name());
		return messageSource;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		return localeChangeInterceptor;
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipart = new CommonsMultipartResolver();
	    multipart.setMaxUploadSize(524288000); //500MB
	    return multipart;
	}

	@Bean
	@Order(0)
	public MultipartFilter multipartFilter() {
	    MultipartFilter multipartFilter = new MultipartFilter();
	    multipartFilter.setMultipartResolverBeanName("multipartResolver");
	    return multipartFilter;
	}
	
	 @Bean
	 WebMvcConfigurer configurer () {
	        return new WebMvcConfigurerAdapter() {
	            @Override
	            public void addResourceHandlers (ResourceHandlerRegistry registry) {	            	
	                registry.addResourceHandler("/comprimidos/**").
	                          addResourceLocations("file:" + env.getProperty("sistcorr.directorio") + "/comprimidos/" );
	                
	                registry.addResourceHandler("/librerias/**")
	                .addResourceLocations("classpath:/static/librerias/") 
	                .setCacheControl(CacheControl.noCache());
	                
	            }
	        };
	    }
	
	 /*@Bean
	    CommandLineRunner lookup(SOAPConnector soapConnector) {
	        return args -> {
	        	String name = "Sajal";//Default Name
	            if(args.length>0){
	                name = args[0];
	            }
	            
	            EntidadExternaSunatPorRucRequest request = WS_CLIENT_FACTORY.createEntidadExternaSunatPorRucRequest();
	            
	            request.setNumruc("10472800502");
	            
	            EntidadExternaSunatResponse response =(EntidadExternaSunatResponse) soapConnector.callWebService("http://wscr.sunat.gob.pe:90/cl-ti-iaconsulruc-ws/services/ConsultaRuc", request);
	            System.out.println("Got Response As below ========= : ");
	            System.out.println("valor: " + response.getDatosPrincipales().get(0).getDdp_nombre());
	        };
	    }*/
}