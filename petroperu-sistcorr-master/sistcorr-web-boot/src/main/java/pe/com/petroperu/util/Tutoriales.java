package pe.com.petroperu.util;

public enum Tutoriales {
	
	NORMATIVA("normativa/", "Normativa"),
	GUIA("guia/", "Guía de Usuario"),
	VIDEO("video/", "Video Tutoriales"),
	MANUAL("manual/", "Manuales de Usuario"),
	PREGUNTAS("preguntas/", "Preguntas Frecuentes");
	
	public final String TD_Directorio;
	public final String TD_Nombre;
	
	private Tutoriales(String tD_Directorio, String tD_Nombre) {
		TD_Directorio = tD_Directorio;
		TD_Nombre = tD_Nombre;
	}

}
