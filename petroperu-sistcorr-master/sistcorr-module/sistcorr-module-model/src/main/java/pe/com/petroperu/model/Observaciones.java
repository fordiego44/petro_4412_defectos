package pe.com.petroperu.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class Observaciones {
	
	private String fecha;
	private String nomApeUsuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utilitario.FORMATO_FECHA_FRONT, timezone = "America/Bogota")
	private String fechaSTR;
	private String observacion;
	private boolean numCaract;
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
	public String getFechaSTR() {
		Date dateFechaSTR = Utilitario.convertirToDate(this.fecha);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.fechaSTR = sdf.format(dateFechaSTR);
		return fechaSTR;
	}
	public void setFechaSTR(String fechaSTR) {
		this.fechaSTR = fechaSTR;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	@Override
	public String toString() {
		return "Observaciones [fecha=" + fecha + ", nomApeUsuario=" + nomApeUsuario + ", fechaSTR=" + fechaSTR + ", observacion=" + observacion + "]";
	}
	public boolean isNumCaract() {
		return numCaract;
	}
	public void setNumCaract(boolean numCaract) {
		this.numCaract = numCaract;
	}
		
}
