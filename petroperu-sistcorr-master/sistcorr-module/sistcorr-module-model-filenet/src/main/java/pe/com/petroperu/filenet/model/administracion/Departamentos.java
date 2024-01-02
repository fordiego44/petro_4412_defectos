package pe.com.petroperu.filenet.model.administracion;

public class Departamentos {

	private Integer id;
	private Integer codigoDepartamento;
	private String departamento;
	private Integer itemPagina;
	private String codigoAccion;
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodigoDepartamento() {
		return codigoDepartamento;
	}
	public void setCodigoDepartamento(Integer codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public Integer getItemPagina() {
		return itemPagina;
	}
	public void setItemPagina(Integer itemPagina) {
		this.itemPagina = itemPagina;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	
	@Override
	public String toString() {
		return "Departamentos [id=" + id + ", codigoDepartamento=" + codigoDepartamento + ", departamento="
				+ departamento + ", itemPagina=" + itemPagina + ", codigoAccion=" + codigoAccion + "]";
	}
		
}
