package Regexp.Models;

public class Declaracion {
  
  private String regexp;
  private String nombre;


  public Declaracion(String regexp, String nombre) {
    this.regexp = regexp;
    this.nombre = nombre;
  }

  public String getRegExp() {
    return this.regexp;
  }

  public void setRegExp(String regexp) {
    this.regexp = regexp;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

}
