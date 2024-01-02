package pe.com.petroperu.filenet.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

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

@Repository("AdministracionDAOImpl")
public class AdministracionDAOImpl implements AdministracionDAO {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("emFactoryBDFilenet")
	private EntityManagerFactory entityManagerBDFilenet;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarDepartamentosGeograficos(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_departamento");

			query.registerStoredProcedureParameter("Departamento", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Departamento", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarDepartamentosGeograficos", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudDepartamento(Departamentos departameto, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_departamento");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idDepartamento", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("departamento", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (departameto.getId() == null) ? 0 : departameto.getId();
			Integer codDepartamento = (departameto.getCodigoDepartamento() == null) ? 0
					: departameto.getCodigoDepartamento();
			String departamento = ((departameto.getDepartamento() == null) ? ""
					: departameto.getDepartamento().toUpperCase());

			stpr_obtInfCorresp.setParameter("idDepartamento", id);
			stpr_obtInfCorresp.setParameter("codigoDepartamento", codDepartamento);
			stpr_obtInfCorresp.setParameter("departamento", departamento);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			// stpr_obtInfCorresp.setParameter("IdPadre", ((idPadre != null &&
			// !idPadre.trim().equalsIgnoreCase(""))?(Integer.parseInt(idPadre)):(0)));
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");

		} catch (Exception e) {
			e.printStackTrace();
			//this.logger.error("[ERROR] cruedDepartamento", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarPaises(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_paises");

			query.registerStoredProcedureParameter("Pais", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Pais", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarPaises", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudPaises(Pais pais, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_paises");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idPais", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoPais", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("pais", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (pais.getId() == null) ? 0 : pais.getId();
			Integer codigo = (pais.getCodigoPais() == null) ? 0 : pais.getCodigoPais();
			String nombre = ((pais.getNombrePais() == null) ? "" : pais.getNombrePais().toUpperCase());

			stpr_obtInfCorresp.setParameter("idPais", id);
			stpr_obtInfCorresp.setParameter("codigoPais", codigo);
			stpr_obtInfCorresp.setParameter("pais", nombre);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");

		} catch (Exception e) {
			e.printStackTrace();
			//this.logger.error("[ERROR] crudPaises", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarJerarquias(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_jerarquia");

			query.registerStoredProcedureParameter("Jerarquia", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Jerarquia", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarDepartamentosGeograficos", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudJerarquias(Jerarquia jerarquia, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_jerarquias");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idJerarquia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoJerarquia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("jerarquia", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (jerarquia.getId() == null) ? 0 : jerarquia.getId();
			Integer codigo = (jerarquia.getCodigoJerarquia() == null) ? 0 : jerarquia.getCodigoJerarquia();
			String nombre = ((jerarquia.getNombreJerarquia() == null) ? ""
					: jerarquia.getNombreJerarquia().toUpperCase());

			stpr_obtInfCorresp.setParameter("idJerarquia", id);
			stpr_obtInfCorresp.setParameter("codigoJerarquia", codigo);
			stpr_obtInfCorresp.setParameter("jerarquia", nombre);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			e.printStackTrace();
			//this.logger.error("[ERROR] crudJerarquias", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarCouriers(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_couriers");

			query.registerStoredProcedureParameter("Courier", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Courier", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarCouriers", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudCouriers(Courier courier, String usuario, String accion)  throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_couriers");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idCourier", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCourier", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("courier", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (courier.getId() == null) ? 0 : courier.getId();
			Integer codigo = (courier.getCodigoCourier() == null) ? 0 : courier.getCodigoCourier();
			String nombre = ((courier.getNombreCourier() == null) ? "" : courier.getNombreCourier().toUpperCase());

			stpr_obtInfCorresp.setParameter("idCourier", id);
			stpr_obtInfCorresp.setParameter("codigoCourier", codigo);
			stpr_obtInfCorresp.setParameter("courier", nombre);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			e.printStackTrace();
			//this.logger.error("[ERROR] crudCouriers", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarTipoComprobate(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_tipo_comprobante");

			query.registerStoredProcedureParameter("Comprobante", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Comprobante", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarTipoComprobate", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudTipoComprobante(TipoComprobante comprobante, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_tipos_comprobantes");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idComprobante", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoComprobante", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("comprobante", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (comprobante.getId() == null) ? 0 : comprobante.getId();
			String codigo = ((comprobante.getCodigoComprobante() == null) ? ""
					: comprobante.getCodigoComprobante().toUpperCase());
			String descripcion = ((comprobante.getNombreComprobante() == null) ? ""
					: comprobante.getNombreComprobante().toUpperCase());

			stpr_obtInfCorresp.setParameter("idComprobante", id);
			stpr_obtInfCorresp.setParameter("codigoComprobante", codigo);
			stpr_obtInfCorresp.setParameter("comprobante", descripcion);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			e.printStackTrace();
			//this.logger.error("[ERROR] crudTipoComprobante", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarMoneda(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_moneda");

			query.registerStoredProcedureParameter("Moneda", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Moneda", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarMoneda", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudMonedas(Moneda moneda, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_monedas");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idMoneda", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoMoneda", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("moneda", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (moneda.getId() == null) ? 0 : moneda.getId();
			String codigo = ((moneda.getCodigoMoneda() == null) ? "" : moneda.getCodigoMoneda().toUpperCase());
			String descripcion = ((moneda.getDescripcion() == null) ? "" : moneda.getDescripcion().toUpperCase());

			stpr_obtInfCorresp.setParameter("idMoneda", id);
			stpr_obtInfCorresp.setParameter("codigoMoneda", codigo);
			stpr_obtInfCorresp.setParameter("moneda", descripcion);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			e.printStackTrace();
			//this.logger.error("[ERROR] crudMonedas", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarFormaEnvio(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_forma_envio");

			query.registerStoredProcedureParameter("FormaEnvio", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("FormaEnvio", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarFormaEnvio", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudFormaEnvio(FormaEnvio moneda, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_formas_envio");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idFormaEnvio", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoFormaEnvio", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("formaEnvio", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (moneda.getId() == null) ? 0 : moneda.getId();
			String codigo = ((moneda.getCodigoFormaEnvio() == null) ? "" : moneda.getCodigoFormaEnvio().toUpperCase());
			String descripcion = ((moneda.getDescripcion() == null) ? "" : moneda.getDescripcion().toUpperCase());

			stpr_obtInfCorresp.setParameter("idFormaEnvio", id);
			stpr_obtInfCorresp.setParameter("codigoFormaEnvio", codigo);
			stpr_obtInfCorresp.setParameter("formaEnvio", descripcion);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudFormaEnvio", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarNumeradores(Integer codigo, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_numeradores");

			query.registerStoredProcedureParameter("codigoNumerador", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoNumerador", codigo);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarNumeradores", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudNumeradores(Numeradores numerador, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_numeradores");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idNumerador", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("ultimoCorrelativo", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoNumerador", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (numerador.getId() == null) ? 0 : numerador.getId();
			Integer codigo = (numerador.getCodigoNumerador() == null) ? 0 : numerador.getCodigoNumerador();
			Integer ultimoCorrelativo = (numerador.getUltimoCorrelativo() == null) ? 0
					: numerador.getUltimoCorrelativo();

			stpr_obtInfCorresp.setParameter("idNumerador", id);
			stpr_obtInfCorresp.setParameter("ultimoCorrelativo", ultimoCorrelativo);
			stpr_obtInfCorresp.setParameter("codigoNumerador", codigo);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudNumeradores", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarEstados(String estado, String codProceso, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_estado");

			query.registerStoredProcedureParameter("Estado", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("CodigoProceso", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Estado", (estado == null) ? "" : estado);
			query.setParameter("CodigoProceso", (codProceso == null) ? "" : codProceso);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarEstados", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudEstados(Estado estado, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_estados");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idEstado", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoEstado", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("estado", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoProceso", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (estado.getId() == null) ? 0 : estado.getId();
			Integer codigoEstado = (estado.getCodigoEstado() == null) ? 0 : estado.getCodigoEstado();
			String flgEstado = ((estado.getEstado() == null) ? "" : estado.getEstado().toUpperCase());
			String codigoProceso = ((estado.getCodigoProceso() == null) ? "" : estado.getCodigoProceso().toUpperCase());

			stpr_obtInfCorresp.setParameter("idEstado", id);
			stpr_obtInfCorresp.setParameter("codigoEstado", codigoEstado);
			stpr_obtInfCorresp.setParameter("estado", flgEstado);
			stpr_obtInfCorresp.setParameter("codigoProceso", codigoProceso);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudEstados", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarMotivos(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_motivos_devolucion");

			query.registerStoredProcedureParameter("Motivos", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Motivos", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarMotivos", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudMotivos(Motivo motivo, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_motivos_devolucion");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idDevolucion", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoMotivo", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("motivo", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (motivo.getId() == null) ? 0 : motivo.getId();
			Integer codigo = (motivo.getCodigoMotivo() == null) ? 0 : motivo.getCodigoMotivo();
			String nombre = ((motivo.getNombreMotivo() == null) ? "" : motivo.getNombreMotivo().toUpperCase());

			stpr_obtInfCorresp.setParameter("idDevolucion", id);
			stpr_obtInfCorresp.setParameter("codigoMotivo", codigo);
			stpr_obtInfCorresp.setParameter("motivo", nombre);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudMotivos", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarTipoUnidadMatricial(String nombre, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_unidad_matricial");

			query.registerStoredProcedureParameter("UnidadMatricial", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("UnidadMatricial", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarTipoUnidadMatricial", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudTipoUnidadMatricial(TipoUnidadMatricial tipoUnidad, String usuario, String accion)  throws Exception{
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_unidad_matricial");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idUnidaMatricial", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("unidadMatricial", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (tipoUnidad.getId() == null) ? 0 : tipoUnidad.getId();
			String nombre = ((tipoUnidad.getNombre() == null) ? "" : tipoUnidad.getNombre().toUpperCase());

			stpr_obtInfCorresp.setParameter("idUnidaMatricial", id);
			stpr_obtInfCorresp.setParameter("unidadMatricial", nombre);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudTipoUnidadMatricial", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarProvincia(Integer codDepartamento, String provincia, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_provincias");

			query.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("provincia", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoDepartamento", codDepartamento);
			query.setParameter("provincia", (provincia == null) ? "" : provincia);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarProvincia", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudProvincia(Provincia provincia, String usuario, String accion) throws Exception{
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_provincias");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idProvincia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("provincia", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (provincia.getId() == null) ? 0 : provincia.getId();
			Integer codProvincia = (provincia.getCodigoProvincia() == null) ? 0 : provincia.getCodigoProvincia();
			Integer codDepartamento = (provincia.getCodigoDepartamento() == null) ? 0
					: provincia.getCodigoDepartamento();
			String nomProvincia = ((provincia.getNombreProvincia() == null) ? ""
					: provincia.getNombreProvincia().toUpperCase());

			stpr_obtInfCorresp.setParameter("idProvincia", id);
			stpr_obtInfCorresp.setParameter("codigoProvincia", codProvincia);
			stpr_obtInfCorresp.setParameter("codigoDepartamento", codDepartamento);
			stpr_obtInfCorresp.setParameter("provincia", nomProvincia);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudProvincia", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarCiudad(Integer codPais, String nomCiudad, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_ciudades");

			query.registerStoredProcedureParameter("codigoPais", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ciudad", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoPais", codPais);
			query.setParameter("ciudad", (nomCiudad == null) ? "" : nomCiudad);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarCiudad", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudCiudad(Ciudad ciudad, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_ciudades");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idCiudad", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCiudad", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoPais", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("ciudad", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (ciudad.getId() == null) ? 0 : ciudad.getId();
			Integer codCiudad = (ciudad.getCodigoCiudad() == null) ? 0 : ciudad.getCodigoCiudad();
			String nomCiudad = ((ciudad.getNombreCiudad() == null) ? "" : ciudad.getNombreCiudad().toUpperCase());
			Integer codPais = (ciudad.getCodigoPais() == null) ? 0 : ciudad.getCodigoPais();

			stpr_obtInfCorresp.setParameter("idCiudad", id);
			stpr_obtInfCorresp.setParameter("codigoCiudad", codCiudad);
			stpr_obtInfCorresp.setParameter("codigoPais", codPais);
			stpr_obtInfCorresp.setParameter("ciudad", nomCiudad);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudCiudad", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarCGCorrespondencia(String codLugar, String nomCGC, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_cgc");

			query.registerStoredProcedureParameter("CodigoLugar", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("nombre", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("CodigoLugar", ((codLugar != null && codLugar.equalsIgnoreCase("0"))?(""):(codLugar)));
			query.setParameter("nombre", (nomCGC == null) ? "" : nomCGC);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarCGCorrespondencia", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudCGCorrespondencia(CgcCorrespondencia objCGC, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_cgc");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idCGC", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("nombre", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("tipoRotulo", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mCodigoBarras", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("impresora", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("tipoImpresora", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoLugar", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mComputarizado", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoERP", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (objCGC.getId() == null) ? 0 : objCGC.getId();

			stpr_obtInfCorresp.setParameter("idCGC", id);
			stpr_obtInfCorresp.setParameter("codigoCGC", (objCGC.getCodigoCGC() == null) ? "" : objCGC.getCodigoCGC().toUpperCase());
			stpr_obtInfCorresp.setParameter("nombre", (objCGC.getNombreCGC() == null) ? "" : objCGC.getNombreCGC().toUpperCase());
			stpr_obtInfCorresp.setParameter("tipoRotulo",
					(objCGC.getTipoRotulo() == null) ? "" : objCGC.getTipoRotulo().toUpperCase());
			stpr_obtInfCorresp.setParameter("mCodigoBarras",
					(objCGC.getmCodigoBarras() == null) ? "" : objCGC.getmCodigoBarras());
			stpr_obtInfCorresp.setParameter("impresora", (objCGC.getImpresora() == null) ? "" : objCGC.getImpresora().toUpperCase());
			stpr_obtInfCorresp.setParameter("tipoImpresora",
					(objCGC.getTipoImpresora() == null) ? "" : objCGC.getTipoImpresora().toUpperCase());
			stpr_obtInfCorresp.setParameter("codigoLugar",
					(objCGC.getCodigoLugar() == null) ? "" : objCGC.getCodigoLugar().toUpperCase());
			stpr_obtInfCorresp.setParameter("mComputarizado",
					(objCGC.getmComputarizado() == null) ? "" : objCGC.getmComputarizado().toUpperCase());
			stpr_obtInfCorresp.setParameter("codigoERP", (objCGC.getCodigoERP() == null) ? "" : objCGC.getCodigoERP().toUpperCase());
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudCGCorrespondencia", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarDistritos(Integer codDepa, Integer codProv, String nomDistrito,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_distritos");

			query.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("distrito", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoDepartamento", codDepa);
			query.setParameter("codigoProvincia", codProv);
			query.setParameter("distrito", (nomDistrito == null) ? "" : nomDistrito);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarDistritos", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudDistritos(Distrito distrito, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_distritos");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idDistrito", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDistrito", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("distrito", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoPostal", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (distrito.getId() == null) ? 0 : distrito.getId();
			Integer codDepa = (distrito.getCodigoDepartamento() == null) ? 0 : distrito.getCodigoDepartamento();
			Integer codProv = (distrito.getCodigoProvincia() == null) ? 0 : distrito.getCodigoProvincia();
			Integer codDistrito = (distrito.getCodigoDistrito() == null) ? 0 : distrito.getCodigoDistrito();
			String postal = (distrito.getCodigoPostal() == null) ? "" : distrito.getCodigoPostal().toUpperCase();

			stpr_obtInfCorresp.setParameter("idDistrito", id);
			stpr_obtInfCorresp.setParameter("codigoDistrito", codDistrito);
			stpr_obtInfCorresp.setParameter("codigoProvincia", codProv);
			stpr_obtInfCorresp.setParameter("codigoDepartamento", codDepa);
			stpr_obtInfCorresp.setParameter("distrito",
					(distrito.getNombreDistrito() == null) ? "" : distrito.getNombreDistrito().toUpperCase());
			stpr_obtInfCorresp.setParameter("codigoPostal", postal);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudDistritos", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarGestorDependencia(String nomDependencia, String nomGestor, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_gestor_dependencia");

			query.registerStoredProcedureParameter("dependencia", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("gestor", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("dependencia", (nomDependencia == null) ? "" : nomDependencia);
			query.setParameter("gestor", (nomGestor == null) ? "" : nomGestor);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarGestorDependencia", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudGestorDependencia(GestorDependencia gestor, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_gestor_dependencia");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idGestorDep", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDependencia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("registro", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (gestor.getId() == null) ? 0 : gestor.getId();
			Integer codDependencia = (gestor.getCodigoDependencia() == null) ? 0 : gestor.getCodigoDependencia();
			String reg = ((gestor.getRegistro() == null) ? "" : gestor.getRegistro().toUpperCase());

			stpr_obtInfCorresp.setParameter("idGestorDep", id);
			stpr_obtInfCorresp.setParameter("codigoDependencia", codDependencia);
			stpr_obtInfCorresp.setParameter("registro", reg);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudGestorDependencia", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarUsuarioCGC(String codCGC, String nomGestor, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_usuario_cgc");

			query.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("registro", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoCGC", codCGC);
			query.setParameter("registro", (nomGestor == null) ? "" : nomGestor);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarUsuarioCGC", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudUsuarioCGC(UsuarioCgc usuCGC, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_usuario_cgc");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idUsuarioCGC", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("registro", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (usuCGC.getId() == null) ? 0 : usuCGC.getId();
			String codCGC = (usuCGC.getCodigoCGC() == null) ? "" : usuCGC.getCodigoCGC().toUpperCase();
			String reg = (usuCGC.getRegistro() == null) ? "" : usuCGC.getRegistro().toUpperCase();

			stpr_obtInfCorresp.setParameter("idUsuarioCGC", id);
			stpr_obtInfCorresp.setParameter("codigoCGC", codCGC);
			stpr_obtInfCorresp.setParameter("registro", reg);
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudUsuarioCGC", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------ Lugares de Trabajo
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarLugaresDeTrabajo(LugarTrabajoRequest request, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_lugares_de_trabajo");

			query.registerStoredProcedureParameter("CodigoLugar", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("nombre", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("codigoDistrito", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			String codLugar = (request.getCodigoLugar() == null) ? "" : request.getCodigoLugar();
			String nomLugar = (request.getNombreLugar() == null) ? "" : request.getNombreLugar();
			String nomColum = (request.getColumnaOrden() == null) ? "" : request.getColumnaOrden();

			Integer codDepa = (request.getCodigoDepartamento() == null) ? 0 : request.getCodigoDepartamento();
			Integer codProv = (request.getCodigoProvincia() == null) ? 0 : request.getCodigoProvincia();
			Integer codDist = (request.getCodigoDistrito() == null) ? 0 : request.getCodigoDistrito();

			query.setParameter("CodigoLugar", codLugar);
			query.setParameter("nombre", nomLugar);
			query.setParameter("codigoDepartamento", codDepa);
			query.setParameter("codigoProvincia", codProv);
			query.setParameter("codigoDistrito", codDist);
			query.setParameter("ItemsPorPagina", request.getItemsPorPagina());
			query.setParameter("NumeroPagina", request.getNumeroPagina());
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", nomColum);
			query.setParameter("desc", request.getOrden());

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarLugaresDeTrabajo", e);

		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudLugaresDeTrabajo(LugarTrabajo lugarTrabajo, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_lugar_trabajo");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idLugarTrabajo", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoLugarTrabajo", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("nombre", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("direccion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDistrito", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (lugarTrabajo.getId() == null) ? 0 : lugarTrabajo.getId();
			String codLugarTrabajo = (lugarTrabajo.getCodigoLugar() == null) ? ""
					: lugarTrabajo.getCodigoLugar().toUpperCase();

			stpr_obtInfCorresp.setParameter("idLugarTrabajo", id);
			stpr_obtInfCorresp.setParameter("codigoLugarTrabajo", codLugarTrabajo);
			stpr_obtInfCorresp.setParameter("nombre",
					(lugarTrabajo.getNombreLugar() == null) ? "" : lugarTrabajo.getNombreLugar().toUpperCase());
			stpr_obtInfCorresp.setParameter("direccion",
					(lugarTrabajo.getDireccionLugar() == null) ? "" : lugarTrabajo.getDireccionLugar().toUpperCase());
			stpr_obtInfCorresp.setParameter("codigoDistrito",
					(lugarTrabajo.getCodigoDistrito() == null) ? 0 : lugarTrabajo.getCodigoDistrito());
			stpr_obtInfCorresp.setParameter("codigoProvincia",
					(lugarTrabajo.getCodigoProvincia() == null) ? 0 : lugarTrabajo.getCodigoProvincia());
			stpr_obtInfCorresp.setParameter("codigoDepartamento",
					(lugarTrabajo.getCodigoDepartamento() == null) ? 0 : lugarTrabajo.getCodigoDepartamento());
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudLugaresDeTrabajo", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------Courier Lugar de Trabajo

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarCourierLugarTrabajo(String codCGC, Integer codCourier, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_courier_lugartrabajo");

			query.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("codigoCorrier", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoCGC", codCGC);
			query.setParameter("codigoCorrier", ((codCourier != null)?(codCourier):(0)));
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarCourierLugarTrabajo", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudCourierLugarTrabajo(CourierLugarTrabajo objCourier, String usuario, String accion) throws Exception{
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em
					.createStoredProcedureQuery("sic_sp_admin_courier_lugartrabajo");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idCourierLugar", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCourier", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("alcance", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (objCourier.getId() == null) ? 0 : objCourier.getId();
			String codCGC = (objCourier.getCodigoCGC() == null) ? "" : objCourier.getCodigoCGC().toUpperCase();
			Integer codCourier = (objCourier.getCodigoCourier() == null) ? 0 : objCourier.getCodigoCourier();

			stpr_obtInfCorresp.setParameter("idCourierLugar", id);
			stpr_obtInfCorresp.setParameter("codigoCGC", codCGC);
			stpr_obtInfCorresp.setParameter("codigoCourier", codCourier);
			stpr_obtInfCorresp.setParameter("alcance",
					(objCourier.getAlcance() == null) ? "" : objCourier.getAlcance().toUpperCase());
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudCourierLugarTrabajo", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------ Provinciales Locales según lugar de Trabajo
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarProvinciaLugarTrabajo(Integer codDepa, Integer codProv, String codLugar,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_provincia_lugartrabajo");

			query.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("CodigoLugar", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoDepartamento", ((codDepa != null)?(codDepa):(0)));
			query.setParameter("codigoProvincia", ((codProv != null)?(codProv):(0)));
			query.setParameter("CodigoLugar", codLugar);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarProvinciaLugarTrabajo", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudProvinciaLugarTrabajo(ProvinciaLugarTrabajo objProvLugar, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em
					.createStoredProcedureQuery("sic_sp_admin_provincia_lugartrabajo");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idProvinciaLugar", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoLugar", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (objProvLugar.getId() == null) ? 0 : objProvLugar.getId();
			String codLugarTrabajo = (objProvLugar.getCodigoLugar() == null) ? ""
					: objProvLugar.getCodigoLugar().toUpperCase();

			stpr_obtInfCorresp.setParameter("idProvinciaLugar", id);
			stpr_obtInfCorresp.setParameter("codigoLugar", codLugarTrabajo);
			stpr_obtInfCorresp.setParameter("codigoProvincia",
					(objProvLugar.getCodigoProvincia() == null) ? 0 : objProvLugar.getCodigoProvincia());
			stpr_obtInfCorresp.setParameter("codigoDepartamento",
					(objProvLugar.getCodigoDepartamento() == null) ? 0 : objProvLugar.getCodigoDepartamento());
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudProvinciaLugarTrabajo", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------ Dependencia Externa
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarDependenciaExterna(String nombre, String ruc, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_dependencias_externas");

			query.registerStoredProcedureParameter("ruc", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("dependencia", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("ruc", ruc);
			query.setParameter("dependencia", nombre);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarDependenciaExterna", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudDependenciaExterna(DependenciaExterna dependencia, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em
					.createStoredProcedureQuery("sic_sp_admin_dependencias_externas");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idDependenciaExterna", Integer.class,
					ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("dependencia", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("direccion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDepartamento", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoProvincia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDistrito", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoPais", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCiudad", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("ruc", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("email", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (dependencia.getId() == null) ? 0 : dependencia.getId();
			String codLugarTrabajo = (dependencia.getNombreDependencia() == null) ? ""
					: dependencia.getNombreDependencia().toUpperCase();

			stpr_obtInfCorresp.setParameter("idDependenciaExterna", id);
			stpr_obtInfCorresp.setParameter("dependencia", codLugarTrabajo);
			stpr_obtInfCorresp.setParameter("direccion",
					(dependencia.getDireccion() == null) ? "" : dependencia.getDireccion().toUpperCase());

			stpr_obtInfCorresp.setParameter("codigoDepartamento",
					(dependencia.getCodigoDepartamento() == null) ? 0 : dependencia.getCodigoDepartamento());
			stpr_obtInfCorresp.setParameter("codigoProvincia",
					(dependencia.getCodigoProvincia() == null) ? 0 : dependencia.getCodigoProvincia());
			stpr_obtInfCorresp.setParameter("codigoDistrito",
					(dependencia.getCodigoDistrito() == null) ? 0 : dependencia.getCodigoDistrito());
			stpr_obtInfCorresp.setParameter("codigoPais",
					(dependencia.getCodigoPais() == null) ? 0 : dependencia.getCodigoPais());
			stpr_obtInfCorresp.setParameter("codigoCiudad",
					(dependencia.getCodigoCiudad() == null) ? 0 : dependencia.getCodigoCiudad());

			stpr_obtInfCorresp.setParameter("ruc", (dependencia.getRuc() == null) ? "" : dependencia.getRuc().toUpperCase());
			stpr_obtInfCorresp.setParameter("email", (dependencia.getEmail() == null) ? "" : dependencia.getEmail().toUpperCase());

			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudDependenciaExterna", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------ Tipo Accion
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarTipoAccion(String nomAccion, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_tipos_accion");

			query.registerStoredProcedureParameter("Accion", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("Accion", nomAccion);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarTipoAccion", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudTipoAccion(TipoAccion tipoAccion, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_tipos_accion");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idTipoAccion", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoAccion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mAccion", String.class, ParameterMode.IN);

			stpr_obtInfCorresp.registerStoredProcedureParameter("mTextoReq", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mMutiple", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mReqTextoRta", String.class, ParameterMode.IN);

			stpr_obtInfCorresp.registerStoredProcedureParameter("mEnviaMailRta", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("procesos", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("prioridad", Integer.class, ParameterMode.IN);

			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (tipoAccion.getId() == null) ? 0 : tipoAccion.getId();

			stpr_obtInfCorresp.setParameter("idTipoAccion", id);
			stpr_obtInfCorresp.setParameter("codigoAccion",
					(tipoAccion.getCodigoAccion() == null) ? "" : tipoAccion.getCodigoAccion().toUpperCase());
			stpr_obtInfCorresp.setParameter("mAccion",
					(tipoAccion.getNombreAccion() == null) ? "" : tipoAccion.getNombreAccion().toUpperCase());

			stpr_obtInfCorresp.setParameter("mTextoReq",
					(tipoAccion.getmTextoReq() == null) ? "" : tipoAccion.getmTextoReq().toUpperCase());
			stpr_obtInfCorresp.setParameter("mMutiple",
					(tipoAccion.getmMultipli() == null) ? "" : tipoAccion.getmMultipli().toUpperCase());
			stpr_obtInfCorresp.setParameter("mReqTextoRta",
					(tipoAccion.getmReqTextoRta() == null) ? "" : tipoAccion.getmReqTextoRta().toUpperCase());

			stpr_obtInfCorresp.setParameter("mEnviaMailRta",
					(tipoAccion.getmEnviaMailRta() == null) ? "" : tipoAccion.getmEnviaMailRta().toUpperCase());
			stpr_obtInfCorresp.setParameter("procesos",
					(tipoAccion.getProcesos() == null) ? "" : tipoAccion.getProcesos().toUpperCase());
			stpr_obtInfCorresp.setParameter("prioridad",
					(tipoAccion.getPrioridad() == null) ? 0 : tipoAccion.getPrioridad());

			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", tipoAccion.getAccion());
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudTipoAccion", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------ Tipos de Correspondencias
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarTipoCorrespondencia(String nomTipoCorr, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_tipo_correspondencia");

			query.registerStoredProcedureParameter("tipoCorrespondencia", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("tipoCorrespondencia", (nomTipoCorr == null) ? "" : nomTipoCorr);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarTipoCorrespondencia", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudTipoCorrespondencia(TipoCorrespondencia tipoCorre, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em
					.createStoredProcedureQuery("sic_sp_admin_tipos_correspondencia");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idTipoCorrespondencia", Integer.class,
					ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoTipoCorrespondencia", Integer.class,
					ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("tipoCorrespondencia", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mAplicaEnvInterna", String.class, ParameterMode.IN);

			stpr_obtInfCorresp.registerStoredProcedureParameter("mAplicaEnvExterna", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mAplicaRecInterna", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mAplicaRecExterna", String.class, ParameterMode.IN);

			stpr_obtInfCorresp.registerStoredProcedureParameter("mRequiereFecha", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("MFinalizaAceptar", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mManualCorresp", String.class, ParameterMode.IN);

			stpr_obtInfCorresp.registerStoredProcedureParameter("secuencia", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mMultiple", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("reqCopia", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("reqDest", String.class, ParameterMode.IN);

			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (tipoCorre.getId() == null) ? 0 : tipoCorre.getId();

			stpr_obtInfCorresp.setParameter("idTipoCorrespondencia", id);
			stpr_obtInfCorresp.setParameter("codigoTipoCorrespondencia",
					(tipoCorre.getCodigoTipoCorr() == null) ? 0 : tipoCorre.getCodigoTipoCorr());
			stpr_obtInfCorresp.setParameter("tipoCorrespondencia",
					(tipoCorre.getNombreTipoCorr() == null) ? "" : tipoCorre.getNombreTipoCorr().toUpperCase());
			stpr_obtInfCorresp.setParameter("mAplicaEnvInterna",
					(tipoCorre.getmAplicaEnvInterna() == null) ? "" : tipoCorre.getmAplicaEnvInterna().toUpperCase());

			stpr_obtInfCorresp.setParameter("mAplicaEnvExterna",
					(tipoCorre.getmAplicaEnvExterna() == null) ? "" : tipoCorre.getmAplicaEnvExterna().toUpperCase());
			stpr_obtInfCorresp.setParameter("mAplicaRecInterna",
					(tipoCorre.getmAplicaRecInterna() == null) ? "" : tipoCorre.getmAplicaRecInterna().toUpperCase());
			stpr_obtInfCorresp.setParameter("mAplicaRecExterna",
					(tipoCorre.getmAplicaRecExterna() == null) ? "" : tipoCorre.getmAplicaRecExterna().toUpperCase());

			stpr_obtInfCorresp.setParameter("mRequiereFecha",
					(tipoCorre.getmRequiereFecha() == null) ? "" : tipoCorre.getmRequiereFecha().toUpperCase());
			stpr_obtInfCorresp.setParameter("MFinalizaAceptar",
					(tipoCorre.getmFinalizaAceptar() == null) ? "" : tipoCorre.getmFinalizaAceptar().toUpperCase());
			stpr_obtInfCorresp.setParameter("mManualCorresp",
					(tipoCorre.getmManualCorresp() == null) ? 0 : tipoCorre.getmManualCorresp());

			stpr_obtInfCorresp.setParameter("secuencia",
					(tipoCorre.getSecuencia() == null) ? 0 : tipoCorre.getSecuencia());
			stpr_obtInfCorresp.setParameter("mMultiple",
					(tipoCorre.getmMultiple() == null) ? "" : tipoCorre.getmMultiple().toUpperCase());
			stpr_obtInfCorresp.setParameter("reqCopia",
					(tipoCorre.getReqCopia() == null) ? "" : tipoCorre.getReqCopia().toUpperCase());
			stpr_obtInfCorresp.setParameter("reqDest", (tipoCorre.getReqDest() == null) ? "" : tipoCorre.getReqDest().toUpperCase());

			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			//this.logger.error("[ERROR] crudTipoCorrespondencia", e);
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------ Transacciones por CGC
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarTransaccionesCGC(String tipoTrans, String cgcOrigin, String cgcDestino,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_transaccion_x_cgc");

			query.registerStoredProcedureParameter("tipoTransaccion", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("cgcOrigen", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("cgcDestino", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("tipoTransaccion", tipoTrans);
			query.setParameter("cgcOrigen", cgcOrigin);
			query.setParameter("cgcDestino", cgcDestino);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarTransaccionesCGC", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudTransaccionesCGC(TransaccionesCgc objTrans, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_transaccion_x_cgc");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idTrasanccion", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("tipoTrasansaccion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("cgcOrigen", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("cgcDestino", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoNumerador", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (objTrans.getId() == null) ? 0 : objTrans.getId();

			stpr_obtInfCorresp.setParameter("idTrasanccion", id);
			stpr_obtInfCorresp.setParameter("tipoTrasansaccion",
					(objTrans.getTipoTransaccion() == null) ? "" : objTrans.getTipoTransaccion().toUpperCase());
			stpr_obtInfCorresp.setParameter("cgcOrigen",
					(objTrans.getCgcOrigen() == null) ? "" : objTrans.getCgcOrigen().toUpperCase());
			stpr_obtInfCorresp.setParameter("cgcDestino",
					(objTrans.getCgcDestino() == null) ? "" : objTrans.getCgcDestino().toUpperCase());
			stpr_obtInfCorresp.setParameter("codigoNumerador",
					(objTrans.getCodigoNumerador() == null) ? 0 : objTrans.getCodigoNumerador());
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

	// ------------------ CGC Lugares de Trabajo
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarCgcLugarTrabajo(String codCgc, String codLugar, Integer itemsPorPagina,
			Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {

		EntityManager em = null;
		List<Object[]> listRows = null;
		try {

			em = entityManagerBDFilenet.createEntityManager();

			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_admin_select_cgc_lugartrabajo");

			query.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("codigoLugar", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);

			query.setParameter("codigoCGC", (codCgc == null) ? "" : codCgc);
			query.setParameter("codigoLugar", (codLugar == null) ? "" : codLugar);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", (columnaOrden == null) ? "" : columnaOrden);
			query.setParameter("desc", orden);

			query.execute();

			listRows = query.getResultList();
			Object[] total = { 0 };
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);

		} catch (Exception e) {
			this.logger.error("[ERROR] consultarCgcLugarTrabajo", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return listRows;
	}

	@Override
	public String crudCgcLugarTrabajo(CgcLugarTrabajo objCgcLugar, String usuario, String accion) throws Exception {
		EntityManager em = null;
		String msjOut = null;
		try {
			em = entityManagerBDFilenet.createEntityManager();
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_admin_cgc_lugares_trabajo");

			stpr_obtInfCorresp.registerStoredProcedureParameter("idTrasanccion", Integer.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoLugar", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioAdmin", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("accion", String.class, ParameterMode.IN);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);

			Integer id = (objCgcLugar.getId() == null) ? 0 : objCgcLugar.getId();

			stpr_obtInfCorresp.setParameter("idTrasanccion", id);
			stpr_obtInfCorresp.setParameter("codigoCGC",
					(objCgcLugar.getCodigoCgc() == null) ? "" : objCgcLugar.getCodigoCgc().toUpperCase());
			stpr_obtInfCorresp.setParameter("codigoLugar",
					(objCgcLugar.getCodigoLugar() == null) ? "" : objCgcLugar.getCodigoLugar().toUpperCase());
			stpr_obtInfCorresp.setParameter("usuarioAdmin", usuario);
			stpr_obtInfCorresp.setParameter("accion", accion);
			stpr_obtInfCorresp.setParameter("mensaje", "");

			stpr_obtInfCorresp.execute();

			msjOut = (String) stpr_obtInfCorresp.getOutputParameterValue("mensaje");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return msjOut;
	}

}
