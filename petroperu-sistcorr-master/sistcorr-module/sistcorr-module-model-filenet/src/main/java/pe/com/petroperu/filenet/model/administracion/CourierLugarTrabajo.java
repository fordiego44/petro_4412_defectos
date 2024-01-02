package pe.com.petroperu.filenet.model.administracion;

public class CourierLugarTrabajo {
	
	private Integer id;
	private Integer codigoCourier;
	private String  nombreCourier;
	private String  alcance;
	private String  codigoCGC;	
	private String  nombreCGC;
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
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public String getCodigoCGC() {
		return codigoCGC;
	}
	public void setCodigoCGC(String codigoCGC) {
		this.codigoCGC = codigoCGC;
	}
	public String getNombreCGC() {
		return nombreCGC;
	}
	public void setNombreCGC(String nombreCGC) {
		this.nombreCGC = nombreCGC;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "CourierLugarTrabajo [id=" + id + ", codigoCourier=" + codigoCourier + ", nombreCourier=" + nombreCourier
				+ ", alcance=" + alcance + ", codigoCGC=" + codigoCGC + ", nombreCGC=" + nombreCGC + ", codigoAccion="
				+ codigoAccion + "]";
	}

}
