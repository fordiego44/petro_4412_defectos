package pe.com.petroperu.model;

import java.util.List;

public class DependenciaUnidadMatricial {
	
	private Long idDependenciaUnidadMatricial;
	private String codigo;
	private String dependenciaSuperior;
	private String descripcionCargo;
	private String estado;
	private String jefe;
	private String jerarquia;
	private String lugarTrabajo;
	private String nombre;
	private String siglas;
	private String tipo;
	private String tipoUnidadMatricial;
	private List<Integrante> integrantes;
	private Integer cantidadIntegrantes;
	private String textoIntegrantes;
	private String mensajeRpt;
	
	public Long getIdDependenciaUnidadMatricial() {
		return idDependenciaUnidadMatricial;
	}
	
	public void setIdDependenciaUnidadMatricial(Long idDependenciaUnidadMatricial) {
		this.idDependenciaUnidadMatricial = idDependenciaUnidadMatricial;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getDependenciaSuperior() {
		return dependenciaSuperior;
	}
	
	public void setDependenciaSuperior(String dependenciaSuperior) {
		this.dependenciaSuperior = dependenciaSuperior;
	}
	
	public String getDescripcionCargo() {
		return descripcionCargo;
	}
	
	public void setDescripcionCargo(String descripcionCargo) {
		this.descripcionCargo = descripcionCargo;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getJefe() {
		return jefe;
	}
	
	public void setJefe(String jefe) {
		this.jefe = jefe;
	}
	
	public String getJerarquia() {
		return jerarquia;
	}
	
	public void setJerarquia(String jerarquia) {
		this.jerarquia = jerarquia;
	}
	
	public String getLugarTrabajo() {
		return lugarTrabajo;
	}
	
	public void setLugarTrabajo(String lugarTrabajo) {
		this.lugarTrabajo = lugarTrabajo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getSiglas() {
		return siglas;
	}
	
	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipoUnidadMatricial() {
		return tipoUnidadMatricial;
	}

	public void setTipoUnidadMatricial(String tipoUnidadMatricial) {
		this.tipoUnidadMatricial = tipoUnidadMatricial;
	}

	public List<Integrante> getIntegrantes() {
		return integrantes;
	}
	
	public void setIntegrantes(List<Integrante> integrantes) {
		this.integrantes = integrantes;
	}
	
	public Integer getCantidadIntegrantes() {
		return cantidadIntegrantes;
	}

	public void setCantidadIntegrantes(Integer cantidadIntegrantes) {
		this.cantidadIntegrantes = cantidadIntegrantes;
	}

	public String getTextoIntegrantes() {
		return textoIntegrantes;
	}

	public void setTextoIntegrantes(String textoIntegrantes) {
		this.textoIntegrantes = textoIntegrantes;
	}

	public String getMensajeRpt() {
		return mensajeRpt;
	}

	public void setMensajeRpt(String mensajeRpt) {
		this.mensajeRpt = mensajeRpt;
	}

	@Override
	public String toString() {
		return "DependenciaUnidadMatricial [idDependenciaUnidadMatricial=" + idDependenciaUnidadMatricial + ", codigo="
				+ codigo + ", dependenciaSuperior=" + dependenciaSuperior + ", descripcionCargo=" + descripcionCargo
				+ ", estado=" + estado + ", jefe=" + jefe + ", jerarquia=" + jerarquia + ", lugarTrabajo="
				+ lugarTrabajo + ", nombre=" + nombre + ", siglas=" + siglas + ", tipo=" + tipo
				+ ", tipoUnidadMatricial=" + tipoUnidadMatricial + ", integrantes=" + integrantes
				+ ", cantidadIntegrantes=" + cantidadIntegrantes + ", textoIntegrantes=" + textoIntegrantes + "]";
	}
	
}
