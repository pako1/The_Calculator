package com.example.lib.models

open class NumberExpressionSyntax(var numberToken: SyntaxToken) : ExpressionSyntax(){
    override var syntaxKind: SyntaxKind= SyntaxKind.NumberExpression
}