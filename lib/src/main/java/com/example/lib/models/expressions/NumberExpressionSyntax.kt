package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal class NumberExpressionSyntax(var numberToken: SyntaxToken) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.NumberExpression
}