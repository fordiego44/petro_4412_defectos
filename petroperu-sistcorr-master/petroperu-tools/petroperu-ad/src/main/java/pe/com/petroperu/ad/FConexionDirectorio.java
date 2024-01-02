package pe.com.petroperu.ad;

import java.util.List;
import pe.com.petroperu.ad.model.Rol;
import pe.com.petroperu.ad.model.Usuario;
import pe.com.petroperu.ad.service.Conexion;
import pe.com.petroperu.ad.service.FedoraADService;
import pe.com.petroperu.ad.util.AConfiguracion;
import pe.com.petroperu.ad.util.ConfiguracionPROD;
import pe.com.petroperu.ad.util.ConfiguracionQA;
import pe.com.petroperu.ad.util.Enviroment;




public class FConexionDirectorio
{
  public static String getUsuarioDN(String usuario, Enviroment env) {
    FedoraADService service = getService(env);
    Conexion conexion = service.getConexion();
    return conexion.getUserDN(usuario);
  }
  
  public static boolean validarAcceso(String usuario, String contrasenia, Enviroment env) {
    usuario = usuario.toLowerCase().trim();
    FedoraADService service = getService(env);
    Conexion conexion = service.getConexion();
    String ldapUsuario = getUsuarioDN(usuario, env);
    return conexion.bindToServer(ldapUsuario, contrasenia, service.getConfiguracion());
  }
  
  public static Usuario datosUsuario(String usuario, Enviroment env) {
    FedoraADService service = getService(env);
    Conexion conexion = service.getConexion();
    return conexion.getUsuario(usuario);
  }
  
  public static List<Rol> datosGrupos(String usuario, Enviroment env) {
    FedoraADService service = getService(env);
    Conexion conexion = service.getConexion();
    return conexion.datoGrupo(getUsuarioDN(usuario, env), service.getConfiguracion().getROLES());
  }
  
  private static FedoraADService getService(Enviroment env) {
    FedoraADService service = null;
    if (env.name.equals(Enviroment.PROD.name)) {
      service = new FedoraADService((AConfiguracion)new ConfiguracionPROD());
    } else if (env.name.equals(Enviroment.QA.name)) {
      service = new FedoraADService((AConfiguracion)new ConfiguracionQA());
    } 
    return service;
  }
}
