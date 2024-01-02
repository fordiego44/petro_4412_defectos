package pe.com.petroperu.cliente.util;

/**
 * 
 * @author KenyoJoelPechoNaupar
 *
 */
public enum SistcorrBandeja {
	
	PENDIENTE("Pendientes", "pendientes", "Pendientes"),
	EN_ATENCION("EnAtencion", "atencion", "En atención"),
	COMPLETADA("Completadas", "completadas", "Completadas"),
	// TICKET 9000003862
	DE_GESTOR("DelGestor", "delgestor", "Del Gestor"),
	// Ticket 9*4413
	DE_RECEPTOR("DelReceptor", "delreceptor", "Del Receptor");
	
	public final String TD_BANDEJA;
	public final String URL;
	public final String ETIQUETA;
	
	SistcorrBandeja(String TD_BANDEJA, String URL, String ETIQUETA) {
		this.TD_BANDEJA = TD_BANDEJA;
		this.URL = URL;
		this.ETIQUETA = ETIQUETA;
	}
}