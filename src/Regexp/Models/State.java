package Regexp.Models;

import java.util.LinkedList;
import java.util.List;

public class State {
  private List<State> estadosPrevios;
  private List<State> estadosSiguientes;
  private int estadoId;

  private boolean esInicial;
  private boolean esFinal;

  public State(int estadoId) {
    this.estadoId = estadoId;
    this.estadosPrevios = new LinkedList<State>();
    this.estadosSiguientes = new LinkedList<State>();
    AFNLambdaWriter.stateCount++;
  }

  public State(int estadoId, List<State> estadoPrevio, List<State> estadoSiguiente) {
    this.estadoId = estadoId;
    this.estadosPrevios = estadoPrevio;
    this.estadosSiguientes = estadoSiguiente;
    AFNLambdaWriter.stateCount++;
  }

  public State(int estadoId, boolean dfa) {
    this.estadoId = estadoId;
    this.estadosPrevios = new LinkedList<>();
    this.estadosSiguientes = new LinkedList<>();
  }

  public void addEstadoPrevio(State estadoPrevio) {
    this.estadosPrevios.add(estadoPrevio);
  }

  public void addEstadoSiguiente(State estadoSiguiente) {
    this.estadosSiguientes.add(estadoSiguiente);
  }

  public List<State> getEstadosPrevios() {
    return this.estadosPrevios;
  }

  public List<State> getEstadosSiguientes() {
    return this.estadosSiguientes;
  }

  public String toString() {
    return "s" + String.valueOf(this.estadoId);
  }

  public int getEstadoId() {
    return this.estadoId;
  }

  public void setInicial(boolean esInicial) {
    this.esInicial = esInicial;
  }

  public boolean getInicial() {
    return this.esInicial;
  }

  public void setFinal(boolean esFinal) {
    this.esFinal = esFinal;
  }

  public boolean getFinal() {
    return this.esFinal;
  }
}
