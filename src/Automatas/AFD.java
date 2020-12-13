package Automatas;

/**
 * @author Arthur
 */

import java.util.*;

public class AFD {

    private ArrayList<Character> sigma;
    private ArrayList<String> states;
    private String q;
    private ArrayList<String> finalStates;
    private ArrayList<String>[][] delta;

    public AFD() {
        this.sigma = new ArrayList<>();
        this.states = new ArrayList<>();
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

    public void showSigma() {
        System.out.println("#Alphabet:");
        if(this.sigma.size()>1){
        System.out.println(this.sigma.get(0)+"-"+this.sigma.get(this.sigma.size()-1));
        }else{
        System.out.println(this.sigma.get(0));
        }
    }

    public void showStates() {
        System.out.println("#States:");
        for (int i = 0; i < this.states.size(); i++) {
            System.out.println(this.states.get(i));
        }
    }

    public void showFinalStates() {
        System.out.println("#Accepting:");
        for (int i = 0; i < this.finalStates.size(); i++) {
            System.out.println(this.finalStates.get(i));
        }
    }

    public void showInitialState() {
        System.out.println("#Initial: ");
        System.out.println(this.q);
    }


    public void initializeAFDwithData(ArrayList<Character> sigma, ArrayList<String> states, String q, ArrayList<String> finalStates, ArrayList<String>[][] delta){
        
        //se guarda el sigma producido
        //System.out.print("sigma: ");
        for (int i = 0; i < sigma.size(); i++) {
            this.sigma.add(sigma.get(i));
            //System.out.print(this.sigma.get(i) + " ");
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
                this.delta[i][j].add(delta[i][j].get(0));
                //System.out.print(this.delta[i][j].get(0) + " ");
            }
            //System.out.println("");
        }
        //System.out.println("");

    }

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
    
//***************************PROCESAMIENTO****************************************    
        //Este metodo es el procesamiento
        public boolean processString(AFD afd, String string, boolean imprimir) { //Procesar string con o sin detalles

        if (imprimir == true) {
            System.out.print(string + "\n");
        }
        String actualState;// este es el estado actual
        int actualStateP;//fila del estado actual
        String actualSymbol; //char a leer
        int actualSymbolP; //columna del char a leer

        actualState = afd.getQ();
        //Ahora empezamos el proceso de la cadena
        while (!string.isEmpty() && !string.contains("$")) {
            actualStateP = afd.getRow(actualState);
            actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda en cada iteración y luego actualizamos el string.

            if (string.length() > 1) {
                string = string.substring(1); //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
            } else {
                string = "";
            }
            if (imprimir == true) {
                System.out.print("[" + actualState + "," + actualSymbol + string + "]->");
            }

            actualSymbolP = afd.getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)

            if (!afd.getDelta()[actualStateP][actualSymbolP].get(0).isEmpty()) {

                actualState = afd.getDelta()[actualStateP][actualSymbolP].get(0);//Ya que esto es un AFD, siempre habra como maximo un elemento en esa posición
            } else {
                break;
            }
        }
        if (imprimir == true) {
            System.out.print("[" + actualState + "]" + "\t");
        }

        for (int k = 0; k < afd.getFinalStates().size(); k++) {
            if (actualState.equals(afd.getFinalStates().get(k))) {
                System.out.print("Aceptación\n");
                return true;
            }
        }
        System.out.print("No aceptación\n");
        return false;

    }
    //Este es el que se llama, y hace el procesamiento de ariba sin imprimir los detalles    
    public boolean processString(AFD afd, String string) {//Procesar string SIN detalles

        return processString(afd, string, false);
    }
    //Este si los imprime
    public boolean processStringWithDetails(AFD afd, String string) {//Procesar string CON detalles

        return processString(afd, string, true);
    }
    
//***************************PROCESAMIENTO****************************************        

    public static void main(String[] args) throws Exception {

        AFD afd = new AFD();
        

        
    }

}