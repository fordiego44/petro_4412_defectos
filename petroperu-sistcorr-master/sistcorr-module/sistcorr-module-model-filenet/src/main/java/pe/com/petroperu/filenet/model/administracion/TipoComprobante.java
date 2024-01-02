package pe.com.petroperu.filenet.model.administracion;

public class TipoComprobante {
	private Integer id;
	private String codigoComprobante;
	private String nombreComprobante;
	private String codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigoComprobante() {
		return codigoComprobante;
	}
	public void setCodigoComprobante(String codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}
	public String getNombreComprobante() {
		return nombreComprobante;
	}
	public void setNombreComprobante(String nombreComprobante) {
		this.nombreComprobante = nombreComprobante;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}

	@Override
	public String toString() {
		return "TipoComprobante [id=" + id + ", codigoComprobante=" + codigoComprobante + ", nombreComprobante="
				+ nombreComprobante + ", codigoAccion=" + codigoAccion + "]";
	}

}
