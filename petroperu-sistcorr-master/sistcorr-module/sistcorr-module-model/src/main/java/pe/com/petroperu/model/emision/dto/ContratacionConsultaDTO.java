//9000004276
package pe.com.petroperu.model.emision.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class ContratacionConsultaDTO {
	private String nroProceso;
	private String tipoProceso;
	private String nroMemo;
	private String dependencia;
	private String personaContacto;
	private String cantBases;
	private String valorBase;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date vbFechaHoraInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private Date vbFechaHoraFin;

	@Override
	public String toString() {
		return "ContratacionConsultaDTO [nroProceso=" + nroProceso + ", tipoProceso=" + tipoProceso + ", nroMemo=" + nroMemo + ", dependencia=" + dependencia + ", personaContacto=" + personaContacto + ", cantBases=" + cantBases + ", valorBase=" + valorBase + ", vbFechaHoraInicio=" + vbFechaHoraInicio + ", vbFechaHoraFin=" + vbFechaHoraFin + "]";
	}

	public ContratacionConsultaDTO() {
	}

	public ContratacionConsultaDTO(String nroProceso, String tipoProceso, String nroMemo, String dependencia, String personaContacto, String cantBases, String valorBase, Date vbFechaHoraInicio, Date vbFechaHoraFin) {
		super();
		this.nroProceso = nroProceso;
		this.tipoProceso = tipoProceso;
		this.nroMemo = nroMemo;
		this.dependencia = dependencia;
		this.personaContacto = personaContacto;
		this.cantBases = cantBases;
		this.valorBase = valorBase;
		this.vbFechaHoraInicio = vbFechaHoraInicio;
		this.vbFechaHoraFin = vbFechaHoraFin;
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

	public String getNroMemo() {
		return nroMemo;
	}

	public void setNroMemo(String nroMemo) {
		this.nroMemo = nroMemo;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public String getPersonaContacto() {
		return personaContacto;
	}

	public void setPersonaContacto(String personaContacto) {
		this.personaContacto = personaContacto;
	}

	public String getCantBases() {
		return cantBases;
	}

	public void setCantBases(String cantBases) {
		this.cantBases = cantBases;
	}

	public String getValorBase() {
		return valorBase;
	}

	public void setValorBase(String valorBase) {
		this.valorBase = valorBase;
	}

	public Date getVbFechaHoraInicio() {
		return vbFechaHoraInicio;
	}

	public void setVbFechaHoraInicio(Date vbFechaHoraInicio) {
		this.vbFechaHoraInicio = vbFechaHoraInicio;
	}

	public Date getVbFechaHoraFin() {
		return vbFechaHoraFin;
	}

	public void setVbFechaHoraFin(Date vbFechaHoraFin) {
		this.vbFechaHoraFin = vbFechaHoraFin;
	}

}
