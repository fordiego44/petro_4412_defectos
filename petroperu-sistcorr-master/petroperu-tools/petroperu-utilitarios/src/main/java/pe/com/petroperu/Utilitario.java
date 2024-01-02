package pe.com.petroperu;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utilitario {
	
	  public static final String FORMATO_FECHA_API_1 = "yyyy-MM-dd HH:mm:SS";
	  public static final String FORMATO_FECHA_API_2 = "yyyy-MM-dd HH:mm:ss";
	  public static final String FORMATO_FECHA_API_3 = "E MMM dd HH:mm:ss Z yyyy";
	  public static final String FORMATO_FECHA_API_4 = "EEE MMM yy HH:mm:ss";
	  public static final String FORMATO_FECHA_API_5 = "yyyy/MM/dd";
	  public static final String FORMATO_FECHA_FRONT = "dd/MM/yyyy HH:mm";
	  public static final String FORMATO_FECHA_SIMPLE = "dd/MM/yyyy";
	
	public static LocalDateTime convertirToLocalDateTime(String fechaTexto) {
		LocalDateTime fecha;
		if(fechaTexto == null)
			fecha = null;
		if("".equals(fechaTexto))
			fecha = null;
		try {
			DateTimeFormatter formato = DateTimeFormatter.ofPattern(Utilitario.FORMATO_FECHA_API_1);
			return LocalDateTime.parse(fechaTexto, formato);
		} catch (Exception e) {
			e.printStackTrace();
			fecha = null;
		}
		return fecha;
	}
	
	public static Date convertirToDate(String fechaTexto) {
		Date fecha;
		if(fechaTexto == null)
			fecha = null;
		if("".equals(fechaTexto))
			fecha = null;
		boolean convertido = false;
		try {
			fecha = new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(fechaTexto);
			convertido = true;
		} catch (Exception e) {
			fecha = null;
		}
		if(convertido == false) {
			try {
				fecha = new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_2).parse(fechaTexto);
				convertido = true;
			} catch (Exception e) {
				fecha = null;
			}
		}
		if(convertido == false) {
			try {
				fecha = new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_3).parse(fechaTexto);
				convertido = true;
			} catch (Exception e) {
				fecha = null;
			}
		}
		if(convertido == false) {
			try {
				fecha = new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_4).parse(fechaTexto);
				convertido = true;
			} catch (Exception e) {
				fecha = null;
			}
		}
		if(convertido == false) {
			try {
				fecha = new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_5).parse(fechaTexto);
				convertido = true;
			} catch (Exception e) {
				fecha = null;
			}
		}
		return fecha;
	}
	
	public static String generateKey(){
		String key = "";
		SecureRandom random = new SecureRandom();
		String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
		String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	    String NUMBER = "0123456789";
	    String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	    StringBuilder sb = new StringBuilder(10);
	    for(int i=0;i<10;i++){
	    	int indice = random.nextInt(DATA_FOR_RANDOM_STRING.length());
	    	sb.append(DATA_FOR_RANDOM_STRING.charAt(indice));
	    }
	    key = sb.toString();
		return key;
	}
	
	// TICKET 7000004455
	public String nombreArchivoDescarga(String fileName) {
		String nombreEncode = "";
		try{
			nombreEncode = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
			nombreEncode = nombreEncode.replaceAll("\\+", " ");
		}catch(Exception e){
			e.printStackTrace();
			nombreEncode = fileName;
		}
		return nombreEncode;
	}
	// FIN TICKET

}
