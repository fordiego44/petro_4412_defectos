package pe.com.petroperu.filenet.model.administracion;

public class Estado {
	
	private Integer id;
	private Integer codigoEstado;
	private String codigoProceso;
	private String estado;
	private String codigoAccion;
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodigoEstado() {
		return codigoEstado;
	}

	public void setCodigoEstado(Integer codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	public String getCodigoProceso() {
		return codigoProceso;
	}

	public void setCodigoProceso(String codigoProceso) {
		this.codigoProceso = codigoProceso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}

	@Override
	public String toString() {
		return "Estado [id=" + id + ", codigoEstado=" + codigoEstado + ", codigoProceso=" + codigoProceso + ", estado="
				+ estado + ", codigoAccion=" + codigoAccion + "]";
	}

}
