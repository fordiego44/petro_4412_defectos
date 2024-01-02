package pe.com.petroperu.model;

import java.io.Serializable;
import java.util.Date;
import pe.com.petroperu.Semaforo;
import pe.com.petroperu.Utilitario;

public class HistoricoCorrespondencia implements Serializable {
	private static final long serialVersionUID = 1L;
	private String asignadoPor;
	private String requerimiento;
	private Integer avance;
	private String atendido;
	private String plazo;
	private String fechaRespuesta;
	private String asignadoA;
	private String respuesta;
	private String accion;
	private String fechaAsignacion;
	private Date fechaRespuestaProc;
	private Date fechaAsignacionProc;
	private String color;

	public String getAsignadoPor() {
		return this.asignadoPor;
	}

	public void setAsignadoPor(String asignadoPor) {
		this.asignadoPor = asignadoPor;
	}

	public String getRequerimiento() {
		return this.requerimiento;
	}

	public void setRequerimiento(String requerimiento) {
		this.requerimiento = requerimiento;
	}

	public Integer getAvance() {
		return this.avance;
	}

	public void setAvance(Integer avance) {
		this.avance = avance;
	}

	public String getAtendido() {
		return this.atendido;
	}

	public void setAtendido(String atendido) {
		this.atendido = atendido;
	}

	public String getPlazo() {
		return this.plazo;
	}

	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}

	public String getFechaRespuesta() {
		return this.fechaRespuesta;
	}

	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	public String getAsignadoA() {
		return this.asignadoA;
	}

	public void setAsignadoA(String asignadoA) {
		this.asignadoA = asignadoA;
	}

	public String getRespuesta() {
		return this.respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getFechaAsignacion() {
		return this.fechaAsignacion;
	}

	public void setFechaAsignacion(String fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	public Date getFechaRespuestaProc() {
		this.fechaRespuestaProc = Utilitario.convertirToDate(this.fechaRespuesta);
		return this.fechaRespuestaProc;
	}

	public void setFechaRespuestaProc(Date fechaRespuestaProc) {
		this.fechaRespuestaProc = fechaRespuestaProc;
	}

	public Date getFechaAsignacionProc() {
		this.fechaAsignacionProc = Utilitario.convertirToDate(this.fechaAsignacion);
		return this.fechaAsignacionProc;
	}

	public void setFechaAsignacionProc(Date fechaAsignacionProc) {
		this.fechaAsignacionProc = fechaAsignacionProc;
	}

	public String getColor() {
		if (this.avance == null)
			this.avance = Integer.valueOf(0);
		if (this.avance.intValue() == 0)
			this.color = Semaforo.ROJO.name();
		if (this.avance.intValue() > 0 && this.avance.intValue() < 100)
			this.color = Semaforo.AMARILLO.name();
		if (this.avance.intValue() == 100)
			this.color = Semaforo.VERDE.name();
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
