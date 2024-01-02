package pe.com.petroperu.filenet.model.administracion;

public class Numeradores {

	private Integer id;
	private Integer codigoNumerador;
	private Integer  ultimoCorrelativo;
	private String codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoNumerador() {
		return codigoNumerador;
	}
	public void setCodigoNumerador(Integer codigoNumerador) {
		this.codigoNumerador = codigoNumerador;
	}
	public Integer getUltimoCorrelativo() {
		return ultimoCorrelativo;
	}
	public void setUltimoCorrelativo(Integer ultimoCorrelativo) {
		this.ultimoCorrelativo = ultimoCorrelativo;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "Numeradores [id=" + id + ", codigoNumerador=" + codigoNumerador + ", ultimoCorrelativo="
				+ ultimoCorrelativo + ", codigoAccion=" + codigoAccion + "]";
	}
		
}
