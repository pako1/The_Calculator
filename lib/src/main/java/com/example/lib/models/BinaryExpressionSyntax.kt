package com.example.lib.models

internal class BinaryExpressionSyntax(
    val left: ExpressionSyntax,
    val operatorToken: SyntaxToken,
    val right: ExpressionSyntax
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.BinaryExpression
}