package pe.com.petroperu.cliente.util;

/**
 * 
 * @author KenyoJoelPechoNaupar
 *
 */
public enum Operador
{
  LIKE("%LIKE%"),
  EQUALS("=");
  
  public String valor;

  
  Operador(String valor) { this.valor = valor; }
}
