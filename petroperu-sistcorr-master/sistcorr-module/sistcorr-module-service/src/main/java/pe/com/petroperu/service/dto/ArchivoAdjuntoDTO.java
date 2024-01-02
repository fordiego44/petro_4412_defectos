package pe.com.petroperu.service.dto;

import org.springframework.web.multipart.MultipartFile;

public class ArchivoAdjuntoDTO {
	private Long indentificador;
	private boolean principal;
	private MultipartFile file;
	private String tipo;
	private String nombre;

	public Long getIndentificador() {
		return this.indentificador;
	}

	public void setIndentificador(Long indentificador) {
		this.indentificador = indentificador;
	}

	public boolean isPrincipal() {
		return this.principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public MultipartFile getFile() {
		return this.file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
