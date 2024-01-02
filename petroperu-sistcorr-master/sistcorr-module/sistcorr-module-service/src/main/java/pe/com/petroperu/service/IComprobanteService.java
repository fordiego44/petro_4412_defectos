//9000004276
package pe.com.petroperu.service;

import java.io.ByteArrayInputStream;
import java.util.Locale;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.model.Observaciones;
import pe.com.petroperu.model.emision.dto.ComprobanteConsultaDTO;
import pe.com.petroperu.service.dto.FiltroConsultaComprobanteDTO;

public interface IComprobanteService {

	Respuesta<ComprobanteConsultaDTO> consultarComprobantesPaginado(String usuario, FiltroConsultaComprobanteDTO filtro, int length, int start, String columna, String order, String excel, Locale locale);

	Respuesta<ByteArrayInputStream> consultarComprobantesExcel(String usuario, FiltroConsultaComprobanteDTO filtro, Locale locale);

	Respuesta<ComprobanteConsultaDTO> consultarDatosComprobante(String usuario, String correlativo, Locale locale);

	Respuesta<InformacionDocumento> obtenerDocumentosAdjuntos(String token, String correlativo, Locale locale);

	Respuesta<Observaciones> obtenerListaObservaciones(String token, String correlativo, Locale locale);

	Respuesta<RespuestaApi> registrarObservacion(String idUsuario, String correlativo, String observacion, Locale locale);

}
