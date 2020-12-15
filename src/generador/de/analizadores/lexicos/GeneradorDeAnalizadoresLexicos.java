/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generador.de.analizadores.lexicos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import Automatas.AFN;
import Automatas.ElAutomata;
import Regexp.Models.Declaracion;
import Regexp.Models.Definicion;
import Regexp.Util.Writer;

/**
 *
 * @author Equipo
 */
public class GeneradorDeAnalizadoresLexicos {

    public static String regexp;

    public static void main(String[] args) throws Exception {

        String cadena;
        int linea = 0;
        boolean error = false;
        boolean def = false;
        boolean space;
        int openbracket1 = 0, closedbracket1 = 0, openbracket2 = 0, closedbracket2 = 0;
        String nombre, regexp, ret;
        ArrayList<Declaracion> decList = new ArrayList<Declaracion>();
        ArrayList<Definicion> defList = new ArrayList<Definicion>();
        boolean bracket = false;

        FileReader f = new FileReader("entrada.txt");
        BufferedReader b = new BufferedReader(f);
        while ((cadena = b.readLine()) != null) {
            if (!cadena.equals("")) {
                linea++;
                if (!def) {
                    Declaracion declaracion = new Declaracion();
                    if (cadena.equals("%%")) {
                        def = true;
                    } else {
                        error = true;
                        for (int i = 0; i < cadena.length(); i++) {
                            if (cadena.charAt(i) == ' ') {
                                declaracion.setNombre(cadena.substring(0, i));
                                declaracion.setRegexp(cadena.substring(i + 1));
                                decList.add(declaracion);
                                error = false;
                            }
                        }
                        if (error) {
                            System.out.println("ERROR: Formato incorrecto en la linea: " + linea);
                        }
                    }
                } else {
                    Definicion definicion = new Definicion();
                    openbracket1 = -1;
                    closedbracket1 = -1;
                    openbracket2 = -1;
                    closedbracket2 = -1;
                    space = false;
                    if (!cadena.equals("%%")) {
                        error = true;
                        for (int i = 0; i < cadena.length(); i++) {
                            if (!space) {
                                if (cadena.charAt(i) == ' ' && !bracket && openbracket1 != -1 && closedbracket1 != -1) {
                                    definicion.setNombre(cadena.substring(openbracket1 + 1, closedbracket1));
                                    space = true;
                                } else if (cadena.charAt(i) == '{' && !bracket) {
                                    bracket = true;
                                    openbracket1 = i;
                                } else if (cadena.charAt(i) == '}' && bracket) {
                                    bracket = false;
                                    closedbracket1 = i;
                                }
                            } else {
                                if (cadena.charAt(i) == '{' && !bracket) {
                                    bracket = true;
                                    openbracket2 = i;
                                } else if (cadena.charAt(i) == '}' && bracket) {
                                    bracket = false;
                                    closedbracket2 = i;
                                    definicion.setRetorno(cadena.substring(openbracket2 + 1, closedbracket2));
                                    error = false;
                                    defList.add(definicion);
                                }
                            }
                        }
                        if (openbracket1 == -1) {
                            System.out.println("ERROR: Se esperaba un '{' en la linea: " + linea);
                        }
                        if (closedbracket1 == -1) {
                            System.out.println("ERROR: Se esperaba un '}' en la linea: " + linea);
                        }
                        if (openbracket2 == -1) {
                            System.out.println("ERROR: Se esperaba un '{' en la linea: " + linea);
                        }
                        if (closedbracket2 == -1) {
                            System.out.println("ERROR: Se esperaba un '}' en la linea: " + linea);
                        }
                        if (error) {
                            System.out.println("ERROR: Formato incorrecto en la linea: " + linea);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        b.close();

        boolean consistente;
        // Consistencia de las definiciones respecto a las declaraciones
        for (int i = 0; i < defList.size(); i++) {
            consistente = false;
            for (int j = 0; j < decList.size(); j++) {
                if (defList.get(i).getNombre().equals(decList.get(j).getNombre())) {
                    consistente = true;
                    defList.get(i).setDeclaracion(decList.get(j));
                    defList.get(i).setDecList(decList);
                }
            }
            if (consistente == false) {
                System.out.println("ERROR: La definicion '" + defList.get(i).getNombre()
                        + "' NO es consistente con ninguna declaracion");
            }

            defList.get(i).ExpandRegexp();
        }

        // // ArrayList<Declaracion> declaraciones = new ArrayList<>();
        // ArrayList<Definicion> definiciones = new ArrayList<>();

        // definiciones.add(new Definicion("definicion1", "b+(ab)*", "null"));
        // definiciones.add(new Definicion("definicion2", "([)+", "null"));
        // definiciones.add(new Definicion("definicion3", "(0|1|2|3|4|5|6|7|8|9)+",
        // "null"));
        // definiciones.add(new Definicion("definicion4", "(0|1|2|3|4|5|6|7|8|9)?",
        // "null"));
        // definiciones.add(new Definicion("definicion5", "a*", "null"));

        int cont = 1;
        for (Definicion definicion : defList) {
            System.out.println("Entre aqui");
            Writer wr = new Writer(definicion, "definicion" + cont);
            wr.writeFile();
            cont++;
        }

        ArrayList<AFN> afnlist;
        afnlist = ElAutomata.InicializadorDeAutomatas(cont);
        System.out.println("Ingrese una oracion: (Solo puede ingresar una a la vez.)\n");
        Scanner sc = new Scanner(System.in);
        String opciones = "";
        boolean exit = false;
        while (exit != true) {
            opciones = sc.nextLine();
            if (opciones.equals("exit")) {
                exit = true;

            } else {
                ElAutomata.ProbarEnTodosLosAutomatas(opciones, afnlist);
            }
        }

    }

}
