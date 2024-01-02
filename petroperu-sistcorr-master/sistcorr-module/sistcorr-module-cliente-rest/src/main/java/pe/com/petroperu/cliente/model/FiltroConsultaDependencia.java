package pe.com.petroperu.cliente.model;

public class FiltroConsultaDependencia {
	
	private String codigoDependencia;
	private String nombreDependencia;
	private String tipo;
	private String jefe;
	
	public String getCodigoDependencia() {
		return codigoDependencia;
	}
	
	public void setCodigoDependencia(String codigoDependencia) {
		this.codigoDependencia = codigoDependencia;
	}
	
	public String getNombreDependencia() {
		return nombreDependencia;
	}
	
	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getJefe() {
		return jefe;
	}
	
	public void setJefe(String jefe) {
		this.jefe = jefe;
	}

	@Override
	public String toString() {
		return "FiltroConsultaDependencia [codigoDependencia=" + codigoDependencia + ", nombreDependencia="
				+ nombreDependencia + ", tipo=" + tipo + ", jefe=" + jefe + "]";
	}
	
}
