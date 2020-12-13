/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author andre
 */
public class ElAutomata {

//***************************DE AFNL A AFN************************************
    public static AFN AFN_LambdaToAFN(AFN_Lambda afnl) {

        AFN afn = new AFN();
        int rowsNumber = afnl.getStates().size();
        int columnsNumber = afnl.getSigma().size();

        ArrayList<String>[][] newDelta = new ArrayList[rowsNumber][columnsNumber]; // este es el delta del nuevo AFN
        ArrayList<String> newFinalStates = new ArrayList<>();//estos son los nuevos estados de aceptación
        ArrayList<Character> newSigma = new ArrayList<>();

        for (int u = 0; u < afnl.getSigma().size() - 1; u++) {

            newSigma.add(afnl.getSigma().get(u));

        }
        ArrayList<String> comparación = new ArrayList<>();//estos son los nuevos estados de aceptación
        //inicializando newDelta
        for (int i = 0; i < afnl.getStates().size(); i++) {
            for (int j = 0; j < afnl.getSigma().size(); j++) {
                newDelta[i][j] = new ArrayList<String>();
            }
        }

        //calculando los nuevos estados de aceptación
        for (int i = 0; i < afnl.getStates().size(); i++) {

            comparación.clear();
            comparación = (ArrayList<String>) afnl.calcularLambdaClausura(afnl.getStates().get(i)).clone();

            for (int j = 0; j < afnl.getFinalStates().size(); j++) {

                if (comparación.contains(afnl.getFinalStates().get(j))) {

                    if (!newFinalStates.isEmpty()) {

                        if (!newFinalStates.contains(afnl.getStates().get(i))) {
                            newFinalStates.add(afnl.getStates().get(i));
                        }

                    } else {
                        newFinalStates.add(afnl.getStates().get(i));
                    }

                }

            }

        }

        Collections.sort(newFinalStates);

        //Llenando la matriz newDelta
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < columnsNumber - 1; j++) {
                ArrayList<String> states = new ArrayList<>();
                states = afnl.calcularLambdaClausura(afnl.getStates().get(i));// Aqui inicia el proceso de calcular la lambda clausura de un estado
                String symbol = Character.toString(afnl.getSigma().get(j));

                ArrayList<String> states2 = new ArrayList<>();//Ahora se mire a donde se llega con un simbolo y la lambda clausura del estado anterior
                states2 = afnl.ProcessStatesWithSymbol(states, symbol, afnl); // y los estados resultantes son states2
                Collections.sort(states2);

                // esta son las nuevas transiciones para el estado y el simbolo en el nuevo AFN
                ArrayList<String> states3 = new ArrayList<>();
                states3 = afnl.calcularMuchasLambdaClausura(states2);
                Collections.sort(states3);

                newDelta[i][j] = states3;
            }

        }

        for (int u = 0; u < newDelta.length; u++) {

            for (int v = 0; v < newDelta[u].length; v++) {

                if (newDelta[u][v].isEmpty()) {
                    newDelta[u][v].add(afnl.getStates().get(u));
                }

            }

        }

        afn.initializeAFNwithData(newSigma, afnl.getStates(), afnl.getQ(), newFinalStates, newDelta);

        return afn;
    }

    ;
//***************************DE AFNL A AFN************************************

//***************************DE AFN A AFD*************************************

    public static int getRow(String state, ArrayList<String> states) {
        //esta función es para obtener la fila en la que se encuentra un estado (se asume columna 0)
        for (int i = 0; i < states.size(); i++) {
            //solo nos interesa la los elementos de la primera columna entonces por eso la fijamos en [j][0]
            if (state.equals(states.get(i))) {
                return i;
            }
        }
        return -1; // esto nunca deberia pasar a no se que pas eun error de digitación
    }

    public static String concatStates(ArrayList<String> delta) {
        ArrayList<String> mydelta = delta;
        Collections.sort(mydelta);

        //se concatenan los estados
        String newState = "";
        if (mydelta.size() > 1) {
            for (int i = 0; i < mydelta.size() - 1; i++) {
                newState = newState.concat(mydelta.get(i) + ";");
            }
            newState = newState.concat(mydelta.get(mydelta.size() - 1));
        } else {
            newState = mydelta.get(0);
        }
        return newState;
    }

    public static boolean containsFinal(ArrayList<String> delta, ArrayList<String> finalStates) {
        boolean bool = false;
        for (int i = 0; i < delta.size(); i++) {
            if (finalStates.contains(delta.get(i))) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    public static AFD AFNtoAFD(AFN afn) {

        //System.out.println("Iniciando transformacion de AFN a AFD\n\n");
        //se almacena localmente el AFN para ser editado
        ArrayList<String>[][] delta = afn.getDelta();
        ArrayList<Character> sigma = afn.getSigma();
        ArrayList<String> AFNstates = afn.getStates();
        ArrayList<String> finalStates = afn.getFinalStates();
        String q = afn.getQ();
        //tamaño original de la lista de estados
        int statesSize = AFNstates.size();

        //nuevo estado para añadir
        String newState;

        //datos del AFD producido
        ArrayList<String> AFDStates = new ArrayList<>();
        ArrayList<String> AFDfinalStates = new ArrayList<>();
        AFDStates.add(q);
        //se crea una matriz de ArrayList para el AFD
        ArrayList<String>[][] AFDdelta = new ArrayList[statesSize * 3][sigma.size()];
        for (int i = 0; i < statesSize * 3; i++) {
            for (int j = 0; j < sigma.size(); j++) {
                AFDdelta[i][j] = new ArrayList<String>();
            }
        }

        //si q0 esta en los estados finales, se añade a los de AFD
        if (finalStates.contains(q)) {
            AFDfinalStates.add(q);
        }

        //Se imprime la matriz delta original
        //System.out.print("  D  !!  ");
        for (int j = 0; j < sigma.size(); j++) {
            //System.out.print(sigma.get(j) + "  |");
        }
        //System.out.println("|");

        for (int i = 0; i < statesSize; i++) {
            //System.out.print(AFNstates.get(i) + " !! ");
            for (int j = 0; j < sigma.size(); j++) {
                //System.out.print(" ");
                for (int k = 0; k < delta[i][j].size(); k++) {
                    //System.out.print(delta[i][j].get(k) + ";");
                }
                //System.out.print(" |");
            }
            //System.out.println("|");
        }

        //System.out.println("----------------------------------");
        //System.out.println("----------------------------------");
        //recorre cada uno de los estados alcanzables por el AFD final
        for (int i = 0; i < AFDStates.size(); i++) {

            //index para usar en la matriz delta del AFN
            int stateindex = getRow(AFDStates.get(i), AFNstates);

            //evalua si el estado pertenece a los estados del AFN original
            if (stateindex >= 0) {

                //itera a través del sigma
                for (int j = 0; j < sigma.size(); j++) {

                    //si mas de un estados en el delta
                    if (delta[stateindex][j].size() > 1) {

                        //concatena los estados a los que pasan
                        newState = concatStates(delta[stateindex][j]);

                        //añade el edge al estado con el que se calculo el index
                        AFDdelta[i][j].add(newState);

                        //si este nuevo estado no esta registrado ya, lo añade
                        if (!AFDStates.contains(newState)) {

                            AFDStates.add(newState);

                            //si tiene un estado final, lo añade a los estados finales
                            if (containsFinal(delta[stateindex][j], finalStates)) {
                                AFDfinalStates.add(newState);
                            }
                        }

                    } //si el estado pertenece a los estados del AFN original y no esta en los del AFD, se añade
                    else if (delta[stateindex][j].size() == 1 && !AFDStates.contains(delta[stateindex][j].get(0))) {
                        AFDStates.add(delta[stateindex][j].get(0));
                        AFDdelta[i][j].add(delta[stateindex][j].get(0));
                        if (finalStates.contains(delta[stateindex][j].get(0))) {
                            AFDfinalStates.add(delta[stateindex][j].get(0));
                        }
                    } else {
                        AFDdelta[i][j].add(delta[stateindex][j].get(0));
                    }

                }

            } //Si el estado originario es uno de los estados concatenados
            else {

                //System.out.print(AFDStates.get(i) + " !! ");
                //encontrar a los estados que puede saltar
                ArrayList<String> newStates = new ArrayList<>();

                //se divide el estado en sus componentes
                String[] split = AFDStates.get(i).split(";");

                //se ubica en una letra de sigma
                for (int j = 0; j < sigma.size(); j++) {

                    //se mueve por los diferentes Arraylist que contienen los estados que componen el estado
                    for (int k = 0; k < split.length; k++) {

                        //index para usar en la matriz delta del AFN
                        int index = getRow(split[k], AFNstates);

                        //itera sobre los diferentes estados del arraylist
                        for (int l = 0; l < delta[index][j].size(); l++) {

                            newState = delta[index][j].get(l);

                            //si no esta contenido, lo añade
                            if (!newStates.contains(newState)) {
                                newStates.add(newState);
                            }
                        }

                    }

                    //concatena los estados a los que pasan
                    newState = concatStates(newStates);

                    //añade el edge al estado con el que se calculo el index
                    AFDdelta[i][j].add(newState);
                    //System.out.print(" " + newState + " |");

                    //si este nuevo estado no esta registrado ya, lo añade
                    if (!AFDStates.contains(newState)) {

                        AFDStates.add(newState);

                        //si tiene un estado final, lo añade a los estados finales
                        if (containsFinal(newStates, finalStates)) {
                            AFDfinalStates.add(newState);
                        }
                    }

                    //se limpia el arraylist para volver a usarse
                    newStates.clear();
                }
                //System.out.println("|");
            }

        }

        //System.out.println("\n");
        //se crea y se retorna el AFD producido
        AFD afd = new AFD();
        afd.initializeAFDwithData(sigma, AFDStates, q, AFDfinalStates, AFDdelta);

        //System.out.println("Terminando transformacion de AFN a AFD\n\n");
        return afd;
    }
//***************************DE AFN A AFD*************************************

//***************************DE AFNL A AFD*************************************
    public static AFD AFN_LambdaToAFD(AFN_Lambda afnl) {

        AFN afn = new AFN();

        afn = AFN_LambdaToAFN(afnl);
        return AFNtoAFD(afn);

    }

//***************************DE AFNL A AFD*************************************    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
