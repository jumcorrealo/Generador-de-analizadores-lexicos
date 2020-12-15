package Regexp.Models;

public class Declaracion {
  String nombre;
  String regexp;

  public Declaracion() {
  }

  public Declaracion(String nombre, String regexp) {
    this.nombre = nombre;
    this.regexp = regexp;
  }

  public String getNombre() {
    return nombre;
  }

  public String getRegexp() {
    return regexp;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setRegexp(String regexp) {
    this.regexp = regexp;
  }

}
