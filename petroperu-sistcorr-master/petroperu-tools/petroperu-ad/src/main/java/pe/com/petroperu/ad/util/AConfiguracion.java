package pe.com.petroperu.ad.util;

import java.util.Arrays;



public abstract class AConfiguracion
{
  protected String IP_SERVIDOR;
  protected String URL_SERVIDOR;
  protected String PUERTO_SERVIDOR;
  protected static final String PAQUETE_BASE = "pe.com.petropeperu";
  protected static final String FILE_NAME = "ad.properties";
  protected String USUARIO;
  protected String CLAVE;
  protected String AUTENTICACION;
  protected String INITIAL_CONTEXT_FACTORY;
  protected String[] ROLES;
  
  public String getIP_SERVIDOR() { return this.IP_SERVIDOR; }

  
  public String getURL_SERVIDOR() { return this.URL_SERVIDOR; }

  
  public String getPUERTO_SERVIDOR() { return this.PUERTO_SERVIDOR; }

  
  public static String getPaqueteBase() { return "pe.com.petropeperu"; }

  
  public static String getFileName() { return "ad.properties"; }

  
  public String getUSUARIO() { return this.USUARIO; }

  
  public String getCLAVE() { return this.CLAVE; }

  
  public String getAUTENTICACION() { return this.AUTENTICACION; }

  
  public String getINITIAL_CONTEXT_FACTORY() { return this.INITIAL_CONTEXT_FACTORY; }

  
  public String[] getROLES() { return this.ROLES; }


  
  public String toString() {
    return "AConfiguracion [IP_SERVIDOR=" + this.IP_SERVIDOR + ", URL_SERVIDOR=" + this.URL_SERVIDOR + ", PUERTO_SERVIDOR=" + this.PUERTO_SERVIDOR + ", USUARIO=" + this.USUARIO + ", CLAVE=" + this.CLAVE + ", AUTENTICACION=" + this.AUTENTICACION + ", INITIAL_CONTEXT_FACTORY=" + this.INITIAL_CONTEXT_FACTORY + ", ROLES=" + 
      
      Arrays.toString((Object[])this.ROLES) + "]";
  }
}
