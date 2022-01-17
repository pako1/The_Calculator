package com.example.calc.data

enum class Operation(var operatorSymbol: Char) {
    ADDITION('+'),
    SUBTRACTION('-'),
    DIVISION('/'),
    MULTIPLICATION('×'),
    PERCENTAGE('%'),
    INVALID(' '),
    INCOMPLETE(' '),
    SIGNED_NUMBER_REPRESENTATION(' '),
    FACTORIAL('!'),
    OPEN_PARENTHESIS('('),
    CLOSE_PARENTHESIS(')');

    companion object {
        fun replaceUISymbolToMathSymbol(equation: String): String {
            return equation.replace(oldChar = MULTIPLICATION.operatorSymbol, newChar = '*')
        }
    }
}