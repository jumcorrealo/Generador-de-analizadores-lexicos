package Regexp.Util;

import java.io.IOException;
import java.io.PrintWriter;

import Regexp.Models.AFNLambdaWriter;
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
    this.regexp = declaracion.getRegexp();
    this.nombreArchivo = nombreArchivo;
  }

  public void writeFile() {
    System.out.println("puta");
    try {
      AFNLambdaWriter afn = new AFNLambdaWriter(regexp);
      PrintWriter writer = new PrintWriter(nombreArchivo + ".txt", "UTF-8");
      writer.println("#alphabet");
      writer.println(afn.getListaSimbolos());
      writer.println("#states");
      writer.println(afn.getEstados().toString());
      writer.println("#initial");
      writer.println(afn.getEstadoInicial().toString());
      writer.println("#transitions");
      writer.println(afn.getListaTransiciones());
      System.out.println("puta finalizo.");
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
