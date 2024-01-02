package pe.com.petroperu;

import java.util.ArrayList;
import java.util.List;






/**
 * 
 * @author KenyoJoelPechoNaupar
 *
 * @param <T>
 */
public class Respuesta<T>
{
  public boolean estado = false;
  public String mensaje = "PETROPERMENSAJE";
  public Integer total = 0;
  
  public List<T> datos;
  
  public Respuesta(boolean estado, String mensaje, List<T> datos) {
    this.estado = estado;
    this.mensaje = mensaje;
    this.datos = datos;
  }

  
  public Respuesta() { this.datos = new ArrayList<>(); }
}
