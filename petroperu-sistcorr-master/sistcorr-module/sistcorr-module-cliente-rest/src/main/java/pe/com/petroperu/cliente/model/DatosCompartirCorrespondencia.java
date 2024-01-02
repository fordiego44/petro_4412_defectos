package pe.com.petroperu.cliente.model;

import java.util.List;

import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.CorrespondenciaCompartida;

public class DatosCompartirCorrespondencia {
	
	private CorrespondenciaCompartida correspondencia;
	
	private List<ArchivoAdjunto> archivos;

	public CorrespondenciaCompartida getCorrespondencia() {
		return correspondencia;
	}

	public void setCorrespondencia(CorrespondenciaCompartida correspondencia) {
		this.correspondencia = correspondencia;
	}

	public List<ArchivoAdjunto> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<ArchivoAdjunto> archivos) {
		this.archivos = archivos;
	}
	
}
