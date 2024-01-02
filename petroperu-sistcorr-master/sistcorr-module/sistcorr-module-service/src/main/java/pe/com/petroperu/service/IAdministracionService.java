package pe.com.petroperu.service;

import java.util.Locale;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.RespuestaApi;
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

public interface IAdministracionService {

	Respuesta<Departamentos> consultarDepartamentosGeograficos(String nombre, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudDepartamento(Departamentos departameto, String usuario, String accion, Locale locale);

	Respuesta<Pais> consultarPaises(String nombre, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden,
			String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudPaises(Pais pais, String usuario, String accion, Locale locale);

	Respuesta<Jerarquia> consultarJerarquias(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudJerarquias(Jerarquia jerarquia, String usuario, String accion, Locale locale);

	Respuesta<Courier> consultarCouriers(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudCouriers(Courier jerarquia, String usuario, String accion, Locale locale);

	Respuesta<TipoComprobante> consultarTipoComprobate(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudTipoComprobante(TipoComprobante tipoComprobante, String usuario, String accion,
			Locale locale);

	Respuesta<Moneda> consultarMoneda(String nombre, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden,
			String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudMonedas(Moneda moneda, String usuario, String accion, Locale locale);

	Respuesta<FormaEnvio> consultarFormaEnvio(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudFormaEnvio(FormaEnvio formaEnvio, String usuario, String accion, Locale locale);

	Respuesta<Numeradores> consultarNumeradores(Integer codigo, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudNumeradores(Numeradores numerador, String usuario, String accion, Locale locale);

	Respuesta<Estado> consultarEtados(String estado, String codProceso, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudEstados(Estado estado, String usuario, String accion, Locale locale);

	Respuesta<Motivo> consultarMotivos(String nombre, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden,
			String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudMotivos(Motivo motivo, String usuario, String accion, Locale locale);

	Respuesta<TipoUnidadMatricial> consultarTipoUnidadMatricial(String nombre, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudTipoUnidadMatricial(TipoUnidadMatricial tipoUM, String usuario, String accion,
			Locale locale);

	Respuesta<Provincia> consultarProvincia(Integer codDepartamento, String nomProvincia, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudProvincias(Provincia provincia, String usuario, String accion, Locale locale);

	Respuesta<Ciudad> consultarCiudad(Integer codPais, String nomCiudad, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudCiudad(Ciudad ciudad, String usuario, String accion, Locale locale);

	Respuesta<CgcCorrespondencia> consultarCGCorrespondencia(String codLugar, String nomCGC, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudCGCorrespondencia(CgcCorrespondencia objCGC, String usuario, String accion,
			Locale locale);

	Respuesta<Distrito> consultarDistritos(Integer codDepa, Integer codProv, String nomDistrito, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudDistritos(Distrito distrito, String usuario, String accion, Locale locale);

	Respuesta<GestorDependencia> consultarGestorDependencia(String nomDependencia, String nomGestor,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale);

	Respuesta<RespuestaApi> crudGestorDependencia(GestorDependencia provincia, String usuario, String accion,
			Locale locale);

	Respuesta<UsuarioCgc> consultarUsuarioCGC(String codCGC, String nomGestor, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudUsuarioCGC(UsuarioCgc usrCGC, String usuario, String accion, Locale locale);

	Respuesta<LugarTrabajo> consultarLugaresDeTrabajo(LugarTrabajoRequest request, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudLugaresDeTrabajo(LugarTrabajo lugarTrabajo, String usuario, String accion,
			Locale locale);

	Respuesta<CourierLugarTrabajo> consultarCourierLugarTrabajo(String codCGC, Integer codCourier,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale);

	Respuesta<RespuestaApi> crudCourierLugarTrabajo(CourierLugarTrabajo objCourier, String usuario, String accion,
			Locale locale);

	Respuesta<ProvinciaLugarTrabajo> consultarProvinciaLugarTrabajo(Integer codDepa, Integer codProv, String codLugar,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale);

	Respuesta<RespuestaApi> crudProvinciaLugarTrabajo(ProvinciaLugarTrabajo objProvLugar, String usuario, String accion,
			Locale locale);

	Respuesta<DependenciaExterna> consultarDependenciaExterna(String nombre, String ruc, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudDependenciaExterna(DependenciaExterna dependencia, String usuario, String accion,
			Locale locale);

	Respuesta<TipoAccion> consultarTipoAccion(String nomAccion, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudTipoAccion(TipoAccion tipoAccion, String usuario, String accion, Locale locale);

	Respuesta<TipoCorrespondencia> consultarTipoCorrespondencia(String nomTipoCorr, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudTipoCorrespondencia(TipoCorrespondencia tipoCorre, String usuario, String accion,
			Locale locale);

	Respuesta<TransaccionesCgc> consultarTransaccionesCGC(String tipoTrans, String cgcOrigin, String cgcDestino,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel,
			Locale locale);

	Respuesta<RespuestaApi> crudTransaccionesCGC(TransaccionesCgc objTrans, String usuario, String accion,
			Locale locale);

	Respuesta<CgcLugarTrabajo> consultarCgcLugarTrabajo(String codCgc, String codLugar, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale);

	Respuesta<RespuestaApi> crudCgcLugarTrabajo(CgcLugarTrabajo objCgcLugar, String usuario, String accion,
			Locale locale);

}
