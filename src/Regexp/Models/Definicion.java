package Regexp.Models;

import java.util.ArrayList;

public class Definicion {

  String nombre;
  String retorno;
  String regexp;
  Declaracion declaracion;
  ArrayList<Declaracion> decList;

  public Definicion() {
  }

  public Definicion(String nombre, String retorno, String regexp, Declaracion declaracion) {
    this.decList = new ArrayList<>();
    this.nombre = nombre;
    this.retorno = retorno;
    this.regexp = regexp;
    this.declaracion = declaracion;
  }

  public String getNombre() {
    return nombre;
  }

  public String getRetorno() {
    return retorno;
  }

  public String getRegexp() {
    return regexp;
  }

  public Declaracion getDeclaracion() {
    return declaracion;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setRetorno(String retorno) {
    this.retorno = retorno;
  }

  public void setRegexp(String regexp) {
    this.regexp = regexp;
  }

  public void setDeclaracion(Declaracion declaracion) {
    this.declaracion = declaracion;
  }

  public void ExpandRegexp() {
    this.regexp = declaracion.regexp;
    boolean bracket = false;
    boolean decl;
    int openBracket = -1;
    int closedBracket = -1;
    int i = 0;
    String subString1, subString2, dec, replace = "";
    while (i < this.regexp.length()) {

      if (this.regexp.charAt(i) == '{' && !bracket) {
        bracket = true;
        openBracket = i;
      } else if (this.regexp.charAt(i) == '}' && bracket) {
        bracket = false;
        closedBracket = i;
        dec = this.regexp.substring(openBracket + 1, closedBracket);
        // System.out.println(dec);
        subString1 = this.regexp.substring(0, openBracket);
        // System.out.println(subString1);
        subString2 = this.regexp.substring(closedBracket + 1, this.regexp.length());
        // System.out.println(subString2);
        decl = false;
        for (int j = 0; j < this.decList.size(); j++) {
          if (dec.equals(this.decList.get(j).nombre)) {
            replace = "(" + this.decList.get(j).regexp + ")";
            decl = true;
          }
        }
        if (!decl) {
          System.out.println("ERROR: '" + dec + "' no esta definida como una declaracion");
        }
        this.regexp = subString1 + replace + subString2;
        i = openBracket;
      }
      i++;
    }
  }

  public ArrayList<Declaracion> getDecList() {
    return this.decList;
  }

  public void setDecList(ArrayList<Declaracion> decList) {
    this.decList = decList;
  }

}