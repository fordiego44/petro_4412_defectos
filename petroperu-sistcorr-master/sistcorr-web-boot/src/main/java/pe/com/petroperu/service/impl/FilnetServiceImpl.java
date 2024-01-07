package pe.com.petroperu.service.impl;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.model.FiltroConsultaDependencia;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.filenet.model.ItemTipoCorrespondencia;
import pe.com.petroperu.model.DependenciaUnidadMatricial;
import pe.com.petroperu.model.Devolucion;
import pe.com.petroperu.model.Estado;
import pe.com.petroperu.model.Expediente;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.Integrante;
import pe.com.petroperu.model.OrdenServicio;
import pe.com.petroperu.model.ReemplazoAdicion;
import pe.com.petroperu.model.Tracking;
import pe.com.petroperu.model.UsuarioRemplazo;
import pe.com.petroperu.model.Valija;
import pe.com.petroperu.model.VentaBases;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.FuncionariosDTO;
import pe.com.petroperu.model.emision.dto.LogDTO;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;
import pe.com.petroperu.model.emision.dto.ReemplazoRolesAnterioresConsultaDTO;
import pe.com.petroperu.model.emision.dto.TrackingConsultaDTO;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.IFilenetService;
import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.service.dto.FiltroConsultaFuncionariosDTO;
import pe.com.petroperu.service.dto.FiltroConsultaLogDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoDTO;
import pe.com.petroperu.service.dto.FiltroConsultaReemplazoRolesAnterioresDTO;
import pe.com.petroperu.service.dto.FiltroConsultaTrackingDTO;
import pe.com.petroperu.service.impl.FilnetServiceImpl;
import pe.com.petroperu.service.util.IReport;
import pe.com.petroperu.service.util.ReportExcel;
import pe.com.petroperu.service.util.ReportExcelDependencias;
import pe.com.petroperu.service.util.admin.ReporteExcelConsultaReeemplazos;
import pe.com.petroperu.service.util.admin.ReporteExcelFuncionarios;
import pe.com.petroperu.service.util.admin.ReporteExcelLog;
import pe.com.petroperu.service.util.admin.ReporteExcelReemplazos;

@Service
public class FilnetServiceImpl implements IFilenetService {
	
	@Autowired
	private IFilenetDAO filenetDAO;
	@Autowired
	private MessageSource messageSource;//TICKET 9000004275
	
	@Autowired
	private ICorrespondenciaService correspondeciaService;
	
	// TICKET 9000003992
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public List<ItemFilenet> listarPaises(String texto) {
		return this.filenetDAO.listarPaises(texto);
	}

	public List<ItemFilenet> listarDepartamentos(String texto) {
		return this.filenetDAO.listarDepartamentos(texto);
	}

	public List<ItemFilenet> listarProvincias(String codDepartamento, String texto) {
		return this.filenetDAO.listarProvincias(codDepartamento, texto);
	}

	public List<ItemFilenet> listarDistritos(String codDepartamento, String codProvincia, String texto) {
		return this.filenetDAO.listarDistritos(codDepartamento, codProvincia, texto);
	}

	public List<ItemFilenet> listarLugares(String texto) {
		return this.filenetDAO.listarLugares(texto);
	}

	public List<ItemTipoCorrespondencia> listarTiposCorresponciaEmision(String texto) {
		return (List<ItemTipoCorrespondencia>) this.filenetDAO.listarTiposCorresponciaEmision(texto);
	}

	public List<ItemFilenet> listarDependencias(String codLugar, String texto) {
		return this.filenetDAO.listarDependencias(codLugar, texto);
	}
	
	public List<ItemFilenet> listarDependenciasNuevo(String codLugar, String texto) {
		return this.filenetDAO.listarDependenciasNuevo(codLugar, texto).stream()
				.filter(dep -> dep.getText().toLowerCase().contains(texto.toLowerCase()))
				.collect(Collectors.toList());
	}
	
	public List<ItemFilenet> listarDependenciasRemitente(String texto) {
		return this.filenetDAO.listarDependenciasRemitente(texto);
	}
	
	public List<ItemFilenet> listarDependenciasAsignacion(String texto) {
		return this.filenetDAO.listarDependenciasAsignacion(texto);
	}

	public List<ItemFilenet> listarFuncionarios(String codDependencia) {
		return this.filenetDAO.listarFuncionarios(codDependencia);
	}

	public List<ItemFilenet> listarDependenciasExternas(String texto) {
		return this.filenetDAO.listarDependenciasExternas(texto);
	}

	public ItemFilenet obtenerFirmante(String codDependencia) {
		return this.filenetDAO.obtenerFirmante(codDependencia);
	}

	public ItemFilenet obtenerFirmanteRutaAprobacion(String codDependencia) {
		return this.filenetDAO.obtenerFirmanteRutaAprobacion(codDependencia);
	}

	public List<ItemFilenet> listarFuncionariosPorDependencia(String codDependencia, String texto) {
		List<ItemFilenet> lista = this.filenetDAO.listarFuncionariosPorDependencia(codDependencia);
		List<ItemFilenet> respuesta = new ArrayList<>();
		for (ItemFilenet itemFilenet : lista) {
			if (itemFilenet.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
				respuesta.add(itemFilenet);
			}
		}
		return respuesta;
	}

	public String generarCorrelativoTemporal() {
		return this.filenetDAO.generarCorrelativoTemporal();
	}

	public String[] obtenerFechaCGCUsuario(String usuario) {
		String[] respuesta = this.filenetDAO.obtenerFechaCGCUsuario(usuario);
		String fecha = respuesta[2];
		if (fecha != null) {
			try {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date1 = (new SimpleDateFormat("dd/MM/yy")).parse(fecha);
				fecha = dateFormat.format(date1);
				respuesta[2] = fecha;
			} catch (Exception exception) {
			}
		}

		return respuesta;
	}

	public List<ItemFilenet> obtenerDependenciaPorUsuario(String usuario, String texto) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] obtenerDependenciaPorUsuario");
		List<ItemFilenet> listaFiltrada = new ArrayList<>();
		List<ItemFilenet> lista = this.filenetDAO.obtenerDependenciaPorUsuario(usuario);
		// TICKET 9000003992
		this.LOGGER.info("[INFO] obtenerDependenciaPorUsuario " + " This is info : Cantidad de dependencias destino "
				+ lista.size());
		// System.out.println("Cantidad de dependencias destino:" +
		// lista.size());
		for (ItemFilenet itemFilenet : lista) {
			if (itemFilenet.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
				listaFiltrada.add(itemFilenet);
			}
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] obtenerDependenciaPorUsuario");
		return listaFiltrada;
	}
	
	// TICKET 9000003944
	public List<ItemFilenet> obtenerDependenciaPorUsuarioConsulta(String usuario, String texto) {
		this.LOGGER.info("[INICIO] obtenerDependenciaPorUsuarioConsulta");
		List<ItemFilenet> listaFiltrada = new ArrayList<>();
		List<ItemFilenet> lista = this.filenetDAO.obtenerDependenciaPorUsuario(usuario);
		this.LOGGER.info("[INFO] obtenerDependenciaPorUsuarioConsulta " + " This is info : Cantidad de dependencias destino "
				+ lista.size());
		for (ItemFilenet itemFilenet : lista) {
			if (itemFilenet.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
				listaFiltrada.add(itemFilenet);
			}
		}
		this.LOGGER.info("[FIN] obtenerDependenciaPorUsuarioConsulta");
		return listaFiltrada;
	}
	// FIN TICKET

	public ItemFilenet obtenerLugarPorDependencia(String codDependencia) {
		return this.filenetDAO.obtenerLugarPorDependencia(codDependencia);
	}

	public List<ItemFilenet> listarDependenciasExternas(String codDepartamento, String codProvincia, String codDistrito,
			String texto) {
		return this.filenetDAO.listarDependenciasExternas(codDepartamento, codProvincia, codDistrito, texto);
	}

	@Override
	public List<ItemFilenet> listarDependenciasSuperiores(String codDependencia,  String textoFiltro) {
		List<ItemFilenet> dependencias = filenetDAO.listarDependenciasSuperiores(codDependencia).stream()
										.filter(dep -> dep.getText().toLowerCase().contains(textoFiltro.toLowerCase()))
										.collect(Collectors.toList());
		dependencias = dependencias == null ? new ArrayList<>() : dependencias;
		return dependencias;
	}

	@Override
	public List<ItemFilenet> listarDependenciasSubordinadas(String codDependencia) {
		List<ItemFilenet> dependencias = filenetDAO.listarDependenciasSubordinadas(codDependencia);
		dependencias = dependencias == null ? new ArrayList<>() : dependencias;
		return dependencias;
	}
	
	@Override
	public List<ItemFilenet> listarTodosFuncionarios(String filtro){
		List<ItemFilenet> funcionarios = filenetDAO.listarTodosFuncionarios(filtro);
		funcionarios = funcionarios == null ? new ArrayList<>() : funcionarios;
		return funcionarios;
	}
	
	
	@Override
	public List<ItemFilenet> listarTodosDependencias(String filtro){
		List<ItemFilenet> dependencias = filenetDAO.listarTodosDependencias(filtro);
		dependencias = dependencias == null ? new ArrayList<>() : dependencias;
		return dependencias;
	}
	
	@Override
	public List<ItemFilenet> listarTodosDependenciasExternas(String filtro){
		List<ItemFilenet> dependencias = filenetDAO.listarTodosDependenciasExternas(filtro);
		dependencias = dependencias == null ? new ArrayList<>() : dependencias;
		return dependencias;
	}
	
	// TICKET 9000003514
	public List<Estado> listarEstados(String tipo){
		return this.filenetDAO.listarEstados(tipo);
	}
	
	public List<Estado> listarEstadosAsignacion(){
		return this.filenetDAO.listarEstadosAsignacion();
	}
	
	public List<Estado> listarTiposCorrespondencia(){
		return this.filenetDAO.listarTiposCorrespondencia();
	}
	
	public List<Funcionario> listarPersonaAsignada(String codigoDependencia, String codigoLugar, String Cadena, String MInclNoActivo){
		return this.filenetDAO.listarPersonaAsignada(codigoDependencia, codigoLugar, Cadena, MInclNoActivo);
	}
	// FIN TICKET 9000003514
	
	// TICKET 9000003780
	public List<ItemFilenet> listarDependenciasNivelJerarquia(String codigo, String cgc){
		return this.filenetDAO.listarDependenciasNivelJerarquia(codigo, cgc);
	}
	// FIN TICKET
	
	// TICKET 9000003997
	public List<ItemFilenet> obtenerStatusCorrespondencia(String nroDocumento){
		return this.filenetDAO.obtenerStatusCorrespondencia(nroDocumento);
	}
	// FIN TICKET
	
	// TICKET 9000003994
	public List<ItemFilenet> obtenerDependenciasGestor(String usuario){
		return this.filenetDAO.obtenerDependenciasGestor(usuario);
	}
	// FIN TICKET
	
	// TICKET 9000003944
	public List<DependenciaUnidadMatricial> buscarDependencias(int codigo, String nombre, String tipo, String jefe) {
		return this.filenetDAO.buscarDependencias(codigo, nombre, tipo, jefe);
	}
	
	public List<ItemFilenet> listarTipoUnidadMatricial() {
		return this.filenetDAO.listarTipoUnidadMatricial();
	}
	
	public List<ItemFilenet> listarJerarquia() {
		return this.filenetDAO.listarJerarquia();
	}
	
	public List<ItemFilenet> listarDependenciasUnidadMatricial(int jerarquia, String codLugar){
		return this.filenetDAO.listarDependenciasUnidadMatricial(jerarquia, codLugar);
	}
	
	public List<ItemFilenet> registrarDependencia(DependenciaUnidadMatricial dep, String usuario){
		return this.filenetDAO.registrarDependencia(dep, usuario);
	}
	
	public List<ItemFilenet> modificarDependencia(DependenciaUnidadMatricial dep, String usuario){
		return this.filenetDAO.modificarDependencia(dep, usuario);
	}
	
	public List<ItemFilenet> registrarIntegrante(String integrante, int codigoDependencia){
		return this.filenetDAO.registrarIntegrante(integrante, codigoDependencia);
	}
	
	public List<ItemFilenet> eliminarIntegrante(String integrante, int codigoDependencia){
		return this.filenetDAO.eliminarIntegrante(integrante, codigoDependencia);
	}
	@Override
	public String generarPlanilla(String usuario, String alcance, String courier, String urgente) {
		LOGGER.info("[INICIO] GENERAR PLANILLA");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			estado = filenetDAO.generarPlanilla(usuario, alcance, courier, urgente);
			 System.out.println("estado:" + estado);

			/*List<ItemFilenet> result = this.registrarTracking(tracking, usuario, modo);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			} */
		}catch(Exception e){
			LOGGER.error("[ERROR] registrarTracking ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	@Override
	public String generarPlanillaGuiaRemision(String usuario, String lugarTrabajo, String courier) {
		LOGGER.info("[INICIO] GENERAR PLANILLA GUIA REMISION");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			estado = filenetDAO.generarPlanillaGuiaRemision(usuario, lugarTrabajo, courier);
			 System.out.println("estado:" + estado);
 
		}catch(Exception e){
			LOGGER.error("[ERROR] registrarTracking ", e);
			estado = "ERROR";
		}
		return estado;
	}
	@Override
	public Respuesta<ByteArrayInputStream> consultarDependenciasExcel(FiltroConsultaDependencia filtro, String nombreUsuario, 
			List<ItemFilenet> datosFuncionarios, List<ItemFilenet> jerarquias, List<ItemFilenet> lugares,
			List<ItemFilenet> dependencias, Locale locale){
		LOGGER.info("[INICIO] consultarDependenciasExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		int codigo = 0;
		try{
			codigo = Integer.valueOf(String.valueOf(filtro.getCodigoDependencia()));
		}catch(Exception e){
			codigo = 0;
		}
		try {
			List<DependenciaUnidadMatricial> consulta = filenetDAO.buscarDependencias(codigo, filtro.getNombreDependencia(), 
					filtro.getTipo(), filtro.getJefe()); 
			for(int i=0;i<consulta.size();i++){
				DependenciaUnidadMatricial dep = consulta.get(i);
				// TIPO
				if("DEPENDENCIA".equalsIgnoreCase(dep.getTipo().toUpperCase())){
					consulta.get(i).setTipo("DEPENDENCIA");
					consulta.get(i).setTipoUnidadMatricial("");
				}else{
					consulta.get(i).setTipoUnidadMatricial(consulta.get(i).getTipo());
					consulta.get(i).setTipo("UNIDAD MATRICIAL");
				}
				// ESTADO
				if("SI".equalsIgnoreCase(dep.getEstado().toUpperCase())){
					consulta.get(i).setEstado("Activo");
				}else{
					consulta.get(i).setEstado("Inactivo");
				}
				// JEFE
				for(ItemFilenet it : datosFuncionarios){
					if(it.getCodigo().toUpperCase().equalsIgnoreCase(dep.getJefe().toUpperCase())){
						consulta.get(i).setJefe(it.getDescripcion());
						break;
					}
				}
				// INTEGRANTES
				if("DEPENDENCIA".equalsIgnoreCase(dep.getTipo().toUpperCase())){
					List<Funcionario> integrantes = filenetDAO.listarPersonaAsignada(dep.getCodigo(), "", "", "");
					this.LOGGER.info(i + "/" + consulta.size() + ":" + integrantes.size());
					List<Integrante> integr = new ArrayList<>();
					//String textoIntegrantes = "";
					for(int j=0;j<integrantes.size();j++){
						Integrante inte = new Integrante();
						this.LOGGER.info(integrantes.get(j).getNombreApellido() + "-" + integrantes.get(j).getNombreApellidoUsuario());
						inte.setNombreIntegrante(integrantes.get(j).getNombreApellido());
						integr.add(inte);
					}
					consulta.get(i).setIntegrantes(integr);
				}else{
					List<Integrante> integrantes = filenetDAO.listarIntegrantesUM(dep.getCodigo());
					consulta.get(i).setIntegrantes(integrantes);
				}
				// JERARQUIA
				for(ItemFilenet it : jerarquias){
					if(it.getCodigo().toUpperCase().equalsIgnoreCase(dep.getJerarquia().toUpperCase())){
						consulta.get(i).setJerarquia(it.getDescripcion());
						break;
					}
				}
				// LUGARES
				boolean encontradoLug = false;
				for(ItemFilenet it : lugares){
					if(it.getCodigo().toUpperCase().equalsIgnoreCase(dep.getLugarTrabajo().toUpperCase())){
						consulta.get(i).setLugarTrabajo(it.getDescripcion());
						encontradoLug = true;
						break;
					}
				}
				if(!encontradoLug){
					consulta.get(i).setLugarTrabajo("");
				}
				// DEPENDENCIA SUPERIOR
				boolean encontradoDep = false;
				for(ItemFilenet it : dependencias){
					if(it.getCodigo().toUpperCase().equalsIgnoreCase(dep.getDependenciaSuperior().toUpperCase())){
						consulta.get(i).setDependenciaSuperior(it.getDescripcion());
						encontradoDep = true;
						break;
					}
				}
				if(!encontradoDep){
					consulta.get(i).setDependenciaSuperior("");
				}
			}
			IReport<ByteArrayInputStream> report;
			if(consulta != null) {
				report = new ReportExcelDependencias(consulta, nombreUsuario);
			} else {
				report = new ReportExcelDependencias(new ArrayList<>(), nombreUsuario);
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
	
	public String registrarDependenciaUnidadMatricialCompleto(DependenciaUnidadMatricial dependencia, String usuario){
		LOGGER.info("[INICIO] registrarDependenciaUnidadMatricialCompleto");
		String estado = "";
		try{
			List<ItemFilenet> result = this.registrarDependencia(dependencia, usuario);
			if(result != null && result.size() > 0){
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					for(int i=0;i<dependencia.getIntegrantes().size();i++){
						this.registrarIntegrante(dependencia.getIntegrantes().get(i).getCodigoIntegrante(), Integer.valueOf(dependencia.getCodigo()));
					}
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] registrarDependenciaUnidadMatricialCompleto ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	public boolean modificarDependenciaUnidadMatricialCompleto(DependenciaUnidadMatricial dependencia, String usuario){
		LOGGER.info("[INICIO] modificarDependenciaUnidadMatricialCompleto");
		boolean estado = true;
		try{
			this.modificarDependencia(dependencia, usuario);
			if("D".equalsIgnoreCase(dependencia.getTipo())){
				List<Funcionario> integrantes = filenetDAO.listarPersonaAsignada(dependencia.getCodigo(), "", "", "");
				List<Funcionario> integrantesRepetidos = new ArrayList<>();
				for(int i=0;i<dependencia.getIntegrantes().size();i++){
					//System.out.println("Integrante buscado:" + dependencia.getIntegrantes().get(i).getCodigoIntegrante());
					boolean repetido = false;
					for(int j=0;j<integrantes.size();j++){
						//System.out.println("Comparar con:" + integrantes.get(j).getId());
						if(integrantes.get(j).getId().equalsIgnoreCase(dependencia.getIntegrantes().get(i).getCodigoIntegrante())){
							integrantesRepetidos.add(integrantes.get(j));
							repetido = true;
							break;
						}
					}
					if(!repetido){
						//System.out.println("Registrando integrante:" + dependencia.getIntegrantes().get(i).getCodigoIntegrante());
						this.registrarIntegrante(dependencia.getIntegrantes().get(i).getCodigoIntegrante(), Integer.valueOf(dependencia.getCodigo()));
					}
				}
				for(int i=0;i<integrantes.size();i++){
					//System.out.println("Eliminar integrante? " + integrantes.get(i).getId());
					boolean encontrado = false;
					for(int j=0;j<integrantesRepetidos.size();j++){
						//System.out.println("Encontrado? " + integrantesRepetidos.get(j).getId());
						if(integrantes.get(i).getId().equalsIgnoreCase(integrantesRepetidos.get(j).getId())){
							encontrado = true;
						}
					}
					if(!encontrado){
						int codigo = Integer.valueOf(dependencia.getCodigo());
						//System.out.println("Eliminando:" + integrantes.get(i).getId());
						filenetDAO.eliminarIntegrante(integrantes.get(i).getId(), codigo);
					}
				}
			}else{
				List<Integrante> integrantes = filenetDAO.listarIntegrantesUM(dependencia.getCodigo());
				List<Integrante> integrantesRepetidos = new ArrayList<>();
				for(int i=0;i<dependencia.getIntegrantes().size();i++){
					//System.out.println("Integrante buscado:" + dependencia.getIntegrantes().get(i).getCodigoIntegrante());
					boolean repetido = false;
					for(int j=0;j<integrantes.size();j++){
						//System.out.println("Comparar con:" + integrantes.get(j).getCodigoIntegrante());
						if(integrantes.get(j).getCodigoIntegrante().equalsIgnoreCase(dependencia.getIntegrantes().get(i).getCodigoIntegrante())){
							integrantesRepetidos.add(integrantes.get(j));
							repetido = true;
							break;
						}
					}
					if(!repetido){
						//System.out.println("Registrando integrante:" + dependencia.getIntegrantes().get(i).getCodigoIntegrante());
						this.registrarIntegrante(dependencia.getIntegrantes().get(i).getCodigoIntegrante(), Integer.valueOf(dependencia.getCodigo()));
					}
				}
				for(int i=0;i<integrantes.size();i++){
					//System.out.println("Eliminar integrante? " + integrantes.get(i).getCodigoIntegrante());
					boolean encontrado = false;
					for(int j=0;j<integrantesRepetidos.size();j++){
						//System.out.println("Encontrado? " + integrantesRepetidos.get(j).getCodigoIntegrante());
						if(integrantes.get(i).getCodigoIntegrante().equalsIgnoreCase(integrantesRepetidos.get(j).getCodigoIntegrante())){
							encontrado = true;
						}
					}
					if(!encontrado){
						int codigo = Integer.valueOf(dependencia.getCodigo());
						//System.out.println("Eliminando:" + integrantes.get(i).getCodigoIntegrante());
						filenetDAO.eliminarIntegrante(integrantes.get(i).getCodigoIntegrante(), codigo);
					}
				}
			}
			
		}catch(Exception e){
			LOGGER.error("[ERROR] modificarDependenciaUnidadMatricialCompleto ", e);
			estado = false;
		}
		return estado;
	}
	
	public List<Funcionario> listaIntegrante(String codigoDependencia, String codigoLugar,String Cadena, String MInclNoActivo){
		return this.filenetDAO.listarPersonaAsignada(codigoDependencia, codigoLugar, Cadena, MInclNoActivo);
	}
	
	public List<ItemFilenet> listarDependenciasInterno(String codLugar, String texto) {
		return this.filenetDAO.listarDependenciasInterno(codLugar, texto);
	}
	
	public List<Integrante> listarIntegrantesUM(String codigoDependencia){
		return this.filenetDAO.listarIntegrantesUM(codigoDependencia);
	}
	// FIN TICKET
	
	//Inicio 4408
	@Override
	public List<ItemFilenet> listarTodosDependenciasJefeGestor(String filtro , String parametro, List<Integer> rolesInt) {
		List<ItemFilenet> dependencias = null;
		List<ItemFilenet> dependenciasJefe = null;
		if(rolesInt.contains(2)){
		 dependencias = filenetDAO.obtenerDependenciasGestor(filtro);
		}
		dependencias = dependencias == null ? new ArrayList<>() : dependencias;	
		if(rolesInt.contains(1)){
		 dependenciasJefe = filenetDAO.obtenerDependenciasJefe(filtro);
		}
		dependenciasJefe = dependenciasJefe == null ? new ArrayList<>() : dependenciasJefe;
		dependencias.addAll(dependenciasJefe);		
		List<ItemFilenet> newDependencias = dependencias.stream().distinct().collect(Collectors.toList());
		
		
		
		return newDependencias;		
	}
	//Fin 4408
	
	//ticket 9000004409
	public String validarRemplazoVigenteUsuario(String usuario){
		return this.filenetDAO.validarRemplazoVigenteUsuario(usuario);
	}
	
	public List<UsuarioRemplazo> obtenerUsuarioRemplazo(String usuario){
		return this.filenetDAO.obtenerUsuarioRemplazo(usuario);
	}
	// fin ticket 9000004409
	
	/* 9000004276 - INICIO */
	@Override
	public List<ItemFilenet> listarTiposProceso() {
		return this.filenetDAO.listarTiposProceso();
	}

	@Override
	public List<ItemFilenet> listarDependenciasCEE(String registro, String cadena,String funcionario) {
		return this.filenetDAO.listarDependenciasCEE(registro, cadena,funcionario);
	}
	/* 9000004276 - FIN */

	/*INI Ticket 9*4275*/
	@Override
	public List<ItemFilenet> listaCGC(String texto) {
		return this.filenetDAO.listaCGC(texto);
	}
	
	@Override
	public List<ItemFilenet> listaCouriers(String texto) {
		return this.filenetDAO.listaCouriers(texto);
	}
	
	@Override
	public List<ItemFilenet> listaLugares(String texto) {
		return this.filenetDAO.listaLugares(texto);
	}
	
	@Override
	public List<ItemFilenet> listarCiudades(String codPais, String texto) {
		return this.filenetDAO.listarCiudades(codPais, texto);
	}
	
	@Override
	public List<ItemFilenet> listarCiudadesPorPais(String codPais, String texto) {
		return this.filenetDAO.listarCiudadesPorPais(codPais, texto);
	}
	
	@Override
	public List<ItemFilenet> listaNumeradores() {
		return this.filenetDAO.listaNumeradores();
	}
	
	@Override
	public List<ItemFilenet> listaFuncionariosTodos() {
		return this.filenetDAO.listaFuncionariosTodos();
	}
	
	public List<ItemFilenet>obtenerTablas(){
		return this.filenetDAO.obtenerTablas();
	}
	
	public List<ItemFilenet> guardarFuncionario(Funcionario funcionario, String username){
		return this.filenetDAO.guardarFuncionario(funcionario, username);
	}
	
	public List<ItemFilenet> eliminarFuncionario(Integer idFuncinario,String registro, String username){
		return this.filenetDAO.eliminarFuncionario(idFuncinario,registro, username);
	}
	
	public List<ItemFilenet> listarDependenciaApoyo(String tipoReemplazo, String term){
		return this.filenetDAO.listarDependenciaApoyo(tipoReemplazo, term);
	}
	
	public List<ItemFilenet>listarJefeXDependencia(String codDepend, String rol, String term){
		return this.filenetDAO.listarJefeXDependencia(codDepend,rol, term);
	}
	
	public List<ItemFilenet>listarFuncionariosApoyo(String tipoReemp,String codDepend, String rol, String term){
		return this.filenetDAO.listarFuncionariosApoyo(tipoReemp,codDepend,rol,term);
	}
	
	public List<ItemFilenet>comboRolDependenciaReemplazo(String codDepend){
		return this.filenetDAO.comboRolDependenciaReemplazo(codDepend);
	}
	
	public List<ItemFilenet> eliminarReemplazo(Integer idReemplazo, String usuario){
		return this.filenetDAO.eliminarReemplazo(idReemplazo,usuario);
	}
	
	public List<ItemFilenet> validarReemplazo(ReemplazoConsultaDTO reemplazo, String tipoReemplazo, String usuario){
		return this.filenetDAO.validarReemplazo(reemplazo,tipoReemplazo,usuario);
	}
	
	public List<ItemFilenet> guardarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String username){
		return this.filenetDAO.guardarReemplazoAdicion(reemplazo, username);
	}
	
	public List<ItemFilenet> obtenerRolDepOriginal(String usuarioEntrante, String username){
		return this.filenetDAO.obtenerRolDepOriginal(usuarioEntrante, username);
	}
	
	public List<ItemFilenet> actualizarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String username){
		return this.filenetDAO.actualizarReemplazoAdicion(reemplazo, username);
	}
	
	public List<ItemFilenet> obtenerValorVar(String username,String variableName){
		return this.filenetDAO.obtenerValorVar(username,variableName);
	}
	public List<ItemFilenet> eliminarRemplazo(ReemplazoConsultaDTO reemplazo, String usuario) {
		// TODO Auto-generated method stub
		return this.filenetDAO.eliminarReemplazo(reemplazo, usuario);
	}
	public List<ReemplazoAdicion> listarReemplazoAdicion(ReemplazoConsultaDTO reemplazo, String usuario) {
		// TODO Auto-generated method stub
		return this.filenetDAO.listarReemplazosAdicion(reemplazo, usuario);
	}
	public List<ItemFilenet> modificarValorVariale(String usuario, String variableName, String valorStr, Integer valor) {
		// TODO Auto-generated method stub
		return this.filenetDAO.modificarValorVariale(usuario, variableName, valorStr, valor);
	}
	@Override
	public List<ItemFilenet> listarLugaresTV(String texto) {
		// TODO Auto-generated method stub
		return this.filenetDAO.listarLugaresTV(texto);
	}
	
	@Override
	public Respuesta<LogDTO> consultaLogPaginado(FiltroConsultaLogDTO filtro,String token, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<LogDTO> respuesta = new Respuesta<>();
	    try {
	    	List<Object[]> listObjs = this.filenetDAO.consultaLog(filtro.getTabla(), filtro.getUsuario(), filtro.getAccion(), 
	    			filtro.getFechaDesdeText(), filtro.getFechaHastaText(),token, itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel,locale);
	      LOGGER.info("Logs obtenidos:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  System.out.println("Total Service:" + Integer.valueOf(String.valueOf(total[0])));
	    	  listObjs.remove(0);
	    	  List<LogDTO> lstLog = new ArrayList<>();
	    	  LOGGER.info("Correspondencias obtenidas:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  LogDTO obj = new LogDTO();
	    		  obj.setNombreTabla(obja[2] != null?obja[2].toString():"");
	    		  obj.setTipoTransaccion(obja[3] != null?obja[3].toString():"");
	    		  obj.setUsuario(obja[4] != null?obja[4].toString():"");
	    		  obj.setFecha(obja[5] != null?obja[5].toString():"");
	    		  obj.setIdArtefacto(obja[6] != null?obja[6].toString():"");
	    		  obj.setMensaje(obja[7] != null?obja[7].toString():"");
	    		  lstLog.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(lstLog);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
				
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}

	@Override
	public Respuesta<ByteArrayInputStream> consultarLogExcel(FiltroConsultaLogDTO filtro, String usuarioCreado,
			String usuario, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] consultarCorrespondenciasExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			List<Object[]> listObjs = this.filenetDAO.consultaLog(filtro.getTabla(), filtro.getUsuario(), filtro.getAccion(), 
	    			filtro.getFechaDesdeText(), filtro.getFechaHastaText(),"", 0,0, "", "", "SI",locale);
			
			IReport<ByteArrayInputStream> report;
			if(listObjs != null) {
				report = new ReporteExcelLog(listObjs, usuarioCreado);
			} else {
				report = new ReporteExcelLog(listObjs, usuarioCreado,"LOG");
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
	public Respuesta<ReemplazoRolesAnterioresConsultaDTO> consultaRolesAnterioresPaginado(String usuario,
			FiltroConsultaReemplazoRolesAnterioresDTO filtro, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String tipo, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<ReemplazoRolesAnterioresConsultaDTO> respuesta = new Respuesta<>();
	    try {
	    	Integer codDependenciaRemitente=0;
	    	Integer codDependenciaDestinatario=0;
	    	if (filtro.getCodDependenciaDestinatario() == null || filtro.getCodDependenciaDestinatario().equalsIgnoreCase("")){
	    		codDependenciaDestinatario=0;
	    	}else{
	    		codDependenciaDestinatario=Integer.valueOf(filtro.getCodDependenciaDestinatario());
	    	}
	    	
	    	if (filtro.getCodDependenciaRemitente() == null || filtro.getCodDependenciaRemitente().equalsIgnoreCase("")){
	    		codDependenciaRemitente=0;
	    	}else{
	    		codDependenciaRemitente=Integer.valueOf(filtro.getCodDependenciaRemitente());
	    	}
	    	
	    	List<Object[]> listObjs = this.filenetDAO.consultaRolesAnterioresPaginado(usuario, "CR","TOTAL", filtro.getCorrelativo(),filtro.getFechaDesdeText(), filtro.getFechaHastaText(),filtro.getTipoCorrespondencia(), 
	    					filtro.getEstado(),filtro.getFechaText(),
	    					codDependenciaDestinatario,
	    					codDependenciaRemitente, 
	    					filtro.getEntidadExterna(), filtro.getAsunto(),filtro.getNroDocumento(), filtro.getRol(), itemsPorPagina, numeroPagina, columnaOrden, orden, tipo);
	      LOGGER.info("Logs obtenidos:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  System.out.println("Total Service:" + Integer.valueOf(String.valueOf(total[0])));
	    	  //listObjs.remove(0);
	    	  List<ReemplazoRolesAnterioresConsultaDTO> lst = new ArrayList<>();
	    	  LOGGER.info("Roles Anteriores Obtenidoos:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  ReemplazoRolesAnterioresConsultaDTO obj = new ReemplazoRolesAnterioresConsultaDTO();
	    		  obj.setRadicado(obja[1] != null?obja[1].toString():"");
	    		  obj.setNroDocInterno(obja[4] != null?obja[4].toString():"");
	    		  obj.setAsunto(obja[3] != null?obja[3].toString():"");
	    		  obj.setFecha(obja[2] != null?obja[2].toString():"");	    		  
	    		  obj.setOrigen(obja[5] != null?obja[5].toString():"");
	    		  obj.setDestino(obja[6] != null?obja[6].toString():"");
	    		  obj.setEstado(obja[7] != null?obja[7].toString():"");
	    		  obj.setTipoCorrespondencia(obja[8] != null?obja[8].toString():"");
	    		  obj.setRoles(obja[9] != null?obja[9].toString():"");
	    		  lst.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(lst);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
				
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}

	@Override
	public Respuesta<ByteArrayInputStream> consultarRolesAnterioresExcel(
			FiltroConsultaReemplazoRolesAnterioresDTO filtro, String usuarioCreado, String usuario, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] consultarCorrespondenciasExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			
			Integer codDependenciaRemitente=0;
	    	Integer codDependenciaDestinatario=0;
	    	if (filtro.getCodDependenciaDestinatario() == null || filtro.getCodDependenciaDestinatario().equalsIgnoreCase("")){
	    		codDependenciaDestinatario=0;
	    	}else{
	    		codDependenciaDestinatario=Integer.valueOf(filtro.getCodDependenciaDestinatario());
	    	}
	    	
	    	if (filtro.getCodDependenciaRemitente() == null || filtro.getCodDependenciaRemitente().equalsIgnoreCase("")){
	    		codDependenciaRemitente=0;
	    	}else{
	    		codDependenciaRemitente=Integer.valueOf(filtro.getCodDependenciaRemitente());
	    	}
			
			List<Object[]> listObjs = this.filenetDAO.consultaRolesAnterioresPaginado(usuario, "CR","TOTAL", filtro.getCorrelativo(),filtro.getFechaDesdeText(), filtro.getFechaHastaText(),filtro.getTipoCorrespondencia(), 
					filtro.getEstado(),filtro.getFechaText(),
					codDependenciaDestinatario,
					codDependenciaRemitente, 
					filtro.getEntidadExterna(), filtro.getAsunto(),filtro.getNroDocumento(), filtro.getRol(), 20000, 1, "", "", "SI");
			
			IReport<ByteArrayInputStream> report;
			if(listObjs != null) {
				report = new ReporteExcelConsultaReeemplazos(listObjs, usuarioCreado,"ROLES ANTERIORES");
			} else {
				report = new ReporteExcelConsultaReeemplazos(listObjs, usuarioCreado,"ROLES ANTERIORES");
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
	public Respuesta<ReemplazoRolesAnterioresConsultaDTO> consultaRolesReemplazoPaginado(String usuario,
			FiltroConsultaReemplazoRolesAnterioresDTO filtro, Integer itemsPorPagina, Integer numeroPagina,
			String columnaOrden, String orden, String excel, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<ReemplazoRolesAnterioresConsultaDTO> respuesta = new Respuesta<>();
	    try {
	    	Integer codDependenciaRemitente=0;
	    	Integer codDependenciaDestinatario=0;
	    	if (filtro.getCodDependenciaDestinatario() == null || filtro.getCodDependenciaDestinatario().equalsIgnoreCase("")){
	    		codDependenciaDestinatario=0;
	    	}else{
	    		codDependenciaDestinatario=Integer.valueOf(filtro.getCodDependenciaDestinatario());
	    	}
	    	
	    	if (filtro.getCodDependenciaRemitente() == null || filtro.getCodDependenciaRemitente().equalsIgnoreCase("")){
	    		codDependenciaRemitente=0;
	    	}else{
	    		codDependenciaRemitente=Integer.valueOf(filtro.getCodDependenciaRemitente());
	    	}
	    	
	    	List<Object[]> listObjs = this.filenetDAO.consultaRolesAnterioresPaginado(usuario, "CR","ADICION", filtro.getCorrelativo(),filtro.getFechaDesdeText(), filtro.getFechaHastaText(),filtro.getTipoCorrespondencia(), 
	    					filtro.getEstado(),filtro.getFechaText(),
	    					codDependenciaDestinatario,
	    					codDependenciaRemitente, 
	    					filtro.getEntidadExterna(), filtro.getAsunto(),filtro.getNroDocumento(), filtro.getRol(), itemsPorPagina, numeroPagina, columnaOrden, orden, excel);
	      LOGGER.info("Logs obtenidos:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  System.out.println("Total Service:" + Integer.valueOf(String.valueOf(total[0])));
	    	  //listObjs.remove(0);
	    	  List<ReemplazoRolesAnterioresConsultaDTO> lst = new ArrayList<>();
	    	  LOGGER.info("Roles Anteriores Obtenidoos:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  ReemplazoRolesAnterioresConsultaDTO obj = new ReemplazoRolesAnterioresConsultaDTO();
	    		  obj.setRadicado(obja[1] != null?obja[1].toString():"");
	    		  obj.setNroDocInterno(obja[4] != null?obja[4].toString():"");
	    		  obj.setAsunto(obja[3] != null?obja[3].toString():"");
	    		  obj.setFecha(obja[2] != null?obja[2].toString():"");	    		  
	    		  obj.setOrigen(obja[6] != null?obja[6].toString():"");
	    		  obj.setDestino(obja[5] != null?obja[5].toString():"");
	    		  obj.setEstado(obja[7] != null?obja[7].toString():"");
	    		  obj.setTipoCorrespondencia(obja[8] != null?obja[8].toString():"");
	    		  obj.setRoles(obja[9] != null?obja[9].toString():"");
	    		  lst.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(lst);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
				
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}

	@Override
	public Respuesta<ByteArrayInputStream> consultarRolesReemplazosExcel(
			FiltroConsultaReemplazoRolesAnterioresDTO filtro, String usuarioCreado, String usuario, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] consultarRolesReemplazosExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			
			Integer codDependenciaRemitente=0;
	    	Integer codDependenciaDestinatario=0;
	    	if (filtro.getCodDependenciaDestinatario() == null || filtro.getCodDependenciaDestinatario().equalsIgnoreCase("")){
	    		codDependenciaDestinatario=0;
	    	}else{
	    		codDependenciaDestinatario=Integer.valueOf(filtro.getCodDependenciaDestinatario());
	    	}
	    	
	    	if (filtro.getCodDependenciaRemitente() == null || filtro.getCodDependenciaRemitente().equalsIgnoreCase("")){
	    		codDependenciaRemitente=0;
	    	}else{
	    		codDependenciaRemitente=Integer.valueOf(filtro.getCodDependenciaRemitente());
	    	}
			
			List<Object[]> listObjs = this.filenetDAO.consultaRolesAnterioresPaginado(usuario, "CR","ADICION", filtro.getCorrelativo(),filtro.getFechaDesdeText(), filtro.getFechaHastaText(),filtro.getTipoCorrespondencia(), 
					filtro.getEstado(),filtro.getFechaText(),
					codDependenciaDestinatario,
					codDependenciaRemitente, 
					filtro.getEntidadExterna(), filtro.getAsunto(),filtro.getNroDocumento(), filtro.getRol(), 20000, 1, "", "", "SI");
			
			IReport<ByteArrayInputStream> report;
			if(listObjs != null) {
				report = new ReporteExcelConsultaReeemplazos(listObjs, usuarioCreado,"ROLES REEMPLAZOS");
			} else {
				report = new ReporteExcelConsultaReeemplazos(listObjs, usuarioCreado,"ROLES REEMPLAZOS");
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
	public Respuesta<FuncionariosDTO> consultaFuncionariosPaginado(String token, FiltroConsultaFuncionariosDTO filtro,
			Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String excel,
			Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<FuncionariosDTO> respuesta = new Respuesta<>();
	    try {
	    	
	    	List<Object[]> listObjs = this.filenetDAO.consultaAdministracionFuncionarios(token,filtro.getDependencia(),filtro.getRegistro(),
	    			filtro.getNombres(),filtro.getApellidos(),itemsPorPagina, numeroPagina, columnaOrden, orden, excel);
	      LOGGER.info("consultaFuncionariosPaginado obtenidos:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  System.out.println("Total Service:" + Integer.valueOf(String.valueOf(total[0])));
	    	  listObjs.remove(0);
	    	  List<FuncionariosDTO> lstLog = new ArrayList<>();
	    	  LOGGER.info("consultaFuncionariosPaginado obtenidas:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  FuncionariosDTO obj = new FuncionariosDTO();
	    		  obj.setId(obja[1] != null?(Integer)obja[1]:0);
	    		  obj.setRegistro(obja[2] != null?obja[2].toString():"");
	    		  obj.setNombreApellidos(obja[3] != null?obja[3].toString():"");
	    		  obj.setNombre1(obja[4] != null?obja[4].toString():"");
	    		  obj.setNombre2(obja[5] != null?obja[5].toString():"");
	    		  obj.setApellido1(obja[6] != null?obja[6].toString():"");
	    		  obj.setApellido2(obja[7] != null?obja[7].toString():"");
	    		  obj.setEmail(obja[8] != null?obja[8].toString():"");
	    		  obj.setCodigoDependencia(obja[9] != null?(Integer)obja[9]:0);;
	    		  obj.setNombreDependencia(obja[10] != null?obja[10].toString():"");
	    		  obj.setNotificaciones(obja[11] != null?obja[11].toString():"");
	    		  obj.setProceso(obja[12] != null?obja[12].toString():"");
	    		  obj.setTipoFuncionario(obja[13] != null?obja[13].toString():"");
	    		  obj.setFicha(obja[14] != null?obja[14].toString():"");
	    		  obj.setOperaciones(obja[15] != null?obja[15].toString():"");
	    		  obj.setSupervisor(obja[16] != null?obja[16].toString():"");
	    		  obj.setActivo(obja[17] != null?obja[17].toString():"");
	    		  lstLog.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(lstLog);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
				
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}

	@Override
	public Respuesta<ByteArrayInputStream> consultarFuncionarioExcel(FiltroConsultaFuncionariosDTO filtro,
			String usuarioCreado, String usuario, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] consultarCorrespondenciasExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			
			List<Object[]> listObjs = this.filenetDAO.consultaAdministracionFuncionarios(usuarioCreado,filtro.getDependencia(),filtro.getRegistro(),
	    			filtro.getNombres(),filtro.getApellidos(),0, 0, "", "", "SI");
			
			IReport<ByteArrayInputStream> report;
			if(listObjs != null) {
				report = new ReporteExcelFuncionarios(listObjs, usuarioCreado);
			} else {
				report = new ReporteExcelFuncionarios(listObjs, usuarioCreado,"FUNCIONARIOS");
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
	public Respuesta<ReemplazoConsultaDTO> consultaReemplazoApoyoPaginado(String token,
			FiltroConsultaReemplazoDTO filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden,
			String orden, String excel, String tipoReemplazo, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<ReemplazoConsultaDTO> respuesta = new Respuesta<>();
	    try {
	    	
	    	List<Object[]> listObjs = this.filenetDAO.consultaReemplazoAdicion(token,filtro.getDependencia(),filtro.getRol(),
	    			filtro.getUsuarioSaliente(),filtro.getUsuarioEntrante(),filtro.getFechaDesdeText(),filtro.getFechaHastaText(),0,filtro.getReferencia(),tipoReemplazo,itemsPorPagina, numeroPagina, excel, columnaOrden, orden);

	    	LOGGER.info("consultaReemplazoApoyoPaginado obtenidos:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  listObjs.remove(0);
	    	  List<ReemplazoConsultaDTO> lstLog = new ArrayList<>();
	    	  LOGGER.info("consultaFuncionariosPaginado obtenidas:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  ReemplazoConsultaDTO obj = new ReemplazoConsultaDTO();
	    		  obj.setId_reemplazo(obja[1] != null?(Integer)obja[1]:0);
	    		  obj.setCodUsuarioSaliente(obja[2] != null?obja[2].toString():"");
	    		  obj.setUsuarioSaliente(obja[3] != null?obja[3].toString():"");
	    		  obj.setCodUsuarioEntrante(obja[4] != null?obja[4].toString():"");
	    		  obj.setUsuarioEntrante(obja[5] != null?obja[5].toString():"");
	    		  obj.setRol(obja[6] != null?obja[6].toString():"");
	    		  obj.setCodDependencia(obja[7] != null?(Integer)obja[7]:0);
	    		  obj.setDependencia(obja[8] != null?obja[8].toString():"");
	    		  obj.setFechaInicio(obja[9] != null?obja[9].toString():"");
	    		  obj.setFechaTermino(obja[10] != null?obja[10].toString():"");
	    		  obj.setEstado(obja[11] != null?obja[11].toString():"");
	    		  lstLog.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(lstLog);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
				
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}

	@Override
	public Respuesta<ByteArrayInputStream> consultarReemplazoAdicionExcel(FiltroConsultaReemplazoDTO filtro,
			String usuarioCreado, String usuario, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] consultarReemplazoAdicionExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			
			List<Object[]> listObjs = this.filenetDAO.consultaReemplazoAdicion(usuario,filtro.getDependencia(),filtro.getRol(),
	    			filtro.getUsuarioSaliente(),filtro.getUsuarioEntrante(),filtro.getFechaDesdeText(),filtro.getFechaHastaText(),0,filtro.getReferencia(),"ADICION",10, 1,"SI", "", "");

			IReport<ByteArrayInputStream> report;
			if(listObjs != null) {
				report = new ReporteExcelReemplazos(listObjs, usuarioCreado,"ADICION");
			} else {
				report = new ReporteExcelReemplazos(listObjs, usuarioCreado,"ADICION");
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
	public Respuesta<ByteArrayInputStream> consultarReemplazoTotalExcel(FiltroConsultaReemplazoDTO filtro,
			String usuarioCreado, String usuario, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] consultarReemplazoTotalExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			
			List<Object[]> listObjs = this.filenetDAO.consultaReemplazoAdicion(usuario,filtro.getDependencia(),filtro.getRol(),
	    			filtro.getUsuarioSaliente(),filtro.getUsuarioEntrante(),filtro.getFechaDesdeText(),filtro.getFechaHastaText(),0,filtro.getReferencia(),"TOTAL",10, 1,"SI", "", "");

			IReport<ByteArrayInputStream> report;
			if(listObjs != null) {
				report = new ReporteExcelReemplazos(listObjs, usuarioCreado,"TOTAL");
			} else {
				report = new ReporteExcelReemplazos(listObjs, usuarioCreado,"TOTAL");
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
	public Respuesta<ByteArrayInputStream> consultarReemplazoApoyoExcel(FiltroConsultaReemplazoDTO filtro,
			String usuarioCreado, String usuario, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] consultarReemplazoApoyoExcel " + filtro.toString());
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			
			List<Object[]> listObjs = this.filenetDAO.consultaReemplazoAdicion(usuario,filtro.getDependencia(),filtro.getRol(),
	    			filtro.getUsuarioSaliente(),filtro.getUsuarioEntrante(),filtro.getFechaDesdeText(),filtro.getFechaHastaText(),0,filtro.getReferencia(),"APOYO",10, 1,"SI", "", "");

			IReport<ByteArrayInputStream> report;
			if(listObjs != null) {
				report = new ReporteExcelReemplazos(listObjs, usuarioCreado,"APOYO");
			} else {
				report = new ReporteExcelReemplazos(listObjs, usuarioCreado,"APOYO");
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
	
	/*FIN Ticket 9*4275*/

	// TICKET 9000003866
	public List<ItemFilenet> obtenerDependenciasBandES(String usuario){
			return this.filenetDAO.obtenerDependenciasBandES(usuario);
	}
	// FIN TICKET
	
	
	/*INI Ticket 9000004412*/
	@Override
	public List<ItemFilenet> obtenerCentroGestionRemitente(String texto) {
		return this.filenetDAO.obtenerCentroGestionRemitente(texto);
	}
	@Override
	public List<ItemFilenet> listarProcesos(String texto) {
		return this.filenetDAO.listarProcesos(texto);
	}
	
	@Override
	public String obtenerCGCUsuario(String usuario) {
		return (String) this.filenetDAO.obtenerCGCUsuario(usuario);
	}
	
	public List<ItemFilenet> registrarValija(Valija valija, String usuario, String cgc){
		return this.filenetDAO.registrarValija(valija, usuario,cgc);
	}
	
	public List<ItemFilenet> validarVentaBase(VentaBases ventaBase, String usuario){
		return this.filenetDAO.validarVentaBases(ventaBase, usuario);
	}
	
	public List<ItemFilenet> validarConsultaVentaBase(VentaBases ventaBase, String usuario){
		return this.filenetDAO.validarConsultaVentaBases(ventaBase, usuario);
	}
	
	public List<ItemFilenet> registrarVentaBase(VentaBases ventaBase, String usuario){
		return this.filenetDAO.registrarVentaBases(ventaBase, usuario);
	}
	
	public List<ItemFilenet> registrarConsultaVentaBase(VentaBases ventaBase, String usuario){
		return this.filenetDAO.registrarConsultaVentaBases(ventaBase, usuario);
	}
	
	public List<ItemFilenet> registrarExpediente(Expediente expediente, String usuario){
		return this.filenetDAO.registrarExpediente(expediente, usuario);
	}
	
	public List<ItemFilenet> registrarTracking(Tracking tracking, String usuario, String modo){
		return this.filenetDAO.registrarTracking(tracking, usuario,modo);
	}
	
	@Override
	public List<ItemFilenet> listaCouriersCGC(String texto) {
		return this.filenetDAO.listaCouriersCGC(texto);
	}
	
	@Override
	public List<ItemFilenet> listarMotivos(String texto) {
		return this.filenetDAO.listarMotivos(texto);
	}
	
	public List<ItemFilenet> registrarDevolucion(Devolucion devolucion, String usuario){
		return this.filenetDAO.registrarDevolucion(devolucion, usuario);
	}
	
	public List<ItemFilenet> asociarOrdenServicio(OrdenServicio ordenServicio, String usuario){
		return this.filenetDAO.asociarOrdenServicio(ordenServicio, usuario);
	}
	
	public String registrarValijas(Valija valija, String usuario,String cgc, String token, Locale locale){
		LOGGER.info("[INICIO] registrarValijas");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.registrarValija(valija, usuario, cgc);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					estado = result.get(0).getDescripcion();
					nroBatch=result.get(0).getCodigo().toString();
					
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] registrarValijas ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	
	public String registrarExpedientes(Expediente expediente, String usuario,String token, Locale locale){
		LOGGER.info("[INICIO] registrarExpedientes");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.registrarExpediente(expediente, usuario);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					respuesta = this.correspondeciaService.crearExpediente(token,expediente.getNroProceso(), locale);
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] registrarExpedientes ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	public String validarVentaBases(VentaBases ventaBases, String usuario,String token, Locale locale){
		LOGGER.info("[INICIO] validarVentaBases");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.validarVentaBase(ventaBases, usuario);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] validarVentaBases ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	public String registrarVentaBases(VentaBases ventaBases, String usuario,String token, Locale locale){
		LOGGER.info("[INICIO] registarVentaBases");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.registrarVentaBase(ventaBases, usuario);
			if(result != null && result.size() > 0){
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] registarVentaBases ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	public String validarConsultaVentaBases(VentaBases ventaBases, String usuario,String token, Locale locale){
		LOGGER.info("[INICIO] validarConsultaVentaBases");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.validarConsultaVentaBase(ventaBases, usuario);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] validarConsultaVentaBases ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	
	public String registrarConsultaVentaBases(VentaBases ventaBases, String usuario,String token, Locale locale){
		LOGGER.info("[INICIO] registrarConsultaVentaBases");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.registrarConsultaVentaBase(ventaBases, usuario);
			if(result != null && result.size() > 0){
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] registarVentaBases ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	public String registrarTracking(Tracking tracking, String usuario,String token, String modo, Locale locale){
		LOGGER.info("[INICIO] registrarTracking");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.registrarTracking(tracking, usuario,modo);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] registrarTracking ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	
	@Override
	public Respuesta<TrackingConsultaDTO> consultaTracking(String token, String tipo,
			FiltroConsultaTrackingDTO filtro, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<TrackingConsultaDTO> respuesta = new Respuesta<>();
	    try {
	    	
	    	List<Object[]> listObjs = this.filenetDAO.consultaTracking(token,filtro.getCorrelativo());

	    	LOGGER.info("consultaTracking obtenidos:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  listObjs.remove(0);
	    	  List<TrackingConsultaDTO> lstLog = new ArrayList<>();
	    	  LOGGER.info("consultaTracking obtenidas:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  TrackingConsultaDTO obj = new TrackingConsultaDTO();
	    		  //obj.setItem(correlativo);
	    		  obj.setItem(obja[2] != null?obja[2].toString():"");
	    		  obj.setTipo(tipo);
	    		  obj.setCorrelativo(filtro.getCorrelativo());
	    		  obj.setFechaHora(obja[0] != null?obja[0].toString():"");
	    		  lstLog.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(lstLog);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
				
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public String registrarDevolucion(Devolucion devolucion, String usuario,String token, Locale locale){
		LOGGER.info("[INICIO] registrarDevolucion");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.registrarDevolucion(devolucion, usuario);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] registrarDevolucion ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	public String asociarOrdenServicio(OrdenServicio ordenServicio, String usuario,String token, Locale locale){
		LOGGER.info("[INICIO] asociarOrdenServicio");
		String estado = "";
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			List<ItemFilenet> result = this.asociarOrdenServicio(ordenServicio, usuario);
			if(result != null && result.size() > 0){
				String nroBatch="";
				this.LOGGER.info("Tamaño:" + result.size());
				this.LOGGER.info(result.get(0).getDescripcion());
				if("".equalsIgnoreCase(result.get(0).getDescripcion())){
					this.LOGGER.info("Estado:" + estado);
				}else{
					estado = result.get(0).getDescripcion();
					this.LOGGER.info("Estado:" + estado);
				}
			}else{
				estado = "ERROR EN LA RESPUESTA";
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] asociarOrdenServicio ", e);
			estado = "ERROR";
		}
		return estado;
	}
	
	/*FIN Ticket 90000004412*/
}
