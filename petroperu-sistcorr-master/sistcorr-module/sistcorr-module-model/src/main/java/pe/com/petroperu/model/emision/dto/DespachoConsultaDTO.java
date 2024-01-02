//9000004276
package pe.com.petroperu.model.emision.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class DespachoConsultaDTO {
	
	private Long idDespacho;
	private String correlativo;
	private String fechaCreacion;	
	private String asunto;
	private String nroDocInterno;
	private String destino;
	private String origen;
	private String estado;
	private String desEmision;
	public Long getIdDespacho() {
		return idDespacho;
	}
	public void setIdDespacho(Long idDespacho) {
		this.idDespacho = idDespacho;
	}
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getNroDocInterno() {
		return nroDocInterno;
	}
	public void setNroDocInterno(String nroDocInterno) {
		this.nroDocInterno = nroDocInterno;
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
	public String getDesEmision() {
		return desEmision;
	}
	public void setDesEmision(String desEmision) {
		this.desEmision = desEmision;
	}
	@Override
	public String toString() {
		return "DespachoConsultaDTO [idDespacho=" + idDespacho + ", correlativo=" + correlativo + ", fechaCreacion="
				+ fechaCreacion + ", asunto=" + asunto + ", nroDocInterno=" + nroDocInterno + ", destino=" + destino
				+ ", origen=" + origen + ", estado=" + estado + ", desEmision=" + desEmision + "]";
	}
	public DespachoConsultaDTO(Long idDespacho, String correlativo, String fechaCreacion, String asunto,
			String nroDocInterno, String destino, String origen, String estado, String desEmision) {
		super();
		this.idDespacho = idDespacho;
		this.correlativo = correlativo;
		this.fechaCreacion = fechaCreacion;
		this.asunto = asunto;
		this.nroDocInterno = nroDocInterno;
		this.destino = destino;
		this.origen = origen;
		this.estado = estado;
		this.desEmision = desEmision;
	}
	public DespachoConsultaDTO() {
		super();
	}
	
	
	
	
	
}
