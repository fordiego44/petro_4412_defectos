package pe.com.petroperu.service.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class FiltroConsultaReemplazoDTO {
	
	private String usuarioSaliente;
	private String usuarioEntrante;
	private String rol;
	private Integer dependencia;
	private boolean masFiltros = false;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaDesde;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_SIMPLE, timezone = "America/Bogota")
	private Date fechaHasta;
	private String fechaDesdeText;
	private String fechaHastaText;
	private boolean todos = false;
	private String referencia;
	
	public String getUsuarioSaliente() {
		return usuarioSaliente;
	}
	public void setUsuarioSaliente(String usuarioSaliente) {
		this.usuarioSaliente = usuarioSaliente;
	}
	public String getUsuarioEntrante() {
		return usuarioEntrante;
	}
	public void setUsuarioEntrante(String usuarioEntrante) {
		this.usuarioEntrante = usuarioEntrante;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public Integer getDependencia() {
		return dependencia;
	}
	public void setDependencia(Integer dependencia) {
		this.dependencia = dependencia;
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
	
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	@Override
	public String toString() {
		return "FiltroConsultaReemplazoDTO [usuarioSaliente=" + usuarioSaliente
				+ ", usuarioEntrante=" + usuarioEntrante + ", rol="
				+ rol + ", dependencia=" + dependencia +  ", fechaDesde="
				+ fechaDesde + ", fechaHasta=" + fechaHasta + ", fechaDesdeText="
				+ fechaDesdeText + ", fechaHastaText=" + fechaHastaText + ", referencia=" + referencia + "]";
	}
	
	public boolean isTodos() {
		return todos;
	}
	public void setTodos(boolean todos) {
		this.todos = todos;
	}
	
	
	
}
