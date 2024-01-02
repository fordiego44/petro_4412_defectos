package pe.com.petroperu.ad;

import java.time.Instant;
import java.util.List;

import pe.com.petroperu.ad.model.Rol;
import pe.com.petroperu.ad.model.Usuario;
import pe.com.petroperu.ad.util.Configuracion;

public class Test {
	
	@org.junit.Test
	public void properties() {
		System.out.println(Configuracion.IP_SERVIDOR_PROD);
		System.out.println(Configuracion.URL_SERVIDOR_PROD);
		System.out.println(Configuracion.PUERTO_SERVIDOR_PROD);
		System.out.println(Configuracion.USUARIO);
		System.out.println(Configuracion.CLAVE);
		System.out.println(Configuracion.AUTENTICACION);
		System.out.println(Configuracion.INITIAL_CONTEXT_FACTORY);
	}
	
	
	
	@org.junit.Test
	public void getUsuario() {
		//System.out.println(FConexionDirectorioProd.getUsuarioDN("rchanduvi"));
	}
	
	@org.junit.Test
	public void validAcceso() {
		//System.out.println(FConexionDirectorioProd.validarAcceso("kpecho", "Lima4Lima"));
	}
	
	@org.junit.Test
	public void datosUsuario() {
		//Usuario _uUsuario = FConexionDirectorioProd.datosUsuario("kpecho");
		//System.out.println(_uUsuario.toString());
		
	}
	
	@org.junit.Test
	public void datosGrupos() {
		/*List<Rol> roles = FConexionDirectorioProd.datosGrupos("rchanduvi");
		for (Rol rol : roles) {
			System.out.println(rol.toString());
		}*/
	}
	
	@org.junit.Test
	public void timetest() {
		/*for (int i = 0; i < 10; i++) {
			System.out.println(System.nanoTime());
			System.out.println(Instant.now().toEpochMilli());
			System.out.println("-------");
		}*/
		
	}
}
