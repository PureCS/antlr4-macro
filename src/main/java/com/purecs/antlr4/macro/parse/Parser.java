package com.purecs.antlr4.macro.parse;

import com.purecs.antlr4.macro.lang.MacroRule;
import com.purecs.antlr4.macro.lang.MacroUse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Parser {

    void parse(Path file) throws IOException;

    List<MacroRule> getMacros();

    List<MacroUse> getMacroUses();
}
