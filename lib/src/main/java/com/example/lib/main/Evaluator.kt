package com.example.lib.main

import com.example.lib.models.SyntaxKind
import com.example.lib.models.expressions.*

class Evaluator(val root: ExpressionSyntax) {

    fun evaluate(): String? {
        return try {
            val result = evaluateExpression(root)
            return if (!hasDecimalPart(result)) {
                result.toDouble().toInt().toString()
            } else {
                result
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun evaluateExpression(node: ExpressionSyntax): String {
        if (node is NumberExpressionSyntax) {
            return (node.numberToken.value as Int).toDouble().toString()
        }

        if (node is UnaryExpressionSyntax) {
            val right = evaluateExpression(node.right)
            return when (node.operatorToken.syntaxKind) {
                SyntaxKind.PlusToken -> right.toDouble().toString()
                SyntaxKind.MinusToken -> (-(right.toDouble())).toString()
                else -> throw Exception("ERROR: unexpected unary operator ${node.operatorToken.syntaxKind}")
            }
        }

        if (node is DecimalNumberExpressionSyntax) {
            val dotPosition = node.dotToken.value as Int
            val number = evaluateExpression(node.leftNumber) + evaluateExpression(node.rightNumber)
            return number.substring(0, dotPosition) + "." + number.substring(
                dotPosition,
                number.length
            )
        }

        if (node is BinaryExpressionSyntax) {
            val left = evaluateExpression(node.left)
            val right = evaluateExpression(node.right)
            return when (node.operatorToken.syntaxKind) {
                SyntaxKind.PlusToken -> (left.toDouble() + right.toDouble()).toString()
                SyntaxKind.MinusToken -> (left.toDouble() - right.toDouble()).toString()
                SyntaxKind.StarToken -> (left.toDouble() * right.toDouble()).toString()
                SyntaxKind.SlashToken -> (left.toDouble() / right.toDouble()).toString()
                else -> throw Exception("ERROR: unexpected binary operator")
            }
        }

        return if (node is ParenthesisExpressionSyntax) {
            evaluateExpression(node.expressionSyntax)
        } else {
            throw Exception("ERROR: unexpected binary operator")
        }
    }

    private fun hasDecimalPart(number: String): Boolean {
        val intPart = number.toDouble().toInt()
        val decimalPart = number.toDouble() - intPart
        return decimalPart > 0.0000
    }
}