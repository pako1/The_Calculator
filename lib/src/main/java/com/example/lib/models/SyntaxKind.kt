package com.example.lib.models

enum class SyntaxKind {
    NumberToken,
    DotToken,
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken,
    FactorialToken,
    OpenParenthesisToken,
    CloseParenthesisToken,
    UnrecognizedToken,
    EOFToken,
    NumberExpression,
    DecimalExpression,
    BinaryExpression,
    ParenthesizedExpression,
    FactorialExpression,
    UnaryExpression
}