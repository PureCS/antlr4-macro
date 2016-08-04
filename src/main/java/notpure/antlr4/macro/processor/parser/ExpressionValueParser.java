package notpure.antlr4.macro.processor.parser;

import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.exprval.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A static utility class to parse {@link ExpressionValue} from {@link TokenParserIterator}.
 */
public final class ExpressionValueParser {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionValueParser.class);
    private static final List<ExpressionParser> parsers = new ArrayList<>();

    static {
        initParsers();
    }

    /* Prevent instantiation */
    private ExpressionValueParser() { }

    public static List<ExpressionValue> parse(TokenParserIterator it) {
        List<ExpressionValue> values = new ArrayList<>();
        boolean afterRedirectSymbol = false;

        while (it.hasNext()) {
            // Check if end of statement
            if (it.peek().nameEquals(TokenDefinition.SEMICOLON))
                return values;

            // Parse regularly
            ExpressionParser parser = getParser(it);

            if (parser != null) {
                ExpressionValue expr;

                try {
                    if (parser instanceof RuleReferenceParser) {
                        expr = ((RuleReferenceParser)parser).parse(it, afterRedirectSymbol);
                    } else {
                        expr = (ExpressionValue)parser.parse(it);
                    }

                    values.add(expr);
                    LOGGER.info("Parsed ExpressionValue: {}", expr);

                    if (expr.getType() == ExpressionValueType.REDIRECT)
                        afterRedirectSymbol = true;
                } catch (ParserException e) {
                    // TODO throw ex
                }
            } else {
                it.next(); // consume unhandled token
            }
        }
        return null; // TODO throw ex
    }

    private static void initParsers() {
        parsers.add(new AlternatorParser());
        parsers.add(new RedirectParser());
        parsers.add(new StringParser());
        parsers.add(new RuleReferenceParser());
        parsers.add(new RegexGroupParser());
    }

    private static ExpressionParser getParser(TokenParserIterator it) {
        for (ExpressionParser parser : parsers) {
            if (parser.validate(it)) {
                return parser;
            }
        }
        return null;
    }
}
