package pe.com.petroperu.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.Utilitario;
import pe.com.petroperu.cliente.model.CompletarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.controller.ModuloEmisionController;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.DestinatarioRespuesta;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaDTO;
import pe.com.petroperu.service.ICorrespondenciaEmisionService;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.IEmisionService;
import pe.com.petroperu.service.IFilenetService;
import pe.com.petroperu.service.IRutaAprobacionService;
import pe.com.petroperu.service.dto.ArchivoAdjuntoDTO;
import pe.com.petroperu.service.dto.FiltroBandeja;
import pe.com.petroperu.service.dto.FiltroConsultaCorrespondenciaDTO;
import pe.com.petroperu.sistcorr.dao.IRolDAO;
import pe.com.petroperu.sistcorr.dao.IRutaAprobacionDAO;
import pe.com.petroperu.util.Constante;
import pe.com.petroperu.util.datatable.DataTableResults;
import pe.com.petroperu.util.datatable.entity.DataTableRequestCorrespondenciaConsultaDTO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;



@Controller
@RequestMapping({"/app/emision"})
@PropertySource({"classpath:application.properties"})
public class ModuloEmisionController
{
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private IFilenetService fileNetService;
  
  @Autowired
  private IEmisionService emisionService;
  
  @Autowired
	private IRolDAO rolDAO;
  
  @Value("${sistcorr.archivo.max_size}")
  private Integer tamanioMaxArchivoUpload;
  
  @Autowired
  private MessageSource messageSource;
  
  // TICKET 9000003908
  @Autowired
  private ICorrespondenciaDAO correspondenciaDAO;
  
  // TICKET 9000003943
  @Autowired
  private IRutaAprobacionDAO rutaAprobacionDAO;
  
  @Autowired
  private IRutaAprobacionService rutaAprobacionService;
  
  // TICKET 900004044
  @Autowired
  private ICorrespondenciaService correspondenciaService;
  
  	// TICKET 9000004510
  	@Autowired
	private ICorrespondenciaEmisionService correspondenciaEmisionService;
  	// FIN TICKET

  
  @GetMapping(value = {"/buscar/pais"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2Paises(@RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarPaises(texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/buscar/departamento"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2Departamentos(@RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarDepartamentos(texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/buscar/provincia"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2Provincias(@RequestParam(value = "codDep", defaultValue = "") String codDepartamento, @RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarProvincias(codDepartamento, texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/buscar/distrito"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2Distritos(@RequestParam(value = "codDep", defaultValue = "") String codDepartamento, @RequestParam(value = "codProv", defaultValue = "") String codProvincia, @RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarDistritos(codDepartamento, codProvincia, texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/buscar/lugar"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2Lugar(@RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarLugares(texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping({"/buscar/lugar-dependencia/{codDependencia}"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2LugarXDependencia(@PathVariable("codDependencia") String codDependencia) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    ItemFilenet lugar = this.fileNetService.obtenerLugarPorDependencia(codDependencia);
    if (lugar == null) {
      respuesta.estado = false;
      respuesta.mensaje = "OK";
    } else {
      respuesta.estado = true;
      respuesta.mensaje = "OK";
      respuesta.datos.add(lugar);
    } 
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/buscar/tipo_correspondencia"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> selectTipoCorrespondencia(Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarTiposCorresponciaEmision(""));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/buscar/dependencia"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2Dependencias(@RequestParam(value = "codLugar", defaultValue = "") String codLugar, @RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarDependencias(codLugar, texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  // TICKET 9000003944
  @GetMapping(value = {"/buscar/dependenciaUM"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2DependenciasUM(@RequestParam(value = "codLugar", defaultValue = "") String codLugar, @RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarDependenciasInterno(codLugar, texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  // FIN TICKET
  
  @GetMapping(value = {"/buscar/dependencia-usuario"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2DependenciaPorUsuario(@RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    UsuarioPetroperu usuario = obtenerUsuario();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.obtenerDependenciaPorUsuario(usuario.getUsername(), texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  // TICKET 9000003944
  @GetMapping(value = {"/buscar/dependencia-usuario-um"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2DependenciaPorUsuarioUM(@RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    UsuarioPetroperu usuario = obtenerUsuario();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.obtenerDependenciaPorUsuarioConsulta(usuario.getUsername(), texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  // FIN TICKET
  
  @GetMapping(value = {"/buscar/funcionario"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> selectFuncionarios(@RequestParam(value = "codDep", defaultValue = "") String codDependencia) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarFuncionarios(codDependencia));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }

  
  @GetMapping(value = {"/buscar/dependencia_externa"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2DependenciasExternas(@RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarDependenciasExternas("", "", "", texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/buscar/funcionarios"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> select2Funcionarios(@RequestParam(value = "codDep", defaultValue = "") String codDependencia, @RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    respuesta.mensaje = "OK";
    respuesta.datos.addAll(this.fileNetService.listarFuncionariosPorDependencia(codDependencia, texto));
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/obtener/funcionario_jefe/{codDependencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> obtenerFuncionarioJefe(@PathVariable("codDependencia") String codDependencia) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    ItemFilenet funcionario = this.fileNetService.obtenerFirmante(codDependencia);
    respuesta.datos.add(funcionario);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/obtener/funcionario_jefe/ruta_aprobacion/{codDependencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<ItemFilenet>> obtenerFuncionarioJefeRutaAprobacion(@PathVariable("codDependencia") String codDependencia) {
    Respuesta<ItemFilenet> respuesta = new Respuesta<>();
    respuesta.estado = true;
    ItemFilenet funcionario = this.fileNetService.obtenerFirmanteRutaAprobacion(codDependencia);
    respuesta.datos.add(funcionario);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = {"/obtener/ruta_aprobacion/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<RutaAprobacion>> obtenerRutaAprobacion(@PathVariable("idCorrespondencia") Long idCorrespondencia) {
    Respuesta<RutaAprobacion> respuesta = new Respuesta<>();
    respuesta.estado = true;
    List<RutaAprobacion> aprobadores = new ArrayList<RutaAprobacion>();
    Correspondencia cor = correspondenciaDAO.findOne(idCorrespondencia);
	respuesta.datos = rutaAprobacionService.obtenerRutaAprobacion(cor);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }

  
  @PostMapping(value = {"/registrar/correspondencia"}, consumes = {"multipart/form-data"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> registrarCorrespondencia(@RequestPart("correspondencia") Correspondencia correspondencia, @RequestPart("principales") List<Boolean> principales, @RequestPart("archivos") MultipartFile[] archivos, @RequestPart("rutaAprobacion") List<RutaAprobacion> aprobadores, Locale locale) {
	  LOGGER.info("Nro envio correspondencia:" + correspondencia.getNroEnvio());
    Respuesta<Correspondencia> respuesta = new Respuesta<>();
    try {
      UsuarioPetroperu usuario = obtenerUsuario();
      List<ArchivoAdjuntoDTO> _archivos = new ArrayList<>();
      int indice = 0;
      for (MultipartFile archivo : archivos) {
        double tamanioMB = archivo.getSize() / Math.pow(1024.0D, 2.0D);
        if (tamanioMB > this.tamanioMaxArchivoUpload.intValue()) {
          Object[] parametros = { archivo.getOriginalFilename(), this.tamanioMaxArchivoUpload };
          throw new Exception(MessageFormat.format(this.messageSource.getMessage("sistcorr.cargar_archivo.tamanio_superior", null, locale), parametros));
        } 
        if (correspondencia.isFirmaDigital() == true && ((Boolean)principales.get(indice)).booleanValue() == true && !archivo.getContentType().toLowerCase().contains("pdf")) {
          throw new Exception(this.messageSource.getMessage("sistcorr.cargar_archivo.principal_invalido", null, locale));
        }
        ArchivoAdjuntoDTO adjuntoDTO = new ArchivoAdjuntoDTO();
        adjuntoDTO.setFile(archivo);
        adjuntoDTO.setPrincipal(((Boolean)principales.get(indice)).booleanValue());
        _archivos.add(adjuntoDTO);
        indice++;
      } 
      respuesta = this.emisionService.registrar(correspondencia, _archivos, usuario.getUsername(), usuario.getNombreCompleto(), aprobadores, locale);
	  if(respuesta.estado){
		  boolean esCorrespondenciaAsignada = false;//TICKET 9000004714
      	// TICKET 9000003943
	      LOGGER.info("Rutas de Aprobacion");
	      RutaAprobacion aprobadorInicial = new RutaAprobacion();
	      	for(int i=0;i<aprobadores.size();i++){
	      		RutaAprobacion ra = aprobadores.get(i); 
	      		/*INI Ticket 7000004827*/
	      		//if(ra.getId()>1000000L){
	      		if(ra.getId()>10000000L){
	      		/*FIN Ticket 7000004827*/	
	      			ra.setId(null);
	      		}
	      		ra.setUsuario(ra.getUsuario().toLowerCase());
	      		ra.setCorrespondencia(respuesta.datos.get(0));
	      		ra.setUsuarioCrea(usuario.getUsername());
				ra.setFechaCrea(new Date());
				LOGGER.info("Ruta Aprobacion:" + ra.toString());
	      		if(i==0 && respuesta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA){
	      			Respuesta<Firmante> respuestaFirmante = this.emisionService.obtenerFirmantes(respuesta.datos.get(0).getId(), locale);
	      			ra.setFirmante(respuestaFirmante.datos.get(0));
	      			esCorrespondenciaAsignada = true;// TICKET 9000004714
	      		}
	      		
	      		LOGGER.info("ModuloEmisionController Linea 331");
	      		RutaAprobacion ra2 = rutaAprobacionDAO.save(ra);
	      		LOGGER.info("ID generado:" + ra2.getId());
	  		}
	      // FIN TICKET
	      	//inicio TICKET 9000004714
			if(esCorrespondenciaAsignada) {
				this.emisionService.registrarDatosFirmante(respuesta.datos.get(0).getId(), locale);
			}else if(!correspondencia.isRutaAprobacion() && correspondencia.isPrimerFirmante()) {
				this.emisionService.registrarDatosFirmante(respuesta.datos.get(0).getId(), locale);
			}
			//fin TICKET 9000004714
	  }
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PostMapping(value = {"/modificar-completo/correspondencia"}, consumes = {"multipart/form-data"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> modificarCompletoCorrespondencia(@RequestPart("correspondencia") Correspondencia correspondencia, @RequestPart("principales") List<Boolean> principales, @RequestPart("archivos") MultipartFile[] archivos, @RequestPart("rutaAprobacion") List<RutaAprobacion> aprobadores, Locale locale) {
    Respuesta<Correspondencia> respuesta = new Respuesta<>();
    try {
      UsuarioPetroperu usuario = obtenerUsuario();
      List<ArchivoAdjuntoDTO> _archivos = new ArrayList<>();
      int indice = 0;
      for (MultipartFile archivo : archivos) {
        double tamanioMB = archivo.getSize() / Math.pow(1024.0D, 2.0D);
        if (tamanioMB > this.tamanioMaxArchivoUpload.intValue()) {
          Object[] parametros = { this.tamanioMaxArchivoUpload };
          throw new Exception(MessageFormat.format(this.messageSource.getMessage("sistcorr.cargar_archivo.tamanio_superior", null, locale), parametros));
        } 
        if (correspondencia.isFirmaDigital() == true && ((Boolean)principales.get(indice)).booleanValue() == true && !archivo.getContentType().toLowerCase().contains("pdf")) {
          throw new Exception(this.messageSource.getMessage("sistcorr.cargar_archivo.principal_invalido", null, locale));
        }
        ArchivoAdjuntoDTO adjuntoDTO = new ArchivoAdjuntoDTO();
        adjuntoDTO.setFile(archivo);
        adjuntoDTO.setPrincipal(((Boolean)principales.get(indice)).booleanValue());
        _archivos.add(adjuntoDTO);
        indice++;
      } 
      // TICKET 9000003943
      for(int i=0;i<aprobadores.size();i++){
    	  LOGGER.info("Aprobador modificar correspondencia (" + i + "):" + aprobadores.get(i).getId() + "||" + aprobadores.get(i).getUsuarioNombre());
      }
      // FIN TICKET
      respuesta = this.emisionService.modificar(correspondencia, _archivos, usuario.getUsername(), aprobadores, locale);
    //INICIO TICKET 9000004714
      if (respuesta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA) {
    	  this.emisionService.registrarDatosFirmante(respuesta.datos.get(0).getId(), locale);
	  }
    //FIN TICKET 9000004714
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PutMapping(value = {"/modificar/correspondencia"}, consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> modificarCorrespondencia(@RequestBody Correspondencia correspondencia, Locale locale) {
    Respuesta<Correspondencia> respuesta = new Respuesta<>();
    UsuarioPetroperu usuario = obtenerUsuario();
    List<ArchivoAdjuntoDTO> _archivos = new ArrayList<>();
    respuesta = this.emisionService.modificar(correspondencia, _archivos, usuario.getUsername(), correspondencia.getAprobadores(), locale);
    //INICIO TICKET 9000004714
    if (respuesta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA) {
  	  this.emisionService.registrarDatosFirmante(respuesta.datos.get(0).getId(), locale);
	}
    //FIN TICKET 9000004714
    // TICKET 9000003943
    /*List<RutaAprobacion> aprobadores = correspondencia.getAprobadores();
    Correspondencia corr = correspondenciaDAO.findOne(correspondencia.getId());
    List<RutaAprobacion> aprobs = rutaAprobacionDAO.findAllByCorrespondencia(corr);
    for(RutaAprobacion ra : aprobs){
    	boolean encontrado = false;
    	for(int j=0;j<aprobadores.size();j++){
    		if(aprobadores.get(j).getId().equals(ra.getId())){
    			encontrado = true;
    		}
    	}
    	if(!encontrado){
    		LOGGER.info("Modificador correspondencia");
    		rutaAprobacionDAO.delete(ra);
    	}
    }
    for(int i=0;i<aprobadores.size();i++){
    	LOGGER.info("Aprobador (" + i + "):" + aprobadores.get(i).getId() + "||" + aprobadores.get(i).getDependenciaNombre() + aprobadores.get(i).getUsuarioNombre());
    	RutaAprobacion ra = aprobadores.get(i); 
    	ra.setUsuario(ra.getUsuario().toLowerCase());
  		if(ra.getId()>10000000L){
  			ra.setId(null);
  	  		ra.setUsuarioCrea(usuario.getUsername());
  			ra.setFechaCrea(new Date());
  			ra.setCorrespondencia(correspondencia);
  		}else{
  	  		ra.setUsuarioModifica(usuario.getUsername());
  			ra.setFechaModifica(new Date());
  		}
  		
		LOGGER.info("Ruta Aprobacion:" + ra.toString());
  		if(i==0 && respuesta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA){
  			Respuesta<Firmante> respuestaFirmante = this.emisionService.obtenerFirmantes(respuesta.datos.get(0).getId(), locale);
  			if(respuestaFirmante.datos.size()==1){
  				ra.setFirmante(respuestaFirmante.datos.get(0));
  			}
  		}
  		RutaAprobacion ra2 = rutaAprobacionDAO.save(ra);
  		LOGGER.info("ID generado:" + ra2.getId());
    }*/
    // FIN TICKET
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PostMapping(value = {"/modificar/remitenteCorrespondencia/{id}/{cod}"}, consumes ={"application/json"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> modificarRemitenteCorrespondencia(@PathVariable("id") Long idCorrespondencia, @PathVariable("cod") String remitente, Locale locale){
	  LOGGER.info("[INICIO] modificarRemitenteCorrespondencia");
	  Respuesta<Correspondencia> respuesta = new Respuesta<>();
	  UsuarioPetroperu usuario = obtenerUsuario();
	  Correspondencia cor = correspondenciaDAO.findOne(idCorrespondencia);
	  cor.setCodRemitente(remitente);
	  cor.setUsuarioModifica(usuario.getUsername());
	  cor.setFechaModifica(new Date());
	  LOGGER.info("PRE SAVE REMITENTE");
	  correspondenciaDAO.save(cor);
	  respuesta.estado = true;
	  respuesta.mensaje = "";
	  respuesta.datos = new ArrayList();
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  // TICKET 9000003514
  @PostMapping(value = {"/registrar/adjunto"}, consumes = {"multipart/form-data"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<RespuestaCargaAdjunto>> registrarAdjunto(@RequestPart("correspondencia") pe.com.petroperu.model.Correspondencia correspondencia, @RequestPart("archivos") MultipartFile[] archivos, Locale locale) {
	  Respuesta<RespuestaCargaAdjunto> respuesta = new Respuesta<>();
	  UsuarioPetroperu usuario = obtenerUsuario();
	  LOGGER.info("Correspondencia:" + correspondencia.getCorrelativo());
	  LOGGER.info("Archivos:" + archivos.length);
	  LOGGER.info("Archivo 1 - Nombre:" + archivos[0].getName());
	  LOGGER.info("Archivo 1 - Tamaño:" + archivos[0].getSize());
	  LOGGER.info("Archivo 1 - Original:" + archivos[0].getOriginalFilename());
	  ArchivoAdjuntoDTO _archivoAdjunto = new ArchivoAdjuntoDTO();
	  _archivoAdjunto.setFile(archivos[0]);
	  respuesta = this.emisionService.registrarArchivoAdjunto(correspondencia, _archivoAdjunto, usuario, locale);
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  // FIN TICKET 9000003514
  
  @GetMapping(value = {"/buscar/correspondencia/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> obtenerCorrespondencia(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
    Respuesta<Correspondencia> respuesta = new Respuesta<>();
    respuesta = this.emisionService.buscar(idCorrespondencia, locale);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  //INICIO TICKET 9000004807
  @PostMapping(value = "/exportar-correspondencias/{asignacion}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<InputStreamResource> exportarExcelCorrespondenciasBS(
			@PathVariable("asignacion") String bandeja, @RequestBody FiltroBandeja filtro, Locale locale) throws NoSuchMessageException, Exception {

		LOGGER.info("[INICIO] exportarExcelCorrespondenciasBS");
		UsuarioPetroperu usuario = obtenerUsuario();
		
		if (usuario == null)
			throw new Exception(this.messageSource.getMessage("sistcorr.usuario.error", null, locale));
		
	    if(filtro.getUsuario() == null || "0".equalsIgnoreCase(filtro.getUsuario()) || "".equalsIgnoreCase(filtro.getUsuario())){
	    	filtro.setUsuario(usuario.getUsername());
	    }
		Respuesta<ByteArrayInputStream> respuesta = this.emisionService.exportarExcelCorrespondenciasBS(filtro, usuario.getNombreCompleto(), usuario.getUsername(), bandeja, locale);
		if(respuesta.estado == false) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=report.xlsx");
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new InputStreamResource(respuesta.datos.get(0)));
  }
  //FIN TICKET 9000004807
	
  @PostMapping(value = {"/listar-correspondencias/{asignacion}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<CorrespondenciaDTO>> obtenerCorrespondencias(@PathVariable("asignacion") String asignacion, @RequestBody FiltroBandeja filtro, Locale locale) {
    Respuesta<CorrespondenciaDTO> respuesta = new Respuesta<>();
    UsuarioPetroperu usuario = obtenerUsuario();
    // TICKET 9000003994
    if(filtro.getUsuario() == null || "0".equalsIgnoreCase(filtro.getUsuario()) || "".equalsIgnoreCase(filtro.getUsuario())){
    	filtro.setUsuario(usuario.getUsername());
    }
    // FIN TICKET
    // TICKET 9000004409
    if (usuario.getUsername().equals(filtro.getUsuario())){
		List<Object[]> roles = rolDAO.listByUsuario(usuario.getUsername());
		int esJefe = 0;
		for (Object[] rol : roles) {
			if("ROLE_JEFE".equals(rol[3].toString())){
				esJefe = 1;
				this.emisionService.actualizarRutaAprobacionJefeActual(usuario,esJefe,locale);
				break;
			}			
		}
    }
    // FIN TICKET
    if ("pendiente".equals(asignacion)) {
      respuesta = this.emisionService.pendientes(filtro, locale);
    } else if ("firmado".equals(asignacion)) {
      respuesta = this.emisionService.firmados(filtro, locale);
    } else if ("enviado".equals(asignacion)) {
      respuesta = this.emisionService.enviados(filtro, locale);
    } else {
      respuesta.estado = false;
      respuesta.mensaje = "No existe la bandeja " + asignacion;
    } 
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping({"/descargar-archivo/{idArchivoAdjunto}"})
  public ResponseEntity descargarArchivoAdjunto(@PathVariable("idArchivoAdjunto") Long idArchivoAdjunto, Locale locale) {
	  // TICKET 9000004510
	  Respuesta<ArchivoAdjunto> respuesta = correspondenciaEmisionService.buscarArchivoAdjunto(idArchivoAdjunto, locale);
	  if(respuesta.estado && respuesta.datos != null && respuesta.datos.size() > 0){
		  if(respuesta.datos.get(0).getIndicadorRemoto().equalsIgnoreCase(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO)){
			  // FIN TICKET
			  Object[] data = this.emisionService.obtenerArchivAdjunto(idArchivoAdjunto, locale);
			    if (data.length == 3) {
			    	LOGGER.info(data[0] + "||" + data[1] + "||" + data[2]);
			    	Utilitario utilitario = new Utilitario();
			    	String nombreArchivo = utilitario.nombreArchivoDescarga((String)data[2]);
			    	//String contenDisposition = "attachment; filename=\"" + (String)data[2] + "\"";
			    	String contenDisposition = "attachment; filename=\"" + nombreArchivo + "\"";
			      return ((ResponseEntity.BodyBuilder)ResponseEntity.ok()
			        .header("Content-Disposition", new String[] { contenDisposition
			          })).contentType(MediaType.parseMediaType((String)data[1]))
			        .body(data[0]);
			    } 
			    return new ResponseEntity<>(data[0], HttpStatus.OK);
			    // TICKET 9000004510
		  }else{
			  UsuarioPetroperu usuario = obtenerUsuario();
			  Object[] data;
			  Object[] file = correspondenciaService.descargarDocumentoServidor(usuario.getToken(), String.valueOf(idArchivoAdjunto), locale);
			  if(file == null || file[0] == null){
				  /*data = new Object[1];
				  data[0] = "No se puede descargar del repositorio";
				  return new ResponseEntity<>(data[0], HttpStatus.OK);*/
				  Object[] data2 = this.emisionService.obtenerArchivAdjunto(idArchivoAdjunto, locale);
				    if (data2.length == 3) {
				    	LOGGER.info(data2[0] + "||" + data2[1] + "||" + data2[2]);
				    	Utilitario utilitario = new Utilitario();
				    	String nombreArchivo = utilitario.nombreArchivoDescarga((String)data2[2]);
				    	//String contenDisposition = "attachment; filename=\"" + (String)data2[2] + "\"";
				    	String contenDisposition = "attachment; filename=\"" + nombreArchivo + "\"";
				      return ((ResponseEntity.BodyBuilder)ResponseEntity.ok()
				        .header("Content-Disposition", new String[] { contenDisposition
				          })).contentType(MediaType.parseMediaType((String)data2[1]))
				        .body(data2[0]);
				    } 
				    return new ResponseEntity<>(data2[0], HttpStatus.OK);
			  }else{
				  data = new Object[3];
				  data[0] = file[0];
				  data[1] = ((ArchivoAdjunto) respuesta.datos.get(0)).getContentType();
				  data[2] = ((ArchivoAdjunto) respuesta.datos.get(0)).getNombre();
				  Utilitario utilitario = new Utilitario();
				  String contenDisposition = "";
				  String nombreArchivo = utilitario.nombreArchivoDescarga((String)data[2]);
				  contenDisposition = "attachment; filename=\"" + nombreArchivo + "\"";
			      return ((ResponseEntity.BodyBuilder)ResponseEntity.ok()
			        .header("Content-Disposition", new String[] { contenDisposition
			          })).contentType(MediaType.parseMediaType((String)data[1]))
			        .body(data[0]);
			  }
		  }
	  }else{
		  Object[] data = new Object[1];
		  data[0] = "No se encontró el archivo en base de datos.";
		  return new ResponseEntity<>(data[0], HttpStatus.OK);
	  }
	  // FIN TICKET
    
  }

  
  @PostMapping(value = {"/asignar-firmante-automatico/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Firmante>> asignarFirmanteAutomatico(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<Firmante> respuesta = this.emisionService.asignarFirmanteAutomatico(idCorrespondencia, usuario.getUsername(), locale);
    //INICIO TICKET 9000004714
    if (respuesta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA) {
  	  this.emisionService.registrarDatosFirmante(idCorrespondencia, locale);
	}
    //FIN TICKET 9000004714
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PostMapping(value = {"/asignar-firmante-ruta-aprobacion/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Firmante>> asignarFirmanteRutaAprobacion(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<Firmante> respuesta = this.emisionService.asignarFirmanteRutaAprobacion(idCorrespondencia, usuario.getUsername(), locale);
    //INICIO TICKET 9000004714
    if (respuesta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA) {
  	  this.emisionService.registrarDatosFirmante(idCorrespondencia, locale);
	}
    //FIN TICKET 9000004714
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PostMapping(value = {"/validar-firmante/{idCorrespondencia}"}, consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<Respuesta> validarFirmante(@PathVariable("idCorrespondencia") Long idCorrespondencia, @RequestBody Firmante firmante, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta respuesta = this.emisionService.validarFirmante(idCorrespondencia, firmante, usuario.getUsername(), locale);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PostMapping(value = {"/agregar-firmante/{idCorrespondencia}"}, consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Firmante>> asignarOtroFirmante(@PathVariable("idCorrespondencia") Long idCorrespondencia, @RequestBody Firmante firmante, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<Firmante> respuesta = this.emisionService.asignarFirmante(idCorrespondencia, firmante, usuario.getUsername(), locale);
    //INICIO TICKET 9000004714
    if (respuesta.datos.get(0).getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA) {
  	  this.emisionService.registrarDatosFirmante(idCorrespondencia, locale);
	}
    //FIN TICKET 9000004714
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }


  
  @GetMapping(value = {"/obtener-flujo/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Firmante>> obtenerFirmantes(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
	  // TICKET 9000003908
	  Respuesta<Firmante> respuesta = this.emisionService.obtenerFirmantes(idCorrespondencia, locale);
	  List<Firmante> firmantes = respuesta.datos;
	  Integer nroFlujo = 0;
		for(Firmante _f : firmantes){
			if(_f.getNroFlujo() > nroFlujo){
				nroFlujo = _f.getNroFlujo();
			}
		}
		List<Firmante> ultimosFirmantes = new ArrayList();
		for(Firmante _f : firmantes){
			if(_f.getNroFlujo() == nroFlujo){
				ultimosFirmantes.add(_f);
			}
		}
		respuesta.datos = ultimosFirmantes;
	  // FIN TICKET
	  //return new ResponseEntity<>(this.emisionService.obtenerFirmantes(idCorrespondencia, locale), HttpStatus.OK); 
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  // TICKET 9000003997
  @GetMapping(value = {"/obtener-recepcion/{idCorrespondencia}/{nroEnvio}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<DestinatarioRespuesta>> obtenerRecepcion(@PathVariable("idCorrespondencia") Long idCorrespondencia, @PathVariable("nroEnvio") int nroEnvio, Locale locale) {
	  LOGGER.info("[INICIO] obtenerRecepcion");
	  Respuesta<DestinatarioRespuesta> respuesta = this.emisionService.obtenerDestinatarioRespuesta(idCorrespondencia, nroEnvio, locale);
	  LOGGER.info("Cantidad:" + respuesta.datos.size());
	  LOGGER.info("[FIN] obtenerRecepcion");
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  // FIN TICKET

  
  @PutMapping(value = {"/rechazar-solicitud-firma/{idCorrespondencia}"}, consumes = {"application/json"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Firmante>> rechazarFirma(@PathVariable("idCorrespondencia") Long idCorrespondencia, @RequestBody Firmante firmante, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<Firmante> respuesta = this.emisionService.rechazarSolicitudFirma(idCorrespondencia, firmante, usuario.getUsername(), usuario.getToken(), locale);
    //INICIO TICKET 9000004714
    if (respuesta.estado && (firmante.getMotivoRechazo() != null && (firmante.getMotivoRechazo().getId() == Constante.DOCUMENTO_MAL_ASIGNADO || 
    		firmante.getMotivoRechazo().getId() == Constante.DOCUMENTO_REQUIERE_VISTOS_PREVIOS ||
    		firmante.getMotivoRechazo().getId() == Constante.DOCUMENTO_POR_ANULAR))) {
    	//ACTUALIZAR DATOS FIRMANTE POR DOCUMENTO PARA REASIGNAR y DOCUMENTO REQUIERE VISTOS PREVIOS
  	  	this.emisionService.registrarDatosFirmante(idCorrespondencia, locale);
	}
    //FIN TICKET 9000004714
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PutMapping(value = {"/enviar-correspondencia/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> enviarCorrespondencia(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<Correspondencia> respuesta = this.emisionService.enviarCorrespondencia(idCorrespondencia, usuario.getUsername(), usuario.getToken(), locale);
    Respuesta<RespuestaApi> respuesta2 = new Respuesta<>();
    // TICKET 9000004044
    LOGGER.info("Verificando cerrar correspondencia automático");
    if(respuesta.estado == true){
    	Correspondencia correspondencia = correspondenciaDAO.findOne(idCorrespondencia);
		int cantidad = correspondencia.getDetalleInterno().size();
		LOGGER.info(correspondencia.isEsDocumentoRespuesta() + "" + !correspondencia.isFirmaDigital() + "-" + cantidad);
    	if(correspondencia.isEsDocumentoRespuesta() && !correspondencia.isFirmaDigital() && cantidad == 0){
    		if(correspondencia.getIdAsignacion() != null && correspondencia.getIdAsignacion().compareTo(0L) > 0){
				CompletarCorrespondenciaParametro ccp = new CompletarCorrespondenciaParametro(correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo());
				respuesta2 = correspondenciaService.completarCorrespondencia(usuario.getToken(), Integer.valueOf(correspondencia.getIdAsignacion().toString()), ccp, locale);
				LOGGER.info("COMPLETAR CORRESPONDENCIA:" + respuesta2.mensaje);
				Object[] parametros = { correspondencia.getCorrelativoEntrada() };
				if(respuesta2.estado){
					respuesta2.mensaje = MessageFormat.format(
							this.messageSource.getMessage("sistcorr.completarCorrespondenciaInternoExito", null, locale),
							parametros); 
				}
			}else{
				respuesta2 = correspondenciaService.cerrarCorrespondencia(usuario.getToken(), correspondencia.getCorrelativoEntrada(), correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo(), locale);
				LOGGER.info("CERRAR CORRESPONDENCIA:" + respuesta2.mensaje);
				Object[] parametros = { correspondencia.getCorrelativoEntrada() };
				if(respuesta2.estado){
					respuesta2.mensaje = MessageFormat.format(
							this.messageSource.getMessage("sistcorr.cerrarCorrespondenciaInternoExito", null, locale),
							parametros); 
				}
			}
    		respuesta.mensaje = respuesta.mensaje + "|||" + respuesta2.estado + "<<>>" + respuesta2.mensaje;
    	}
    }
    // FIN TICKET
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  	// TICKET 9000004651
  	@PutMapping(value = {"/enviar-correspondencia-grupal/{idsCorrespondencias}"}, produces = {"application/json"})
  	public ResponseEntity<Respuesta<Correspondencia>> enviarCorrespondenciaGrupal(@PathVariable("idsCorrespondencias") String idsCorrespondencias, Locale locale) {
  		UsuarioPetroperu usuario = obtenerUsuario();
  		Respuesta<Correspondencia> respuestaGrupal = new Respuesta<>();
  		respuestaGrupal.estado = true;
  		respuestaGrupal.mensaje = this.messageSource.getMessage("sistcorr.enviar_correspondencia.exito", null,
				locale);
  		String[] idsCorrespondencia = idsCorrespondencias.split(",");
  		for(String idCorr : idsCorrespondencia){
  			Long idCorrespondencia = Long.valueOf(idCorr);
  			Respuesta<Correspondencia> respuesta = this.emisionService.enviarCorrespondencia(idCorrespondencia, usuario.getUsername(), usuario.getToken(), locale);
  			Respuesta<RespuestaApi> respuesta2 = new Respuesta<>();
  			// TICKET 9000004044
  			LOGGER.info("Verificando cerrar correspondencia automático");
  			if(respuesta.estado == true){
  				Correspondencia correspondencia = correspondenciaDAO.findOne(idCorrespondencia);
  				int cantidad = correspondencia.getDetalleInterno().size();
  				LOGGER.info(correspondencia.isEsDocumentoRespuesta() + "" + !correspondencia.isFirmaDigital() + "-" + cantidad);
  				if(correspondencia.isEsDocumentoRespuesta() && !correspondencia.isFirmaDigital() && cantidad == 0){
  					if(correspondencia.getIdAsignacion() != null && correspondencia.getIdAsignacion().compareTo(0L) > 0){
  						CompletarCorrespondenciaParametro ccp = new CompletarCorrespondenciaParametro(correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo());
  						respuesta2 = correspondenciaService.completarCorrespondencia(usuario.getToken(), Integer.valueOf(correspondencia.getIdAsignacion().toString()), ccp, locale);
  						LOGGER.info("COMPLETAR CORRESPONDENCIA:" + respuesta2.mensaje);
  						Object[] parametros = { correspondencia.getCorrelativoEntrada() };
  						if(respuesta2.estado){
  							respuesta2.mensaje = MessageFormat.format(
  									this.messageSource.getMessage("sistcorr.completarCorrespondenciaInternoExito", null, locale),
  									parametros); 
  						}
  					}else{
  						respuesta2 = correspondenciaService.cerrarCorrespondencia(usuario.getToken(), correspondencia.getCorrelativoEntrada(), correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo(), locale);
  						LOGGER.info("CERRAR CORRESPONDENCIA:" + respuesta2.mensaje);
  						Object[] parametros = { correspondencia.getCorrelativoEntrada() };
  						if(respuesta2.estado){
  							respuesta2.mensaje = MessageFormat.format(
  									this.messageSource.getMessage("sistcorr.cerrarCorrespondenciaInternoExito", null, locale),
  									parametros); 
  						}
  					}
  					respuesta.mensaje = respuesta.mensaje + "|||" + respuesta2.estado + "<<>>" + respuesta2.mensaje;
  				}
  			}else{
  				respuestaGrupal.estado = false;
  				respuestaGrupal.mensaje = this.messageSource.getMessage("sistcorr.enviar_correspondencia.error", null, locale);
  			}
  		}
  		return new ResponseEntity<>(respuestaGrupal, HttpStatus.OK);
  	}
  // FIN TICKET
  
  // TICKET 9000004044
  @PutMapping(value = {"/procesar-existencia-enlace/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<RespuestaApi>> procesarExistenciaEnlace(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	Correspondencia correspondencia = correspondenciaDAO.findOne(idCorrespondencia);
	if(correspondencia.isEsDocumentoRespuesta()){
		if(correspondencia.getIdAsignacion() != null && correspondencia.getIdAsignacion().compareTo(0L) > 0){
			CompletarCorrespondenciaParametro ccp = new CompletarCorrespondenciaParametro(correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo());
			LOGGER.info("CCP:" + ccp.toString());
			respuesta = correspondenciaService.completarCorrespondencia(usuario.getToken(), Integer.valueOf(correspondencia.getIdAsignacion().toString()), ccp, locale);
			if(respuesta.estado){
				Object[] parametros = { correspondencia.getCorrelativoEntrada() };
				respuesta.mensaje = MessageFormat.format(
						this.messageSource.getMessage("sistcorr.completarCorrespondenciaInternoExito", null, locale),
						parametros); 
			}
		}else{
			respuesta = correspondenciaService.cerrarCorrespondencia(usuario.getToken(), correspondencia.getCorrelativoEntrada(), correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo(), locale);
			if(respuesta.estado){
				Object[] parametros = { correspondencia.getCorrelativoEntrada() };
				respuesta.mensaje = MessageFormat.format(
						this.messageSource.getMessage("sistcorr.cerrarCorrespondenciaInternoExito", null, locale),
						parametros); 
			}
		}
	}else{
		respuesta.estado = false;
		respuesta.mensaje = "";
	}
    // FIN TICKET
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  // FIN TICKET
  
  //TICKET 9000004044
  @PutMapping(value = {"/procesar-existencia-enlace-grupal/{idsCorrespondencias}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<RespuestaApi>> procesarExistenciaEnlaceGrupal(@PathVariable("idsCorrespondencias") String idsCorrespondencias, Locale locale) {
	  UsuarioPetroperu usuario = obtenerUsuario();
	  Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	  Respuesta<RespuestaApi> respuestaGrupal = new Respuesta<>();
	  respuestaGrupal.estado = true;
	  respuestaGrupal.mensaje = "";
	  String[] idsCorrespondencia = idsCorrespondencias.split(",");
	  List<String> mensajesError = new ArrayList<>();
	  for(String idCorr : idsCorrespondencia){
		  Long idCorrespondencia = Long.valueOf(idCorr);
		  Correspondencia correspondencia = correspondenciaDAO.findOne(idCorrespondencia);
		  if(correspondencia.isEsDocumentoRespuesta()){
			  if(correspondencia.getIdAsignacion() != null && correspondencia.getIdAsignacion().compareTo(0L) > 0){
				  CompletarCorrespondenciaParametro ccp = new CompletarCorrespondenciaParametro(correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo());
				  LOGGER.info("CCP:" + ccp.toString());
				  respuesta = correspondenciaService.completarCorrespondencia(usuario.getToken(), Integer.valueOf(correspondencia.getIdAsignacion().toString()), ccp, locale);
				  if(respuesta.estado){
					  Object[] parametros = { correspondencia.getCorrelativoEntrada() };
					  respuesta.mensaje = MessageFormat.format(
							  this.messageSource.getMessage("sistcorr.completarCorrespondenciaInternoExito", null, locale),
							  parametros); 
				  }else{
					  String mensaje = respuesta.mensaje;
					  LOGGER.info("Mensaje:" + mensaje);
					  mensaje = mensaje.replace("La asignación", "La asignación asociada al correlativo " + correspondencia.getCorrelativoEntrada());
					  LOGGER.info("Mensaje:" + mensaje);
					  mensaje = mensaje.replace("La asignacion", "La asignacion asociada al correlativo " + correspondencia.getCorrelativoEntrada());
					  mensajesError.add(mensaje);
					  respuestaGrupal.estado = false;
					  respuestaGrupal.mensaje = this.messageSource.getMessage("sistcorr.completarCorrespondenciaError", null, locale);
				  }
			  }else{
				  respuesta = correspondenciaService.cerrarCorrespondencia(usuario.getToken(), correspondencia.getCorrelativoEntrada(), correspondencia.getRespuesta(), correspondencia.getUsuarioCrea(), correspondencia.getCorrelativo().getCodigo(), locale);
				  if(respuesta.estado){
					  Object[] parametros = { correspondencia.getCorrelativoEntrada() };
					  respuesta.mensaje = MessageFormat.format(
							  this.messageSource.getMessage("sistcorr.cerrarCorrespondenciaInternoExito", null, locale),
							  parametros); 
				  }else{
					  String mensaje = respuesta.mensaje;
					  mensaje = mensaje.replace("La correspondencia", "La correspondencia asociada al correlativo " + correspondencia.getCorrelativoEntrada());
					  mensajesError.add(mensaje);
					  respuestaGrupal.estado = false;
					  respuestaGrupal.mensaje = this.messageSource.getMessage("sistcorr.cerrarCorrespondenciaError", null, locale);
				  }
			  }
		  }
	  }
	  // SE SETEA ESTOS VALORES YA QUE NO SE MOSTRARA NINGUN MENSAJE
	  respuesta.estado = false;
	  respuesta.mensaje = String.join(" ", mensajesError);
	  // FIN SETEA
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  // FIN TICKET
  
  @PutMapping(value = {"/reasignar-correspondencia/{idCorrespondencia}/{usuario}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> reasignarCorrespondencia(@PathVariable("idCorrespondencia") Long idCorrespondencia, @PathVariable("usuario") String nuevoResponsable, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<Correspondencia> respuesta = this.emisionService.reasignarCorrespondencia(idCorrespondencia, nuevoResponsable, usuario.getUsername(), locale);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PutMapping(value = {"/declinar-correspondencia/{idCorrespondencia}"}, produces = {"application/json"})
  public ResponseEntity<Respuesta<Correspondencia>> declinarCorrespondencia(@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<Correspondencia> respuesta = this.emisionService.declinarCorrespondencia(idCorrespondencia, usuario.getUsername(), usuario.getToken(), locale);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping({"/buscar/funcionarios-reemplazantes"})
  public ResponseEntity<Respuesta<ItemFilenet>> funcionariosReeemplazantes(@RequestParam("correspondencia") Long idCorrespondencia, @RequestParam(value = "q", defaultValue = "") String texto, Locale locale) {
    UsuarioPetroperu usuario = obtenerUsuario();
    Respuesta<ItemFilenet> respuesta = this.emisionService.buscarFuncionariosReemplazantes(idCorrespondencia, texto, locale);
    return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  
  @GetMapping(value = "/buscar/dependencias-superiores", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Respuesta<ItemFilenet>> listarDependenciasSuperiores(@RequestParam("codDependencia") String codDependencia,  @RequestParam(value = "q", defaultValue = "") String texto, Locale locale){
	  UsuarioPetroperu usuario = obtenerUsuario();
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarDependenciasSuperiores(codDependencia, texto));
	  /*try{
		  Thread.sleep(20000);
	  }catch(Exception e){
		  e.printStackTrace();
	  }*/
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = "/buscar/dependencias-todas", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Respuesta<ItemFilenet>> listarDependenciasTodas(@RequestParam("codDependencia") String codDependencia,  @RequestParam(value = "q", defaultValue = "") String texto, Locale locale){
	  UsuarioPetroperu usuario = obtenerUsuario();
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarDependencias("", texto));
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = "/buscar/dependencias-todas-nuevo", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Respuesta<ItemFilenet>> listarDependenciasTodasNuevo(@RequestParam("codDependencia") String codDependencia,  @RequestParam(value = "q", defaultValue = "") String texto, Locale locale){
	  UsuarioPetroperu usuario = obtenerUsuario();
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarDependenciasNuevo("", texto));
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = "/buscar/dependencias-subordinadas", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Respuesta<ItemFilenet>> listarDependenciasSubordinadas(@RequestParam("codDependencia") String codDependencia, Locale locale){
	  UsuarioPetroperu usuario = obtenerUsuario();
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarDependenciasSubordinadas(codDependencia));
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @PostMapping(value = "/consultar-bandeja-salida", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Respuesta<CorrespondenciaConsultaDTO>> consultarCorrespondencias(@RequestBody FiltroConsultaCorrespondenciaDTO filtro, Locale locale){
	  UsuarioPetroperu usuario = obtenerUsuario();
	  String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		int esJefe = 0;
		for (Object[] rol : roles) {
			LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
				esJefe = 1;
			}
		}
	  Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
	  respuesta = emisionService.consultarCorrespondencuas(filtro, usuario.getUsername(), esJefe, locale);
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  	//TICKET 9000004494
  	@GetMapping(value = "/consultar-bandeja-salida-paginado", produces =  MediaType.APPLICATION_JSON_VALUE)
  	public ResponseEntity<Respuesta<DataTableResults<CorrespondenciaConsultaDTO>>> consultarCorrespondenciasPaginado(HttpServletRequest request, Locale locale){
  		Respuesta<DataTableResults<CorrespondenciaConsultaDTO>> respuesta = new Respuesta<>();
  		try{
  			UsuarioPetroperu usuario = obtenerUsuario();
  			String username = usuario.getUsername();
			List<Object[]> roles = rolDAO.listByUsuario(username);
			int esJefe = 0;
			for (Object[] rol : roles) {
				LOGGER.info(rol.toString());
				LOGGER.info("ROL:" + rol[3].toString());
				if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
					esJefe = 1;
				}
			}
			DataTableRequestCorrespondenciaConsultaDTO dataTableRequest = new DataTableRequestCorrespondenciaConsultaDTO(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<CorrespondenciaConsultaDTO> respuestaCorrespondenciaConsulta = emisionService.consultarCorrespondenciasPaginado(dataTableRequest.getFiltro(), usuario.getUsername(), esJefe, start, dataTableRequest.getLength(), dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);
			//LOGGER.info("DRAW:" + dataTableRequest.getDraw());
			DataTableResults<CorrespondenciaConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaCorrespondenciaConsulta.datos);
			/*for(int i=0;i<dataTableResults.getListOfDataObjects().size(); i++){
				LOGGER.info("DATOS:" + dataTableResults.getListOfDataObjects().get(i).toString());
			}*/
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaCorrespondenciaConsulta.total.toString());
			dataTableResults.setRecordsTotal(respuestaCorrespondenciaConsulta.total.toString());
			respuesta.datos.add(dataTableResults);
			//Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
			//respuesta = emisionService.consultarCorrespondenciasJefeGestor(filtro, usuario.getUsername(), esJefe, locale);
  		} catch(Exception e) {
  			LOGGER.error("[ERROR]", e);
  		}
  		return new ResponseEntity<>(respuesta, HttpStatus.OK);
  	}
  	// FIN TICKET
  
  //Inicio 4408
  @PostMapping(value = "/consultar-jefe-gestor", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Respuesta<CorrespondenciaConsultaDTO>> consultarCorrespondenciasJefeGestor(@RequestBody FiltroConsultaCorrespondenciaDTO filtro, Locale locale){
	  UsuarioPetroperu usuario = obtenerUsuario();
	  String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		int esJefe = 0;
		for (Object[] rol : roles) {
			LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
				esJefe = 1;
			}
		}
	  Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
	  respuesta = emisionService.consultarCorrespondenciasJefeGestor(filtro, usuario.getUsername(), esJefe, locale);
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  //Fin 4408
  
  @PostMapping(value = "/consultar-bandeja-salida-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<InputStreamResource> exportExcelConsulta(@RequestBody FiltroConsultaCorrespondenciaDTO filtro, Locale locale){
	  LOGGER.info("[INICIO] exportExcelConsulta");
	  UsuarioPetroperu usuario = obtenerUsuario();
	  String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		int esJefe = 0;
		for (Object[] rol : roles) {
			LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
				esJefe = 1;
			}
		}
	  Respuesta<ByteArrayInputStream> respuesta = emisionService.consultarCorrespondenciasExcel(filtro, usuario.getNombreCompleto(), username, esJefe, locale);
	  if(respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	  }
	  HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
  }
  
  // TICKET 9000004494
  @GetMapping(value = "/consultar-jefe-gestor-paginado", produces =  MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Respuesta<DataTableResults<CorrespondenciaConsultaDTO>>> consultarCorrespondenciasJefeGestorPaginado(HttpServletRequest request, Locale locale){
	  Respuesta<DataTableResults<CorrespondenciaConsultaDTO>> respuesta = new Respuesta<>();
	  try{
		  UsuarioPetroperu usuario = obtenerUsuario();
		  String username = usuario.getUsername();
			List<Object[]> roles = rolDAO.listByUsuario(username);
			int esJefe = 0;
			for (Object[] rol : roles) {
				LOGGER.info(rol.toString());
				LOGGER.info("ROL:" + rol[3].toString());
				if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
					esJefe = 1;
				}
			}
			DataTableRequestCorrespondenciaConsultaDTO dataTableRequest = new DataTableRequestCorrespondenciaConsultaDTO(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<CorrespondenciaConsultaDTO> respuestaCorrespondenciaConsultaJefeGestor = emisionService.consultarCorrespondenciasJefeGestorPaginado(dataTableRequest.getFiltro(), usuario.getUsername(), esJefe, usuario.getToken(), dataTableRequest.getLength(), start, dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);
			//LOGGER.info("DRAW:" + dataTableRequest.getDraw());
			DataTableResults<CorrespondenciaConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaCorrespondenciaConsultaJefeGestor.datos);
			/*for(int i=0;i<dataTableResults.getListOfDataObjects().size(); i++){
				LOGGER.info("DATOS:" + dataTableResults.getListOfDataObjects().get(i).toString());
			}*/
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaCorrespondenciaConsultaJefeGestor.total.toString());
			dataTableResults.setRecordsTotal(respuestaCorrespondenciaConsultaJefeGestor.total.toString());
			respuesta.datos.add(dataTableResults);
		  //Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		  //respuesta = emisionService.consultarCorrespondenciasJefeGestor(filtro, usuario.getUsername(), esJefe, locale);
	  } catch(Exception e) {
		  LOGGER.error("[ERROR]", e);
	  }
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  // FIN TICKET
  
//Inicio 4408
  @PostMapping(value = "/consultar-jefe-gestor-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<InputStreamResource> exportExcelConsultaJefeGestor(@RequestBody FiltroConsultaCorrespondenciaDTO filtro, Locale locale){
	  LOGGER.info("[INICIO] exportExcelConsulta");
	  UsuarioPetroperu usuario = obtenerUsuario();
	  String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		int esJefe = 0;
		for (Object[] rol : roles) {
			LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
				esJefe = 1;
			}
		}
	  Respuesta<ByteArrayInputStream> respuesta = emisionService.consultarCorrespondenciasExcelJefeGestor(filtro, usuario.getNombreCompleto(), username, esJefe, locale);
	  if(respuesta.estado == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	  }
	  HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=report.xlsx");
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(respuesta.datos.get(0)));
  }
//Fin 4408
  
  @GetMapping(value = "/buscar/todos-funcionarios")
  public ResponseEntity<Respuesta<ItemFilenet>> listarTodosFuncionarios(@RequestParam(value = "q", defaultValue = "") String filtro, Locale locale){
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarTodosFuncionarios(filtro));
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  @GetMapping(value = "/buscar/todos-dependencias")
  public ResponseEntity<Respuesta<ItemFilenet>> listarTodosDependencias(@RequestParam(value = "q", defaultValue = "") String filtro, Locale locale){
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarTodosDependencias(filtro));
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  //Inicio 4408
  @GetMapping(value = "/buscar/todos-dependencias-jefe-gestor")
  public ResponseEntity<Respuesta<ItemFilenet>> listarTodosDependenciasJefeGestor(@RequestParam(value = "q", defaultValue = "") String filtro, Locale locale){
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  UsuarioPetroperu usuario = obtenerUsuario();	 
	  String username = usuario.getUsername();
		List<Object[]> roles = rolDAO.listByUsuario(username);
		List<Integer> rolesInt = new ArrayList<>();
		//int esJefe = 0;
		for (Object[] rol : roles) {
			LOGGER.info(rol.toString());
			LOGGER.info("ROL:" + rol[3].toString());
			
			if("ROLE_GESTOR".equals(rol[3].toString())){ 
				rolesInt.add(2);
			}
			if("ROLE_JEFE".equals(rol[3].toString())){
				rolesInt.add(1);
			}			
		}
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarTodosDependenciasJefeGestor(usuario.getUsername(), filtro, rolesInt));
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  //Fin 4408
  
  	// TICKET 9000004651
  	@GetMapping(value = "/validar/nivel-firma/{correlativos}")
  	public ResponseEntity<Respuesta<ItemFilenet>> validarNivelFirma(@PathVariable("correlativos") String correlativos, Locale locale){
  		LOGGER.info("[INICIO] validarNivelFirma" + correlativos);
		Respuesta<ItemFilenet> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.mensaje = "OK";
		try{
			Long tipoFirma = correspondenciaEmisionService.validarNivelFirma(correlativos, locale);
			LOGGER.info("TIPO FIRMA:" + tipoFirma);
			if(tipoFirma.compareTo(0L) == 0){
				respuesta.estado = false;
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.notificar.error_nivel_firma", null, locale);
			}else{
				LOGGER.info("NO REALIZA CORRECTAMENTE LA COMPARACION");
			}
		} catch (Exception e) {
			LOGGER.error("[ERROR]", e);
		}
		LOGGER.info("[FIN] validarNivelFirma");
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
  	}
  	// FIN TICKET

	// TICKET 9000004808
	@GetMapping({ "/descargar-archivo-con_firma/{idCorrespondencia}" })
	public ResponseEntity descargarArchivoAdjuntoConFirmaDigital(
			@PathVariable("idCorrespondencia") Long idCorrespondencia, Locale locale) throws IOException {
		List<Object[]> listadatos = new ArrayList<>();
		Respuesta<Correspondencia> correspondencia = new Respuesta<>();
		correspondencia = this.emisionService.buscar(idCorrespondencia, locale);

		Long idArchivoAdjunto = 0L;
		List<ArchivoAdjunto> archivos = correspondencia.datos.get(0).getAdjuntos();

		for (ArchivoAdjunto a : archivos) {
			LOGGER.info("Archivo " + a.getNombre() + " es principal: " + a.isPrincipal());
			if (a.isPrincipal()) {
				idArchivoAdjunto = a.getId();

				Respuesta<ArchivoAdjunto> respuesta = correspondenciaEmisionService
						.buscarArchivoAdjunto(idArchivoAdjunto, locale);
				if (respuesta.estado && respuesta.datos != null && respuesta.datos.size() > 0) {
					if (respuesta.datos.get(0).getIndicadorRemoto()
							.equalsIgnoreCase(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO)) {
						// FIN TICKET
						Object[] data = this.emisionService.obtenerArchivAdjunto(idArchivoAdjunto, locale);
						listadatos.add(data);

					} else {
						UsuarioPetroperu usuario = obtenerUsuario();
						Object[] data;
						Object[] file = correspondenciaService.descargarDocumentoServidor(usuario.getToken(),
								String.valueOf(idArchivoAdjunto), locale);
						if (file == null || file[0] == null) {
							Object[] data2 = this.emisionService.obtenerArchivAdjunto(idArchivoAdjunto, locale);
							listadatos.add(data2);
						} else {
							data = new Object[3];
							data[0] = file[0];
							data[1] = ((ArchivoAdjunto) respuesta.datos.get(0)).getContentType();
							data[2] = ((ArchivoAdjunto) respuesta.datos.get(0)).getNombre();
							
							listadatos.add(data);

						}
					}
				} else {
					Object[] data = new Object[1];
					data[0] = "No se encontró el archivo en base de datos.";
					return new ResponseEntity<>(data[0], HttpStatus.OK);
				}

			}
		}

		String nombreArchivo = "";
		if (listadatos.size() > 1) {
			nombreArchivo = correspondencia.datos.get(0).getCorrelativo().getCodigo() + ".zip";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos);
			for (Object[] d : listadatos) {
				if (d.length == 3) {
					byte[] dd = (byte[]) d[0];
					ZipEntry entry = new ZipEntry(d[2].toString());
					entry.setSize(dd.length);
					zos.putNextEntry(entry);
					zos.write(dd);
				} else {
					zos.closeEntry();
					zos.close();
					return new ResponseEntity<>(d[0], HttpStatus.OK);
				}
			}
			zos.closeEntry();
			zos.close();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=" + nombreArchivo);
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new InputStreamResource(new ByteArrayInputStream(baos.toByteArray())));

		} else {

			Object[] d = listadatos.get(0);
			if (d.length == 3){
				// INICIO TICKET 9000004944
				Utilitario utilitario = new Utilitario();
		    	String nombreArchi = utilitario.nombreArchivoDescarga((String)d[2]);
		    	String contenDisposition = "attachment; filename=\"" + nombreArchi + "\"";
		    	// FIN 9000004944
			return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().header("Content-Disposition",
					new String[] { contenDisposition })).contentType(MediaType.parseMediaType((String) d[1]))
							.body(d[0]);
			}else{
				return new ResponseEntity<>(d[0], HttpStatus.OK);
			}

		}

	}
	  // FIN TICKET
    
	
  	//TICKET 9000004462
  	@GetMapping(value = "/consultar-auditoria-bandeja-salida-paginado", produces =  MediaType.APPLICATION_JSON_VALUE)
  	public ResponseEntity<Respuesta<DataTableResults<CorrespondenciaConsultaDTO>>> consultarAuditoriaCorrespondenciasPaginado(HttpServletRequest request, Locale locale){
  		Respuesta<DataTableResults<CorrespondenciaConsultaDTO>> respuesta = new Respuesta<>();
  		try{
  			UsuarioPetroperu usuario = obtenerUsuario();
  			String username = usuario.getUsername();
			List<Object[]> roles = rolDAO.listByUsuario(username);
			int esJefe = 0;
			for (Object[] rol : roles) {
				LOGGER.info(rol.toString());
				LOGGER.info("ROL:" + rol[3].toString());
				if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
					esJefe = 1;
				}
			}
			DataTableRequestCorrespondenciaConsultaDTO dataTableRequest = new DataTableRequestCorrespondenciaConsultaDTO(request);
			int start = (dataTableRequest.getStart() / dataTableRequest.getLength()) + 1;
			Respuesta<CorrespondenciaConsultaDTO> respuestaCorrespondenciaConsulta = emisionService.consultarAuditoriaCorrespondenciasPaginado(dataTableRequest.getFiltro(), usuario.getUsername(), esJefe, start, dataTableRequest.getLength(), dataTableRequest.getColumna(), dataTableRequest.getOrdenDesc(), "NO", locale);
			//LOGGER.info("DRAW:" + dataTableRequest.getDraw());
			DataTableResults<CorrespondenciaConsultaDTO> dataTableResults = new DataTableResults<>();
			dataTableResults.setListOfDataObjects(respuestaCorrespondenciaConsulta.datos);
			/*for(int i=0;i<dataTableResults.getListOfDataObjects().size(); i++){
				LOGGER.info("DATOS:" + dataTableResults.getListOfDataObjects().get(i).toString());
			}*/
			dataTableResults.setDraw(dataTableRequest.getDraw());
			dataTableResults.setRecordsFiltered(respuestaCorrespondenciaConsulta.total.toString());
			dataTableResults.setRecordsTotal(respuestaCorrespondenciaConsulta.total.toString());
			respuesta.datos.add(dataTableResults);
			//Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
			//respuesta = emisionService.consultarCorrespondenciasJefeGestor(filtro, usuario.getUsername(), esJefe, locale);
  		} catch(Exception e) {
  			LOGGER.error("[ERROR]", e);
  		}
  		return new ResponseEntity<>(respuesta, HttpStatus.OK);
  	}
  	
    @PostMapping(value = "/consultar-auditoria_bandeja-salida-excel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> exportExcelConsultaAuditoria(@RequestBody FiltroConsultaCorrespondenciaDTO filtro, Locale locale){
  	  LOGGER.info("[INICIO] exportExcelConsulta");
  	  UsuarioPetroperu usuario = obtenerUsuario();
  	  String username = usuario.getUsername();
  		List<Object[]> roles = rolDAO.listByUsuario(username);
  		int esJefe = 0;
  		for (Object[] rol : roles) {
  			LOGGER.info(rol.toString());
  			LOGGER.info("ROL:" + rol[3].toString());
  			if("ROLE_GESTOR".equals(rol[3].toString()) || "ROLE_JEFE".equals(rol[3].toString())){
  				esJefe = 1;
  			}
  		}
  	  Respuesta<ByteArrayInputStream> respuesta = emisionService.consultarAuditoriaCorrespondenciasExcel(filtro, usuario.getNombreCompleto(), username, esJefe, locale);
  	  if(respuesta.estado == false) {
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  	  }
  	  HttpHeaders headers = new HttpHeaders();
  		headers.add("Content-Disposition", "inline; filename=report.xlsx");
  		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
  				.body(new InputStreamResource(respuesta.datos.get(0)));
    }
    
  	// FIN TICKET
  	
  	
  @GetMapping(value = "/buscar/todos-dependencias-externas")
  public ResponseEntity<Respuesta<ItemFilenet>> listarTodosDependenciasExternas(@RequestParam(value = "q", defaultValue = "") String filtro, Locale locale){
	  Respuesta<ItemFilenet> respuesta = new Respuesta<>();
	  respuesta.estado = true;
	  respuesta.mensaje = "OK";
	  respuesta.datos.addAll(fileNetService.listarTodosDependenciasExternas(filtro));
	  return new ResponseEntity<>(respuesta, HttpStatus.OK);
  }
  
  private UsuarioPetroperu obtenerUsuario() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
      return (UsuarioPetroperu)auth.getPrincipal();
    }
    return null;
  }
  
}
