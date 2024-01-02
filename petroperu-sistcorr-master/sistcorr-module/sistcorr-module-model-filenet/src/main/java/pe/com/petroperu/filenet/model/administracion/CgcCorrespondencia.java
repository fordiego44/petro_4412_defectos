package pe.com.petroperu.filenet.model.administracion;

public class CgcCorrespondencia {
	
	private Integer id;
	private String codigoCGC;
	private String nombreCGC;
	private String tipoRotulo;
	private String mCodigoBarras;
	private String impresora;
	private String tipoImpresora;
	private String codigoLugar;
	private String nombreLugar;
	private String mComputarizado;
	private String codigoERP;
	private String codigoAccion;
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public String getTipoRotulo() {
		return tipoRotulo;
	}


	public void setTipoRotulo(String tipoRotulo) {
		this.tipoRotulo = tipoRotulo;
	}


	public String getmCodigoBarras() {
		return mCodigoBarras;
	}


	public void setmCodigoBarras(String mCodigoBarras) {
		this.mCodigoBarras = mCodigoBarras;
	}


	public String getImpresora() {
		return impresora;
	}


	public void setImpresora(String impresora) {
		this.impresora = impresora;
	}


	public String getTipoImpresora() {
		return tipoImpresora;
	}


	public void setTipoImpresora(String tipoImpresora) {
		this.tipoImpresora = tipoImpresora;
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


	public String getmComputarizado() {
		return mComputarizado;
	}


	public void setmComputarizado(String mComputarizado) {
		this.mComputarizado = mComputarizado;
	}


	public String getCodigoERP() {
		return codigoERP;
	}


	public void setCodigoERP(String codigoERP) {
		this.codigoERP = codigoERP;
	}


	public String getCodigoAccion() {
		return codigoAccion;
	}


	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}


	@Override
	public String toString() {
		return "CGCorrespondencia [id=" + id + ", codigoCGC=" + codigoCGC + ", nombreCGC=" + nombreCGC + ", tipoRotulo="
				+ tipoRotulo + ", mCodigoBarras=" + mCodigoBarras + ", impresora=" + impresora + ", tipoImpresora="
				+ tipoImpresora + ", codigoLugar=" + codigoLugar + ", nombreLugar=" + nombreLugar + ", mComputarizado="
				+ mComputarizado + ", codigoERP=" + codigoERP + ", codigoAccion=" + codigoAccion + "]";
	}
	
}
