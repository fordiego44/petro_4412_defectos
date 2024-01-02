package pe.com.petroperu.service.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class FiltroConsultaConductorDTO {
	
	private String tareasExcepcion;
	private String procesos;
	private String referenciaPrincipal;
	private String referenciaAlternativa;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaDesde;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaHasta;
	private String fechaDesdeText;
	private String fechaHastaText;
	private boolean todos = false;
	
	
	public String getTareasExcepcion() {
		return tareasExcepcion;
	}
	public void setTareasExcepcion(String tareasExcepcion) {
		this.tareasExcepcion = tareasExcepcion;
	}
	public String getProcesos() {
		return procesos;
	}
	public void setProcesos(String procesos) {
		this.procesos = procesos;
	}
	public String getReferenciaPrincipal() {
		return referenciaPrincipal;
	}
	public void setReferenciaPrincipal(String referenciaPrincipal) {
		this.referenciaPrincipal = referenciaPrincipal;
	}
	public String getReferenciaAlternativa() {
		return referenciaAlternativa;
	}
	public void setReferenciaAlternativa(String referenciaAlternativa) {
		this.referenciaAlternativa = referenciaAlternativa;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getFechaDesdeText() {
		return fechaDesdeText;
	}
	public void setFechaDesdeText(String fechaDesdeText) {
		this.fechaDesdeText = fechaDesdeText;
	}
	public String getFechaHastaText() {
		return fechaHastaText;
	}
	public void setFechaHastaText(String fechaHastaText) {
		this.fechaHastaText = fechaHastaText;
	}
	public boolean isTodos() {
		return todos;
	}
	public void setTodos(boolean todos) {
		this.todos = todos;
	}
	
	
	@Override
	public String toString() {
		return "FiltroConsultaConductorDTO [tareasExcepcion=" + tareasExcepcion + ", procesos=" + procesos
				+ ", referenciaPrincipal=" + referenciaPrincipal + ", referenciaAlternativa=" + referenciaAlternativa
				+ ", fechaDesde=" + fechaDesde + ", fechaHasta=" + fechaHasta + ", fechaDesdeText=" + fechaDesdeText
				+ ", fechaHastaText=" + fechaHastaText + ", todos=" + todos + "]";
	}
	
	
	
}
