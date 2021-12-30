package com.example.lib.models.expressions

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal data class ParenthesisExpressionSyntax(
    var openParenthesisToken: SyntaxToken,
    var expressionSyntax: ExpressionSyntax,
    var closeParenthesisExpressionSyntax: SyntaxToken
) : ExpressionSyntax() {
    override var syntaxKind: SyntaxKind = SyntaxKind.ParenthesizedExpression
}