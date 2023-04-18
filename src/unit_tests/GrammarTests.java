package unit_tests;

import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.*;

public class GrammarTests {



    @Test
    void generateWord(){
        Grammar grammar = Grammar.baseGrammar();
        int count = 10;
        boolean wordCheck = true;

        while (count > 0){
            String word = grammar.generateWord();
            count--;

            for (int i = 0; i<word.length(); i++){
                if (!grammar.getTerminals().contains(String.valueOf(word.charAt(i)))){
                    wordCheck = false;
                    break;
                }
            }
        }

        Assertions.assertTrue(wordCheck, String.valueOf(true));

    }

    @Test
    void toFiniteAutomaton(){
        Grammar grammar = Grammar.baseGrammar();
        FiniteAutomaton fa = grammar.toFiniteAutomaton();
        Assertions.assertArrayEquals(fa.getStates().toArray(), new String[]{"A", "B", "S", "X"});
        Assertions.assertArrayEquals(fa.getAlphabet().toArray(), new String[]{"a", "b"});
        Assertions.assertEquals(fa.getStartState(), "S");
        Assertions.assertArrayEquals(fa.getAcceptStates().toArray(), new String[]{"X"});
        Assertions.assertEquals(Arrays.toString(fa.getTransitions()), Arrays.toString(new Transition[]{
                new Transition("A", "S", "b"),
                new Transition("A", "B", "a"),
                new Transition("B", "A", "b"),
                new Transition("B", "X", "a"),
                new Transition("S", "A", "a"),
                new Transition("S", "B", "b"),

        }));
    }

    @Test
    void getGrammarType(){
        Grammar grammar = Grammar.baseGrammar();
        Assertions.assertEquals(grammar.getGrammarType(), "Regular (Type 3)");
    }

    @Test
    void convertToChomskyNormalForm(){
        Grammar grammar = new Grammar(
                "S",
                new HashSet<>(Set.of("a", "b")),
                new HashSet<>(Set.of("S", "A", "B", "C", "E")),
                new HashMap<>() {{ put("S", new ArrayList<>(List.of("aC", "bB")));
                    put("A", new ArrayList<>(List.of("bS", "aB")));
                    put("B", new ArrayList<>(List.of("a", "bC")));
                    put("C", new ArrayList<>(List.of("")));
                    put("E", new ArrayList<>(List.of("aA", "aB")));}}
        );

        grammar.convertToChomskyNormalForm();
        Assertions.assertArrayEquals(grammar.getNonTerminals().toArray(), new String[]{"A", "B", "S", "Y", "Z"});
        Assertions.assertArrayEquals(grammar.getTerminals().toArray(), new String[]{"a", "b"});
        Assertions.assertEquals(grammar.getStartSymbol(), "S");
        Assertions.assertEquals(grammar.getProductionRules(), new HashMap<>() {{
            put("A", new ArrayList<>(List.of("ZS", "YB")));
            put("B", new ArrayList<>(List.of("a", "b")));
            put("S", new ArrayList<>(List.of("ZB", "a")));
            put("Y", new ArrayList<>(List.of("a")));
            put("Z", new ArrayList<>(List.of("b")));
                }}
        );
    }
}
