package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal class BinaryExpressionSyntax(
    val left: ExpressionSyntax,
    val operatorToken: SyntaxToken,
    val right: ExpressionSyntax
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.BinaryExpression
}