package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal open class DecimalNumberExpressionSyntax(
    val leftNumber: NumberExpressionSyntax,
    val dotToken: SyntaxToken,
    val rightNumber: NumberExpressionSyntax
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.DecimalExpression
}