package pe.com.petroperu.model.util;

public enum SistcorrEstado {
	SIN_ASIGNACION("pendiente", "lista-documentos/pendientes", "sin_asignar", "Sin asignar"),
	ASIGNADO("firmado", "lista-documentos/firmados", "asignado", "Por firmar"),
	FIRMADO("firmado", "lista-documentos/firmados", "firmado", "Firmado"),
	COMPLETADO("firmado", "lista-documentos/firmados", "ultima_fima", "Firmado"),
	RECHAZADO_ASIGNACION("firmado", "lista-documentos/firmados", "rechazado_asignacion", "Rechazado"),
	RECHAZADO_ELABORACION("firmado", "lista-documentos/firmados", "rechazado_elaboracion", "Rechazado"),
	ENVIADO("enviado", "lista-documentos/enviados", "enviado", "enviado");

	public final String BANDEJA;
	public final String URL;
	public final String ESTADO;
	public final String DESCRIPCION_ESTADO;

	SistcorrEstado(String bandeja, String url, String estado, String descripcionEstado) {
		this.BANDEJA = bandeja;
		this.URL = url;
		this.ESTADO = estado;
		this.DESCRIPCION_ESTADO = descripcionEstado;
	}
}
