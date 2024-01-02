package pe.com.petroperu.ad.util;

public enum Enviroment
{
  PROD("prod"),
  QA("qa");
  
  public String name;

  
  Enviroment(String name) { this.name = name; }
}
