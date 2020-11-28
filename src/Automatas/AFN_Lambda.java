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
public class AFN_Lambda {
     private ArrayList<Character> sigma;
        private ArrayList<String> states;
        private String q;
        private ArrayList<String> finalStates;
        private ArrayList<String>[][] delta;
        
        public AFN_Lambda() {
            this.sigma = new ArrayList<>();
            this.states = new ArrayList<>();
            this.finalStates = new ArrayList<>();
}

    public ArrayList<Character> getSigma() {
        return sigma;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public ArrayList<String> getFinalStates() {
        return finalStates;
    }

    public ArrayList<String>[][] getDelta() {
        return delta;
    }

    public String getQ() {
        return q;
    }
    
    
     
        public ArrayList<String> calcularMuchasLambdaClausuraSinImprimir(ArrayList<String> estados){
        
            ArrayList<String> muchasLambdaClausuras = new ArrayList<>();
            ArrayList<String> temporal = new ArrayList<>();
            
            for(int i = 0; i < estados.size(); i++){
                
                temporal = calcularLambdaClausura(estados.get(i));
                if(!temporal.isEmpty()){
                    
                    for(int j = 0;j < temporal.size();j++){
                      
                        if(!muchasLambdaClausuras.contains(temporal.get(j))){
                        
                            muchasLambdaClausuras.add(temporal.get(j));
                            
                        }
                        
                    }
                    
                    
                }
                
            }
        
            
            
            return muchasLambdaClausuras;
        }
        
        public int getPosEstado(String estado){
            int val = -1; // aqui se asignan posiciones a estados que no existen, posible solución es hacer val = -1 pero no se si afecte el codigo de cesar
            for(int i=0;i<this.states.size();i++){
//                System.out.println(this.states.get(i));
                if(estado.equals(this.states.get(i))){
                    val = i;
                    break;
                }
            }
            return val;
        }
        
        public int getPosSimbolo(String simbolo){
            int val = -1;
            for(int i=0;i<this.sigma.size();i++){
//                System.out.println(this.states.get(i));
                if(simbolo.equals(Character.toString(this.sigma.get(i)))){
                    val = i;
                    break;
                }
            }
            return val;
        }
    
          public ArrayList<String> calcularLambdaClausura(String estado){
        
            ArrayList<String> visitedStates = new ArrayList<>();//Los estados que ya visitamos para evitar caer en bucles
            ArrayList<String> statesToVisit = new ArrayList<>();//los estados que tenemos que visitar (solo entran los que no estan en el array de arriba)
            ArrayList<String> antiMultiplesVisitas = new ArrayList<>(); // esto evitara que un mismo lugar se visite varias veces (es complicado explicarlo aqui entonces preguntenme plox :v (A Nicolas))
            for(int i = 0;i < this.states.size();i++){
            antiMultiplesVisitas.add(this.states.get(i));
            }
           
            statesToVisit.add(estado);
            visitedStates.add(estado); // esto lo hago para que la función no tenga problema iniciando, al final del codigo se quita
            
            while(!statesToVisit.isEmpty()){
                
                int statePosition  = getPosEstado(statesToVisit.get(0));
                int symbolPosition = getPosSimbolo("$");
                
                if(this.delta[statePosition][symbolPosition].isEmpty()){
                //en el caso de que no vaya hacia ningun lado con lambda
                    visitedStates.add(statesToVisit.remove(0));
             
                }else{
                    //verificando que el estado que se quiere agregar no se haya visitado antes
                    
                    for(int i = 0; i < this.delta[statePosition][symbolPosition].size(); i++){
                  
                        for(int j = 0; j < visitedStates.size();j++ ){
                            
                            if(this.delta[statePosition][symbolPosition].get(i).equals(visitedStates.get(j))){
                                
                                break;
                                
                            }
                            
                            if(j == visitedStates.size()-1){
                                // si llego hasta aqui es por que el estado no se ha visitado hasta el momento entonces se agrega

                                if(antiMultiplesVisitas.contains(this.delta[statePosition][symbolPosition].get(i))){
                                    
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
        
        
        
        
        return visitedStates;
        
        };
        

}