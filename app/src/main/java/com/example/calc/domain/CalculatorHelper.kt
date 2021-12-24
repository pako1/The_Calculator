package com.example.calc.domain

import com.example.calc.data.Operation
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
    fun isRootingPossible(textInput: String): Boolean
    fun isApplyingPercentagePossible(textInput: String): Boolean
    fun isPlainNumber(textInput: String): Boolean
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
        return if (isEquationCorrect(textInput)) {
            when (findOperationOut()) {
                Operation.PLUS -> {
                    if (denotesPlusOrMinusSign(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.DONATING_SIGN.name
                    }
                    if (isEquationNotComplete(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.INCOMPLETE.name
                    }
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.PLUS.operationSymbol
                    )
                    textResult = modifyDisplayedResult(firstNumber + secondNumber)
                    return textResult
                }
                Operation.MINUS -> {
                    if (denotesPlusOrMinusSign(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.DONATING_SIGN.name
                    }
                    if (isEquationNotComplete(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.INCOMPLETE.name
                    }
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.MINUS.operationSymbol
                    )
                    textResult = modifyDisplayedResult(firstNumber - secondNumber)
                    return textResult
                }
                Operation.DIVISION -> {
                    if (denotesPlusOrMinusSign(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.DONATING_SIGN.name
                    }
                    if (isEquationNotComplete(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.INCOMPLETE.name
                    }
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.DIVISION.operationSymbol
                    )

                    textResult = modifyDisplayedResult(firstNumber / secondNumber)
                    return textResult
                }
                Operation.MULTIPLICATION -> {
                    if (denotesPlusOrMinusSign(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.DONATING_SIGN.name
                    }
                    if (isEquationNotComplete(textInput, Operation.PLUS.operationSymbol)) {
                        return Operation.INCOMPLETE.name
                    }
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.MULTIPLICATION.operationSymbol
                    )
                    textResult = modifyDisplayedResult(firstNumber * secondNumber)
                    return textResult
                }
                else -> Operation.INVALID.name
            }
        } else {
            Operation.INVALID.name
        }
    }

    private fun getNumbersFromOperation(
        mathematicalOperation: String,
        operation: Char
    ): Pair<Double, Double> {
        val firstNumber = mathematicalOperation.substringBefore(operation).toDouble()
        val secondNumber = mathematicalOperation.substringAfter(operation).toDouble()
        return firstNumber to secondNumber
    }

    override fun modifyDisplayedResult(number: Double): String {
        val df = DecimalFormat(DECIMAL_FORMAT, DecimalFormatSymbols.getInstance(Locale.US))
        return df.format(number)
    }

    override fun isRootingPossible(textInput: String): Boolean {
        val operator = findOperationOut()
        return (operator != Operation.INVALID
                && !isEquationNotComplete(textInput, operator.operationSymbol))
                || operator == Operation.INVALID
    }

    override fun isApplyingPercentagePossible(textInput: String): Boolean {
        val operator = findOperationOut()
        return (operator != Operation.INVALID
                && !isEquationNotComplete(textInput, operator.operationSymbol))
                || operator == Operation.INVALID
    }

    override fun isPlainNumber(textInput: String): Boolean {
        return NUMBER_VALIDATOR.toRegex().matches(textInput)
    }

    override fun containsAlreadyOperatorAtPosition(position: Int): Boolean {
        var containsAlreadyOperator = false
        Operation.values().forEach {
            if (containsAlreadyOperator) {
                return@forEach
            }
            containsAlreadyOperator = textInput[position - 1] == it.operationSymbol
        }
        return containsAlreadyOperator
    }

    private fun isEquationCorrect(equation: String): Boolean =
        MATHEMATICAL_EXPRESSION_VALIDATOR.toRegex().matches(equation)


    private fun denotesPlusOrMinusSign(mathematicalOperation: String, operation: Char): Boolean {
        var numberOfOperators = 0
        mathematicalOperation.forEach {
            if (it == Operation.PLUS.operationSymbol || it == Operation.MINUS.operationSymbol) numberOfOperators += 1
        }
        return if (numberOfOperators > 1) {
            false
        } else {
            mathematicalOperation.substringBefore(operation).isEmpty()
        }
    }

    private fun isEquationNotComplete(mathematicalOperation: String, operation: Char): Boolean {
        return mathematicalOperation.substringAfter(operation).isEmpty()
    }

    private fun findOperationOut(): Operation {
        return when {
            textInput.contains("+") -> Operation.PLUS
            textInput.contains("-") -> Operation.MINUS
            textInput.contains("/") -> Operation.DIVISION
            textInput.contains("×") -> Operation.MULTIPLICATION
            else -> Operation.INVALID
        }
    }

    companion object {
        private const val DECIMAL_FORMAT = "#.################"
        private const val MATHEMATICAL_EXPRESSION_VALIDATOR = "^([-+/×]*\\d+(\\.\\d+)?)*"
        private const val NUMBER_VALIDATOR = "\\d+"
    }

}