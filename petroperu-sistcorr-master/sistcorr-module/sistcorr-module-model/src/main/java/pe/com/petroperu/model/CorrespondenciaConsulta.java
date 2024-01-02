package pe.com.petroperu.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class CorrespondenciaConsulta {
	
	private String idCorrespondencia;
	private String correlativo;
	private String fechaRadicado;
	private String asunto;
	private String numeroDocumentoInterno;
	private String destino;
	private String origen;
	private String estado;
	private String tipoCorrespondencia;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaRadicadoProc;
	private String tipoIcono;
	private String esAsignado;
	private String esConfidencial;
	
	
	public String getEsAsignado() {
		return esAsignado;
	}

	public void setEsAsignado(String esAsignado) {
		this.esAsignado = esAsignado;
	}

	public String getEsConfidencial() {
		return esConfidencial;
	}

	public void setEsConfidencial(String esConfidencial) {
		this.esConfidencial = esConfidencial;
	}

	public String getTipoIcono() {
		return tipoIcono;
	}

	public void setTipoIcono(String tipoIcono) {
		this.tipoIcono = tipoIcono;
	}

	public String getCorrelativo() {
		return correlativo;
	}
	
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	
	public String getFechaRadicado() {
		return fechaRadicado;
	}
	
	public void setFechaRadicado(String fechaRadicado) {
		this.fechaRadicado = fechaRadicado;
	}
	
	public String getAsunto() {
		return asunto;
	}
	
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	public String getNumeroDocumentoInterno() {
		return numeroDocumentoInterno;
	}
	
	public void setNumeroDocumentoInterno(String numeroDocumentoInterno) {
		this.numeroDocumentoInterno = numeroDocumentoInterno;
	}
	
	public String getDestino() {
		return destino;
	}
	
	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	public String getOrigen() {
		return origen;
	}
	
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getTipoCorrespondencia() {
		return tipoCorrespondencia;
	}
	
	public void setTipoCorrespondencia(String tipoCorrespondencia) {
		this.tipoCorrespondencia = tipoCorrespondencia;
	}
	
	public String getFechaRadicadoProc() {
		Date dateFechaRadicado = Utilitario.convertirToDate(this.fechaRadicado);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.fechaRadicadoProc = sdf.format(dateFechaRadicado);
		return fechaRadicadoProc;
	}
	
	public void setFechaRadicadoProc(String fechaRadicadoProc) {
		this.fechaRadicadoProc = fechaRadicadoProc;
	}

	public String getIdCorrespondencia() {
		return idCorrespondencia;
	}

	public void setIdCorrespondencia(String idCorrespondencia) {
		this.idCorrespondencia = idCorrespondencia;
	}
	
}
