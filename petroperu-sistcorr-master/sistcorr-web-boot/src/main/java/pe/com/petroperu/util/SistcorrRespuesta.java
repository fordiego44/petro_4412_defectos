package pe.com.petroperu.util;

import java.util.ArrayList;
import java.util.List;

public class SistcorrRespuesta<T> {
	
	public boolean estado = false;
	public String mensaje = "OK";
	public List<T> datos;
	
	public SistcorrRespuesta(boolean estado, String mensaje, List<T> datos) {
		super();
		this.estado = estado;
		this.mensaje = mensaje == null ? "" : mensaje;
		this.datos = datos == null ? new ArrayList<>() : datos;
	}
	
	public SistcorrRespuesta() {
		this.datos = new ArrayList<>();
	}

}
