package pe.com.petroperu.cliente.model.emision;


public class PropiedadesDocumento
{
  public String propiedad;
  public String valor;
  
  public PropiedadesDocumento() {}
  
  public PropiedadesDocumento(String propiedad, String valor) {
    this.propiedad = propiedad;
    this.valor = valor;
  }


  
  public String toString() { return "PropiedadesDocumento [propiedad=" + this.propiedad + ", valor=" + this.valor + "]"; }
}
