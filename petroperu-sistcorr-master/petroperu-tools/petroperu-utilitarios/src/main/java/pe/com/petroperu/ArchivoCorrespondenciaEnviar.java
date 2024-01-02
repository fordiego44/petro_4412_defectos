package pe.com.petroperu;

public class ArchivoCorrespondenciaEnviar {

	private String nombre;
	private String nombreDocServidor;
	private String claveArcCompartido;
	private String numeroDocumento;
	private boolean esEnviarComoAdjunto;
	private String ubicacionArchivo;
	private boolean existeAchivoEnviarComoDireccion;

	public boolean isExisteAchivoEnviarComoDireccion() {
		return existeAchivoEnviarComoDireccion;
	}

	public void setExisteAchivoEnviarComoDireccion(boolean existeAchivoEnviarComoDireccion) {
		this.existeAchivoEnviarComoDireccion = existeAchivoEnviarComoDireccion;
	}

	public String getNombreDocServidor() {
		return nombreDocServidor;
	}

	public void setNombreDocServidor(String nombreDocServidor) {
		this.nombreDocServidor = nombreDocServidor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClaveArcCompartido() {
		return claveArcCompartido;
	}

	public void setClaveArcCompartido(String claveArcCompartido) {
		this.claveArcCompartido = claveArcCompartido;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public boolean isEsEnviarComoAdjunto() {
		return esEnviarComoAdjunto;
	}

	public void setEsEnviarComoAdjunto(boolean esEnviarComoAdjunto) {
		this.esEnviarComoAdjunto = esEnviarComoAdjunto;
	}

	public String getUbicacionArchivo() {
		return ubicacionArchivo;
	}

	public void setUbicacionArchivo(String ubicacionArchivo) {
		this.ubicacionArchivo = ubicacionArchivo;
	}

}
