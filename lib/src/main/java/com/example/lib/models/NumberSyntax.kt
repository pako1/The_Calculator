package com.example.lib.models

internal open class NumberExpressionSyntax(var numberToken: SyntaxToken) : ExpressionSyntax(){
    override var syntaxKind: SyntaxKind= SyntaxKind.NumberExpression
}