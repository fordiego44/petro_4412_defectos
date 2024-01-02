package pe.com.petroperu.filenet.model.administracion;

public class TransaccionesCgc {
	
	private Integer id;
	private String tipoTransaccion;
	private String cgcOrigen;
	private String cgcDestino;
	private Integer codigoNumerador;
	private String codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getCgcOrigen() {
		return cgcOrigen;
	}
	public void setCgcOrigen(String cgcOrigen) {
		this.cgcOrigen = cgcOrigen;
	}
	public String getCgcDestino() {
		return cgcDestino;
	}
	public void setCgcDestino(String cgcDestino) {
		this.cgcDestino = cgcDestino;
	}
	public Integer getCodigoNumerador() {
		return codigoNumerador;
	}
	public void setCodigoNumerador(Integer codigoNumerador) {
		this.codigoNumerador = codigoNumerador;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "TransaccionesCGC [id=" + id + ", tipoTransaccion=" + tipoTransaccion + ", cgcOrigen=" + cgcOrigen
				+ ", cgcDestino=" + cgcDestino + ", codigoNumerador=" + codigoNumerador + ", codigoAccion="
				+ codigoAccion + "]";
	}
	
}
