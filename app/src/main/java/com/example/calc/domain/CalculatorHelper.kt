package com.example.calc.domain

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
            textInput setOperator operator
            resetResult()
        } else {
            textInput setOperator operator
        }
    }

    override fun doesResultExist(): Boolean = textResult.isNotEmpty()

    override fun performEquation(): String {
        return if (checkIfEquationIsCorrect(textInput)) {
            when (findOperationOut()) {
                Operation.PLUS -> {
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.PLUS.operationSymbol
                    )
                    textResult = (firstNumber + secondNumber).toString()
                    return textResult
                }
                Operation.MINUS -> {
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.MINUS.operationSymbol
                    )
                    textResult = (firstNumber - secondNumber).toString()
                    return textResult
                }
                Operation.DIVISION -> {
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.DIVISION.operationSymbol
                    )
                    textResult = (firstNumber / secondNumber).toString()
                    return textResult
                }
                Operation.MULTIPLICATION -> {
                    val (firstNumber, secondNumber) = getNumbersFromOperation(
                        textInput,
                        Operation.MULTIPLICATION.operationSymbol
                    )
                    textResult = (firstNumber * secondNumber).toString()
                    return textResult
                }
                else -> Operation.INVALID.name
            }
        } else {
            Operation.INVALID.name
        }
    }

    private fun checkIfEquationIsCorrect(equation: String): Boolean {
        return MATHEMATICAL_EXPRESSION_VALIDATOR.toRegex().matches(equation)
    }

    private fun getNumbersFromOperation(
        mathematicalOperation: String,
        operation: String
    ): Pair<Int, Int> {
        val firstNumber = mathematicalOperation.substringBefore(operation).toInt()
        val secondNumber = mathematicalOperation.substringAfter(operation).toInt()
        return firstNumber to secondNumber
    }

    private fun findOperationOut(): Operation {
        return when {
            textInput.contains("+") -> Operation.PLUS
            textInput.contains("-") -> Operation.MINUS
            textInput.contains("/") -> Operation.DIVISION
            textInput.contains("*") -> Operation.MULTIPLICATION
            else -> Operation.INVALID
        }
    }

    private infix fun String.setOperator(mathematicalOperation: String) {
        textInput += mathematicalOperation
    }

    companion object {
        private const val MATHEMATICAL_EXPRESSION_VALIDATOR = "^([-+/*]*\\d+(\\.\\d+)?)*"
    }

}

enum class Operation(var operationSymbol: String) {
    PLUS("+"),
    MINUS("-"),
    DIVISION("/"),
    MULTIPLICATION("*"),
    INVALID("")
}