package com.example.lib.main

import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken
import com.example.lib.models.SyntaxTree
import com.example.lib.models.expressions.*

class Parser {

    private var text = ""
    private var syntaxTokens = mutableListOf<SyntaxToken>()
    private var position = 0
    private val diagnostics = mutableListOf<String>()

    private var lexer: Lexer = Lexer()

    fun setTextInput(textInput: String) {
        text = textInput
    }

    fun startProcess() {
        lexer.setText(text)

        var syntaxToken: SyntaxToken
        val tokens = mutableListOf<SyntaxToken>()

        do {
            syntaxToken = lexer.nextToken()

            if (syntaxToken.syntaxKind != SyntaxKind.UnrecognizedToken) {
                tokens.add(syntaxToken)
            }

        } while (syntaxToken.syntaxKind != SyntaxKind.EOFToken)

        syntaxTokens.addAll(tokens)
        diagnostics.addAll(lexer.diagnostics)

    }

    private fun provideCurrentToken(offset: Int = 0): SyntaxToken {
        val index = position + offset
        return if (index >= syntaxTokens.size) {
            syntaxTokens.last()
        } else {
            syntaxTokens[index]
        }
    }

    private fun nextToken(): SyntaxToken {
        val currentToken = provideCurrentToken()
        position++
        return currentToken
    }

    fun parse(): SyntaxTree {
        val expression = parseExpression()
        val eofToken = matchToken(SyntaxKind.EOFToken)
        return SyntaxTree(diagnostics = diagnostics, root = expression, EOFToken = eofToken)
    }

    private fun parseExpression(): ExpressionSyntax {
        var left = parseFactor()
        while (provideCurrentToken().syntaxKind == SyntaxKind.PlusToken
            || provideCurrentToken().syntaxKind == SyntaxKind.MinusToken
        ) {
            val operatorToken = nextToken()
            val right = parseFactor()
            left = BinaryExpressionSyntax(left = left, operatorToken = operatorToken, right = right)
        }
        return left
    }

    private fun parseFactor(): ExpressionSyntax {
        var left = parsePrimaryExpression()
        while (provideCurrentToken().syntaxKind == SyntaxKind.SlashToken
            || provideCurrentToken().syntaxKind == SyntaxKind.StarToken
        ) {
            val operatorToken = nextToken()
            val right = parsePrimaryExpression()
            left = BinaryExpressionSyntax(left = left, operatorToken = operatorToken, right = right)
        }
        return left
    }

    private fun parsePrimaryExpression(): ExpressionSyntax {
        if (provideCurrentToken().syntaxKind == SyntaxKind.OpenParenthesisToken) {
            val left = nextToken()
            val expression = parseExpression()
            val right = matchToken(SyntaxKind.CloseParenthesisToken)
            return ParenthesisExpressionSyntax(left, expression, right)
        }


        return if (provideCurrentToken().syntaxKind == SyntaxKind.PlusToken ||
            provideCurrentToken().syntaxKind == SyntaxKind.MinusToken
        ) {
            val operator = nextToken()
            val right = matchToken(SyntaxKind.NumberToken)
            UnaryExpressionSyntax(operator, NumberExpressionSyntax(right))
        } else {
            val numberToken = matchToken(SyntaxKind.NumberToken)
            NumberExpressionSyntax(numberToken)
        }

    }

    private fun matchToken(syntaxKind: SyntaxKind): SyntaxToken {
        return if (provideCurrentToken().syntaxKind == syntaxKind) {
            nextToken()
        } else {
            diagnostics.add("ERROR: unexpected token <{${provideCurrentToken().syntaxKind}}>,expected <{${syntaxKind}}>")
            SyntaxToken(syntaxKind, provideCurrentToken().position, null, null)
        }
    }
}