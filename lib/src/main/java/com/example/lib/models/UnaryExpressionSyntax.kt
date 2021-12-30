package com.example.lib.models

internal class UnaryExpressionSyntax(
    val operatorToken: SyntaxToken,
    val right: NumberExpressionSyntax
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.UnaryExpression
}