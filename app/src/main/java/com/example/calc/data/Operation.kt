package com.example.calc.data

enum class Operation(var operationSymbol: Char) {
    PLUS('+'),
    MINUS('-'),
    DIVISION('/'),
    MULTIPLICATION('×'),
    PERCENTAGE('%'),
    INVALID(' '),
    INCOMPLETE(' '),
    DONATING_SIGN(' ')
}