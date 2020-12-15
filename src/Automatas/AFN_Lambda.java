/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 *
 * @author andre
 */
public class AFN_Lambda {

    private ArrayList<Character> sigma;
    private ArrayList<String> states;
    private String q;
    private ArrayList<String> finalStates;
    private ArrayList<String>[][] delta;

    public AFN_Lambda() {
        this.sigma = new ArrayList<>();;
        this.states = new ArrayList<>();;
        this.q = "";
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

    public void initializeAFN_Lambda(String fileRoute) throws FileNotFoundException, IOException {
        int cont = 0;
        File file = new File(fileRoute);

        if (!file.exists()) {
            System.out.println("NO SE ENCONTRO EL ARCHIVO");
            System.exit(1);
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringTokenizer tokenizer;

        String line;
        while ((line = br.readLine()) != null) {

            switch (line) {
                case ("#alphabet"):
                    cont = 1;
                    while (!(line = br.readLine()).startsWith("#")) {

                        
                            char ch = line.charAt(cont);   //el carcater 0 es [
                            
                            this.sigma.add(ch); //Agrego el primer caracter

                            cont++;
                            ch = line.charAt(cont); // y lo dejo parado en una coma
                            //Mientras sea diferente del caracter final
                            while (ch != ']') {
                               
                                cont++;
                                cont++;
                                ch = line.charAt(cont); //siempre estamos parados en una coma en este punto
      
                                this.sigma.add(ch);
                                cont++;
                                ch = line.charAt(cont);
                                
                            }
                    }
                    this.sigma.add('$');
                    


                case ("#states"):
                    cont = 1;
                    while (!(line = br.readLine()).startsWith("#")) {
                        
                        char ch = line.charAt(cont);
                        
                        String st = "";
                        while (ch != ']') {
                        
                            while(ch != ',' && ch != ']'){

                                st = st + ch;
                                cont++;
                                ch = line.charAt(cont);
                                
                            }
                            this.states.add(st);
                            st = "";
                            
                            if(ch != ']'){
                              cont = cont + 2;
                              
                              ch = line.charAt(cont);  // Aqui puede haber problema de limites
                            }
                             
                            }
                    }
                    //Inicializamos delta
                    this.finalStates.add(this.states.get(this.states.size()-1));//Aqui agrego los estados de aceptación
                    this.initializeDelta(this.states.size(), this.sigma.size());
                //System.out.println("Se creo una matriz de " + this.states.size() + " por " + this.sigma.size());

                case ("#initial"):
                    cont = 1;
                    while (!(line = br.readLine()).startsWith("#")) {
                        char ch = line.charAt(cont);
                        String st = "";
                        while (ch != ']') {
                        st = st + ch;
                        cont++;
                        ch = line.charAt(cont);  
                        }
                        this.q = st;
                    }



                case ("#transitions"):
                    cont = 1;
                    while ((line = br.readLine()) != null) {
                        int estadoPos = 0;
                        int simboloPos = 0;
                        char ch = line.charAt(cont);
                        String st = "";
                        while (ch != ']') {
                            while(ch != ':'){
                                st = st + ch;
                                cont++;
                                ch = line.charAt(cont);  
                            }
                           
                            estadoPos = getPosEstado(st);
                            st = "";
                            cont++;
                            ch = line.charAt(cont);
                            while(ch != '>'){
                                
                                st = st + ch;
                                cont++;
                                ch = line.charAt(cont);
                            }
                            
                            simboloPos = getPosSimbolo(st);
                            st = "";
                            cont++;
                            ch = line.charAt(cont);
                            while(ch != ',' && ch != ']'){
                                st = st + ch;
                                cont++;
                                ch = line.charAt(cont);
                            }
                            

                            this.delta[estadoPos][simboloPos].add(st);
                            st = "";
                            if(ch != ']'){
                              cont = cont + 2;
                              ch = line.charAt(cont);  // Aqui puede haber problema de limites
                            }
                             
                            
                            
                        }
                        
                    }
                default:

            }
        }
        br.close();

    }

    public int getPosEstado(String estado) {
        int val = -1; // aqui se asignan posiciones a estados que no existen, posible solución es hacer val = -1 pero no se si afecte el codigo de cesar
        for (int i = 0; i < this.states.size(); i++) {
            if (estado.equals(this.states.get(i))) {
                val = i;
                break;
            }
        }
        return val;
    }

    public int getPosSimbolo(String simbolo) {
        int val = -1;
        for (int i = 0; i < this.sigma.size(); i++) { 
            if (simbolo.equals(Character.toString(this.sigma.get(i)))) {
                val = i;
                break;
            }
        }
        return val;
    }

    public ArrayList<String> calcularLambdaClausura(String estado) {

        ArrayList<String> visitedStates = new ArrayList<>();//Los estados que ya visitamos para evitar caer en bucles
        ArrayList<String> statesToVisit = new ArrayList<>();//los estados que tenemos que visitar (solo entran los que no estan en el array de arriba)
        ArrayList<String> antiMultiplesVisitas = new ArrayList<>(); // esto evitara que un mismo lugar se visite varias veces (es complicado explicarlo aqui entonces preguntenme plox :v (A Nicolas))
        for (int i = 0; i < this.states.size(); i++) {
            antiMultiplesVisitas.add(this.states.get(i));
        }

        statesToVisit.add(estado);
        visitedStates.add(estado); // esto lo hago para que la función no tenga problema iniciando, al final del codigo se quita

        while (!statesToVisit.isEmpty()) {

            int statePosition = getPosEstado(statesToVisit.get(0));
            int symbolPosition = getPosSimbolo("$");

            if (this.delta[statePosition][symbolPosition].isEmpty()) {
                //en el caso de que no vaya hacia ningun lado con lambda
                visitedStates.add(statesToVisit.remove(0));

            } else {
                //verificando que el estado que se quiere agregar no se haya visitado antes

                for (int i = 0; i < this.delta[statePosition][symbolPosition].size(); i++) {

                    for (int j = 0; j < visitedStates.size(); j++) {

                        if (this.delta[statePosition][symbolPosition].get(i).equals(visitedStates.get(j))) {

                            break;

                        }

                        if (j == visitedStates.size() - 1) {
                            // si llego hasta aqui es por que el estado no se ha visitado hasta el momento entonces se agrega

                            if (antiMultiplesVisitas.contains(this.delta[statePosition][symbolPosition].get(i))) {

                                statesToVisit.add(this.delta[statePosition][symbolPosition].get(i)); // esto es para evitar multiples visitas debido a que visitedStates se actualiza lento 
                                antiMultiplesVisitas.remove(this.delta[statePosition][symbolPosition].get(i));

                            }

                        }

                    }

                }
                //pasando el estado que se acaba de visitar a la lista de visitados y quitandolo de los estados a visitar

                visitedStates.add(statesToVisit.remove(0));

            }

        }
        visitedStates.remove(0); // aqui se quita lo que añadi al inicio 
        Collections.sort(visitedStates);
        return visitedStates;

    }

    ;
    
    public ArrayList<String> calcularMuchasLambdaClausura(ArrayList<String> estados) {

        ArrayList<String> muchasLambdaClausuras = new ArrayList<>();
        ArrayList<String> temporal = new ArrayList<>();

        for (int i = 0; i < estados.size(); i++) {

            temporal = calcularLambdaClausura(estados.get(i));
            if (!temporal.isEmpty()) {

                for (int j = 0; j < temporal.size(); j++) {

                    if (!muchasLambdaClausuras.contains(temporal.get(j))) {

                        muchasLambdaClausuras.add(temporal.get(j));

                    }

                }

            }

        }

        return muchasLambdaClausuras;
    }

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

    
     public void showSigma() {
        System.out.println("#Alphabet:");
        if(this.sigma.size()>2){
        System.out.print(this.sigma.get(0)+"-");
        System.out.print(this.sigma.get(this.sigma.size()-2));
        }else{
        System.out.println(this.sigma.get(0));
        }
        System.out.println("");
    }

    public void showStates() {
        System.out.println("#States:");
        for (int i = 0; i < this.states.size(); i++) {
            System.out.println(this.states.get(i));
        }
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


}
