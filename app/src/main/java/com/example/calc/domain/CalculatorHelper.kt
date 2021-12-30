package com.example.calc.domain

import com.example.calc.data.Operation
import com.example.lib.main.Evaluator
import com.example.lib.main.Parser
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Inject

interface CalculatorHelper {
    fun setTextInput(input: String)
    fun setTextResult(result: String)
    fun getTextInput(): String
    fun getTextResult(): String
    fun appendTextInputAtPosition(newChar: Char, position: Int)
    fun resetTextInput()
    fun resetResult()
    fun doesResultExist(): Boolean
    fun performEquation(): String
    fun containsAlreadyOperatorAtPosition(position: Int): Boolean
    fun modifyDisplayedResult(number: Double): String
    fun isRootingPossible(): Boolean
    fun isApplyingPercentagePossible(textInput: String): Boolean
    fun isPlainNumber(textInput: String): Boolean
    fun reverseNumber(): String
    fun addParenthesis(cursorPosition: Int): String
    fun isNegative(textInput: String): Boolean
    fun isEquationCorrect(equation: String): Boolean
    fun hasOperatorAtEnd(): Boolean
}

//By using the @inject constructor we tell hilt how he can provide us an Calculator instance
class Calculator @Inject constructor() : CalculatorHelper {

    private var textInput: String = ""
    private var textResult: String = ""

    override fun setTextInput(input: String) {
        textInput = input
    }

    override fun setTextResult(result: String) {
        textResult = result
    }

    override fun getTextInput(): String {
        return textInput
    }

    override fun getTextResult(): String {
        return textResult
    }

    override fun appendTextInputAtPosition(newChar: Char, position: Int) {
        val newEquationBuilder = StringBuilder(textInput)
        newEquationBuilder.insert(position, newChar)
        textInput = newEquationBuilder.toString()
    }

    override fun resetTextInput() {
        textInput = ""
    }

    override fun resetResult() {
        textResult = ""
    }

    override fun doesResultExist(): Boolean = textResult.isNotEmpty()

    override fun performEquation(): String {

        val parser = Parser()
        val equation = Operation.replaceUISymbolToMathSymbol(textInput)
        parser.setTextInput(equation)
        parser.startProcess()
        val syntaxTree = parser.parse()
        if (syntaxTree.diagnostics.any()) {
            syntaxTree.diagnostics.forEach {
                println(it)
            }
            return Operation.INVALID.name
        }
        val evaluator = Evaluator(syntaxTree.root)
        val result = evaluator.evaluate()
        return result ?: Operation.INVALID.name
    }


    override fun modifyDisplayedResult(number: Double): String {
        val df = DecimalFormat(DECIMAL_FORMAT, DecimalFormatSymbols.getInstance(Locale.US))
        return df.format(number)
    }

    override fun isRootingPossible(): Boolean {
        val operator = findOperationOut()
        return (operator != Operation.INVALID
                && !isEquationNotComplete(textInput, operator.operatorSymbol)
                && !isNegative(textInput)) || operator == Operation.INVALID
    }

    override fun isApplyingPercentagePossible(textInput: String): Boolean {
        val operator = findOperationOut()
        return (operator != Operation.INVALID
                && !isEquationNotComplete(textInput, operator.operatorSymbol))
                || operator == Operation.INVALID
    }

    override fun isPlainNumber(textInput: String): Boolean =
        NUMBER_VALIDATOR.toRegex().matches(textInput)

    override fun reverseNumber(): String {
        return reversalPattern(textInput)
    }

    override fun hasOperatorAtEnd(): Boolean {
        var containsOperatorAtEnd = false
        Operation.values()
            .forEach { if (it.operatorSymbol == textInput.last()) containsOperatorAtEnd = true }
        return containsOperatorAtEnd
    }

    override fun addParenthesis(cursorPosition: Int): String {
        val operator = findOperationOut()
        val operatorPosition = textInput.indexOf(operator.operatorSymbol)

        val numberAfterOperator = textInput.substringAfter(textInput[operatorPosition])
        val numberBeforeOperator = textInput.substringBefore(textInput[operatorPosition])
        return if (cursorPosition > operatorPosition) {
            numberBeforeOperator + operator.operatorSymbol + applyReversalPattern(
                numberAfterOperator
            )
        } else {
            applyReversalPattern(numberBeforeOperator) + operator.operatorSymbol + numberAfterOperator
        }
    }

    private fun applyReversalPattern(numberToReverse: String): String {
        return if (numberToReverse.contains(OPENING_PARENTHESIS) ||
            numberToReverse.contains(CLOSING_PARENTHESIS)
        ) {
            val resultWithoutParenthesis = numberToReverse.removeParenthesisIfAvailable()
            reversalPattern(resultWithoutParenthesis)
        } else {
            val reversedNumber = reversalPattern(numberToReverse)
            "($reversedNumber)"
        }
    }

    private fun String.removeParenthesisIfAvailable(): String {
        return removePrefix(OPENING_PARENTHESIS).removeSuffix(CLOSING_PARENTHESIS)
    }

    private fun reversalPattern(numberToReverse: String): String {
        return if (hasDecimalPart(numberToReverse)) {
            (numberToReverse.toDouble() * REVERSE_ATTRIBUTE).toString()
        } else {
            (numberToReverse.toInt() * REVERSE_ATTRIBUTE).toString()
        }
    }

    override fun containsAlreadyOperatorAtPosition(position: Int): Boolean {
        var containsAlreadyOperator = false
        Operation.values().forEach {
            if (containsAlreadyOperator) {
                return@forEach
            }
            containsAlreadyOperator = textInput[position - 1] == it.operatorSymbol
        }
        return containsAlreadyOperator
    }

    override fun isEquationCorrect(equation: String): Boolean =
        MATHEMATICAL_EXPRESSION_VALIDATOR.toRegex().matches(equation)


    private fun isEquationNotComplete(mathematicalOperation: String, operator: Char): Boolean {
        return mathematicalOperation.substringAfter(operator).isEmpty()
    }

    override fun isNegative(textInput: String): Boolean {
        return try {
            textInput.toDouble() < 0.0
        } catch (e: Exception) {
            false
        }
    }

    private fun findOperationOut(): Operation {
        return when {
            textInput.contains("×") -> Operation.MULTIPLICATION
            textInput.contains("/") -> Operation.DIVISION
            textInput.contains("+") -> Operation.ADDITION
            textInput.contains("-") -> Operation.SUBTRACTION
            else -> Operation.INVALID
        }
    }

    private fun hasDecimalPart(number: String): Boolean {
        val intPart = number.toInt()
        val decimalPart = number.toDouble() - intPart
        return decimalPart > 0.0000
    }

    companion object {
        private const val DECIMAL_FORMAT = "#.################"
        private const val MATHEMATICAL_EXPRESSION_VALIDATOR =
            "^(([(])?[-+/×]*\\d+(\\.\\d+)?([)]?))*([-+/×])(([(])?[-+/×]*\\d+(\\.\\d+)?([)]?))*\$"
        private const val NUMBER_VALIDATOR = "\\d+"
        private const val REVERSE_ATTRIBUTE = -1
        private const val OPENING_PARENTHESIS = "("
        private const val CLOSING_PARENTHESIS = ")"
    }

}