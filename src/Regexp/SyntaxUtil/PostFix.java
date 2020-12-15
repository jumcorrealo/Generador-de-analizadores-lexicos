package Regexp.SyntaxUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class PostFix {

  public static final Map<Character, Integer> operatorMap;
  public static final String lambda = "$";

  //Mapa de todos los operadores
  static {
    Map<Character, Integer> map = new HashMap<>();
    map.put('(', 1);
    map.put('|', 2);
    map.put('.', 3);
    map.put('?', 4);
    map.put('*', 4);
    map.put('+', 4);
    map.put('^', 5);
    operatorMap = Collections.unmodifiableMap(map);
  }

  //Obtiene la precedencia del simbolo
  private static Integer getPrecedence(Character c) {
    Integer precedence = operatorMap.get(c);
    return precedence == null ? 6 : precedence;
  }

  //Aplica formato a la expresion regular dejando las concatenaciones explicitas con el .
  private static String formatRegEx(String regex) {
    String res = new String();
    List<Character> allOperators = Arrays.asList('|', '?', '+', '*', '^');
    List<Character> binaryOperators = Arrays.asList('^', '|');

    for (int i = 0; i < regex.length(); i++) {
      Character c1 = regex.charAt(i);

      if (i + 1 < regex.length()) {
        Character c2 = regex.charAt(i + 1);

        res += c1;

        if (!c1.equals('(') && !c2.equals(')') && !allOperators.contains(c2) && !binaryOperators.contains(c1)) {
          res += '.';
        }
      }
    }
    res += regex.charAt(regex.length() - 1);

    return res;
  }

  // Clase para convertir la expresion regular en forma infija a la forma postfija
  public static String infixToPostfix(String regex) {
    String postfix = new String();

    Stack<Character> stack = new Stack<>();

    String formattedRegEx = formatRegEx(regex);

    for (Character c : formattedRegEx.toCharArray()) {
      switch (c) {
        case '(': {
          stack.push(c);
          break;
        }
        case ')': {
          while (!stack.peek().equals('(')) {
            postfix += stack.pop();
          }
          stack.pop();
          break;
        }
        default: {
          while (stack.size() > 0) {
            Character topChar = stack.peek();

            Integer topCharPrecedence = getPrecedence(topChar);
            Integer actualCharPrecedence = getPrecedence(c);

            if (topCharPrecedence >= actualCharPrecedence) {
              postfix += stack.pop();
            } else {
              break;
            }
          }
          stack.push(c);
          break;
        }
      }
    }
    while (stack.size() > 0) {
      postfix += stack.pop();
    }

    return postfix;
  }
}
