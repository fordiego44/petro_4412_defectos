package pe.com.petroperu.model.emision.dto;

public class CorrespondenciaEnlaceDTO {
	
	private String correlativo;
	private String numeroDocumento;
	private Long idAsignacion;
	private String tipoAccion;
	private String respuesta;
	
	public String getCorrelativo() {
		return correlativo;
	}
	
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Long getIdAsignacion() {
		return idAsignacion;
	}
	
	public void setIdAsignacion(Long idAsignacion) {
		this.idAsignacion = idAsignacion;
	}
	
	public String getTipoAccion() {
		return tipoAccion;
	}
	
	public void setTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
	
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	@Override
	public String toString() {
		return "CorrespondenciaEnlaceDTO [correlativo=" + correlativo + ", numeroDocumento=" + numeroDocumento
				+ ", idAsignacion=" + idAsignacion + ", tipoAccion=" + tipoAccion + ", respuesta=" + respuesta + "]";
	}
	
}
