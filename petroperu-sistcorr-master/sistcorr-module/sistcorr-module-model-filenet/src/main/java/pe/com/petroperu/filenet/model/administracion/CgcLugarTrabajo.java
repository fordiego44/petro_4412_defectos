package pe.com.petroperu.filenet.model.administracion;

public class CgcLugarTrabajo {
	
	private Integer id;
	private String codigoCgc;
	private String nombreCgc;
	private String codigoLugar;
	private String nombreLugar;
	private String codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigoCgc() {
		return codigoCgc;
	}
	public void setCodigoCgc(String codigoCgc) {
		this.codigoCgc = codigoCgc;
	}
	public String getNombreCgc() {
		return nombreCgc;
	}
	public void setNombreCgc(String nombreCgc) {
		this.nombreCgc = nombreCgc;
	}
	public String getCodigoLugar() {
		return codigoLugar;
	}
	public void setCodigoLugar(String codigoLugar) {
		this.codigoLugar = codigoLugar;
	}
	public String getNombreLugar() {
		return nombreLugar;
	}
	public void setNombreLugar(String nombreLugar) {
		this.nombreLugar = nombreLugar;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "CgcLugarTrabajo [id=" + id + ", codigoCgc=" + codigoCgc + ", nombreCgc=" + nombreCgc + ", codigoLugar="
				+ codigoLugar + ", nombreLugar=" + nombreLugar + ", codigoAccion=" + codigoAccion + "]";
	}

}
