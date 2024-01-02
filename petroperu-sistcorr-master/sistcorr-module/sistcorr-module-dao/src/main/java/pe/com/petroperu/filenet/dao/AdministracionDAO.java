package pe.com.petroperu.filenet.dao;

import java.util.List;

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

public interface AdministracionDAO {

	public List<Object[]> consultarDepartamentosGeograficos(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudDepartamento(Departamentos departameto, String usuario, String accion) throws Exception;

	public List<Object[]> consultarPaises(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudPaises(Pais pais, String usuario, String accion) throws Exception;

	public List<Object[]> consultarJerarquias(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudJerarquias(Jerarquia jerarquia, String usuario, String accion) throws Exception;

	public List<Object[]> consultarCouriers(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudCouriers(Courier jerarquia, String usuario, String accion) throws Exception;

	public List<Object[]> consultarTipoComprobate(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudTipoComprobante(TipoComprobante comprobante, String usuario, String accion) throws Exception;

	public List<Object[]> consultarMoneda(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudMonedas(Moneda moneda, String usuario, String accion) throws Exception;

	public List<Object[]> consultarFormaEnvio(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudFormaEnvio(FormaEnvio moneda, String usuario, String accion) throws Exception;

	public List<Object[]> consultarNumeradores(Integer nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudNumeradores(Numeradores numerador, String usuario, String accion) throws Exception;

	public List<Object[]> consultarEstados(String estado, String codProceso, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudEstados(Estado estado, String usuario, String accion) throws Exception;

	public List<Object[]> consultarMotivos(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudMotivos(Motivo motivo, String usuario, String accion) throws Exception;

	public List<Object[]> consultarTipoUnidadMatricial(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudTipoUnidadMatricial(TipoUnidadMatricial motivo, String usuario, String accion) throws Exception;

	public List<Object[]> consultarProvincia(Integer codDepartamento, String provincia, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudProvincia(Provincia provincia, String usuario, String accion) throws Exception;

	public List<Object[]> consultarCiudad(Integer codPais, String nomCiudad, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudCiudad(Ciudad ciudad, String usuario, String accion) throws Exception;

	public List<Object[]> consultarCGCorrespondencia(String codLugar, String nomCGC, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudCGCorrespondencia(CgcCorrespondencia objCGC, String usuario, String accion) throws Exception;

	public List<Object[]> consultarDistritos(Integer codDepa, Integer codProv, String nomDistrito,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudDistritos(Distrito distrito, String usuario, String accion) throws Exception;

	public List<Object[]> consultarGestorDependencia(String nomDependencia, String nomGestor, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudGestorDependencia(GestorDependencia gestor, String usuario, String accion) throws Exception;

	public List<Object[]> consultarUsuarioCGC(String codCGC, String nomGestor, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudUsuarioCGC(UsuarioCgc usuCGC, String usuario, String accion) throws Exception;

	public List<Object[]> consultarLugaresDeTrabajo(LugarTrabajoRequest request, String exportarExcel);

	public String crudLugaresDeTrabajo(LugarTrabajo lugarTrabajo, String usuario, String accion) throws Exception;

	public List<Object[]> consultarCourierLugarTrabajo(String codCGC, Integer codCourier, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudCourierLugarTrabajo(CourierLugarTrabajo objCourier, String usuario, String accion) throws Exception;

	public List<Object[]> consultarProvinciaLugarTrabajo(Integer codDepa, Integer codProv, String codLugar,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudProvinciaLugarTrabajo(ProvinciaLugarTrabajo objProvLugar, String usuario, String accion) throws Exception;

	public List<Object[]> consultarDependenciaExterna(String nombre, String ruc, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudDependenciaExterna(DependenciaExterna dependencia, String usuario, String accion) throws Exception;

	public List<Object[]> consultarTipoAccion(String nomAccion, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudTipoAccion(TipoAccion tipoAccion, String usuario, String accion) throws Exception;

	public List<Object[]> consultarTipoCorrespondencia(String nomTipoCorr, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel);

	public String crudTipoCorrespondencia(TipoCorrespondencia tipoCorre, String usuario, String accion) throws Exception;

	public List<Object[]> consultarTransaccionesCGC(String tipoTrans, String cgcOrigin, String cgcDestino,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudTransaccionesCGC(TransaccionesCgc objTrans, String usuario, String accion) throws Exception;

	public List<Object[]> consultarCgcLugarTrabajo(String codCgc, String codLugar, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel);

	public String crudCgcLugarTrabajo(CgcLugarTrabajo objCgcLugar, String usuario, String accion) throws Exception;
}
