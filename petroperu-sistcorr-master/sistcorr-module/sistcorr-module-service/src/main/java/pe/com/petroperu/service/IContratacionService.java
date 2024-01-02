//9000004276
package pe.com.petroperu.service;

import java.io.ByteArrayInputStream;
import java.util.Locale;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.model.BaseVendida;
import pe.com.petroperu.model.ConsultaBase;
import pe.com.petroperu.model.Contratacion;
import pe.com.petroperu.model.Impugnacion;
import pe.com.petroperu.model.Propuesta;
import pe.com.petroperu.model.emision.dto.ContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.DespachoConsultaDTO;
import pe.com.petroperu.model.emision.dto.EstDigContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.ValijasRecibidasDTO;
import pe.com.petroperu.service.dto.FiltroConsultaContratacionDTO;
import pe.com.petroperu.service.dto.FiltroConsultaDespacho;
import pe.com.petroperu.service.dto.FiltroConsultaEstDigContratacion;
import pe.com.petroperu.service.dto.FiltroConsultaValijasRecibidas;

public interface IContratacionService {

	Respuesta<ContratacionConsultaDTO> consultarContratacionesPaginado(String usuario, FiltroConsultaContratacionDTO filtro, int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale);

	Respuesta<ByteArrayInputStream> consultarContratacionesExcel(String usuario, FiltroConsultaContratacionDTO filtro, Locale locale);

	Respuesta<Contratacion> consultarDatosContratacion(String usuario, String nroProceso, Locale locale);

	Respuesta<BaseVendida> consultarBasesVendidas(String usuario, String nroProceso, Locale locale);

	Respuesta<ConsultaBase> consultarConsultaBases(String username, String nroProceso, Locale locale);

	Respuesta<Propuesta> consultarPropuestas(String username, String nroProceso, Locale locale);

	Respuesta<Impugnacion> consultarImpugnaciones(String username, String nroProceso, Locale locale);

	Respuesta<InformacionDocumento> obtenerDocumentosAdjuntos(String token, String nroProceso, Locale locale);
	
	/*INI Ticket 9000004412*/
	Respuesta<EstDigContratacionConsultaDTO> consultarEstDigContratacionesPaginado(String usuario, FiltroConsultaEstDigContratacion filtro, int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale);
	Respuesta<ByteArrayInputStream> consultarEstDigContratacionExcel(FiltroConsultaEstDigContratacion filtro, String usuarioCreador, String usuario, Locale locale);
	Respuesta<DespachoConsultaDTO> consultaDespachoPaginado(String usuario, FiltroConsultaDespacho filtro,int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale);
	Respuesta<ValijasRecibidasDTO> consultaValijasRecibidasPaginado(String usuario, FiltroConsultaValijasRecibidas filtro,int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale);
	/*FIN Ticket 9000004412*/

}
