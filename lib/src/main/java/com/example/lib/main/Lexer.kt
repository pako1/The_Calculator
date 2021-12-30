package com.example.lib.main

import com.example.lib.models.Operators
import com.example.lib.models.SyntaxKind
import com.example.lib.models.SyntaxToken

internal class Lexer {

    private var position: Int = 0
    private var readOnly: String = ""
    private var previousStartPosition = 0
    var diagnostics = mutableListOf<String>()

    fun setText(textInput: String) {
        readOnly = textInput
    }

    fun nextToken(): SyntaxToken {

        if (position >= readOnly.length) {
            return SyntaxToken(SyntaxKind.EOFToken, position, Operators.EOF.operatorSymbol, null)
        }

        if (getCurrentChar().isDigit()) {
            val startPosition = if (position >= readOnly.length) {
                previousStartPosition
            } else {
                previousStartPosition = position
                position
            }

            while (getCurrentChar().isDigit()) {
                nextPosition()
            }
            val number = readOnly.substring(startPosition, position)

            val value = try {
                number.toInt()
            } catch (e: Exception) {
                diagnostics.add("$number can't be represented as an int!")
            }
            return SyntaxToken(SyntaxKind.NumberToken, startPosition, number, value)
        }

        when (getCurrentChar()) {
            '+' -> {
                return SyntaxToken(
                    SyntaxKind.PlusToken,
                    nextPosition(),
                    Operators.ADDITION.operatorSymbol,
                    null
                )
            }
            '-' -> {
                return SyntaxToken(
                    SyntaxKind.MinusToken,
                    nextPosition(),
                    Operators.SUBTRACTION.operatorSymbol,
                    null
                )
            }
            '*' -> {
                return SyntaxToken(
                    SyntaxKind.StarToken,
                    nextPosition(),
                    Operators.MULTIPLICATION.operatorSymbol,
                    null
                )

            }
            '/' -> {
                return SyntaxToken(
                    SyntaxKind.SlashToken,
                    nextPosition(),
                    Operators.DIVISION.operatorSymbol,
                    null
                )
            }
            '(' -> {
                return SyntaxToken(
                    SyntaxKind.OpenParenthesisToken,
                    nextPosition(),
                    Operators.OPEN_PARENTHESIS.operatorSymbol,
                    null
                )
            }
            ')' -> {
                return SyntaxToken(
                    SyntaxKind.CloseParenthesisToken,
                    nextPosition(),
                    Operators.CLOSING_PARENTHESIS.operatorSymbol,
                    null
                )
            }
            '.' -> {
                return SyntaxToken(
                    SyntaxKind.DotToken,
                    nextPosition(),
                    Operators.DOT.operatorSymbol,
                    position
                )
            }
            else -> {
                diagnostics.add("Bad character input ${getCurrentChar()}")
                return SyntaxToken(
                    SyntaxKind.UnrecognizedToken,
                    nextPosition(),
                    readOnly.substring(position - 1, 1),
                    null
                )
            }
        }
    }

    private fun getCurrentChar(): Char {
        return if (position >= readOnly.length) {
            '\u0000'
        } else {
            readOnly[position]
        }
    }

    private fun nextPosition() = position++

}