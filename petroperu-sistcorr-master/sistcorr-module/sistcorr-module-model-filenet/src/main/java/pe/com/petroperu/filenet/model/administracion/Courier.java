package pe.com.petroperu.filenet.model.administracion;

public class Courier {

	private Integer id;
	private Integer codigoCourier;
	private String  nombreCourier;
	private String codigoAccion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoCourier() {
		return codigoCourier;
	}
	public void setCodigoCourier(Integer codigoCourier) {
		this.codigoCourier = codigoCourier;
	}
	public String getNombreCourier() {
		return nombreCourier;
	}
	public void setNombreCourier(String nombreCourier) {
		this.nombreCourier = nombreCourier;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "Courier [id=" + id + ", codigoCourier=" + codigoCourier + ", nombreCourier=" + nombreCourier
				+ ", codigoAccion=" + codigoAccion + "]";
	}
	
	
}
