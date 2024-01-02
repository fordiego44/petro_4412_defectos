package pe.com.petroperu.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.petroperu.ad.model.Rol;
import pe.com.petroperu.ad.model.Usuario;
import pe.com.petroperu.ad.util.Configuracion;

public class TestQA {
	
	@org.junit.Test
	public void properties() {
		System.out.println(Configuracion.IP_SERVIDOR_QA);
		System.out.println(Configuracion.URL_SERVIDOR_QA);
		System.out.println(Configuracion.PUERTO_SERVIDOR_QA);
		System.out.println(Configuracion.USUARIO);
		System.out.println(Configuracion.CLAVE);
		System.out.println(Configuracion.AUTENTICACION);
		System.out.println(Configuracion.INITIAL_CONTEXT_FACTORY);
	}
	
	
	
	@org.junit.Test
	public void getUsuario() {
		//System.out.println(FConexionDirectorioQA.getUsuarioDN("amurillo"));
	}
	
	@org.junit.Test
	public void validAcceso() {
		//System.out.println(FConexionDirectorioQA.validarAcceso("amurillo", "c0rrespon"));
	}
	
	@org.junit.Test
	public void datosUsuario() {
		/*Usuario _uUsuario = FConexionDirectorioQA.datosUsuario("amurillo");
		System.out.println(_uUsuario.toString());*/
		
	}
	
	@org.junit.Test
	public void datosGrupos() {
		/*List<Rol> roles = FConexionDirectorioQA.datosGrupos("gestor2");
		for (Rol rol : roles) {
			System.out.println(rol.toString());
		}*/
	}
	
	@org.junit.Test
	public void testDate() {
		/*String fecha = "23/09/19";
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			Date date1= new SimpleDateFormat("dd/MM/yy").parse(fecha); 
			fecha = dateFormat.format(date1); 
			System.out.println(fecha);
		} catch (Exception e) {
			// TODO: handle exception
		}*/
	}
	
	@org.junit.Test
	public void testCaden() {
		String ids = "123,123234,345,456,567,567,";
		System.out.println(ids.substring(0, ids.length() -1));
	}

}
