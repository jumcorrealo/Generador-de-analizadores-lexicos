package Regexp.Models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Regexp.SyntaxUtil.ExpSimplify;
import Regexp.SyntaxUtil.PostFix;

public class AFNLambdaWriter {

  // Declaracion de atributos a usar en el AFN
  private List<Character> listaSimbolos;
  private List<Transition> listaTransiciones;
  private List<State> estadoFinal;
  private List<String> estadoInicial;
  private List<String> estados;
  private String regExp;
  private String postFixRegExp;
  public static int stateCount;

  // Atributos secundarios que nos ayudan a tener referencia de los estados
  // finales e iniciales
  private State saveFinal;
  private Stack<State> stackInitial;
  private Stack<State> stackFinal;

  // Simplificador de la expresion
  private ExpSimplify expressionSimplifier;

  // Constructor
  public AFNLambdaWriter(String regExp) {

    // Creacion de todas las estructuras
    listaSimbolos = new LinkedList<Character>();
    listaTransiciones = new LinkedList<Transition>();
    estadoFinal = new LinkedList<State>();
    estadoInicial = new LinkedList<String>();
    estados = new LinkedList<String>();
    stackInitial = new Stack<>();
    stackFinal = new Stack<>();

    // cuenta cuantos estados llevamos
    stateCount = 0;

    // Expresion regular
    this.regExp = regExp;
    expressionSimplifier = new ExpSimplify(this.regExp);

    // Version postfija de la expresion regular
    postFixRegExp = PostFix.infixToPostfix(expressionSimplifier.getRegExp());

    computarListaSimbolos();
    regExpAAFN();
    computarStateList();
    computarEstadoInicial();
  }

  // Computa todos los simbolos en la forma postfija y los agrega a la lista de
  // simbolos
  private void computarListaSimbolos() {
    for (int i = 0; i < postFixRegExp.length(); i++) {
      boolean simboloExiste = PostFix.operatorMap.containsKey(postFixRegExp.charAt(i));
      if (!simboloExiste) {
        boolean simboloContenido = listaSimbolos.contains(postFixRegExp.charAt(i));
        if (!simboloContenido) {
          listaSimbolos.add(postFixRegExp.charAt(i));
          Collections.sort(listaSimbolos);
        }
      }
    }
  }

  // Agrega todos los estados del AFN a la lista de estados
  public void computarStateList() {
    for (int i = 0; i < listaTransiciones.size(); i++) {
      String estadoI = listaTransiciones.get(i).getEstadoInicial().toString();
      if (!estados.contains(estadoI)) {
        estados.add(estadoI);
      }
      String estadoF = listaTransiciones.get(i).getEstadoFinal().toString();
      if (!estados.contains(estadoF)) {
        estados.add(estadoF);
      }
    }
  }

  // Agrega el estado inicial
  public void computarEstadoInicial() {
    estadoInicial.add(stackInitial.pop().toString());
  }

  // Parsea la expresion regular a un AFN
  private void regExpAAFN() {
    for (int i = 0; i < postFixRegExp.length(); i++) {
      // Entra si el caracter i se encuentra en la lista de simbolos
      Character simbolo = postFixRegExp.charAt(i);
      if (listaSimbolos.contains(simbolo)) {
        Transition tr1 = new Transition(Character.toString(simbolo));
        listaTransiciones.add(tr1);

        State estadoInicial = tr1.getEstadoInicial();
        stackInitial.push(estadoInicial);
        State estadoFinal = tr1.getEstadoFinal();
        stackFinal.push(estadoFinal);

      } else if (Character.toString(simbolo).equals("|")) {
        // Entra si el caracter i es un OR "|"
        State inicialAbajo = stackInitial.pop();
        State finalAbajo = stackFinal.pop();
        State inicialArriba = stackInitial.pop();
        State finalArriba = stackFinal.pop();

        unir(inicialArriba, finalArriba, inicialAbajo, finalAbajo);

      } else if (Character.toString(simbolo).equals("*")) {
        // Entra si el caracter i es una estrella de Kleene "*"
        State estadoInicial = stackInitial.pop();
        State estadoFinal = stackFinal.pop();

        kleene(estadoInicial, estadoFinal);
      } else if (Character.toString(simbolo).equals(".")) {
        // Entra si el caracter i es una concatenacion explicita "."
        saveFinal = stackFinal.pop();
        State estadoFinal = stackFinal.pop();
        State estadoInicial = stackInitial.pop();

        concatenar(estadoFinal, estadoInicial);
      }

      //cuando llegamos al final de la cadena
      if (i == postFixRegExp.length() - 1) {
        estadoFinal.add(stackFinal.pop());
        if (listaSimbolos.contains('$')) {
          listaSimbolos.remove(listaSimbolos.indexOf('$'));
        }
      }
    }
  }

  // Caracter OR "|", se realiza la union de los estados
  private void unir(State estadoInicialArriba, State estadoFinalArriba, State estadoInicialAbajo,
      State estadoFinalAbajo) {

    State inicio = new State(stateCount);
    State fin = new State(stateCount);

    // create new transitions
    Transition tr1 = new Transition("$", inicio, estadoInicialArriba);
    Transition tr2 = new Transition("$", inicio, estadoInicialAbajo);

    Transition tr3 = new Transition("$", estadoFinalArriba, fin);
    Transition tr4 = new Transition("$", estadoFinalAbajo, fin);

    listaTransiciones.add(tr1);
    listaTransiciones.add(tr2);
    listaTransiciones.add(tr3);
    listaTransiciones.add(tr4);

    stackInitial.push(inicio);
    stackFinal.push(fin);
  }

  // Concatenacion, se concatenan los dos estados por medio de un estado vacio con
  // transicion lambda
  private void concatenar(State estadoInicial, State estadoFinal) {
    Transition tr1 = new Transition("$", estadoInicial, estadoFinal);
    listaTransiciones.add(tr1);

    stackFinal.push(saveFinal);
    saveFinal = null;
  }

  // Estrella de Kleene "*", se realiza el ciclo de estados para obtener cero o
  // muchos simbolos
  private void kleene(State estadoInicial, State estadoFinal) {
    State inicio = new State(stateCount);
    State fin = new State(stateCount);

    Transition transicion1 = new Transition("$", estadoFinal, estadoInicial);
    Transition transicion2 = new Transition("$", inicio, fin);
    Transition transicion3 = new Transition("$", inicio, estadoInicial);
    Transition transicion4 = new Transition("$", estadoFinal, fin);

    listaTransiciones.add(transicion1);
    listaTransiciones.add(transicion2);
    listaTransiciones.add(transicion3);
    listaTransiciones.add(transicion4);

    stackInitial.push(inicio);
    stackFinal.push(fin);
  }

  public List<Character> getListaSimbolos() {
    return this.listaSimbolos;
  }

  public List<Transition> getListaTransiciones() {
    return this.listaTransiciones;
  }

  public List<State> getEstadoFinal() {
    return this.estadoFinal;
  }

  public List<String> getEstados() {
    return this.estados;
  }

  public List<String> getEstadoInicial() {
    return this.estadoInicial;
  }

  public String getPostFixRegExp() {
    return this.postFixRegExp;
  }

}
