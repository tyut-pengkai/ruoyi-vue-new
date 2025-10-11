package com.common.core;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrPool;

public interface HybasePool {
    String AND = "AND";
    String OR = "OR";
    String NOT = "NOT";
    String TO = "TO";
    String ASTERISK = "*";
    String PARENTHESIS_START = "(";
    String PARENTHESIS_END = ")";
    String CARET = "^";
    String SPACE = String.valueOf(CharPool.SPACE);
    String DOUBLE_QUOTES = String.valueOf(CharPool.DOUBLE_QUOTES);
    String COLON = String.valueOf(CharPool.COLON);
    String BRACKET_START = StrPool.BRACKET_START;
    String BRACKET_END = StrPool.BRACKET_END;
    String DELIM_START = StrPool.DELIM_START;
    String DELIM_END = StrPool.DELIM_END;
    String TILDE = "~";
    String UUID = "#UUID";
    String LIKE = "#LIKE";
    String INCLUDE = "#INCLUDE";
}
