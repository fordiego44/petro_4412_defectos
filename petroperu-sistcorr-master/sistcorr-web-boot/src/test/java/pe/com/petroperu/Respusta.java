package pe.com.petroperu;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.petroperu.cliente.model.ErrorResponse;

public class Respusta {
	
	@Test
	public void respuesta() throws JsonProcessingException, IOException {
		String respuesta = "{\"timestamp\": 1562100077606,  \"status\": 500,  \"error\": \"Internal Server Error\",  \"exception\": \"com.grupolpa.pe.sistcorr.jaxrs.exception.AsignacionException\",  \"message\": \"DEBE INGRESAR AL MENOS UNA ASIGNACIÓN!\",   \"path\": \"/sistcorr-rest-service/v1/tramite-documentario/correspondencia/CRE-OFP-05122-2017/asignaciones\"}";
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = mapper.readTree(respuesta);
	    System.out.println(actualObj.get("message").textValue());
	    
	    mapper = new ObjectMapper();
	    ErrorResponse error = mapper.readValue(respuesta, ErrorResponse.class);
	    System.out.println(error.getMessage());
	}

}
