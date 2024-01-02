//9000004276
package pe.com.petroperu.service.impl;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Service;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.Utilitario;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.filenet.dao.IBandejaEntradaCorrespondenciaDAO;
import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.model.Observaciones;
import pe.com.petroperu.model.emision.dto.ComprobanteConsultaDTO;
import pe.com.petroperu.service.IComprobanteService;
import pe.com.petroperu.service.dto.FiltroConsultaComprobanteDTO;
import pe.com.petroperu.service.util.IReport;
import pe.com.petroperu.service.util.ReportExcelComprobantes;

@Service
@PropertySource({ "classpath:application.properties" })
public class ComprobanteServiceImpl implements IComprobanteService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ISistcorrCliente sistcorrCliente;

	@Autowired
	private IFilenetDAO filenetDAO;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("iBandejaEntradaCorrespondenciaDAOImpl")
	private IBandejaEntradaCorrespondenciaDAO bandejaEntradaCorrespondenciaDAO;

	@Override
	public Respuesta<ComprobanteConsultaDTO> consultarComprobantesPaginado(String usuario, FiltroConsultaComprobanteDTO filtro, int itemsPorPagina, int numeroPagina, String nombreColumna, String desc, String exportaExcel, Locale locale) {
		LOGGER.info("[INICIO] consultarComprobantesPaginado " + filtro.toString());
		Respuesta<ComprobanteConsultaDTO> respuesta = new Respuesta<>();
		try {

			String radicado = filtro.getCorrelativo();
			String fechaDesde = filtro.getFechaDesdeText();
			String fechaHasta = filtro.getFechaHastaText();
			String nroBatch = filtro.getNroBatch();
			String ruc = filtro.getRucProveedor();
			String nroComprobante = filtro.getNumComprobante();
			Integer codigoEstado = filtro.getEstado();
			Integer codDependencia = filtro.getCodDependencia();
			String razonSocial = filtro.getRazonSocial();

			LOGGER.info("usuario: " + usuario + "||" + "radicado: " + radicado + "||" + "fechaDesde: " + fechaDesde + "||" + "fechaHasta: " + fechaHasta + "||" + "nroBatch: " + nroBatch + "||" + "ruc: " + ruc + "||" + "nroComprobante: " + nroComprobante + "||" + "codigoEstado: " + codigoEstado + "||" + "codDependencia: " + codDependencia + "||" + "razonSocial: " + razonSocial + "||" + "itemsPorPagina: " + itemsPorPagina + "||" + "numeroPagina: " + numeroPagina + "||" + "exportaExcel: "
					+ exportaExcel + "||" + "nombreColumna: " + nombreColumna + "||" + "desc: " + desc);

			List<Object[]> _comprobantes = filenetDAO.consultar_comprobantes_paginado(usuario, radicado, fechaDesde, fechaHasta, nroBatch, ruc, nroComprobante, codigoEstado, codDependencia, razonSocial, itemsPorPagina, numeroPagina, exportaExcel, nombreColumna, desc);

			if (_comprobantes != null) {

				Object[] total = _comprobantes.get(0);
				_comprobantes.remove(0);

				List<ComprobanteConsultaDTO> comprobantes = new ArrayList<>();

				for (Object[] _comprobante : _comprobantes) {
					ComprobanteConsultaDTO comp = new ComprobanteConsultaDTO();

					comp.setFechaRecepcion(_comprobante[0] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_comprobante[0].toString().substring(0, 19)));
					comp.setCorrelativo(_comprobante[1] == null ? "" : String.valueOf(_comprobante[1]));
					comp.setNroBatch(_comprobante[2] == null ? "" : String.valueOf(_comprobante[2]));
					comp.setRuc(_comprobante[3] == null ? "" : String.valueOf(_comprobante[3]));
					comp.setRazonSocial(_comprobante[4] == null ? "" : String.valueOf(_comprobante[4]));
					comp.setDescComprobante(_comprobante[5] == null ? "" : String.valueOf(_comprobante[5]));
					comp.setNroComprobante(_comprobante[6] == null ? "" : String.valueOf(_comprobante[6]));
					comp.setFechaComprobante(_comprobante[7] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_comprobante[7].toString().substring(0, 19)));
					comp.setMoneda(_comprobante[8] == null ? "" : String.valueOf(_comprobante[8]));
					comp.setEstado(_comprobante[9] == null ? "" : String.valueOf(_comprobante[9]));
					comp.setDependencia(_comprobante[10] == null ? "" : String.valueOf(_comprobante[10]));

					comprobantes.add(comp);
				}
				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
				respuesta.datos.addAll(comprobantes);
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
	public Respuesta<ByteArrayInputStream> consultarComprobantesExcel(String usuario, FiltroConsultaComprobanteDTO filtro, Locale locale) {
		LOGGER.info("[INICIO] consultarComprobantesExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			Respuesta<ComprobanteConsultaDTO> consulta = consultarComprobantesPaginado(usuario, filtro, 0, 0, "", "", "SI", locale);

			IReport<ByteArrayInputStream> report;
			if (consulta.estado) {
				report = new ReportExcelComprobantes(consulta.datos, usuario);
			} else {
				report = new ReportExcelComprobantes(new ArrayList<>(), usuario);
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
	public Respuesta<ComprobanteConsultaDTO> consultarDatosComprobante(String usuario, String correlativo, Locale locale) {
		LOGGER.info("[INICIO] consultarDatosComprobante " + correlativo.toString());
		Respuesta<ComprobanteConsultaDTO> respuesta = new Respuesta<>();
		try {

			Object[] _comprobante = filenetDAO.consultar_detalle_comprobante(usuario, correlativo);

			if (_comprobante != null && _comprobante.length > 0) {
				ComprobanteConsultaDTO comp = new ComprobanteConsultaDTO();
				comp.setFechaRecepcion(_comprobante[0] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_comprobante[0].toString().substring(0, 19)));
				comp.setCorrelativo(_comprobante[1] == null ? "" : String.valueOf(_comprobante[1]));
				comp.setNroBatch(_comprobante[2] == null ? "" : String.valueOf(_comprobante[2]));
				comp.setRuc(_comprobante[3] == null ? "" : String.valueOf(_comprobante[3]));
				comp.setRazonSocial(_comprobante[4] == null ? "" : String.valueOf(_comprobante[4]));
				comp.setDescComprobante(_comprobante[5] == null ? "" : String.valueOf(_comprobante[5]));
				comp.setNroComprobante(_comprobante[6] == null ? "" : String.valueOf(_comprobante[6]));
				comp.setFechaComprobante(_comprobante[7] == null ? null : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).parse(_comprobante[7].toString().substring(0, 19)));
				comp.setMoneda(_comprobante[8] == null ? "" : String.valueOf(_comprobante[8]));
				comp.setEstado(_comprobante[9] == null ? "" : String.valueOf(_comprobante[9]));
				comp.setDependencia(_comprobante[10] == null ? "" : String.valueOf(_comprobante[10]));

				respuesta.estado = true;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.comprobante.exito", null, locale);
				respuesta.datos.add(comp);
			} else {
				respuesta.estado = false;
				respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.comprobante.error", null, locale);
			}

		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarDatosComprobante", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<InformacionDocumento> obtenerDocumentosAdjuntos(String token, String correlativo, Locale locale) {
		Respuesta<InformacionDocumento> respuesta = new Respuesta<>();
		String clase = "Proveedores";
		try {
			respuesta = this.sistcorrCliente.recuperarDocumentosGeneral(token, clase, correlativo, locale);
			if (respuesta.estado)
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.recuperarDocumentosAsjuntosExito", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;

	}

	@Override
	public Respuesta<Observaciones> obtenerListaObservaciones(String token, String correlativo, Locale locale) {
		Respuesta<Observaciones> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjs = this.bandejaEntradaCorrespondenciaDAO.recuperarListaObservaciones("TD_COM", correlativo, "");
			if (listObjs != null) {
				List<Observaciones> listSP = new ArrayList<>();
				for (Object[] obja : listObjs) {
					Observaciones obj = new Observaciones();
					obj.setFecha(obja[0] != null ? obja[0].toString() : "");
					obj.setNomApeUsuario(obja[1] != null ? obja[1].toString() : "");
					obj.setObservacion(obja[2] != null ? obja[2].toString() : "");
					listSP.add(obj);
				}

				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listSP);
			}
			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.recuperarListaObservacionesExito", null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	@Override
	public Respuesta<RespuestaApi> registrarObservacion(String idUsuario, String correlativo, String observacion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String proceso = "TD_COM";
			boolean respuestaSP = this.bandejaEntradaCorrespondenciaDAO.agregarObservacion(proceso, idUsuario, correlativo, observacion);
			if (respuestaSP) {
				respuesta.estado = respuestaSP;
				respuesta.mensaje = "200";
				RespuestaApi resapi = new RespuestaApi();
				resapi.setStatus(200);
				resapi.setUserMessage("");
				respuesta.datos.add(resapi);
			}
			if (respuesta.estado)
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.registrarObservacionExito", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
}
