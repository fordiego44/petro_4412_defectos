package pe.com.petroperu.model;

import java.io.Serializable;

public class Accion implements Serializable {
	private static final long serialVersionUID = 1L;
	private String codigoAccion;
	private String accion;
	private String requiereRespuesta;

	public String getCodigoAccion() {
		return this.codigoAccion;
	}

	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getRequiereRespuesta() {
		return this.requiereRespuesta;
	}

	public void setRequiereRespuesta(String requiereRespuesta) {
		this.requiereRespuesta = requiereRespuesta;
	}
}
