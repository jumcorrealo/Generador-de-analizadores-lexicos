package Regexp.SyntaxUtil;

public class ExpSimplify {
  private String regExp;

  public ExpSimplify(String regExp) {
    this.regExp = regExp;
    procesarEstrellaSuma();
    procesarInterrogacion();
  }

  public String getRegExp() {
    return this.regExp;
  }

  // Funcion que simplifica el operador + en la aparicion de minimo un simbolo
  // concatenado con la estrella de kleene del simbolo
  public void procesarEstrellaSuma() {
    for (int i = 0; i < regExp.length(); i++) {
      if (Character.toString(regExp.charAt(i)).equals("+")) {
        // Pueden haber dos situaciones
        // 1. que el simbolo + se encuentre al frente de otro simbolo
        // 2. que el simbolo + se encuentre al frente de una expresion/simbolo encerrado
        // en parentesis

        // Caso 1
        if (!Character.toString(regExp.charAt(i - 1)).equals(")")) {
          String symbol = Character.toString(regExp.charAt(i - 1));
          String subExpression = symbol + symbol + "*";

          String leftPart = regExp.substring(0, i - 1);
          String rightPart = regExp.substring(i + 1);
          regExp = leftPart + subExpression + rightPart;
        } else {
          // Caso 2

          int bracketCounter = 0;
          for (int j = (i - 1); j >= 0; j--) {

            // Contar parentesis cerrados
            if ((j != (i - 1)) && (Character.toString(regExp.charAt(j)).equals(")"))) {
              bracketCounter++;
            }

            // Hacer parejas de parentesis
            if ((Character.toString(regExp.charAt(j)).equals("("))) {
              if (bracketCounter != 0) {
                bracketCounter--;
              } else {
                // String symbolSequence = (String) regExp.subSequence(0, j);
                String sequenceWithBrackets = (String) regExp.subSequence(j, i);
                String subExp = sequenceWithBrackets + sequenceWithBrackets + "*";

                String leftPart = regExp.substring(0, j);
                String rightPart = regExp.substring(i + 1);
                regExp = leftPart + subExp + rightPart;
              }
            }
          }
        }
      }
    }
  }

  // Funcion que simplifica el simbolo ? en una expresion entre aparecer la
  // expresion regular
  // o que aparezca la cadena vacia
  public void procesarInterrogacion() {

    for (int i = 0; i < regExp.length(); i++) {
      if (Character.toString(regExp.charAt(i)).equals("?")) {
        // Pueden haber dos situaciones
        // 1. que el simbolo ? se encuentre al frente de otro simbolo
        // 2. que el simbolo ? se encuentre al frente de una expresion/simbolo encerrado
        // en parentesis

        // Caso 1
        if (!Character.toString(regExp.charAt(i - 1)).equals(")")) {
          String symbol = Character.toString(regExp.charAt(i - 1));
          String subExpression = "(" + symbol + "|" + PostFix.lambda + ")";
          String leftPart = regExp.substring(0, i - 1);
          String rightPart = regExp.substring(i + 1);
          regExp = leftPart + subExpression + rightPart;
        } else {
          // Caso 2

          // Encontrar el primer parentesis que encierra la expresion
          for (int j = (i - 1); j >= 0; j--) {
            if ((Character.toString(regExp.charAt(j)).equals("("))) {

              if (j != 0) {
                // String symbolSequence = (String) regExp.subSequence(j + 1, i - 1);
                String sequenceWithBrackets = (String) regExp.subSequence(j, i);
                String subExp = "(" + sequenceWithBrackets + "|" + PostFix.lambda + ")";
                String leftPart = regExp.substring(0, j);
                String rightPart = regExp.substring(i + 1);
                regExp = leftPart + subExp + rightPart;
                break;
              }
            }
          }
        }
      }
    }
  }

}
