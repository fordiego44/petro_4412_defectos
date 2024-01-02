package pe.com.petroperu.filenet.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import pe.com.petroperu.model.Correspondencia;

@Repository("iBandejaEntradaCorrespondenciaDAOImpl")
public class IBandejaEntradaCorrespondenciaDAOImpl implements IBandejaEntradaCorrespondenciaDAO {

	@Autowired
	@Qualifier("emFactoryBDFilenet")
	private EntityManagerFactory entityManagerBDFilenet;

	@Override
	public Object[] obtenerInformacionCorrespondencia(String correlativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		Object[] parameterOut = new Object[32];
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_Obtener_informacion_correspondencia");
			
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("correlativo", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("dependenciaDestino", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("dependenciaRemitente", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("asunto", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("fechaCreacion", Date.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoDependenciaDestino", Integer.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("codigoCGC", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("tipo", String.class, ParameterMode.OUT);
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioResponsable", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("nombreApellidoResponsable", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioRadicador", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("usuarioGestor", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("nombreApellidoGestor", String.class, ParameterMode.OUT);
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("numeroDocumento", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("estado", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("esConfidencial", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("proceso", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("tipoTransaccion", String.class, ParameterMode.OUT);
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("correlativoReferencia", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("numeroFolios", Integer.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("lugarTrabajoDestino", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("lugarTrabajoRemitente", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("nombreApellidoPersonaDestino", String.class, ParameterMode.OUT);
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("nombreApellidoPersonaRemitente", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("guia", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("guiaRemision", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("esRegistroDesdeDependencia", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("fechaDocumento", String.class, ParameterMode.OUT);
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("ultimaObservacion", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("urgente", String.class, ParameterMode.OUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("cgc", String.class, ParameterMode.OUT);
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("documentoRespuesta", String.class, ParameterMode.OUT);
			
			stpr_obtInfCorresp.setParameter("correlativo", correlativo);
			
			stpr_obtInfCorresp.execute();
			//this.FORMATO_FECHA_FRONT_END.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
			//Date valorrr = (Date)stpr_obtInfCorresp.getOutputParameterValue("fechaCreacion");
			//Timestamp stamp = new Timestamp(System.currentTimeMillis());
			//Date date = new Date((Long)stpr_obtInfCorresp.getOutputParameterValue("fechaCreacion"));
			parameterOut[0] = correlativo;
			parameterOut[1] = stpr_obtInfCorresp.getOutputParameterValue("dependenciaDestino");
			parameterOut[2] = stpr_obtInfCorresp.getOutputParameterValue("dependenciaRemitente");
			parameterOut[3] = stpr_obtInfCorresp.getOutputParameterValue("asunto");
			parameterOut[4] = (stpr_obtInfCorresp.getOutputParameterValue("fechaCreacion") != null)?(((Date)stpr_obtInfCorresp.getOutputParameterValue("fechaCreacion")).toString()):("");
			parameterOut[5] = stpr_obtInfCorresp.getOutputParameterValue("codigoDependenciaDestino");
			parameterOut[6] = stpr_obtInfCorresp.getOutputParameterValue("codigoCGC");
			
			parameterOut[7] = stpr_obtInfCorresp.getOutputParameterValue("tipo");
			parameterOut[8] = stpr_obtInfCorresp.getOutputParameterValue("usuarioResponsable");
			parameterOut[9] = stpr_obtInfCorresp.getOutputParameterValue("nombreApellidoResponsable");
			parameterOut[10] = stpr_obtInfCorresp.getOutputParameterValue("usuarioRadicador");
			parameterOut[11] = stpr_obtInfCorresp.getOutputParameterValue("usuarioGestor");
			parameterOut[12] = stpr_obtInfCorresp.getOutputParameterValue("nombreApellidoGestor");
			
			parameterOut[13] = stpr_obtInfCorresp.getOutputParameterValue("numeroDocumento");
			parameterOut[14] = stpr_obtInfCorresp.getOutputParameterValue("estado");
			parameterOut[15] = stpr_obtInfCorresp.getOutputParameterValue("esConfidencial");
			parameterOut[16] = stpr_obtInfCorresp.getOutputParameterValue("proceso");
			parameterOut[17] = stpr_obtInfCorresp.getOutputParameterValue("tipoTransaccion");
			parameterOut[18] = stpr_obtInfCorresp.getOutputParameterValue("correlativoReferencia");
			
			parameterOut[19] = stpr_obtInfCorresp.getOutputParameterValue("numeroFolios");
			parameterOut[20] = stpr_obtInfCorresp.getOutputParameterValue("lugarTrabajoDestino");
			parameterOut[21] = stpr_obtInfCorresp.getOutputParameterValue("lugarTrabajoRemitente");
			parameterOut[22] = stpr_obtInfCorresp.getOutputParameterValue("nombreApellidoPersonaDestino");
			parameterOut[23] = stpr_obtInfCorresp.getOutputParameterValue("nombreApellidoPersonaRemitente");
			parameterOut[24] = stpr_obtInfCorresp.getOutputParameterValue("guia");
			
			parameterOut[25] = stpr_obtInfCorresp.getOutputParameterValue("guiaRemision");
			parameterOut[26] = stpr_obtInfCorresp.getOutputParameterValue("esRegistroDesdeDependencia");
			parameterOut[27] = stpr_obtInfCorresp.getOutputParameterValue("fechaDocumento");
			parameterOut[28] = stpr_obtInfCorresp.getOutputParameterValue("ultimaObservacion");
			parameterOut[29] = stpr_obtInfCorresp.getOutputParameterValue("urgente");
			parameterOut[30] = stpr_obtInfCorresp.getOutputParameterValue("cgc");
			
			parameterOut[31] = stpr_obtInfCorresp.getOutputParameterValue("documentoRespuesta");
			
			//System.out.println("PRUEBA NUMERO DOCUMENTO RESPUESTA:" + parameterOut[31]);
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return parameterOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultaTrackingFisico(String correlativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listTrackingFisico = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_tracking");
			
			query.registerStoredProcedureParameter("correlativo", String.class, ParameterMode.INOUT);
			
			query.setParameter("correlativo", correlativo);
			
			query.execute();
			
			listTrackingFisico = query.getResultList();
			
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listTrackingFisico;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> recuperarListaAcciones(String proceso) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_lista_tipos_accion");
			
			query.registerStoredProcedureParameter("proceso", String.class, ParameterMode.INOUT);
			
			query.setParameter("proceso", proceso);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@Override
	public String agregarAsignacionTemporal(String codigoAccion, String correlativo, String usuarioAsignado,
			String detalleSolicitud, String fechaLimite, String idPadre, String idUsuario) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		String msjOut = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_wfl_insert_tmp_asig");
			
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("CodigoAccion", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("RegistroSolic", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("RegistroAsig", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("TextoAsignacion", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("FechaTope", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("IdPadre", Integer.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("Mensaje", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("CodigoDependenciaAsig", Integer.class, ParameterMode.INOUT);
			
			stpr_obtInfCorresp.setParameter("Correlativo", correlativo);
			stpr_obtInfCorresp.setParameter("CodigoAccion", codigoAccion);
			stpr_obtInfCorresp.setParameter("RegistroSolic", idUsuario);
			stpr_obtInfCorresp.setParameter("RegistroAsig", usuarioAsignado);
			stpr_obtInfCorresp.setParameter("TextoAsignacion", detalleSolicitud);
			stpr_obtInfCorresp.setParameter("FechaTope", fechaLimite);
			stpr_obtInfCorresp.setParameter("IdPadre", ((idPadre != null && !idPadre.trim().equalsIgnoreCase(""))?(Integer.parseInt(idPadre)):(0)));
			stpr_obtInfCorresp.setParameter("Mensaje", "");
			
			stpr_obtInfCorresp.execute();
			
			msjOut = (String)stpr_obtInfCorresp.getOutputParameterValue("Mensaje");
			
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return msjOut;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> obtenerAsignacionesTemporales(String correlativo, String idUsuario) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wfl_lista_tmp_asig");
			
			query.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("RegistroSolic", String.class, ParameterMode.INOUT);
			
			query.setParameter("Correlativo", correlativo);
			query.setParameter("RegistroSolic", idUsuario);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@Override
	public String agregarAsignacionTemporalMasivo(String codigoAccion, String correlativo, String detalleSolicitud,
			String fechaLimite, String idPadre, Integer codigoDependencia, String idUsuario) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		String msjOut = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
			
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_asignacion_agregartodos");
			
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("CodigoAccion", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("TextoAsignacion", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("FechaTope", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("IdPadre", Integer.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("Mensaje", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("CodigoDependenciaAsig", Integer.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("CodigoDependencia", Integer.class, ParameterMode.INOUT);
			
			stpr_obtInfCorresp.setParameter("Correlativo", correlativo);
			stpr_obtInfCorresp.setParameter("Usuario", idUsuario);
			stpr_obtInfCorresp.setParameter("CodigoAccion", codigoAccion);
			stpr_obtInfCorresp.setParameter("TextoAsignacion", detalleSolicitud);
			stpr_obtInfCorresp.setParameter("FechaTope", fechaLimite);
			stpr_obtInfCorresp.setParameter("IdPadre", ((idPadre != null && !idPadre.trim().equalsIgnoreCase(""))?(Integer.parseInt(idPadre)):(0)));
			stpr_obtInfCorresp.setParameter("Mensaje", "");
			stpr_obtInfCorresp.setParameter("CodigoDependenciaAsig", codigoDependencia);
			stpr_obtInfCorresp.setParameter("CodigoDependencia", codigoDependencia);
			
			stpr_obtInfCorresp.execute();
			
			msjOut = (String)stpr_obtInfCorresp.getOutputParameterValue("Mensaje");
			
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return msjOut;
	}

	@Override
	public boolean eliminarAsignacionesTemporales(Integer idAsignacion, String idUsuario) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		boolean esProcessExito = false;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wfl_delete_tmp_asig");
			
			query.registerStoredProcedureParameter("Id", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("RegistroSolic", String.class, ParameterMode.INOUT);
			
			query.setParameter("Id", idAsignacion);
			query.setParameter("RegistroSolic", idUsuario);
			
			query.execute();
			
			esProcessExito = true;
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return esProcessExito;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarListaFuncionarios(Integer codigoDependencia, String codigoLugar, String cadena,
			String minclNoActivo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_select_lista_funcionarios");
			
			query.registerStoredProcedureParameter("CodigoDependencia", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoLugar", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Cadena", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("MInclNoActivo", String.class, ParameterMode.INOUT);
			
			query.setParameter("CodigoDependencia", codigoDependencia);
			query.setParameter("CodigoLugar", codigoLugar);
			query.setParameter("Cadena", cadena);
			query.setParameter("MInclNoActivo", minclNoActivo);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarListaFuncionariosCopiados(String idUsuario, String correlativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wfl_lista_copias");
			
			query.registerStoredProcedureParameter("Registro", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.INOUT);
			
			query.setParameter("Registro", idUsuario);
			query.setParameter("Correlativo", correlativo);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@Override
	public boolean agregarFuncionarioListaCopias(String idUsuario, String usuario, String correlativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		boolean esProcessExito = false;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wfl_insert_copias");
			
			query.registerStoredProcedureParameter("Registro", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("RegistroCopia", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.INOUT);
			
			query.setParameter("Registro", idUsuario);
			query.setParameter("RegistroCopia", usuario);
			query.setParameter("Correlativo", correlativo);
			
			query.execute();
			
			esProcessExito = true;
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return esProcessExito;
	}

	@Override
	public boolean eliminarFuncionarioListaCopias(String idUsuario, String registroCopia, String correlativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		boolean esProcessExito = false;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wfl_delete_copias");
			
			query.registerStoredProcedureParameter("Registro", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("RegistroCopia", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.INOUT);
			
			query.setParameter("Registro", idUsuario);
			query.setParameter("RegistroCopia", registroCopia);
			query.setParameter("Correlativo", correlativo);
			
			query.execute();
			
			esProcessExito = true;
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return esProcessExito;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultaCorresRecibidaXProce(String idUsuario, String correlativo, String fechaDesde,
			String fechaHasta, Integer codigoTipoCorrespondencia, Integer codigoEstado, String fechaDocumento,
			Integer codigoDependenciaDestino, Integer codigoDependenciaRemitente, String nombreDependenciaExterna,
			String asunto, String numeroDocumentoInterno, String guiaRemision, String urgente, String procedencia, 
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_correspondencia_recibida_x_proced");
			
			query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoTipoCorr", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("FechaDocumento", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodDepDestino", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoDepRemitente", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("DependenciaExterna", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Asunto", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NroDocInterno", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("GuiaRemision", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("EsUrgente", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Procedencia", String.class, ParameterMode.INOUT);
			// TICKET 9000004944
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			// FIN TICKET
			
			query.setParameter("Usuario", idUsuario);
			query.setParameter("Radicado", correlativo);
			query.setParameter("FechaDesde", fechaDesde);
			query.setParameter("FechaHasta", fechaHasta);
			query.setParameter("CodigoTipoCorr", codigoTipoCorrespondencia);
			query.setParameter("CodigoEstado", codigoEstado);
			query.setParameter("FechaDocumento", fechaDocumento);
			query.setParameter("CodDepDestino", codigoDependenciaDestino);
			query.setParameter("CodigoDepRemitente", codigoDependenciaRemitente);
			query.setParameter("DependenciaExterna", nombreDependenciaExterna);
			query.setParameter("Asunto", asunto);
			query.setParameter("NroDocInterno", numeroDocumentoInterno);
			query.setParameter("GuiaRemision", guiaRemision);
			query.setParameter("EsUrgente", urgente == null?"":urgente);
			query.setParameter("Procedencia", procedencia);
			// TICKET 9000004494
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", columnaOrden);
			query.setParameter("desc", orden);
			//query.setParameter("TotalRegistros", totalRegistros);
			// FIN TICKET
			/*System.out.println(idUsuario);
			System.out.println(correlativo);
			System.out.println(fechaDesde);
			System.out.println(fechaHasta);
			System.out.println(codigoTipoCorrespondencia);
			System.out.println(codigoEstado);
			System.out.println(fechaDocumento);
			System.out.println(codigoDependenciaDestino);
			System.out.println(codigoDependenciaRemitente);
			System.out.println(nombreDependenciaExterna);
			System.out.println(asunto);
			System.out.println(numeroDocumentoInterno);
			System.out.println(guiaRemision);
			System.out.println(urgente);
			System.out.println(procedencia);
			System.out.println(itemsPorPagina);
			System.out.println(numeroPagina);
			System.out.println(exportarExcel);
			System.out.println(columnaOrden);
			System.out.println(orden);*/
			
			query.execute();
			
			listRows = query.getResultList();
			Object[] total = {0};
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);
		}catch(Exception e){
			e.printStackTrace();
			Object[] total = {0};
			listRows.add(0, total);
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultaAsignacionesv2(String idUsuario, String correlativo, String numeroDocumentoInterno,
			Integer codigoDependenciaAsignante, String usuarioAsignado, String codigoAccion, Integer codigoEstado,
			String fechaDesde, String fechaHasta, String vencimientoDesde, String vencimientoHasta, String urgente, 
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_asignaciones_v2");
			
			query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NroDocumento", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoDependencia", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("PersonaAsignada", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoAccion", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("vFechaDesde", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("vFechaHasta", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("VencimientoDesde", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("VencimientoHasta", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Mensaje", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("EsUrgente", String.class, ParameterMode.INOUT);
			// TICKET 9000004494
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			// FIN TICKET
			
			query.setParameter("Usuario", idUsuario);
			query.setParameter("Radicado", correlativo);
			query.setParameter("NroDocumento", numeroDocumentoInterno);
			query.setParameter("CodigoDependencia", codigoDependenciaAsignante);
			query.setParameter("PersonaAsignada", usuarioAsignado);
			query.setParameter("CodigoAccion", codigoAccion);
			query.setParameter("CodigoEstado", codigoEstado);
			query.setParameter("vFechaDesde", fechaDesde);
			query.setParameter("vFechaHasta", fechaHasta);
			query.setParameter("VencimientoDesde", vencimientoDesde);
			query.setParameter("VencimientoHasta", vencimientoHasta);
			query.setParameter("Mensaje", "");
			query.setParameter("EsUrgente", urgente);
			// TICKET 9000004494
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", columnaOrden);
			query.setParameter("desc", orden);
			// FIN TICKET
			
			query.execute();
			
			listRows = query.getResultList();
			Object[] total = {0};
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultarListaHistorialAsignaciones(String idUsuario, String correlativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_select_lista_asignaciones_v2");
			
			query.registerStoredProcedureParameter("correlativo", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("usuario", String.class, ParameterMode.INOUT);
			
			query.setParameter("correlativo", correlativo);
			query.setParameter("usuario", idUsuario);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> recuperarListaTraza(String proceso, String correlativo, String referencia) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wrapper_lpa_lista_traza");
			
			query.registerStoredProcedureParameter("xva_CDProceso", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_CDReferencia1", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_CDReferencia2", String.class, ParameterMode.INOUT);
			
			query.setParameter("xva_CDProceso", proceso);
			query.setParameter("xva_CDReferencia1", correlativo);
			query.setParameter("xva_CDReferencia2", referencia);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> recuperarListaObservaciones(String proceso, String correlativo, String referencia) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wrapper_lpa_lista_observaciones");
			
			query.registerStoredProcedureParameter("xva_CDProceso", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_CDReferencia1", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_CDReferencia2", String.class, ParameterMode.INOUT);
			
			query.setParameter("xva_CDProceso", proceso);
			query.setParameter("xva_CDReferencia1", correlativo);
			query.setParameter("xva_CDReferencia2", referencia);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@Override
	public boolean agregarObservacion(String proceso, String idUsuario, String correlativo, String observacion) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		boolean esProcessExito = false;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_wrapper_lpa_insert_observaciones");
			
			query.registerStoredProcedureParameter("xva_CDProceso", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_CDUsuario", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_CDReferencia1", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_CDReferencia2", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_DSObservacion", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("xva_DSTipo", String.class, ParameterMode.INOUT);
			
			query.setParameter("xva_CDProceso", proceso);
			query.setParameter("xva_CDUsuario", idUsuario);
			query.setParameter("xva_CDReferencia1", correlativo);
			query.setParameter("xva_CDReferencia2", correlativo);
			query.setParameter("xva_DSObservacion", observacion);
			query.setParameter("xva_DSTipo", "OBSERVACION");
			
			query.execute();
			
			esProcessExito = true;
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return esProcessExito;
	}
	
	// TICKET 9000004497
	@Override
	public String agregarAsignacionGrupalTemporal(String codigoAccion,  String usuarioAsignado,
			String detalleSolicitud, String fechaLimite, String idUsuario,String grupoCorrespondencia, String grupoIdPadre) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		String msjOut = null;
		String correTemp = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery stpr_obtInfCorresp = em.createStoredProcedureQuery("sic_sp_insert_tmp_asignacion_grupal");
			
			
			stpr_obtInfCorresp.registerStoredProcedureParameter("Correlativos", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("IdPadres", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("CodigoAccion", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("TextoAsignacion", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("FechaTope", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("RegistroAsignado", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("UsuarioSesion", String.class, ParameterMode.INOUT);
			stpr_obtInfCorresp.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.INOUT);
			
			stpr_obtInfCorresp.setParameter("Correlativos", grupoCorrespondencia);
			stpr_obtInfCorresp.setParameter("IdPadres", grupoIdPadre);
			stpr_obtInfCorresp.setParameter("CodigoAccion", codigoAccion);
			stpr_obtInfCorresp.setParameter("TextoAsignacion", detalleSolicitud);
			stpr_obtInfCorresp.setParameter("FechaTope", fechaLimite);
			stpr_obtInfCorresp.setParameter("RegistroAsignado", usuarioAsignado);
			stpr_obtInfCorresp.setParameter("UsuarioSesion", idUsuario);
			stpr_obtInfCorresp.setParameter("mensaje", "");	
			stpr_obtInfCorresp.execute();
			
			msjOut = (String)stpr_obtInfCorresp.getOutputParameterValue("mensaje");
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return msjOut;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> obtenerAsignacionGrupalTemporales(String idUsuario, String grupoCorrelativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_lista_tmp_asignacion_grupal");
			
			query.registerStoredProcedureParameter("Correlativos", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("RegistroSolic", String.class, ParameterMode.INOUT);
			
			query.setParameter("Correlativos", grupoCorrelativo);
			query.setParameter("RegistroSolic", idUsuario);
			
			query.execute();
			
			listRows = query.getResultList();
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}

	@Override
	public String validarAsignacionGrupal(String idUsuario,String grupoCorrelativo, String grupoIdPadre) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		String msjOut = null;
		try{
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_validar_asignacion_grupal");
			
			query.registerStoredProcedureParameter("usuario", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("correlativos", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("idAsignaciones", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("mensaje", String.class, ParameterMode.OUT);

			query.setParameter("usuario", idUsuario);
			query.setParameter("correlativos", grupoCorrelativo);
			query.setParameter("idAsignaciones", grupoIdPadre);
		//	query.setParameter("mensaje", "");
			
			query.execute();
			
			msjOut = (String)query.getOutputParameterValue("mensaje");
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return msjOut;
	}
	//FIN TICKET
	
	// TICKET 9000004961
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> consultaCorrespondenciaAuditoria(String idUsuario, String correlativo, String fechaDesde,
			String fechaHasta, Integer codigoTipoCorrespondencia, Integer codigoEstado, String fechaDocumento,
			Integer codigoDependenciaDestino, Integer codigoDependenciaRemitente, String nombreDependenciaExterna,
			String asunto, String numeroDocumentoInterno, String guiaRemision, String urgente, String procedencia, 
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel) {
		
		EntityManager em = null;
		List<Object[]> listRows = null;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_correspondencia_fiscalizador");
			//StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_consulta_correspondencia_recibida_x_proced");
			
			query.registerStoredProcedureParameter("Usuario", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Radicado", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("FechaDesde", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("FechaHasta", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoTipoCorr", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoEstado", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("FechaDocumento", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodDepDestino", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CodigoDepRemitente", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("DependenciaExterna", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Asunto", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NroDocInterno", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("GuiaRemision", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("EsUrgente", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Procedencia", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("ItemsPorPagina", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NumeroPagina", Integer.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("ExportaExcel", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("nombreColumna", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("desc", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("TotalRegistros", Integer.class, ParameterMode.OUT);
			
			query.setParameter("Usuario", idUsuario);
			query.setParameter("Radicado", correlativo);
			query.setParameter("FechaDesde", fechaDesde);
			query.setParameter("FechaHasta", fechaHasta);
			query.setParameter("CodigoTipoCorr", codigoTipoCorrespondencia);
			query.setParameter("CodigoEstado", codigoEstado);
			query.setParameter("FechaDocumento", fechaDocumento);
			query.setParameter("CodDepDestino", codigoDependenciaDestino);
			query.setParameter("CodigoDepRemitente", codigoDependenciaRemitente);
			query.setParameter("DependenciaExterna", nombreDependenciaExterna);
			query.setParameter("Asunto", asunto);
			query.setParameter("NroDocInterno", numeroDocumentoInterno);
			query.setParameter("GuiaRemision", guiaRemision);
			query.setParameter("EsUrgente", urgente);
			query.setParameter("Procedencia", procedencia);
			query.setParameter("ItemsPorPagina", itemsPorPagina);
			query.setParameter("NumeroPagina", numeroPagina);
			query.setParameter("ExportaExcel", exportarExcel);
			query.setParameter("nombreColumna", columnaOrden);
			query.setParameter("desc", orden);
			
			System.out.println(idUsuario);
			System.out.println(correlativo);
			System.out.println(fechaDesde);
			System.out.println(fechaHasta);
			System.out.println(codigoTipoCorrespondencia);
			System.out.println(codigoEstado);
			System.out.println(fechaDocumento);
			System.out.println(codigoDependenciaDestino);
			System.out.println(codigoDependenciaRemitente);
			System.out.println(nombreDependenciaExterna);
			System.out.println(asunto);
			System.out.println(numeroDocumentoInterno);
			System.out.println(guiaRemision);
			System.out.println(urgente);
			System.out.println(procedencia);
			System.out.println(itemsPorPagina);
			System.out.println(numeroPagina);
			System.out.println(exportarExcel);
			System.out.println(columnaOrden);
			System.out.println(orden);
			
			query.execute();
			
			listRows = query.getResultList();
			Object[] total = {0};
			total[0] = query.getOutputParameterValue("TotalRegistros");
			listRows.add(0, total);
		}catch(Exception e){
			e.printStackTrace();
			Object[] total = {0};
			listRows.add(0, total);
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}
	// FIN TICKET
	
	/*INI Ticket 9*4413*/
	@Override
	public List<Correspondencia> obtenerInformacionCorrespondenciaMPV(String correlativo) {
		// TODO Auto-generated method stub
		EntityManager em = null;
		List<Correspondencia> listRows = new ArrayList<Correspondencia>();
		String dni="";
		String nombreApellido="";
		String ruc="";
		String razonSocial;
		try{
			
			em = entityManagerBDFilenet.createEntityManager();
            
			StoredProcedureQuery query = em.createStoredProcedureQuery("sic_sp_obtener_datos_correspondencia_MPV");
			
			query.registerStoredProcedureParameter("Correlativo", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("DNI", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NombresApellido", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("RUC", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("RazonSocial", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Asunto", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("TipoCorrespondencia", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NumeroDocumento", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("FechaDocumento", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("NumeroFolio", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Observacion", String.class, ParameterMode.INOUT);
			//query.registerStoredProcedureParameter("CodigoCGC", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("CentroGestion", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("dependenciadestino", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Estado", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("MotivoRechazo", String.class, ParameterMode.INOUT);
			query.registerStoredProcedureParameter("Email", String.class, ParameterMode.INOUT);
			
			
			
			query.setParameter("Correlativo", correlativo);
			query.setParameter("DNI", "");
			query.setParameter("NombresApellido", "");
			query.setParameter("RUC", "");
			query.setParameter("RazonSocial", "");
			query.setParameter("Asunto", "");
			query.setParameter("TipoCorrespondencia", "");
			query.setParameter("NumeroDocumento", "");
			query.setParameter("FechaDocumento", "");
			query.setParameter("NumeroFolio", "");
			query.setParameter("Observacion", "");
			//query.setParameter("CodigoCGC", "");
			query.setParameter("CentroGestion", "");
			query.setParameter("dependenciadestino", "");
			query.setParameter("Estado", "");
			query.setParameter("MotivoRechazo", "");
			query.setParameter("Email", "");
			
			query.execute();
			
			dni = (String)query.getOutputParameterValue("DNI");
			nombreApellido = (String)query.getOutputParameterValue("NombresApellido");
			ruc = (String)query.getOutputParameterValue("RUC");
			razonSocial = (String)query.getOutputParameterValue("RazonSocial");
			
			Correspondencia corres = new Correspondencia();
			corres.setDni(dni);
			corres.setNombresApellido(nombreApellido);
			corres.setRuc(ruc);
			corres.setRazonSocial(razonSocial);
			
			listRows.add(corres);
			
		}catch(Exception e){
            e.printStackTrace();
        }finally{
        	if (em != null) {em.close();}
        }
        
		return listRows;
	}
	/*FIN Ticket 9*4413*/
}
