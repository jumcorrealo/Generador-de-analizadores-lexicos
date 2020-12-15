package Regexp.Models;

/**
 * Transition.
 * Every symbol in the AFN is represented with a Transition that has an initial State, a final State and a String with the symbol.
 * Speaking in Graph terms, it would be an Edge e.
 * Created by Gabriel Brolo on 22/07/2017.
 */
public class Transition {
    private State estadoInicial;
    private State estadoFinal;
    private String transitionSymbol;

    public Transition(String transitionSymbol) {
        this.transitionSymbol = transitionSymbol;
        this.estadoInicial = new State(AFNLambda.stateCount);
        this.estadoFinal = new State(AFNLambda.stateCount);

        /* add link to states */
        this.estadoInicial.addEstadoSiguiente(this.estadoFinal);
        this.estadoFinal.addEstadoPrevio(this.estadoInicial);
    }

    // concatenate
    public Transition(String transitionSymbol, State estadoInicial, State estadoFinal) {
        this.transitionSymbol = transitionSymbol;
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;

        /* add link to states */
        this.estadoInicial.addEstadoSiguiente(this.estadoFinal);
        this.estadoFinal.addEstadoPrevio(this.estadoInicial);
    }

    public State getEstadoInicial() {
        return this.estadoInicial;
    }

    public State getEstadoFinal() {
        return this.estadoFinal;
    }

    public String toString() {
        return estadoInicial.toString() + ":" + transitionSymbol + ">" + estadoFinal.toString();
    }

    public String getTransitionSymbol() { return this.transitionSymbol; }
}
