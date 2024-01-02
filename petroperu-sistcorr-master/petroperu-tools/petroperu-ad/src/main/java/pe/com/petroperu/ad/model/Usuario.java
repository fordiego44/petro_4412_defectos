package pe.com.petroperu.ad.model;

public class Usuario
{
  private String email;
  private String uid;
  private String nombreCompleto;
  private String nombres;
  private String apellidos;
  private String organizacion;
  private String unidadOrganizativa;
  
  public String getEmail() { return this.email; }

  
  public void setEmail(String email) { this.email = email; }

  
  public String getUid() { return this.uid; }

  
  public void setUid(String uid) { this.uid = uid; }

  
  public String getNombreCompleto() { return this.nombreCompleto; }

  
  public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

  
  public String getNombres() { return this.nombres; }

  
  public void setNombres(String nombres) { this.nombres = nombres; }

  
  public String getApellidos() { return this.apellidos; }

  
  public void setApellidos(String apellidos) { this.apellidos = apellidos; }

  
  public String getOrganizacion() { return this.organizacion; }

  
  public void setOrganizacion(String organizacion) { this.organizacion = organizacion; }

  
  public String getUnidadOrganizativa() { return this.unidadOrganizativa; }

  
  public void setUnidadOrganizativa(String unidadOrganizativa) { this.unidadOrganizativa = unidadOrganizativa; }




  
  public String toString() { return "Usuario [email=" + this.email + ", uid=" + this.uid + ", nombreCompleto=" + this.nombreCompleto + ", nombres=" + this.nombres + ", apellidos=" + this.apellidos + ", organizacion=" + this.organizacion + ", unidadOrganizativa=" + this.unidadOrganizativa + "]"; }
}
