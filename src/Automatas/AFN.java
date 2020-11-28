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
}
