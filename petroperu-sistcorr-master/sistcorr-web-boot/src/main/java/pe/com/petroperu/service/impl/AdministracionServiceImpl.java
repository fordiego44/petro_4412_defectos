package pe.com.petroperu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.filenet.dao.AdministracionDAO;
import pe.com.petroperu.filenet.model.administracion.CgcCorrespondencia;
import pe.com.petroperu.filenet.model.administracion.CgcLugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.Ciudad;
import pe.com.petroperu.filenet.model.administracion.Courier;
import pe.com.petroperu.filenet.model.administracion.CourierLugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.Departamentos;
import pe.com.petroperu.filenet.model.administracion.DependenciaExterna;
import pe.com.petroperu.filenet.model.administracion.Distrito;
import pe.com.petroperu.filenet.model.administracion.Estado;
import pe.com.petroperu.filenet.model.administracion.FormaEnvio;
import pe.com.petroperu.filenet.model.administracion.GestorDependencia;
import pe.com.petroperu.filenet.model.administracion.Jerarquia;
import pe.com.petroperu.filenet.model.administracion.LugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.LugarTrabajoRequest;
import pe.com.petroperu.filenet.model.administracion.Moneda;
import pe.com.petroperu.filenet.model.administracion.Motivo;
import pe.com.petroperu.filenet.model.administracion.Numeradores;
import pe.com.petroperu.filenet.model.administracion.Pais;
import pe.com.petroperu.filenet.model.administracion.Provincia;
import pe.com.petroperu.filenet.model.administracion.ProvinciaLugarTrabajo;
import pe.com.petroperu.filenet.model.administracion.TipoAccion;
import pe.com.petroperu.filenet.model.administracion.TipoComprobante;
import pe.com.petroperu.filenet.model.administracion.TipoCorrespondencia;
import pe.com.petroperu.filenet.model.administracion.TipoUnidadMatricial;
import pe.com.petroperu.filenet.model.administracion.TransaccionesCgc;
import pe.com.petroperu.filenet.model.administracion.UsuarioCgc;
import pe.com.petroperu.service.IAdministracionService;

@Service
@PropertySource({ "classpath:application.properties" })
public class AdministracionServiceImpl implements IAdministracionService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private AdministracionDAO administracionDAO;

	@Autowired
	private MessageSource messageSource;
	
	//TICKET 9000004275
 	@Value("${sistcorr.paginado.conductor}")
 	private String tamanioBandConfConductor;

	public Respuesta<Departamentos> consultarDepartamentosGeograficos(String nombre, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarAsignaciones");
		Respuesta<Departamentos> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarDepartamentosGeograficos(nombre,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("Departamentos obtenidas:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("Departamento obtenidas:" + listObjects.size());
				List<Departamentos> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Departamentos obj = new Departamentos();
					obj.setCodigoDepartamento(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setDepartamento(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudDepartamento(Departamentos departameto, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudDepartamento(departameto, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	public Respuesta<Pais> consultarPaises(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarPaises");
		Respuesta<Pais> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarPaises(nombre, itemsPorPagina, numeroPagina,
					columnaOrden, orden, exportarExcel);
			LOGGER.info("Paises obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("Paises obtenidos:" + listObjects.size());
				List<Pais> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Pais obj = new Pais();
					obj.setCodigoPais(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombrePais(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudPaises(Pais pais, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudPaises(pais, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Jerarquia --------------
	public Respuesta<Jerarquia> consultarJerarquias(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarJerarquias");
		Respuesta<Jerarquia> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarJerarquias(nombre, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("Jerarquia obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("Jerarquia obtenidos:" + listObjects.size());
				List<Jerarquia> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Jerarquia obj = new Jerarquia();
					obj.setCodigoJerarquia(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreJerarquia(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudJerarquias(Jerarquia jerarquia, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudJerarquias(jerarquia, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Courier --------------
	public Respuesta<Courier> consultarCouriers(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarCouriers");
		Respuesta<Courier> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarCouriers(nombre, itemsPorPagina, numeroPagina,
					columnaOrden, orden, exportarExcel);
			LOGGER.info("Couriers obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("Couriers obtenidos:" + listObjects.size());
				List<Courier> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Courier obj = new Courier();
					obj.setCodigoCourier(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreCourier(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudCouriers(Courier courier, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudCouriers(courier, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Tipo comprobante --------------
	public Respuesta<TipoComprobante> consultarTipoComprobate(String nombre, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarTipoComprobate");
		Respuesta<TipoComprobante> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarTipoComprobate(nombre, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("TipoComprobante obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("TipoComprobante obtenidos:" + listObjects.size());
				List<TipoComprobante> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					TipoComprobante obj = new TipoComprobante();
					obj.setCodigoComprobante(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreComprobante(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudTipoComprobante(TipoComprobante tipocomprobante, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudTipoComprobante(tipocomprobante, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Tipo Moneda --------------
	public Respuesta<Moneda> consultarMoneda(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarTipoComprobate");
		Respuesta<Moneda> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarMoneda(nombre, itemsPorPagina, numeroPagina,
					columnaOrden, orden, exportarExcel);
			LOGGER.info("TipoComprobante obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("TipoComprobante obtenidos:" + listObjects.size());
				List<Moneda> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Moneda obj = new Moneda();
					obj.setCodigoMoneda(obja[1] != null ? obja[1].toString() : "");
					obj.setDescripcion(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudMonedas(Moneda moneda, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudMonedas(moneda, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Formas de Envio --------------
	public Respuesta<FormaEnvio> consultarFormaEnvio(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<FormaEnvio> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarFormaEnvio(nombre, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar forma de envio obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar formas de envio obtenidos:" + listObjects.size());
				List<FormaEnvio> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					FormaEnvio obj = new FormaEnvio();
					obj.setCodigoFormaEnvio(obja[1] != null ? obja[1].toString() : "");
					obj.setDescripcion(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudFormaEnvio(FormaEnvio moneda, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudFormaEnvio(moneda, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Numeradores --------------
	public Respuesta<Numeradores> consultarNumeradores(Integer codigo, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<Numeradores> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarNumeradores(codigo, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar forma de envio obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar formas de envio obtenidos:" + listObjects.size());
				List<Numeradores> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Numeradores obj = new Numeradores();
					obj.setCodigoNumerador(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setUltimoCorrelativo(obja[2] != null ? (Integer) obja[2] : 0);
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudNumeradores(Numeradores numerador, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudNumeradores(numerador, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Estado --------------
	public Respuesta<Estado> consultarEtados(String estado, String codProceso, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<Estado> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarEstados(estado, codProceso, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar forma de envio obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar formas de envio obtenidos:" + listObjects.size());
				List<Estado> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Estado obj = new Estado();
					obj.setCodigoEstado(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setEstado(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoProceso(obja[3] != null ? obja[3].toString() : "");
					obj.setId(obja[4] != null ? (Integer) obja[4] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudEstados(Estado estado, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudEstados(estado, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Motivo --------------
	public Respuesta<Motivo> consultarMotivos(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarMotivos");
		Respuesta<Motivo> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarMotivos(nombre, itemsPorPagina, numeroPagina,
					columnaOrden, orden, exportarExcel);
			LOGGER.info("Motivos obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("Motivos obtenidos:" + listObjects.size());
				List<Motivo> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Motivo obj = new Motivo();
					obj.setCodigoMotivo(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreMotivo(obja[2] != null ? obja[2].toString() : "");
					obj.setId(obja[3] != null ? (Integer) obja[3] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudMotivos(Motivo motivo, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudMotivos(motivo, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Consultar tipo unidad Matricial --------------
	public Respuesta<TipoUnidadMatricial> consultarTipoUnidadMatricial(String nombre, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarMotivos");
		Respuesta<TipoUnidadMatricial> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarTipoUnidadMatricial(nombre, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("Motivos obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("Motivos obtenidos:" + listObjects.size());
				List<TipoUnidadMatricial> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					TipoUnidadMatricial obj = new TipoUnidadMatricial();
					obj.setNombre(obja[1] != null ? obja[1].toString() : "");
					obj.setId(obja[2] != null ? (Integer) obja[2] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudTipoUnidadMatricial(TipoUnidadMatricial tipoUM, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudTipoUnidadMatricial(tipoUM, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Provincia --------------

	public Respuesta<Provincia> consultarProvincia(Integer codDepartamento, String provincia, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<Provincia> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarProvincia(codDepartamento, provincia,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar Provincia obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar Provincia obtenidos:" + listObjects.size());
				List<Provincia> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Provincia obj = new Provincia();
					obj.setCodigoDepartamento(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreDepartamento(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoProvincia(obja[3] != null ? (Integer) obja[3] : 0);
					obj.setNombreProvincia(obja[4] != null ? obja[4].toString() : "");
					obj.setId(obja[5] != null ? (Integer) obja[5] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudProvincias(Provincia provincia, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudProvincia(provincia, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Ciudad --------------

	public Respuesta<Ciudad> consultarCiudad(Integer codPais, String nomCiudad, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<Ciudad> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarCiudad(codPais, nomCiudad, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar Ciudad obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar Ciudad obtenidos:" + listObjects.size());
				List<Ciudad> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Ciudad obj = new Ciudad();
					obj.setCodigoCiudad(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreCiudad(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoPais(obja[3] != null ? (Integer) obja[3] : 0);
					obj.setNombrePais(obja[4] != null ? obja[4].toString() : "");
					obj.setId(obja[5] != null ? (Integer) obja[5] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudCiudad(Ciudad ciudad, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudCiudad(ciudad, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- CGC --------------

	public Respuesta<CgcCorrespondencia> consultarCGCorrespondencia(String codLugar, String nomCGC,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale) {

		Respuesta<CgcCorrespondencia> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarCGCorrespondencia(codLugar, nomCGC,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar Ciudad obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar Ciudad obtenidos:" + listObjects.size());
				List<CgcCorrespondencia> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					CgcCorrespondencia obj = new CgcCorrespondencia();
					obj.setCodigoCGC(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreCGC(obja[2] != null ? obja[2].toString() : "");
					obj.setTipoRotulo(obja[3] != null ? obja[3].toString() : "");
					obj.setmCodigoBarras(obja[4] != null ? obja[4].toString() : "");

					obj.setImpresora(obja[5] != null ? obja[5].toString() : "");
					obj.setTipoImpresora(obja[6] != null ? obja[6].toString() : "");
					obj.setCodigoLugar(obja[7] != null ? obja[7].toString() : "");
					obj.setNombreLugar(obja[8] != null ? obja[8].toString() : "");
					obj.setmComputarizado(obja[9] != null ? obja[9].toString() : "");
					obj.setCodigoERP(obja[10] != null ? obja[10].toString() : "");

					obj.setId(obja[11] != null ? (Integer) obja[11] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudCGCorrespondencia(CgcCorrespondencia objCGC, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudCGCorrespondencia(objCGC, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Distrito --------------

	public Respuesta<Distrito> consultarDistritos(Integer codDepa, Integer codProv, String nomDistrito,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale) {

		Respuesta<Distrito> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarDistritos(codDepa, codProv, nomDistrito,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar Distrito obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar Distrito obtenidos:" + listObjects.size());
				List<Distrito> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					Distrito obj = new Distrito();
					obj.setCodigoDepartamento(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreDepartamento(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoProvincia(obja[3] != null ? (Integer) obja[3] : 0);
					obj.setNombreProvincia(obja[4] != null ? obja[4].toString() : "");
					obj.setCodigoDistrito(obja[5] != null ? (Integer) obja[5] : 0);
					obj.setNombreDistrito(obja[6] != null ? obja[6].toString() : "");
					obj.setId(obja[7] != null ? (Integer) obja[7] : 0);
					obj.setCodigoPostal(obja[8] != null ? (String) obja[8] : "");
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudDistritos(Distrito distrito, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudDistritos(distrito, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Gestor Dependencia --------------

	public Respuesta<GestorDependencia> consultarGestorDependencia(String nomDependencia, String nomGestor,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale) {

		Respuesta<GestorDependencia> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarGestorDependencia(nomDependencia, nomGestor,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar Gestordependencia obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar Gestordependencia obtenidos:" + listObjects.size());
				List<GestorDependencia> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					GestorDependencia obj = new GestorDependencia();
					obj.setRegistro(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreGestor(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoDependencia(obja[3] != null ? (Integer) obja[3] : 0);
					obj.setNombreDependencia(obja[4] != null ? obja[4].toString() : "");
					obj.setId(obja[5] != null ? (Integer) obja[5] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudGestorDependencia(GestorDependencia gestor, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudGestorDependencia(gestor, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Usuario CGC --------------

	public Respuesta<UsuarioCgc> consultarUsuarioCGC(String codCGC, String nomGestor, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<UsuarioCgc> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarUsuarioCGC(codCGC, nomGestor, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar UsuarioCGC obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar UsuarioCGC obtenidos:" + listObjects.size());
				List<UsuarioCgc> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					UsuarioCgc obj = new UsuarioCgc();
					obj.setCodigoCGC(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreCGC(obja[2] != null ? obja[2].toString() : "");
					obj.setRegistro(obja[3] != null ? obja[3].toString() : "");
					obj.setNombreGestor(obja[4] != null ? obja[4].toString() : "");
					obj.setId(obja[5] != null ? (Integer) obja[5] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudUsuarioCGC(UsuarioCgc usrCGC, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudUsuarioCGC(usrCGC, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Lugares de Trabajos --------------

	public Respuesta<LugarTrabajo> consultarLugaresDeTrabajo(LugarTrabajoRequest request, String exportarExcel,
			Locale locale) {

		Respuesta<LugarTrabajo> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarLugaresDeTrabajo(request, exportarExcel);
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				List<LugarTrabajo> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					LugarTrabajo obj = new LugarTrabajo();
					obj.setCodigoDepartamento(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreDepartamento(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoProvincia(obja[3] != null ? (Integer) obja[3] : 0);
					obj.setNombreProvincia(obja[4] != null ? obja[4].toString() : "");
					obj.setCodigoDistrito(obja[5] != null ? (Integer) obja[5] : 0);
					obj.setNombreDistrito(obja[6] != null ? obja[6].toString() : "");

					obj.setNombreLugar(obja[7] != null ? obja[7].toString() : "");
					obj.setCodigoLugar(obja[8] != null ? obja[8].toString() : "");
					obj.setId(obja[9] != null ? (Integer) obja[9] : 0);
					obj.setDireccionLugar(obja[10] != null ? obja[10].toString() : "");
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudLugaresDeTrabajo(LugarTrabajo lugartrabajo, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudLugaresDeTrabajo(lugartrabajo, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- consultar Courier Lugar Trabajo --------------

	public Respuesta<CourierLugarTrabajo> consultarCourierLugarTrabajo(String codCGC, Integer codCourier,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale) {

		Respuesta<CourierLugarTrabajo> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarCourierLugarTrabajo(codCGC, codCourier,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar UsuarioCGC obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar UsuarioCGC obtenidos:" + listObjects.size());
				List<CourierLugarTrabajo> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					CourierLugarTrabajo obj = new CourierLugarTrabajo();
					obj.setCodigoCGC(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreCGC(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoCourier(obja[3] != null ? (Integer) obja[3] : 0);
					obj.setNombreCourier(obja[4] != null ? obja[4].toString() : "");
					obj.setAlcance(obja[5] != null ? obja[5].toString() : "");
					obj.setId(obja[6] != null ? (Integer) obja[6] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudCourierLugarTrabajo(CourierLugarTrabajo objCourier, String usuario,
			String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudCourierLugarTrabajo(objCourier, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Consultar Provincia Lugar Trabajo --------------

	public Respuesta<ProvinciaLugarTrabajo> consultarProvinciaLugarTrabajo(Integer codDepa, Integer codProv,
			String codLugar, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden,
			String exportarExcel, Locale locale) {

		Respuesta<ProvinciaLugarTrabajo> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarProvinciaLugarTrabajo(codDepa, codProv,
					codLugar, itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar UsuarioCGC obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar UsuarioCGC obtenidos:" + listObjects.size());
				List<ProvinciaLugarTrabajo> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					ProvinciaLugarTrabajo obj = new ProvinciaLugarTrabajo();
					obj.setCodigoLugar(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreLugar(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoProvincia(obja[3] != null ? (Integer) obja[3] : 0);
					obj.setNombreProvincia(obja[4] != null ? obja[4].toString() : "");
					obj.setCodigoDepartamento(obja[5] != null ? (Integer) obja[5] : 0);
					obj.setNombreDepartamento(obja[6] != null ? obja[6].toString() : "");
					obj.setId(obja[7] != null ? (Integer) obja[7] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudProvinciaLugarTrabajo(ProvinciaLugarTrabajo objProvLugar, String usuario,
			String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudProvinciaLugarTrabajo(objProvLugar, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Dependencia Externa

	public Respuesta<DependenciaExterna> consultarDependenciaExterna(String nombre, String ruc, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<DependenciaExterna> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarDependenciaExterna(nombre, ruc, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);
			LOGGER.info("conultar UsuarioCGC obtenidos:" + listObjects.size());
			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);
				LOGGER.info("consultar UsuarioCGC obtenidos:" + listObjects.size());
				List<DependenciaExterna> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					DependenciaExterna obj = new DependenciaExterna();
					obj.setNombreDependencia(obja[1] != null ? obja[1].toString() : "");
					obj.setDireccion(obja[2] != null ? obja[2].toString() : "");
					obj.setNombreDepartamento(obja[3] != null ? obja[3].toString() : "");
					obj.setNombreProvincia(obja[4] != null ? obja[4].toString() : "");
					obj.setNombreDistrito(obja[5] != null ? obja[5].toString() : "");
					obj.setNombrePais(obja[6] != null ? obja[6].toString() : "");
					obj.setNombreCiudad(obja[7] != null ? obja[7].toString() : "");
					obj.setRuc(obja[8] != null ? obja[8].toString() : "");
					obj.setEmail(obja[9] != null ? obja[9].toString() : "");
					obj.setId(obja[10] != null ? (Integer) obja[10] : 0);
					obj.setCodigoDepartamento(obja[11] != null ? (Integer) obja[11] : 0);
					obj.setCodigoProvincia(obja[12] != null ? (Integer) obja[12] : 0);
					obj.setCodigoDistrito(obja[13] != null ? (Integer) obja[13] : 0);
					obj.setCodigoCiudad(obja[14] != null ? (Integer) obja[14] : 0);
					obj.setCodigoPais(obja[15] != null ? (Integer) obja[15] : 0);
					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudDependenciaExterna(DependenciaExterna dependencia, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudDependenciaExterna(dependencia, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Tipo de Acción

	public Respuesta<TipoAccion> consultarTipoAccion(String nomAccion, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<TipoAccion> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarTipoAccion(nomAccion, itemsPorPagina,
					numeroPagina, columnaOrden, orden, exportarExcel);

			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);

				List<TipoAccion> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					TipoAccion obj = new TipoAccion();
					obj.setCodigoAccion(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreAccion(obja[2] != null ? obja[2].toString() : "");
					obj.setmTextoReq(obja[3] != null ? obja[3].toString() : "");
					obj.setmMultipli(obja[4] != null ? obja[4].toString() : "");
					obj.setmReqTextoRta(obja[5] != null ? obja[5].toString() : "");
					obj.setmEnviaMailRta(obja[6] != null ? obja[6].toString() : "");
					obj.setProcesos(obja[7] != null ? obja[7].toString() : "");
					obj.setPrioridad(obja[8] != null ? (Integer) obja[8] : 0);
					obj.setId(obja[9] != null ? (Integer) obja[9] : 0);

					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudTipoAccion(TipoAccion tipoAccion, String usuario, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudTipoAccion(tipoAccion, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Tipos de Correspondencias

	public Respuesta<TipoCorrespondencia> consultarTipoCorrespondencia(String nomTipoCorr, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<TipoCorrespondencia> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarTipoCorrespondencia(nomTipoCorr,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);

			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);

				List<TipoCorrespondencia> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					TipoCorrespondencia obj = new TipoCorrespondencia();
					obj.setCodigoTipoCorr(obja[1] != null ? (Integer) obja[1] : 0);
					obj.setNombreTipoCorr(obja[2] != null ? obja[2].toString() : "");
					obj.setmAplicaEnvInterna(obja[3] != null ? obja[3].toString() : "");
					obj.setmAplicaEnvExterna(obja[4] != null ? obja[4].toString() : "");
					obj.setmAplicaRecInterna(obja[5] != null ? obja[5].toString() : "");
					obj.setmAplicaRecExterna(obja[6] != null ? obja[6].toString() : "");
					obj.setmRequiereFecha(obja[7] != null ? obja[7].toString() : "");
					obj.setmFinalizaAceptar(obja[8] != null ? obja[8].toString() : "");
					obj.setmManualCorresp(obja[9] != null ? obja[9].toString() : "");
					obj.setSecuencia(obja[10] != null ? (Integer) obja[10] : 0);

					obj.setmMultiple(obja[11] != null ? obja[11].toString() : "");
					obj.setReqCopia(obja[12] != null ? obja[12].toString() : "");
					obj.setReqDest(obja[13] != null ? obja[13].toString() : "");

					obj.setId(obja[14] != null ? (Integer) obja[14] : 0);

					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudTipoCorrespondencia(TipoCorrespondencia tipoCorre, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudTipoCorrespondencia(tipoCorre, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- Transacciones por CGC

	public Respuesta<TransaccionesCgc> consultarTransaccionesCGC(String tipoTrans, String cgcOrigin, String cgcDestino,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale) {

		Respuesta<TransaccionesCgc> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarTransaccionesCGC(tipoTrans, cgcOrigin,
					cgcDestino, itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);

			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);

				List<TransaccionesCgc> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					TransaccionesCgc obj = new TransaccionesCgc();
					obj.setTipoTransaccion(obja[1] != null ? obja[1].toString() : "");
					obj.setCgcOrigen(obja[2] != null ? obja[2].toString() : "");
					obj.setCgcDestino(obja[3] != null ? obja[3].toString() : "");
					obj.setCodigoNumerador(obja[4] != null ? (Integer) obja[4] : 0);

					obj.setId(obja[5] != null ? (Integer) obja[5] : 0);

					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudTransaccionesCGC(TransaccionesCgc objTrans, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudTransaccionesCGC(objTrans, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

	// ---------------------- CGC Lugares de Trabajo

	public Respuesta<CgcLugarTrabajo> consultarCgcLugarTrabajo(String codCgc, String codLugar, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {

		Respuesta<CgcLugarTrabajo> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjects = this.administracionDAO.consultarCgcLugarTrabajo(codCgc, codLugar,
					itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);

			if (listObjects != null) {
				Object[] total = listObjects.get(0);
				listObjects.remove(0);

				List<CgcLugarTrabajo> listObjRp = new ArrayList<>();
				for (Object[] obja : listObjects) {
					CgcLugarTrabajo obj = new CgcLugarTrabajo();
					obj.setCodigoCgc(obja[1] != null ? obja[1].toString() : "");
					obj.setNombreCgc(obja[2] != null ? obja[2].toString() : "");
					obj.setCodigoLugar(obja[3] != null ? obja[3].toString() : "");
					obj.setNombreLugar(obja[4] != null ? obja[4].toString() : "");

					obj.setId(obja[5] != null ? (Integer) obja[5] : 0);

					listObjRp.add(obj);
				}
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjRp);
				respuesta.total = Integer.valueOf(String.valueOf(total[0]));
			}

			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.administracion.configuracion.consulta.Exito",
						null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			LOGGER.error("[ERROR]", e);
		}
		return respuesta;
	}

	public Respuesta<RespuestaApi> crudCgcLugarTrabajo(CgcLugarTrabajo objCgcLugar, String usuario, String accion,
			Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			String msj = this.administracionDAO.crudCgcLugarTrabajo(objCgcLugar, usuario, accion);
			if (msj != null && msj.equalsIgnoreCase("")) {
				respuesta.estado = true;
				respuesta.mensaje = "";
			} else {
				respuesta.estado = false;
				respuesta.mensaje = (msj != null ? msj : "");
			}

			if (respuesta.estado)
				respuesta.mensaje = this.messageSource
						.getMessage("sistcorr.administracion.configuracion.transaccion.Exito", null, locale);
		} catch (Exception e) {
			this.LOGGER.info("[ERROR]" + e.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}

}
