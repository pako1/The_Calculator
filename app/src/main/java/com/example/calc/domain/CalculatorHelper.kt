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
    fun containsAlreadyOperatorAtPosition(position: Int): Boolean
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
                    textResult = (firstNumber + secondNumber).modifyDisplayedResult()
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
                    textResult = (firstNumber - secondNumber).modifyDisplayedResult()
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

                    textResult = (firstNumber / secondNumber).modifyDisplayedResult()
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
                    textResult = (firstNumber * secondNumber).modifyDisplayedResult()
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

    private fun Double.modifyDisplayedResult(): String {
        return if (isNumberARealDouble(this)) {
            this.toString()
        } else {
            (this.toInt()).toString()
        }
    }

    private fun isNumberARealDouble(number: Double): Boolean = number % 1 != 0.0

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

    private infix fun String.setOperator(mathematicalOperation: String) {
        textInput += mathematicalOperation
    }

    companion object {
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