package com.example.lib.models

data class SyntaxTree(
    val root: ExpressionSyntax,
    val EOFToken: SyntaxToken,
    val diagnostics: MutableList<String>
)