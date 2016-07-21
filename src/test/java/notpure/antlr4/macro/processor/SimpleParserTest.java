package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.impl.SimpleParser;
import notpure.antlr4.macro.processor.model.statement.GenericStatement;
import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.processor.TokenHelper.*;
import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link SimpleParser}.
 */
public final class SimpleParserTest {

    @Test
    public void parserTestOfMacroRuleDefinitions() {
        // Create array
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(TokenDefinition.HASH));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getTokens(TokenDefinition.DIGIT, "290"));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "woRld"));
        tokens.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SINGLE_QUOTE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getLiteralTokens(TokenDefinition.SINGLE_QUOTE, TokenDefinition.SEMICOLON));

        // Try parse and compare
        List<Statement> output = new SimpleParser().parse(tokens).getStatements();
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class.getSimpleName(), output.get(0).getName());
        assertEquals("HELLO290woRld:'HELLO'", output.get(0).getValue());

        // Create array
        tokens = new ArrayList<>();
        tokens.add(new Token(TokenDefinition.HASH));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getTokens(TokenDefinition.DIGIT, "290"));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "woRld"));
        tokens.addAll(getLiteralTokens(TokenDefinition.SPACE));
        tokens.addAll(getLiteralTokens(TokenDefinition.SPACE));
        tokens.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SINGLE_QUOTE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getLiteralTokens(TokenDefinition.SINGLE_QUOTE, TokenDefinition.SEMICOLON));

        // Try parse and compare
        output = new SimpleParser().parse(tokens).getStatements();
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class.getSimpleName(), output.get(0).getName());
        assertEquals("HELLO290woRld:'HELLO'", output.get(0).getValue());

        // Create array
        tokens = new ArrayList<>();
        tokens.add(new Token(TokenDefinition.HASH));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getTokens(TokenDefinition.DIGIT, "290"));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "woRld"));
        tokens.add(new Token(TokenDefinition.SPACE));
        tokens.add(new Token(TokenDefinition.SPACE));
        tokens.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SINGLE_QUOTE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getLiteralTokens(TokenDefinition.CARRIAGE_RETURN, TokenDefinition.NEW_LINE));
        tokens.add(new Token(TokenDefinition.VERTICAL_LINE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "WORLD"));
        tokens.addAll(getLiteralTokens(TokenDefinition.SINGLE_QUOTE, TokenDefinition.SEMICOLON));

        // Try parse and compare
        output = new SimpleParser().parse(tokens).getStatements();
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class.getSimpleName(), output.get(0).getName());
        assertEquals("HELLO290woRld:'HELLO|WORLD'", output.get(0).getValue());
    }

    @Test
    public void parserTestOfParserRuleDefinitions() {
        // TODO add
    }

    @Test
    public void parserTestOfLexerRuleDefinitions() {
        // TODO add
    }

    @Test
    public void parserTestOfFileHeaderDefinitions() {
        // TODO add
    }
}