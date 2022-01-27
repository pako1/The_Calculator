package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind

internal class FactorialExpressionSyntax(
    val leftNumber: ExpressionSyntax,
    ) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.FactorialExpression
}