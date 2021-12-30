package com.example.lib.main

import com.example.lib.models.*

class Evaluator(val root: ExpressionSyntax) {

    fun evaluate(): Int {
        return evaluateExpression(root)
    }

    private fun evaluateExpression(node: ExpressionSyntax): Int {
        if (node is NumberExpressionSyntax) {
            return node.numberToken.value as Int
        }

        if (node is UnaryExpressionSyntax) {
            val right = evaluateExpression(node.right)
            return when (node.operatorToken.syntaxKind) {
                SyntaxKind.PlusToken -> right
                SyntaxKind.MinusToken -> -right
                else -> throw Exception("ERROR: unexpected unary operator ${node.operatorToken.syntaxKind}")
            }
        }

        if (node is BinaryExpressionSyntax) {
            val left = evaluateExpression(node.left)
            val right = evaluateExpression(node.right)
            return when (node.operatorToken.syntaxKind) {
                SyntaxKind.PlusToken -> left + right
                SyntaxKind.MinusToken -> left - right
                SyntaxKind.StarToken -> left * right
                SyntaxKind.SlashToken -> left / right
                else -> throw Exception("ERROR: unexpected binary operator")
            }
        }

        return if (node is ParenthesisExpressionSyntax) {
            evaluateExpression(node.expressionSyntax)
        } else {
            throw Exception("ERROR: unexpected binary operator")
        }
    }
}