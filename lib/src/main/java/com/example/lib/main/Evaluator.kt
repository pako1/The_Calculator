package com.example.lib.main

import com.example.lib.models.SyntaxKind
import com.example.lib.models.expressions.*
import kotlin.math.sign

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
            return (node.numberToken.value as Int).toString()
        }

        if (node is FactorialExpressionSyntax) {
            val left = evaluateExpression(node.leftNumber)
            if (hasDecimalPart(left)) {
                return ""
            }
            var leftAsInt = left.toDouble().toInt()
            var isNegative = false
            if (leftAsInt.sign == -1) {
                leftAsInt *= -1
                isNegative = true
            }

            leftAsInt = if (leftAsInt == 0 || leftAsInt == 1)
                1
            else {
                var factorial = 1
                for (i in 1..leftAsInt) {
                    factorial *= i
                }
                if (isNegative) {
                    factorial *= -1
                }
                factorial
            }
            return leftAsInt.toString()

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
            return (evaluateExpression(node.leftNumber)
                    + node.dotToken.text
                    + evaluateExpression(node.rightNumber)).toDouble().toString()
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