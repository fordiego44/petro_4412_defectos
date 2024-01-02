package pe.com.petroperu.filenet.model;

public class ItemFilenet {
	private String codigo;
	private String descripcion;
	private String id;
	private String text;
	private String codSup;
	// TICKET 9000003780
	private String codigoAux;
	private String descripcionAux;
	// FIN TICKET

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getId() {
		this.id = this.codigo;
		return this.id;
	}

	public String getText() {
		this.text = this.descripcion;
		return this.text;
	}

	public String getCodSup() {
		return codSup;
	}

	public void setCodSup(String codSup) {
		this.codSup = codSup;
	}

	public String getCodigoAux() {
		return codigoAux;
	}

	public void setCodigoAux(String codigoAux) {
		this.codigoAux = codigoAux;
	}

	public String getDescripcionAux() {
		return descripcionAux;
	}

	public void setDescripcionAux(String descripcionAux) {
		this.descripcionAux = descripcionAux;
	}
	
}
