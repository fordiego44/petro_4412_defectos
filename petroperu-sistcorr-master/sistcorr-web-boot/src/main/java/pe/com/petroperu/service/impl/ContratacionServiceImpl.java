//9000004276
package pe.com.petroperu.service.impl;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import javax.persistence.ParameterMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Service;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.Utilitario;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.model.BaseVendida;
import pe.com.petroperu.model.ConsultaBase;
import pe.com.petroperu.model.Contratacion;
import pe.com.petroperu.model.Impugnacion;
import pe.com.petroperu.model.Propuesta;
import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.model.emision.dto.ComprobanteConsultaDTO;
import pe.com.petroperu.model.emision.dto.ContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.DespachoConsultaDTO;
import pe.com.petroperu.model.emision.dto.EstDigContratacionConsultaDTO;
import pe.com.petroperu.model.emision.dto.ValijasRecibidasDTO;
import pe.com.petroperu.service.IComprobanteService;
import pe.com.petroperu.service.IContratacionService;
import pe.com.petroperu.service.dto.FiltroConsultaContratacionDTO;
import pe.com.petroperu.service.dto.FiltroConsultaDespacho;
import pe.com.petroperu.service.dto.FiltroConsultaEstDigContratacion;
import pe.com.petroperu.service.dto.FiltroConsultaValijasRecibidas;
import pe.com.petroperu.service.util.IReport;
import pe.com.petroperu.service.util.ReportExcelComprobantes;
import pe.com.petroperu.service.util.ReportExcelContrataciones;
import pe.com.petroperu.service.util.ReportExcelEstDigContratacion;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;

@Service
@PropertySource({ "classpath:application.properties" })
public class ContratacionServiceImpl implements IContratacionService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ISistcorrCliente sistcorrCliente;
	
	@Autowired
	private IFilenetDAO filenetDAO;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Respuesta<ContratacionConsultaDTO> consultarContratacionesPaginado(String usuario, FiltroConsultaContratacionDTO filtro, int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale) {
		LOGGER.info("[INICIO] consultarComprobantesPaginado " + filtro.toString());
		Respuesta<ContratacionConsultaDTO> respuesta = new Respuesta<>();
		try {

			String nroProceso = filtro.getNroProceso();
			String tipoProceso = filtro.getTipoProceso();
			String nroMemo = filtro.getNroMemo();
			Integer codDependencia = filtro.getCodDependencia();

			LOGGER.info("usuario: " + usuario + "||" + "nroProceso: " + nroProceso + "||" + "tipoProceso: " + tipoProceso + "||" + "codDependencia: " + codDependencia + "||" + "itemsPorPagina: " + itemsPorPagina + "||" + "numeroPagina: " + numeroPagina + "||" + "exportaExcel: "
					+ exportaExcel + "||" + "nombreColumna: " + nombreColumna + "||" + "desc: " + desc);

			List<Object[]> _contrataciones = filenetDAO.consultar_contrataciones_paginado(usuario, nroProceso, tipoProceso, nroMemo, codDependencia, itemsPorPagina, numeroPagina, exportaExcel, nombreColumna, desc);

			if (_contrataciones != null) {

				Object[] total = _contrataciones.get(0);
				_contrataciones.remove(0);

				List<ContratacionConsultaDTO> contrataciones = new ArrayList<>();

				for (Object[] _contratacion : _contrataciones) {
					ContratacionConsultaDTO cont = new ContratacionConsultaDTO();
					
					cont.setNroProceso(_contratacion[0] == null ? "" : String.valueOf(_contratacion[0]));
					cont.setTipoProceso(_contratacion[1] == null ? "" : String.valueOf(_contratacion[1]));
					cont.setNroMemo(_contratacion[2] == null ? "" : String.valueOf(_contratacion[2]));
					cont.setDependencia(_contratacion[3] == null ? "" : String.valueOf(_contratacion[3]));
					cont.setPersonaContacto(_contratacion[4] == null ? "" : String.valueOf(_contratacion[4]));
					cont.setCantBases(_contratacion[5] == null ? "" : String.valueOf(_contratacion[5]));
					cont.setValorBase(_contratacion[6] == null ? "" : String.valueOf(_contratacion[6]));					
					cont.setVbFechaHoraInicio(_contratacion[7] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_contratacion[7].toString().substring(0, 19)));
					cont.setVbFechaHoraFin(_contratacion[8] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_contratacion[8].toString().substring(0, 19)));

					contrataciones.add(cont);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(contrataciones);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarComprobantesPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override 
	public Respuesta<ByteArrayInputStream> consultarContratacionesExcel(String usuario, FiltroConsultaContratacionDTO filtro, Locale locale) {
		LOGGER.info("[INICIO] consultarComprobantesExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			Respuesta<ContratacionConsultaDTO> consulta = consultarContratacionesPaginado(usuario, filtro, 0, 0, "", "", "SI", locale);

			IReport<ByteArrayInputStream> report;
			if (consulta.estado) {
				report = new ReportExcelContrataciones(consulta.datos, usuario);
			} else {
				report = new ReportExcelContrataciones(new ArrayList<>(), usuario);
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<Contratacion> consultarDatosContratacion(String usuario, String nroProceso, Locale locale) {
		LOGGER.info("[INICIO] consultarDatosContratacion " + nroProceso.toString());
		Respuesta<Contratacion> respuesta = new Respuesta<>();
		try {

			Contratacion cont = filenetDAO.consultar_detalle_contratacion(usuario, nroProceso);

			if (cont != null) {				
				if(cont.getNroProceso().length()==0) {
					respuesta.estado = false;
					respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.contratacion.inexistente", null, locale);
				}else {
					respuesta.estado = true;
					respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.contratacion.exito", null, locale);
					respuesta.datos.add(cont);
				}
			} else {
				respuesta.estado = false;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.contratacion.error", null, locale);
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarDatosContratacion", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<BaseVendida> consultarBasesVendidas(String usuario, String nroProceso, Locale locale) {
		LOGGER.info("[INICIO] consultarBasesVendidas " + nroProceso.toString());
		Respuesta<BaseVendida> respuesta = new Respuesta<>();
		try {

			List<Object[]> _basesVendidas = filenetDAO.consultar_venta_base(nroProceso);

			if (_basesVendidas != null) {
				List<BaseVendida> basesVendidas = new ArrayList<>();

				for (Object[] _baseVendida : _basesVendidas) {
					BaseVendida bv = new BaseVendida();
					
					bv.setRuc(_baseVendida[0] == null ? "" : String.valueOf(_baseVendida[0]));
					bv.setNombreProveedor(_baseVendida[1] == null ? "" : String.valueOf(_baseVendida[1]));
					bv.setCorrelativo(_baseVendida[2] == null ? "" : String.valueOf(_baseVendida[2]));
					bv.setFechaVenta(_baseVendida[3] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(_baseVendida[3])));
					
					basesVendidas.add(bv);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(basesVendidas);
				respuesta.total = Integer.valueOf(basesVendidas.size());
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarComprobantesPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<ConsultaBase> consultarConsultaBases(String username, String nroProceso, Locale locale) {
		LOGGER.info("[INICIO] consultarConsultaBases " + nroProceso.toString());
		Respuesta<ConsultaBase> respuesta = new Respuesta<>();
		try {

			List<Object[]> _consultasBases = filenetDAO.consultar_consultas_base(nroProceso);

			if (_consultasBases != null) {
				List<ConsultaBase> consultasBases = new ArrayList<>();

				for (Object[] _consultaBase : _consultasBases) {
					ConsultaBase consultaBase = new ConsultaBase();
					
					consultaBase.setRuc(_consultaBase[0] == null ? "" : String.valueOf(_consultaBase[0]));
					consultaBase.setNombreProveedor(_consultaBase[1] == null ? "" : String.valueOf(_consultaBase[1]));
					consultaBase.setCorrelativo(_consultaBase[2] == null ? "" : String.valueOf(_consultaBase[2]));
					consultaBase.setFechaConsulta(_consultaBase[3] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(_consultaBase[3])));
					
					consultasBases.add(consultaBase);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(consultasBases);
				respuesta.total = Integer.valueOf(consultasBases.size());
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarConsultaBases", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<Propuesta> consultarPropuestas(String username, String nroProceso, Locale locale) {
		LOGGER.info("[INICIO] consultarPropuestas " + nroProceso.toString());
		Respuesta<Propuesta> respuesta = new Respuesta<>();
		try {

			List<Object[]> _propuestas = filenetDAO.consultar_propuestas(nroProceso);

			if (_propuestas != null) {
				List<Propuesta> propuestas = new ArrayList<>();

				for (Object[] _propuesta : _propuestas) {
					Propuesta prop = new Propuesta();
					
					prop.setRuc(_propuesta[0] == null ? "" : String.valueOf(_propuesta[0]));
					prop.setNombreProveedor(_propuesta[1] == null ? "" : String.valueOf(_propuesta[1]));
					prop.setCorrelativo(_propuesta[2] == null ? "" : String.valueOf(_propuesta[2]));
					prop.setFechaPropuesta(_propuesta[3] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(_propuesta[3])));
					prop.setPresentadoFueraHora(_propuesta[4] == null ? "" : String.valueOf(_propuesta[4]));
					
					propuestas.add(prop);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(propuestas);
				respuesta.total = Integer.valueOf(propuestas.size());
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarPropuestas", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<Impugnacion> consultarImpugnaciones(String username, String nroProceso, Locale locale) {
		LOGGER.info("[INICIO] consultarImpugnaciones " + nroProceso.toString());
		Respuesta<Impugnacion> respuesta = new Respuesta<>();
		try {

			List<Object[]> _impugnaciones = filenetDAO.consultar_impugnaciones(nroProceso);

			if (_impugnaciones != null) {
				List<Impugnacion> impugnaciones = new ArrayList<>();

				for (Object[] _impugnacion : _impugnaciones) {
					Impugnacion i = new Impugnacion();
					
					i.setRuc(_impugnacion[0] == null ? "" : String.valueOf(_impugnacion[0]));
					i.setNombreProveedor(_impugnacion[1] == null ? "" : String.valueOf(_impugnacion[1]));
					i.setCorrelativo(_impugnacion[2] == null ? "" : String.valueOf(_impugnacion[2]));
					i.setFechaPresentacion(_impugnacion[3] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(_impugnacion[3])));
					i.setTipo(_impugnacion[4] == null ? "" : String.valueOf(_impugnacion[4]));					
					i.setmAdmisible(_impugnacion[5] == null ? "" : String.valueOf(_impugnacion[5]));
					
					impugnaciones.add(i);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(impugnaciones);
				respuesta.total = Integer.valueOf(impugnaciones.size());
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarImpugnaciones", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<InformacionDocumento> obtenerDocumentosAdjuntos(String token, String nroProceso, Locale locale) {
		Respuesta<InformacionDocumento> respuesta = new Respuesta<>();
		String clase = "Contrataciones";
		try {
			respuesta = this.sistcorrCliente.recuperarDocumentosGeneral(token, clase, nroProceso, locale);
			if (respuesta.estado)
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.recuperarDocumentosAsjuntosExito", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;

	}
	
	/*INI Ticket 9000004412*/
	@Override
	public Respuesta<EstDigContratacionConsultaDTO> consultarEstDigContratacionesPaginado(String usuario, FiltroConsultaEstDigContratacion filtro, int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale) {
		LOGGER.info("[INICIO] consultarEstDigContratacionesPaginado " + filtro.toString());
		Respuesta<EstDigContratacionConsultaDTO> respuesta = new Respuesta<>();
		try {

			String nroProceso = filtro.getNroProceso();
			String fechaDesde=filtro.getFechaDesde();
			String fechaHasta=filtro.getFechaHasta();
			String ruc=filtro.getRuc();
			String estado=filtro.getEstado();
			
			LOGGER.info("usuario: " + usuario + "||" + "nroProceso: " + nroProceso + "||" + "fechaDesde: " + fechaDesde + "||" + "fechaHasta: " + fechaHasta + "||" + "ruc: " + ruc + "||"+ "estado: " + estado + "||"  
					+ "itemsPorPagina: " + itemsPorPagina + "||" + "numeroPagina: " + numeroPagina + "||" + "exportaExcel: "
					+ exportaExcel + "||" + "nombreColumna: " + nombreColumna + "||" + "desc: " + desc);

			List<Object[]> _contrataciones = filenetDAO.consultar_estDigContrataciones_paginado(usuario, nroProceso, fechaDesde,fechaHasta,ruc,estado, itemsPorPagina, numeroPagina, exportaExcel, nombreColumna, desc);

			if (_contrataciones != null) {

				Object[] total = _contrataciones.get(0);
				_contrataciones.remove(0);

				List<EstDigContratacionConsultaDTO> contrataciones = new ArrayList<>();

				for (Object[] _contratacion : _contrataciones) {
					EstDigContratacionConsultaDTO cont = new EstDigContratacionConsultaDTO();
					
					cont.setNroProceso(_contratacion[1] == null ? "" : String.valueOf(_contratacion[1]));
					cont.setCorrelativo(_contratacion[2] == null ? "" : String.valueOf(_contratacion[2]));
					cont.setRuc(_contratacion[3] == null ? "" : String.valueOf(_contratacion[3]));
					//cont.setFechaDesde(_contratacion[4] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_contratacion[4].toString().substring(0, 19)));
					cont.setFechaDesde(_contratacion[4] == null ? "" : String.valueOf(_contratacion[4]));
					cont.setDigitalizado(_contratacion[5] == null ? "" : String.valueOf(_contratacion[5]));

					contrataciones.add(cont);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(contrataciones);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarComprobantesPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	@Override
	public Respuesta<ByteArrayInputStream> consultarEstDigContratacionExcel( 
			FiltroConsultaEstDigContratacion filtro, String usuarioCreador, String usuario, Locale locale) {
		LOGGER.info("[INICIO] consultarEstDigContratacionExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			Respuesta<EstDigContratacionConsultaDTO> consulta = consultarEstDigContratacion(filtro,locale); 
			//LOGGER.info("[INICIO] consultarEstDigContratacionExcel -consulta.estado" + consulta.estado);
			IReport<ByteArrayInputStream> report;
			if(consulta.estado) {
				report = new ReportExcelEstDigContratacion(consulta.datos, usuarioCreador);
			} else {
				report = new ReportExcelEstDigContratacion(new ArrayList<>(), usuarioCreador);
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		LOGGER.info("[FIN] consultarCorrespondenciasExcelEventoDocumento " + filtro.toString());
		return respuesta;
	}
	
	
	@Override
	public Respuesta<DespachoConsultaDTO> consultaDespachoPaginado(String usuario, FiltroConsultaDespacho filtro, int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale) {
		LOGGER.info("[INICIO] consultarEstDigContratacionesPaginado " + filtro.toString());
		Respuesta<DespachoConsultaDTO> respuesta = new Respuesta<>();
		try {
			
			
			String nroCorrelativo = filtro.getNroCorrelativo();
			String codEstado=filtro.getCodEstado();
			String fechaDesde=filtro.getFechaDesde();
			String fechaHasta=filtro.getFechaHasta();
			String dependenciaRemitente=filtro.getDependenciaRemitente();
			String usuarioRemitente=filtro.getUsuarioRemitente();
			String numeroDocumento=filtro.getNumeroDocumento();
			String entidadExterna=filtro.getEntidadExterna();
			String asunto=filtro.getAsunto();
			String guiaRemision=filtro.getGuiaRemision();
			
			
			LOGGER.info("nroCorrelativo: " + nroCorrelativo + "||" + "codEstado: " + codEstado + "||" + "fechaDesde: " + 
			          fechaDesde + "||" + "fechaHasta: " + fechaHasta + "||" + "dependenciaRemitente: " + dependenciaRemitente + "||"
					  + "usuarioRemitente: " + usuarioRemitente + "||" 
			          + "numeroDocumento: " + numeroDocumento + "||" 
			          + "entidadExterna: " + entidadExterna + "||" 
			          + "asunto: " + asunto + "||" 
			          + "guiaRemision: " + guiaRemision + "||" 
					  + "itemsPorPagina: " + itemsPorPagina + "||" 
			          + "numeroPagina: " + numeroPagina + "||" + "exportaExcel: "
					  + exportaExcel + "||" + "nombreColumna: " + nombreColumna + "||" + "desc: " + desc);

			List<Object[]> _despachos = filenetDAO.consultar_despacho_paginado(usuario, nroCorrelativo,codEstado,fechaDesde,fechaHasta,dependenciaRemitente,usuarioRemitente,numeroDocumento,entidadExterna,asunto,guiaRemision,itemsPorPagina, numeroPagina, exportaExcel, nombreColumna, desc);

			if (_despachos != null) {

				Object[] total = _despachos.get(0);
				_despachos.remove(0);

				List<DespachoConsultaDTO> despachos = new ArrayList<>();

				for (Object[] _despacho : _despachos) {
					DespachoConsultaDTO cont = new DespachoConsultaDTO();
					
					cont.setCorrelativo(_despacho[1] == null ? "" : String.valueOf(_despacho[1]));
					cont.setFechaCreacion(_despacho[2] == null ? "" : String.valueOf(_despacho[2]));
					cont.setAsunto(_despacho[3] == null ? "" : String.valueOf(_despacho[3]));
					//cont.setFechaDesde(_contratacion[4] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_contratacion[4].toString().substring(0, 19)));
					cont.setNroDocInterno(_despacho[4] == null ? "" : String.valueOf(_despacho[4]));
					cont.setDestino(_despacho[5] == null ? "" : String.valueOf(_despacho[5]));
					cont.setOrigen(_despacho[6] == null ? "" : String.valueOf(_despacho[6]));
					cont.setEstado(_despacho[7] == null ? "" : String.valueOf(_despacho[7]));
					cont.setDesEmision(_despacho[8] == null ? "" : String.valueOf(_despacho[8]));
					despachos.add(cont);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(despachos);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarComprobantesPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	
	@Override
	public Respuesta<ValijasRecibidasDTO> consultaValijasRecibidasPaginado(String usuario, FiltroConsultaValijasRecibidas filtro, int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale) {
		LOGGER.info("[INICIO] consultarEstDigContratacionesPaginado " + filtro.toString());
		Respuesta<ValijasRecibidasDTO> respuesta = new Respuesta<>();
		try {
			
			
			String nroCorrelativo = filtro.getNroCorrelativo();
			String codEstado=filtro.getCodEstado();
			String fechaDesde=filtro.getFechaDesde();
			String fechaHasta=filtro.getFechaHasta();
			String cgcReceptor=filtro.getCgcReceptor();
			String cgcRemitente=filtro.getCgcRemitente();
			String courier=filtro.getCourier();
			
			
			LOGGER.info("nroCorrelativo: " + nroCorrelativo + "||" + "codEstado: " + codEstado + "||" + "fechaDesde: " + 
			          fechaDesde + "||" + "fechaHasta: " + fechaHasta + "||" + "cgcReceptor: " + cgcReceptor + "||"
					  + "cgcRemitente: " + cgcRemitente + "||" 
			          + "courier: " + courier + "||" 
			          + "itemsPorPagina: " + itemsPorPagina + "||" 
			          + "numeroPagina: " + numeroPagina + "||" + "exportaExcel: "
					  + exportaExcel + "||" + "nombreColumna: " + nombreColumna + "||" + "desc: " + desc);

			List<Object[]> _valijasRecibidas = filenetDAO.consultar_valijas_recibidas_paginado(usuario, nroCorrelativo,codEstado,
					                    fechaDesde,fechaHasta,cgcReceptor,cgcRemitente,courier,
					                    itemsPorPagina, numeroPagina, exportaExcel, 
					                    nombreColumna, desc);

			if (_valijasRecibidas != null) {

				Object[] total = _valijasRecibidas.get(0);
				_valijasRecibidas.remove(0);

				List<ValijasRecibidasDTO> valijasRecibidas = new ArrayList<>();

				for (Object[] _valijaRecibida : _valijasRecibidas) {
					ValijasRecibidasDTO cont = new ValijasRecibidasDTO();
					
					cont.setCorrelativo(_valijaRecibida[1] == null ? "" : String.valueOf(_valijaRecibida[1]));
					cont.setFecha(_valijaRecibida[2] == null ? "" : String.valueOf(_valijaRecibida[2]));
					cont.setCgcRecibe(_valijaRecibida[3] == null ? "" : String.valueOf(_valijaRecibida[3]));
					//cont.setFechaDesde(_contratacion[4] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_contratacion[4].toString().substring(0, 19)));
					cont.setCgcRemitente(_valijaRecibida[4] == null ? "" : String.valueOf(_valijaRecibida[4]));
					cont.setCourier(_valijaRecibida[5] == null ? "" : String.valueOf(_valijaRecibida[5]));
					cont.setGuia(_valijaRecibida[6] == null ? "" : String.valueOf(_valijaRecibida[6]));
					cont.setCantidad(_valijaRecibida[7] == null ? "" : String.valueOf(_valijaRecibida[7]));
					cont.setEstado(_valijaRecibida[8] == null ? "" : String.valueOf(_valijaRecibida[8]));
					
					valijasRecibidas.add(cont);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(valijasRecibidas);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarComprobantesPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	/*FIN Ticket 9000004412*/
	

}
