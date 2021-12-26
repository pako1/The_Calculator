package com.example.calc.data

enum class Operation(var operatorSymbol: Char) {
    ADDITION('+'),
    SUBTRACTION('-'),
    DIVISION('/'),
    MULTIPLICATION('×'),
    PERCENTAGE('%'),
    INVALID(' '),
    INCOMPLETE(' '),
    SIGNED_NUMBER_REPRESENTATION(' ')
}