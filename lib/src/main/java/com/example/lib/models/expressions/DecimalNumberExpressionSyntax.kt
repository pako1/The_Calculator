package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal open class DecimalNumberExpressionSyntax(
    val leftNumber: ExpressionSyntax,
    val dotToken: SyntaxToken,
    val rightNumber: ExpressionSyntax
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.DecimalExpression
}