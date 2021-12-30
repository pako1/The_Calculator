package com.example.lib.models

import com.example.lib.models.expressions.ExpressionSyntax

 data class SyntaxTree internal constructor(
    val root: ExpressionSyntax,
    val EOFToken: SyntaxToken,
    val diagnostics: MutableList<String>
)