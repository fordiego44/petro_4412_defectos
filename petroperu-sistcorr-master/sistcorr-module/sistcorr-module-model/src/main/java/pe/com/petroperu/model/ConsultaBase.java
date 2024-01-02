//9000004276
package pe.com.petroperu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ConsultaBase {

	private String ruc;
	private String nombreProveedor;
	private String correlativo;
	@DateTimeFormat (pattern="dd/MM/yyyy HH:mm:ss")
	private Date fechaConsulta;

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

	public Date getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

}
