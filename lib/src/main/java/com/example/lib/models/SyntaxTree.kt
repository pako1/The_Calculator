package com.example.lib.models

internal data class SyntaxTree(
    val root: ExpressionSyntax,
    val EOFToken: SyntaxToken,
    val diagnostics: MutableList<String>
)