package unit_tests;

import automaton.FiniteAutomaton;
import grammar.Grammar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class FiniteAutomatonTests {

    @Test
    void isWordValid(){
        Grammar gr = Grammar.baseGrammar();
        FiniteAutomaton fa = gr.toFiniteAutomaton();

        Assertions.assertTrue(fa.wordIsValid("aaa"));
        Assertions.assertTrue(fa.wordIsValid("ba"));
        Assertions.assertTrue(fa.wordIsValid("bbaa"));
        Assertions.assertTrue(fa.wordIsValid("aabaa"));
        Assertions.assertTrue(fa.wordIsValid("aaa"));
        Assertions.assertFalse(fa.wordIsValid("ac"));
        Assertions.assertFalse(fa.wordIsValid(""));
        Assertions.assertFalse(fa.wordIsValid("abb"));

    }

    @Test
    void toGrammar(){
        FiniteAutomaton fa = FiniteAutomaton.baseFiniteAutomaton();
        Grammar grammar = fa.toGrammar();
        Assertions.assertArrayEquals(grammar.getTerminals().toArray(), new String[]{"a", "b"});
        Assertions.assertArrayEquals(grammar.getNonTerminals().toArray(), new String[]{"q1", "q2", "q0"});
        Assertions.assertEquals(grammar.getStartSymbol(), "q0");
        Assertions.assertEquals(grammar.getProductionRules(), new HashMap<>() {{
            put("q0", new ArrayList<>(List.of("aq1", "bq2", "aq2")));
            put("q1", new ArrayList<>(List.of("bq2", "aq1")));
            put("q2", new ArrayList<>(List.of("ε")));}});
    }

    @Test
    void toDFA(){
        FiniteAutomaton fa = FiniteAutomaton.baseFiniteAutomaton();
        FiniteAutomaton dfa = fa.convertToDFA();
        Assertions.assertEquals(dfa.getAlphabet(), new HashSet(Set.of("a", "b")));
        Assertions.assertEquals(dfa.getStartState(), "{q0}");
    }

    @Test
    void isDeterministic(){
        FiniteAutomaton fa = FiniteAutomaton.baseFiniteAutomaton();
        Assertions.assertFalse(fa.isDeterministic());
    }
}
