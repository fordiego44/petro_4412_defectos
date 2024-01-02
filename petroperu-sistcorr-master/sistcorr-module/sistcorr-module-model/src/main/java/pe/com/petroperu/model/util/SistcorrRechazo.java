package pe.com.petroperu.model.util;

public enum SistcorrRechazo {

	ERROR_ASIGNACION("ERROR DE ASIGNACIÓN"),
	ERROR_ELABORACION("ERROR DE ELABORACIÓN");
	
	public final String DESCRIPCION;
	
	SistcorrRechazo(String descripcion){
		this.DESCRIPCION = descripcion;
	}
}
