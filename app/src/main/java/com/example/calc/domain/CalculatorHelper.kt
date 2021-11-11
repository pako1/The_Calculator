package com.example.calc.domain

interface CalculatorHelper {
    fun setTextInput(input: String)
    fun setTextResult(result: String)
    fun getTextInput(): String
    fun getTextResult(): String
    fun appendTextInput(newChar: String)
    fun resetTextInput()
    fun resetResult()
    fun doesPreviousResultExist(mathematicalOperator: String): Boolean
    fun doesResultExist(result: CharSequence): Boolean
    fun performEquals(): String
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

    override fun appendTextInput(newChar: String) {
        textInput += newChar
    }

    override fun resetTextInput() {
        textInput = ""
    }

    override fun resetResult() {
        textResult = ""
    }

    override fun doesPreviousResultExist(mathematicalOperator: String): Boolean {
        return if (doesResultExist(textResult)) {
            textInput = textResult
            textInput setOperator mathematicalOperator
            resetResult()
            true
        } else {
            textInput setOperator mathematicalOperator
            false
        }
    }

    override fun doesResultExist(result: CharSequence): Boolean = result.isNotEmpty()

    override fun performEquals(): String {
        return when (findOperationOut()) {
            Operation.PLUS -> {
                val (firstNumber, secondNumber) = getNumbersFromOperation(
                    textInput,
                    Operation.PLUS.operationSymbol
                )
                (firstNumber + secondNumber).toString()
            }
            Operation.MINUS -> {
                val (firstNumber, secondNumber) = getNumbersFromOperation(
                    textInput,
                    Operation.MINUS.operationSymbol
                )
                (firstNumber - secondNumber).toString()
            }
            Operation.DIVISION -> {
                val (firstNumber, secondNumber) = getNumbersFromOperation(
                    textInput,
                    Operation.DIVISION.operationSymbol
                )
                (firstNumber.toFloat() / secondNumber.toFloat()).toString()
            }
            Operation.MULTIPLICATION -> {
                val (firstNumber, secondNumber) = getNumbersFromOperation(
                    textInput,
                    Operation.MULTIPLICATION.operationSymbol
                )
                (firstNumber * secondNumber).toString()
            }
            else -> ""
        }
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
            else -> Operation.NOT_AVAILABLE
        }
    }

    enum class Operation(var operationSymbol: String) {
        PLUS("+"),
        MINUS("-"),
        DIVISION("/"),
        MULTIPLICATION("*"),
        NOT_AVAILABLE("")
    }

    private infix fun String.setOperator(mathematicalOperation: String) {
        textInput += mathematicalOperation
    }

}