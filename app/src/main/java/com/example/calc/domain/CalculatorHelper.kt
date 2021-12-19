package com.example.calc.domain

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

interface CalculatorHelper {
    fun setTextInput(input: String)
    fun setTextResult(result: String)
    fun getTextInput(): String
    fun getTextResult(): String
    fun appendTextInputAtPosition(newChar: String, position: Int)
    fun resetTextInput()
    fun resetResult()
    fun addOperatorAt(operator: String, position: Int)
    fun doesResultExist(): Boolean
    fun performEquation(): String
    fun containsAlreadyOperatorAtPosition(position: Int): Boolean
    fun modifyDisplayedResult(number: Double): String
    fun isRootingPossible(textInput: String): Boolean
    fun isApplyingPercentagePossible(textInput: String): Boolean
}

class Calculator : CalculatorHelper {

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

    override fun appendTextInputAtPosition(newChar: String, position: Int) {
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

    override fun addOperatorAt(operator: String, position: Int) {
        if (doesResultExist()) {
            textInput = textResult
            textInput set operator
            resetResult()
        } else {
            textInput set operator
        }
    }

    override fun doesResultExist(): Boolean = textResult.isNotEmpty()

    override fun performEquation(): String {
        return if (checkIfEquationIsCorrect(textInput)) {
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
        operation: String
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

    override fun containsAlreadyOperatorAtPosition(position: Int): Boolean {
        var containsAlreadyOperator = false
        Operation.values().forEach {
            if (containsAlreadyOperator) {
                return@forEach
            }
            containsAlreadyOperator = textInput[position - 1].toString() == it.operationSymbol
        }
        return containsAlreadyOperator
    }

    private fun checkIfEquationIsCorrect(equation: String): Boolean {
        return MATHEMATICAL_EXPRESSION_VALIDATOR.toRegex().matches(equation)
    }

    private fun denotesPlusOrMinusSign(mathematicalOperation: String, operation: String): Boolean {
        var numberOfOperators = 0
        mathematicalOperation.forEach { if (it == '+' || it == '-') numberOfOperators += 1 }
        return if (numberOfOperators > 1) {
            false
        } else {
            mathematicalOperation.substringBefore(operation).isEmpty()
        }
    }

    private fun isEquationNotComplete(mathematicalOperation: String, operation: String): Boolean {
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

    private infix fun String.set(character: String) {
        textInput += character
    }

    companion object {
        private const val DECIMAL_FORMAT = "#.#############"
        private const val MATHEMATICAL_EXPRESSION_VALIDATOR = "^([-+/×]*\\d+(\\.\\d+)?)*"
    }

}

enum class Operation(var operationSymbol: String) {
    PLUS("+"),
    MINUS("-"),
    DIVISION("/"),
    MULTIPLICATION("×"),
    INVALID(""),
    INCOMPLETE(""),
    DONATING_SIGN("")
}