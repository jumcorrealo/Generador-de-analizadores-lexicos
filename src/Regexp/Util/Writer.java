package Regexp.Util;

import java.io.IOException;
import java.io.PrintWriter;

import Regexp.Models.AFNLambda;
import Regexp.Models.Declaracion;
import Regexp.Models.Definicion;

public class Writer {
  String regexp;
  String nombreArchivo;

  public Writer(Definicion definicion, String nombreArchivo) {
    this.regexp = definicion.getRegexp();
    this.nombreArchivo = nombreArchivo;
  }

  public Writer(Declaracion declaracion, String nombreArchivo) {
    this.regexp = declaracion.getRegExp();
    this.nombreArchivo = nombreArchivo;
  }

  public void writeFile() {
    try {
      AFNLambda afn = new AFNLambda(regexp);
      PrintWriter writer = new PrintWriter(nombreArchivo + ".txt", "UTF-8");
      writer.println("REGULAR EXPRESSION: " + regexp);
      writer.println("REGULAR EXPRESSION IN POSTFIX: " + afn.getPostFixRegExp());
      writer.println("SYMBOL LIST: " + afn.getListaSimbolos());
      writer.println("TRANSITIONS LIST: " + afn.getListaTransiciones());
      writer.println("FINAL STATE: " + afn.getEstadoFinal().toString());
      writer.println("STATES: " + afn.getEstados().toString());
      writer.println("INITIAL STATE: " + afn.getEstadoInicial().toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * public static String regexp; public static void main(String[] args) throws
   * Exception {
   * 
   * // ArrayList<Declaracion> declaraciones = new ArrayList<>();
   * ArrayList<Definicion> definiciones = new ArrayList<>();
   * 
   * definiciones.add(new Definicion("definicion1", "b+(ab)*", "null"));
   * definiciones.add(new Definicion("definicion2", "([)+", "null"));
   * definiciones.add(new Definicion("definicion3", "(0|1|2|3|4|5|6|7|8|9)+",
   * "null")); definiciones.add(new Definicion("definicion4",
   * "(0|1|2|3|4|5|6|7|8|9)?", "null"));
   * 
   * int cont = 1; for (Definicion definicion : definiciones) { Writer wr = new
   * Writer(definicion, "definicion"+cont); wr.writeFile(); cont++; } }
   */
}
