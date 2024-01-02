//9000004276
package pe.com.petroperu.model.emision.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class EstDigContratacionConsultaDTO {
	private String nroProceso;
	private String correlativo;
	private String ruc;
	private String estado;
	/*@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechaDesde;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date fechahasta;*/
	private String fechaDesde;
	
	private String digitalizado;
	
	
	
	public String getNroProceso() {
		return nroProceso;
	}
	public void setNroProceso(String nroProceso) {
		this.nroProceso = nroProceso;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getCorrelativo() {
		return correlativo;
	}
	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	public String getDigitalizado() {
		return digitalizado;
	}
	public void setDigitalizado(String digitalizado) {
		this.digitalizado = digitalizado;
	}
	public EstDigContratacionConsultaDTO() {
		super();
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	@Override
	public String toString() {
		return "EstDigContratacionConsultaDTO [nroProceso=" + nroProceso + ", correlativo=" + correlativo + ", ruc="
				+ ruc + ", estado=" + estado + ", fechaDesde=" + fechaDesde + ", digitalizado=" + digitalizado + "]";
	}
	public EstDigContratacionConsultaDTO(String nroProceso, String correlativo, String ruc, String estado,
			String fechaDesde, String digitalizado) {
		super();
		this.nroProceso = nroProceso;
		this.correlativo = correlativo;
		this.ruc = ruc;
		this.estado = estado;
		this.fechaDesde = fechaDesde;
		this.digitalizado = digitalizado;
	}
	
	
	
	

	
}
