package pe.com.petroperu.service.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class FiltroConsultaLogDTO {
	
	private String tabla;
	private String usuario;
	private String accion;
	private boolean masFiltros = false;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaDesde;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaHasta;
	private String fechaDesdeText;
	private String fechaHastaText;
	private boolean todos = false;
	
	
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public void setFechaDesdeText(String fechaDesdeText) {
		this.fechaDesdeText = fechaDesdeText;
	}
	public void setFechaHastaText(String fechaHastaText) {
		this.fechaHastaText = fechaHastaText;
	}
	public boolean isMasFiltros() {
		return masFiltros;
	}
	public void setMasFiltros(boolean masFiltros) {
		this.masFiltros = masFiltros;
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
		if(fechaDesde == null)
			return "";
		fechaDesdeText = new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(fechaDesde);
		return fechaDesdeText;
	}
	public String getFechaHastaText() {
		if(fechaHasta == null)
			return "";
		fechaHastaText = new SimpleDateFormat(Utilitario.FORMATO_FECHA_SIMPLE).format(fechaHasta);
		return fechaHastaText;
	}
	
	@Override
	public String toString() {
		return "FiltroConsultaLogDTO [tabla=" + tabla
				+ ", usuario=" + usuario + ", accion="
				+ accion +  ", fechaDesde="
				+ fechaDesde + ", fechaHasta=" + fechaHasta + ", fechaDesdeText="
				+ fechaDesdeText + ", fechaHastaText=" + fechaHastaText + "]";
	}
	public boolean isTodos() {
		return todos;
	}
	public void setTodos(boolean todos) {
		this.todos = todos;
	}
	
	
	
}
