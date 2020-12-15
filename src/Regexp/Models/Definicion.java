package Regexp.Models;

public class Definicion {
  private String nombre;
  private String regexp;
  private String retorno;


  public Definicion(String nombre, String regexp, String retorno) {
    this.nombre = nombre;
    this.regexp = regexp;
    this.retorno = retorno;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getRegexp() {
    return this.regexp;
  }

  public void setRegexp(String regexp) {
    this.regexp = regexp;
  }

  public String getRetorno() {
    return this.retorno;
  }

  public void setRetorno(String retorno) {
    this.retorno = retorno;
  }
  

}
