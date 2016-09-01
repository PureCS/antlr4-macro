package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.processor.macro.MacroExpressionProcessor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.model.lang.ExpressionValueType.RULE_REFERENCE;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.STRING;
import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link notpure.antlr4.macro.processor.macro.MacroExpressionProcessor}.
 */
public final class MacroExpressionProcessorTest {

    @Test
    public void testValidMacroRuleApplication() throws Exception {
        Expression expr1 = new Expression(ExpressionType.MACRO_RULE, "FNAME", new ExpressionValue(STRING, "John"));
        Expression expr2 = new Expression(ExpressionType.LEXER_RULE, "NAME", new ExpressionValue(RULE_REFERENCE, "#FNAME"));
        Expression expectedOutputExpr = new Expression(ExpressionType.LEXER_RULE, "NAME", new ExpressionValue(STRING, "John"));

        List<Expression> macros = new ArrayList<>();
        macros.add(expr1);

        List<Expression> expressions = new ArrayList<>();
        expressions.add(expr2);

        List<Expression> outputExpressions = MacroExpressionProcessor.process(expressions, macros);
        assertEquals(1, outputExpressions.size());
        assertEquals(expectedOutputExpr, outputExpressions.get(0));
    }

    @Test
    public void testComplexValidMacroRuleApplication() throws Exception {
        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(RULE_REFERENCE, "#FNAME"));
        values.add(new ExpressionValue(RULE_REFERENCE, "#LNAME"));

        Expression expr1 = new Expression(ExpressionType.MACRO_RULE, "FNAME", new ExpressionValue(STRING, "John"));
        Expression expr2 = new Expression(ExpressionType.MACRO_RULE, "LNAME", new ExpressionValue(STRING, "Doe"));
        Expression expr3 = new Expression(ExpressionType.LEXER_RULE, "NAME", values);

        List<ExpressionValue> expectedValues = new ArrayList<>();
        expectedValues.add(new ExpressionValue(STRING, "John"));
        expectedValues.add(new ExpressionValue(STRING, "Doe"));
        Expression expectedOutputExpr = new Expression(ExpressionType.LEXER_RULE, "NAME", expectedValues);

        List<Expression> macros = new ArrayList<>();
        macros.add(expr1);
        macros.add(expr2);
        List<Expression> expressions = new ArrayList<>();
        expressions.add(expr3);

        List<Expression> outputExpressions = MacroExpressionProcessor.process(expressions, macros);
        assertEquals(1, outputExpressions.size());
        assertEquals(expectedOutputExpr, outputExpressions.get(0));
    }

    @Test(expected = Exception.class)
    public void testMissingMacroRuleApplication() throws Exception {
        Expression expr = new Expression(ExpressionType.LEXER_RULE, "NAME", new ExpressionValue(RULE_REFERENCE, "#FNAME"));

        List<Expression> macros = new ArrayList<>();
        List<Expression> expressions = new ArrayList<>();
        expressions.add(expr);

        List<Expression> outputExpressions = MacroExpressionProcessor.process(expressions, macros);
    }
}
