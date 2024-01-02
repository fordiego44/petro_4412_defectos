package pe.com.petroperu.filenet.model.administracion;

public class Motivo {
	
	private Integer id;
	private Integer codigoMotivo;
	private String  nombreMotivo;
	private String codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoMotivo() {
		return codigoMotivo;
	}
	public void setCodigoMotivo(Integer codigoMotivo) {
		this.codigoMotivo = codigoMotivo;
	}
	public String getNombreMotivo() {
		return nombreMotivo;
	}
	public void setNombreMotivo(String nombreMotivo) {
		this.nombreMotivo = nombreMotivo;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	
	@Override
	public String toString() {
		return "Motivo [id=" + id + ", codigoMotivo=" + codigoMotivo + ", nombreMotivo=" + nombreMotivo
				+ ", codigoAccion=" + codigoAccion + "]";
	}
	
}
