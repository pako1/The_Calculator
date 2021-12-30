package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal class UnaryExpressionSyntax(
    val operatorToken: SyntaxToken,
    val right: NumberExpressionSyntax
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.UnaryExpression
}