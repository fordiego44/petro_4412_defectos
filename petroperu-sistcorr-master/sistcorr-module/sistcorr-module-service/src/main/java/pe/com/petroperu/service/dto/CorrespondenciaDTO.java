package pe.com.petroperu.service.dto;

import java.util.List;
import pe.com.petroperu.model.emision.Correspondencia;

public class CorrespondenciaDTO extends Correspondencia {
	private List<ArchivoAdjuntoDTO> archivos;

	public List<ArchivoAdjuntoDTO> getArchivos() {
		return this.archivos;
	}

	public void setArchivos(List<ArchivoAdjuntoDTO> archivos) {
		this.archivos = archivos;
	}
}
