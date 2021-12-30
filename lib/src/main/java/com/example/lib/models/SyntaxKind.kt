package com.example.lib.models

enum class SyntaxKind {
    NumberToken,
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken,
    OpenParenthesisToken,
    CloseParenthesisToken,
    UnrecognizedToken,
    EOFToken,
    NumberExpression,
    BinaryExpression,
    ParenthesizedExpression,
    UnaryExpression
}