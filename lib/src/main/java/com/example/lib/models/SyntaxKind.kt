package com.example.lib.models

enum class SyntaxKind {
    NumberToken,
    DotToken,
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken,
    OpenParenthesisToken,
    CloseParenthesisToken,
    UnrecognizedToken,
    EOFToken,
    NumberExpression,
    DecimalExpression,
    BinaryExpression,
    ParenthesizedExpression,
    UnaryExpression
}