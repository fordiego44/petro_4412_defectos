package pe.com.petroperu.service.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import pe.com.petroperu.Utilitario;

public class FiltroConsultaFuncionariosDTO {
	
	private Integer dependencia;
	private String registro;
	private String nombres;
	private String apellidos;
	private boolean masFiltros = false;
	private boolean todos = false;
	
	public Integer getDependencia() {
		return dependencia;
	}
	public void setDependencia(Integer dependencia) {
		this.dependencia = dependencia;
	}
	public String getRegistro() {
		return registro;
	}
	public void setRegistro(String registro) {
		this.registro = registro;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public boolean isMasFiltros() {
		return masFiltros;
	}
	public void setMasFiltros(boolean masFiltros) {
		this.masFiltros = masFiltros;
	}

	
	@Override
	public String toString() {
		return "FiltroConsultaFuncionariosDTO [dependencia=" + dependencia
				+ ", registro=" + registro + ", nombres="
				+ nombres +  ", apellidos="
				+ apellidos + "]";
	}
	public boolean isTodos() {
		return todos;
	}
	public void setTodos(boolean todos) {
		this.todos = todos;
	}
	
	
	
}
