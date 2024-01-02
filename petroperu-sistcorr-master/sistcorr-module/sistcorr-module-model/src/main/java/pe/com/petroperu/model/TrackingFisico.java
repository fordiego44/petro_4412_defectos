package pe.com.petroperu.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class TrackingFisico {
	
	private String fecha;
	private String nomApeUsuario;
	private String ingreso;
	private String egreso;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaSTR;
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getNomApeUsuario() {
		return nomApeUsuario;
	}
	public void setNomApeUsuario(String nomApeUsuario) {
		this.nomApeUsuario = nomApeUsuario;
	}
	public String getIngreso() {
		return ingreso;
	}
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}
	public String getEgreso() {
		return egreso;
	}
	public void setEgreso(String egreso) {
		this.egreso = egreso;
	}
	public String getFechaSTR() {
		Date dateFechaSTR = Utilitario.convertirToDate(this.fecha);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.fechaSTR = sdf.format(dateFechaSTR);
		return fechaSTR;
	}
	public void setFechaSTR(String fechaSTR) {
		this.fechaSTR = fechaSTR;
	}
	@Override
	public String toString() {
		return "TrackingFisico [fecha=" + fecha + ", nomApeUsuario=" + nomApeUsuario + ", ingreso=" + ingreso
				+ ", egreso=" + egreso + ", fechaSTR=" + fechaSTR + "]";
	}
	
}
