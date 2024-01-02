package pe.com.petroperu.model.emision;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "tipo_emision")
public class TipoEmision
  extends EntidadBase
  implements Serializable
{
  @Id
  private Integer id;
  @Column(nullable = false, length = 25)
  private String nombre;
  
  public Integer getId() { return this.id; }

  
  public void setId(Integer id) { this.id = id; }

  
  public String getNombre() { return this.nombre; }

  
  public void setNombre(String nombre) { this.nombre = nombre; }
}
