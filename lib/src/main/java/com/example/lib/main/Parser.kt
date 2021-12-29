package com.example.lib.main

import com.example.lib.models.*

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

    private fun provideCurrentToken() = peek(0)

    private fun peek(offset: Int): SyntaxToken {
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

    private fun match(syntaxKind: SyntaxKind): SyntaxToken {
        return if (provideCurrentToken().syntaxKind == syntaxKind) {
            nextToken()
        } else {
            diagnostics.add("ERROR: unexpected token <{${provideCurrentToken().syntaxKind}}>,expected <{${syntaxKind}}>")
            SyntaxToken(syntaxKind, provideCurrentToken().position, null, null)
        }
    }

    fun parse(): SyntaxTree {
        val expression = parseTerm()
        val eofToken = match(SyntaxKind.EOFToken)
        return SyntaxTree(diagnostics = diagnostics, root = expression, EOFToken = eofToken)
    }

    private fun parseTerm(): ExpressionSyntax {
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
            val expression = parseTerm()
            val right = match(SyntaxKind.CloseParenthesisToken)
            return ParenthesisExpressionSyntax(left, expression, right)
        }
        val numberToken = match(SyntaxKind.NumberToken)
        return NumberExpressionSyntax(numberToken)
    }

}