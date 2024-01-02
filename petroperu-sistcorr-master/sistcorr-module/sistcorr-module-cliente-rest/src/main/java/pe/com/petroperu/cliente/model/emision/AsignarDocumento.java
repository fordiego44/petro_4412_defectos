package pe.com.petroperu.cliente.model.emision;

import java.util.ArrayList;
import java.util.List;

public class AsignarDocumento
{
  public String nombre;
  public List<PropiedadesDocumento> propiedades = new ArrayList<>();


  
  public String toString() { return "AsignarDocumento [nombre=" + this.nombre + ", propiedades=" + this.propiedades + "]"; }
}
