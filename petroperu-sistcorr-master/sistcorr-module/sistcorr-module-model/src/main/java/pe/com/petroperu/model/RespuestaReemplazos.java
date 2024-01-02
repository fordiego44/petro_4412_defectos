package pe.com.petroperu.model;

import java.sql.Date;

public class RespuestaReemplazos {
	
	private Reemplazos reemplazo;
	private Integer codConfirm;
	private String mensajeConfirmacion;

	
	public Reemplazos getReemplazo() {
		return reemplazo;
	}


	public void setReemplazo(Reemplazos reemplazo) {
		this.reemplazo = reemplazo;
	}


	public Integer getCodConfirm() {
		return codConfirm;
	}


	public void setCodConfirm(Integer codConfirm) {
		this.codConfirm = codConfirm;
	}


	public String getMensajeConfirmacion() {
		return mensajeConfirmacion;
	}


	public void setMensajeConfirmacion(String mensajeConfirmacion) {
		this.mensajeConfirmacion = mensajeConfirmacion;
	}
	
	
	@Override
	public String toString() {
		return "RespuestaReemplazos [reemplazo=" + reemplazo + ", codConfirm=" + codConfirm + ", mensajeConfirmacion="
				+ mensajeConfirmacion + "]";
	}


	
	
	
	
}
