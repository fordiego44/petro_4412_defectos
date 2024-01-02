package pe.com.petroperu.model.emision.dto;

public class LogDTO {
	
	private String nombreTabla;
	private String tipoTransaccion;
	private String valoresColumna;
	private String mensaje;
	private String usuario;
	private String transaccion;
	private String idArtefacto;
	private String fecha;

	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getValoresColumna() {
		return valoresColumna;
	}
	public void setValoresColumna(String valoresColumna) {
		this.valoresColumna = valoresColumna;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	public String getIdArtefacto() {
		return idArtefacto;
	}
	public void setIdArtefacto(String idArtefacto) {
		this.idArtefacto = idArtefacto;
	}
	
	 public LogDTO() {}
	public LogDTO(String nombreTabla, String tipoTransaccion, String valoresColumna,
			String mensaje, String usuario,
			String transaccion,String idArtefacto) {
		super();
		this.nombreTabla = nombreTabla;
		this.tipoTransaccion = tipoTransaccion;
		this.valoresColumna = valoresColumna;
		this.mensaje = mensaje;
		this.usuario = usuario;
		this.transaccion = transaccion;
		this.idArtefacto = idArtefacto;
	}
	
	@Override
	public String toString() {
		return "LogDTO [nombreTabla=" + nombreTabla + ", tipoTransaccion=" + tipoTransaccion
				+ ", valoresColumna=" + valoresColumna + ", mensaje="
				+ mensaje + ", usuario=" + usuario + ", transaccion=" + transaccion
				+ ", idArtefacto=" + idArtefacto + "]";
	}
	
}
