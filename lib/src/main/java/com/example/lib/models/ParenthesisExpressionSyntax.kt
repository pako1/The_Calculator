package com.example.lib.models

internal data class ParenthesisExpressionSyntax(
    var openParenthesisToken: SyntaxToken,
    var expressionSyntax: ExpressionSyntax,
    var closeParenthesisExpressionSyntax: SyntaxToken
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.ParenthesizedExpression
}