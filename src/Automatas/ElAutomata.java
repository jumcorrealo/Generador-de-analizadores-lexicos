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
    
public static ArrayList<String> ProcessStatesWithSymbol(ArrayList<String> states, String symbol, AFN_Lambda afnl) {

        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {

            int row = afnl.getPosEstado(states.get(i));
            int column = afnl.getPosSimbolo(symbol);
            ArrayList<String> temp = new ArrayList<>();
            temp = afnl.getDelta()[row][column]; // Aqui guardo la info que me dio delta para un usarla mas facil

            for (int j = 0; j < temp.size(); j++) {

                if (!result.isEmpty() && !temp.isEmpty()) { // confirmo esto para evitar que el programa se muera en ciertos casos
                    if (!result.contains(temp.get(j))) { // confirmo que no se haya agregado antes

                        result.add(temp.get(j)); // agrego los estados

                    }
                } else if (result.isEmpty() && !temp.isEmpty()) {

                    result.add(temp.get(j));

                }

            }

        }

        return result;
    }
    
public static AFN AFN_LambdaToAFNSinImprimir(AFN_Lambda afnl) {

        
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

        //System.out.println("");
        Collections.sort(newFinalStates);

        //Imprimiendo lambda clausuras de cada estado 
        afnl.calcularMuchasLambdaClausuraSinImprimir(afnl.getStates());
        //System.out.println("");
        //Llenando la matriz newDelta
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < columnsNumber - 1; j++) {
                ArrayList<String> states = new ArrayList<>();
                states = afnl.calcularLambdaClausura(afnl.getStates().get(i));// Aqui inicia el proceso de calcular la lambda clausura de un estado
                String symbol = Character.toString(afnl.getSigma().get(j));
                //System.out.print("D(" + afnl.getStates().get(i) + "," + symbol + ") = ");
                //System.out.print("$[D($[" + afnl.getStates().get(i) + "]," + symbol + ")] = ");
                //System.out.print("$[D({");

                for (int k = 0; k < states.size(); k++) {
                    //System.out.print(states.get(k));
                    if (k != states.size() - 1) {
                        //System.out.print(",");
                    };
                }

                //System.out.print("}," + symbol + ")] = ");

                ArrayList<String> states2 = new ArrayList<>();//Ahora se mire a donde se llega con un simbolo y la lambda clausura del estado anterior
                states2 = ProcessStatesWithSymbol(states, symbol, afnl); // y los estados resultantes son states2
                Collections.sort(states2);

                //System.out.print("$[{");

                for (int k = 0; k < states2.size(); k++) {
                    //System.out.print(states2.get(k));
                    if (k != states2.size() - 1) {
                        //System.out.print(",");
                    };
                }

                //System.out.print("}] = ");

                // esta son las nuevas transiciones para el estado y el simbolo en el nuevo AFN
                ArrayList<String> states3 = new ArrayList<>();
                states3 = afnl.calcularMuchasLambdaClausuraSinImprimir(states2);
                Collections.sort(states3);
                //System.out.print("{");

                for (int k = 0; k < states3.size(); k++) {
                    //System.out.print(states3.get(k));
                    if (k != states3.size() - 1) {
                        //System.out.print(",");
                    };
                }

                //System.out.print("}.");
                //System.out.println("");

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
        //System.out.println("\n\n");
        //System.out.println("Terminando transformacion de AFN_Lambda a AFN\n\n");

        return afn;
    };
    
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
