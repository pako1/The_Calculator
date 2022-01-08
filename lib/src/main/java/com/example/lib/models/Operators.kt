package com.example.lib.models

internal enum class Operators(val operatorSymbol: String) {
    ADDITION("+"),
    SUBTRACTION("-"),
    DIVISION("/"),
    MULTIPLICATION("*"),
    OPEN_PARENTHESIS("("),
    CLOSING_PARENTHESIS(")"),
    DOT("."),
    FACTORIAL("!"),
    EOF("\\u0000")
}