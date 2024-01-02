package pe.com.petroperu.cliente.model;

import java.io.Serializable;
import java.util.List;
import pe.com.petroperu.model.CorrespondenciaSimple;



public class Bandeja
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  List<CorrespondenciaSimple> correspondencias;
  
  public List<CorrespondenciaSimple> getCorrespondencias() { return this.correspondencias; }


  
  public void setCorrespondencias(List<CorrespondenciaSimple> correspondencias) { this.correspondencias = correspondencias; }
}
