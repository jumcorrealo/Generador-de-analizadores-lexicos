/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;

import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class AFN {
    private ArrayList<Character> sigma;
    private ArrayList<String> states;
    private ArrayList<String> endsStates;
    private String q;
    private ArrayList<String> finalStates;
    private ArrayList<String>[][] delta;
    public static boolean finale = false;

    public AFN() {
        this.endsStates = new ArrayList<>();
        this.sigma = new ArrayList<>();
        this.states = new ArrayList<>();
        this.finalStates = new ArrayList<>();
    }
    
        
    public void initializeDelta(int sizeOfStates, int sizeofSigma) {
        this.delta = new ArrayList[sizeOfStates][sizeofSigma];
        for (int i = 0; i < sizeOfStates; i++) {
            for (int j = 0; j < sizeofSigma; j++) {
                this.delta[i][j] = new ArrayList<String>();
            }
        }
    }
    
    public void showDelta() {
        System.out.println("#Transitions:");
        for (int i = 0; i < this.states.size(); i++) {
            for (int j = 0; j < this.sigma.size(); j++) {
                if(!this.delta[i][j].isEmpty()){
                System.out.print(this.states.get(i)+":");
                System.out.print(this.sigma.get(j)+">");
                for(int k=0;k<this.delta[i][j].size();k++){
                    System.out.print(this.delta[i][j].get(k));
                    if(k<this.delta[i][j].size()-1){
                    System.out.print(";");
                    }
                }
                }
                if(!this.delta[i][j].isEmpty()){
                    System.out.println("");
                }
            }
        }

    }
    
    public void initializeAFNwithData(ArrayList<Character> sigma, ArrayList<String> states, String q, ArrayList<String> finalStates, ArrayList<String>[][] delta) {

        //se guarda el sigma producido
        //System.out.print("sigma: ");
        for (int i = 0; i < sigma.size(); i++) {
            this.sigma.add(sigma.get(i));
        }
        //System.out.println("");

        //se guarda los estados producidos
        //System.out.print("states: ");
        for (int i = 0; i < states.size(); i++) {
            this.states.add(states.get(i));
            //System.out.print(this.states.get(i) + " ");
        }
        //System.out.println("");

        //se inicializa el delta
        this.initializeDelta(this.states.size(), this.sigma.size());

        //se guarda el q0
        //System.out.print("q: ");
        this.q = q;
        //System.out.println(this.q);

        //se guarda los estados finales
        //System.out.print("finalStates: ");
        for (int i = 0; i < finalStates.size(); i++) {
            this.finalStates.add(finalStates.get(i));
            //System.out.print(this.finalStates.get(i) + " ");
        }
        //System.out.println("");

        //se guarda la matriz delta
        //System.out.println("Delta: ");
        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < sigma.size(); j++) {
                for (int k = 0; k < delta[i][j].size(); k++) {

                    this.delta[i][j].add(delta[i][j].get(k));

                }

                //System.out.print(this.delta[i][j].get(0) + " ");
            }
            //System.out.println("");
        }
        //System.out.println("");

    }
    
//***************************GETTERS Y SETTERS************************************ 

    public ArrayList<Character> getSigma() {
        return sigma;
    }

    public void setSigma(ArrayList<Character> sigma) {
        this.sigma = sigma;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public void setStates(ArrayList<String> states) {
        this.states = states;
    }

    public ArrayList<String> getEndsStates() {
        return endsStates;
    }

    public void setEndsStates(ArrayList<String> endsStates) {
        this.endsStates = endsStates;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public ArrayList<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(ArrayList<String> finalStates) {
        this.finalStates = finalStates;
    }

    public ArrayList<String>[][] getDelta() {
        return delta;
    }

    public void setDelta(ArrayList<String>[][] delta) {
        this.delta = delta;
    }
    
//***************************GETTERS Y SETTERS************************************     
//***************************PROCESAMIENTO****************************************
    
        public int getRow(String state) {
        //esta función es para obtener la fila en la que se encuentra un estado (se asume columna 0)
        for (int i = 0; i < this.states.size(); i++) {
            //solo nos interesa la los elementos de la primera columna entonces por eso la fijamos en [j][0]
            if (state.equals(this.states.get(i))) {
                return i;
            }

        }
        return -1; // esto nunca deberia pasar a no se que pas eun error de digitación
    }

    public int getColumn(String symbol) {
        //esta función es para obtener la columna en la que se encuentra un simbolo (se asume fila 0 )
        for (int i = 0; i < this.sigma.size(); i++) {
            //solo nos interesa la los elementos de la primera fila entonces por eso la fijamos en [0][i]
            if (symbol.equals(Character.toString(this.sigma.get(i)))) {
                return i;
            }

        }
        return -1; // esto nunca deberia pasar a no se que pase un error de digitación
    }
    
     public boolean getTheResult(String estado, int letra, String cadena) {
        int i;
        if (letra >= cadena.length()) {
            for (i = 0; i < this.finalStates.size(); i++) {
                if (estado.equals(this.finalStates.get(i))) {
                    return true;
                }
            }
            //si supera el for
            return false;
        }
        return false;
    }
    
    public boolean procesarCadenaAFN(String cadena, String estado, int letra) {
        int i;
        //estado Actual
        String state;
        //pocision del estado actual
        int posState;
        //simbolo actual
        String symbol;
        //posicion del simbolo actual            
        int posSymbol;
        //letra
        int posChar = letra;
        //asignamos el estado Actual
        state = estado;

        //empezamos el proceso
        posState = getRow(state);
        if (cadena.length() == 0) {
            if (finalStates.contains(estado)) {
                return true;
            }
            return false;
        }
        symbol = Character.toString(cadena.charAt(posChar));

        posSymbol = getColumn(symbol);
        
        if(posSymbol == -1){
                System.out.print("No aceptación\n");
                return false;
            }

        if (!this.delta[posState][posSymbol].isEmpty()) {
            posChar++;
            for (i = 0; i < this.delta[posState][posSymbol].size(); i++) {
                state = this.delta[posState][posSymbol].get(i);
                //importante no dejar la impresion fuera del for o nada fuera del for porque a veces se lo traga el vacio
                if (getTheResult(state, posChar, cadena)) {
                    finale = true;
                }

                if (posChar < cadena.length()) {
                    //System.out.println("estado :"+state+">"+symbol);..
                    procesarCadenaAFN(cadena, state, posChar);
                }
                //Intento para solucionar el error de la cadena "*"
                //else if(posChar==1&&!cicle){
                //  processStringAFN(cadena,this.q,0,true);
                //}

            }

        } else {
            return false;
        }

        //verificando si esta en estado de aceptacion
        return finale;
    }
    
    public boolean procesarCadena(String string) {
        this.finale = false;
        boolean bool = procesarCadenaAFN(string, this.q, 0);
        if (bool) {
            System.out.println("----------------------------------------Cadena aceptada----------------------------------------");
            return bool;
        }
        System.out.println("----------------------------------------Cadena NO aceptada----------------------------------------");
        return bool;
    }
//***************************PROCESAMIENTO****************************************
}
