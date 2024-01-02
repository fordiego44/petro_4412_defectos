package pe.com.petroperu.model;

import java.util.Date;

public class Expediente {

	private Long idExpediente;
	private String nroProceso;
	private String tipoProceso;
	private String objeto;
	private String nroMemo;
	private String registroContacto;
	private int anexo;
	private int cantidadBases;
	private String fechaInicioVentaBase;
	private String fechaCierreVentaBase;
	private String valorPliego;
	private String fechaInicioRecep;
	private String fechaInicioImpugnacion;
	private String fechaFinalImpugnacion;
	private String horaInicioVentaBase;
	private String horaInicioRecep;
	private String horaCierreVentaBase;
	private String horaFinRecep;
	private String fechaFinRecep;
	private int codigoDependencia;
	private String fechaInicioConsulta;
	private String fechaFinConsulta;
	private String horaFinConsulta;
	private String horaInicioConsulta;
	private String horaInicioRecep_c;
	private String horaFinRecep_c;
	private String objetoProceso;
	public Long getIdExpediente() {
		return idExpediente;
	}
	public void setIdExpediente(Long idExpediente) {
		this.idExpediente = idExpediente;
	}
	public String getNroProceso() {
		return nroProceso;
	}
	public void setNroProceso(String nroProceso) {
		this.nroProceso = nroProceso;
	}
	public String getTipoProceso() {
		return tipoProceso;
	}
	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}
	public String getObjeto() {
		return objeto;
	}
	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}
	public String getNroMemo() {
		return nroMemo;
	}
	public void setNroMemo(String nroMemo) {
		this.nroMemo = nroMemo;
	}
	public String getRegistroContacto() {
		return registroContacto;
	}
	public void setRegistroContacto(String registroContacto) {
		this.registroContacto = registroContacto;
	}
	public int getAnexo() {
		return anexo;
	}
	public void setAnexo(int anexo) {
		this.anexo = anexo;
	}
	public int getCantidadBases() {
		return cantidadBases;
	}
	public void setCantidadBases(int cantidadBases) {
		this.cantidadBases = cantidadBases;
	}
	public String getFechaInicioVentaBase() {
		return fechaInicioVentaBase;
	}
	public void setFechaInicioVentaBase(String fechaInicioVentaBase) {
		this.fechaInicioVentaBase = fechaInicioVentaBase;
	}
	public String getFechaCierreVentaBase() {
		return fechaCierreVentaBase;
	}
	public void setFechaCierreVentaBase(String fechaCierreVentaBase) {
		this.fechaCierreVentaBase = fechaCierreVentaBase;
	}
	public String getValorPliego() {
		return valorPliego;
	}
	public void setValorPliego(String valorPliego) {
		this.valorPliego = valorPliego;
	}
	public String getFechaInicioRecep() {
		return fechaInicioRecep;
	}
	public void setFechaInicioRecep(String fechaInicioRecep) {
		this.fechaInicioRecep = fechaInicioRecep;
	}
	public String getFechaInicioImpugnacion() {
		return fechaInicioImpugnacion;
	}
	public void setFechaInicioImpugnacion(String fechaInicioImpugnacion) {
		this.fechaInicioImpugnacion = fechaInicioImpugnacion;
	}
	public String getFechaFinalImpugnacion() {
		return fechaFinalImpugnacion;
	}
	public void setFechaFinalImpugnacion(String fechaFinalImpugnacion) {
		this.fechaFinalImpugnacion = fechaFinalImpugnacion;
	}
	public String getHoraInicioVentaBase() {
		return horaInicioVentaBase;
	}
	public void setHoraInicioVentaBase(String horaInicioVentaBase) {
		this.horaInicioVentaBase = horaInicioVentaBase;
	}
	public String getHoraInicioRecep() {
		return horaInicioRecep;
	}
	public void setHoraInicioRecep(String horaInicioRecep) {
		this.horaInicioRecep = horaInicioRecep;
	}
	public String getHoraCierreVentaBase() {
		return horaCierreVentaBase;
	}
	public void setHoraCierreVentaBase(String horaCierreVentaBase) {
		this.horaCierreVentaBase = horaCierreVentaBase;
	}
	public String getHoraFinRecep() {
		return horaFinRecep;
	}
	public void setHoraFinRecep(String horaFinRecep) {
		this.horaFinRecep = horaFinRecep;
	}
	public String getFechaFinRecep() {
		return fechaFinRecep;
	}
	public void setFechaFinRecep(String fechaFinRecep) {
		this.fechaFinRecep = fechaFinRecep;
	}
	public int getCodigoDependencia() {
		return codigoDependencia;
	}
	public void setCodigoDependencia(int codigoDependencia) {
		this.codigoDependencia = codigoDependencia;
	}
	public String getFechaInicioConsulta() {
		return fechaInicioConsulta;
	}
	public void setFechaInicioConsulta(String fechaInicioConsulta) {
		this.fechaInicioConsulta = fechaInicioConsulta;
	}
	public String getFechaFinConsulta() {
		return fechaFinConsulta;
	}
	public void setFechaFinConsulta(String fechaFinConsulta) {
		this.fechaFinConsulta = fechaFinConsulta;
	}
	public String getHoraFinConsulta() {
		return horaFinConsulta;
	}
	public void setHoraFinConsulta(String horaFinConsulta) {
		this.horaFinConsulta = horaFinConsulta;
	}
	public String getHoraInicioConsulta() {
		return horaInicioConsulta;
	}
	public void setHoraInicioConsulta(String horaInicioConsulta) {
		this.horaInicioConsulta = horaInicioConsulta;
	}
	public String getHoraInicioRecep_c() {
		return horaInicioRecep_c;
	}
	public void setHoraInicioRecep_c(String horaInicioRecep_c) {
		this.horaInicioRecep_c = horaInicioRecep_c;
	}
	public String getHoraFinRecep_c() {
		return horaFinRecep_c;
	}
	public void setHoraFinRecep_c(String horaFinRecep_c) {
		this.horaFinRecep_c = horaFinRecep_c;
	}
	public String getObjetoProceso() {
		return objetoProceso;
	}
	public void setObjetoProceso(String objetoProceso) {
		this.objetoProceso = objetoProceso;
	}
	
	
	
}
