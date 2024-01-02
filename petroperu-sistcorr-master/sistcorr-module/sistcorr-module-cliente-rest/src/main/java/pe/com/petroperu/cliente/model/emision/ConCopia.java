package pe.com.petroperu.cliente.model.emision;

public class ConCopia
{
  public Long idDependencia;
  public String despachoFisico;
  
  public ConCopia() {}
  
  public ConCopia(Long idDependencia, String despachoFisico) {
    this.idDependencia = idDependencia;
    this.despachoFisico = despachoFisico;
  }


  
  public String toString() { return "ConCopia [idDependencia=" + this.idDependencia + ", despachoFisico=" + this.despachoFisico + "]"; }
}
