package pe.com.petroperu.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IReport<T> {

	public static final  Logger LOGGER = LoggerFactory.getLogger("IReport");
	
	void prepareRequest();
	void process();
	T getResult();
}
