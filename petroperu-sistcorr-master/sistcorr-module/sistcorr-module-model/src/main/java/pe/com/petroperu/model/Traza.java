package pe.com.petroperu.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class Traza {
	
	private String fecha;
	private String usuario;
	private String nomApeUsuario;
	private String detalle;
	private String traza;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaSTR;
	private boolean numCaract;
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getNomApeUsuario() {
		return nomApeUsuario;
	}
	public void setNomApeUsuario(String nomApeUsuario) {
		this.nomApeUsuario = nomApeUsuario;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getTraza() {
		return traza;
	}
	public void setTraza(String traza) {
		this.traza = traza;
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
		return "Traza [fecha=" + fecha + ", usuario=" + usuario + ", nomApeUsuario=" + nomApeUsuario + ", detalle="
				+ detalle + ", traza=" + traza + ", fechaSTR=" + fechaSTR + "]";
	}
	public boolean isNumCaract() {
		return numCaract;
	}
	public void setNumCaract(boolean numCaract) {
		this.numCaract = numCaract;
	}
	
}
