package pe.com.petroperu.cliente.model.emision;

public class Destinatario
{
  public Long idDependencia;
  public String despachoFisico;
  public String esEnviarPorCorreoElectronico;//TICKET 9000003934
  public String correoDestinatario;//TICKET 9000003934
  
  public Destinatario() {}
  
  public Destinatario(Long idDependencia, String despachoFisico) {
    this.idDependencia = idDependencia;
    this.despachoFisico = despachoFisico;
  }


  
  public String toString() { return "Destinatario [idDependencia=" + this.idDependencia + ", despachoFisico=" + this.despachoFisico + "]"; }
}
