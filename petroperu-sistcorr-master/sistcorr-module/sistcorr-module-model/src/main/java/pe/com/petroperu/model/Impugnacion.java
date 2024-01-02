//9000004276
package pe.com.petroperu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Impugnacion {

	private String ruc;
	private String nombreProveedor;
	private String correlativo;
	@DateTimeFormat (pattern="dd/MM/yyyy HH:mm:ss")
	private Date fechaPresentacion;
	private String tipo;
	private String mAdmisible;

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

	public Date getFechaPresentacion() {
		return fechaPresentacion;
	}

	public void setFechaPresentacion(Date fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getmAdmisible() {
		return mAdmisible;
	}

	public void setmAdmisible(String mAdmisible) {
		this.mAdmisible = mAdmisible;
	}

}
