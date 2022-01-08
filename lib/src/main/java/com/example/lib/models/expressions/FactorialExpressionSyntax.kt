package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal class FactorialExpressionSyntax(
    val leftNumber: ExpressionSyntax,
    val factorialToken: SyntaxToken,
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.FactorialExpression
}