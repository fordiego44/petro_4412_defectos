//9000004276
package pe.com.petroperu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Propuesta {

	private String ruc;
	private String nombreProveedor;
	private String correlativo;
	@DateTimeFormat (pattern="dd/MM/yyyy HH:mm:ss")
	private Date fechaPropuesta;
	private String presentadoFueraHora;

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public Date getFechaPropuesta() {
		return fechaPropuesta;
	}

	public void setFechaPropuesta(Date fechaPropuesta) {
		this.fechaPropuesta = fechaPropuesta;
	}

	public String getPresentadoFueraHora() {
		return presentadoFueraHora;
	}

	public void setPresentadoFueraHora(String presentadoFueraHora) {
		this.presentadoFueraHora = presentadoFueraHora;
	}

}
