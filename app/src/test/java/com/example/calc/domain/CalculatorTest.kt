package com.example.calc.domain

import com.example.calc.data.Operation
import org.junit.Test
import kotlin.test.assertEquals

class CalculatorTest {

    private val listOfOperations = listOf(
        "3+3", "3", " ", "3+9+",
        "2*2", "2+3*(6-2)", "--4",
        "9/2+(4*2)", "10!", "))((()"
    )

    @Test
    fun appendTextInputAtPosition() {

    }

    @Test
    fun reset_Text_Input() {
        val calculator = Calculator()
        assertEquals("", calculator.getTextInput())
        calculator.setTextInput(listOfOperations[0])
        assertEquals(listOfOperations[0], calculator.getTextInput())
        calculator.resetTextInput()
        assertEquals("", calculator.getTextInput())
    }

    @Test
    fun reset_Result() {
        val calculator = Calculator()
        assertEquals("", calculator.getTextResult())
        calculator.setTextResult(listOfOperations[1])
        assertEquals(listOfOperations[1], calculator.getTextResult())
        calculator.resetResult()
        assertEquals("", calculator.getTextResult())
    }

    @Test
    fun does_Result_Exist() {
        val calculator = Calculator()
        assertEquals(false, calculator.doesResultExist())
        calculator.setTextResult(listOfOperations[1])
        assertEquals(true, calculator.doesResultExist())
    }

    @Test
    fun performEquation() {
        val calculator = Calculator()
        calculator.setTextInput(listOfOperations[0])
        assertEquals("6", calculator.performEquation())
        calculator.setTextInput(listOfOperations[1])
        assertEquals("3", calculator.performEquation())
        calculator.setTextInput(listOfOperations[2])
        assertEquals(Operation.INVALID.name, calculator.performEquation())
        calculator.setTextInput(listOfOperations[3])
        assertEquals(Operation.INVALID.name, calculator.performEquation())
        calculator.setTextInput(listOfOperations[4])
        assertEquals("4", calculator.performEquation())
        calculator.setTextInput(listOfOperations[5])
        assertEquals("14", calculator.performEquation())
        calculator.setTextInput(listOfOperations[6])
        assertEquals(Operation.INVALID.name, calculator.performEquation())
        calculator.setTextInput(listOfOperations[7])
        assertEquals("12.5", calculator.performEquation())
        calculator.setTextInput(listOfOperations[8])
        assertEquals("36288001", calculator.performEquation())
        calculator.setTextInput(listOfOperations[9])
        assertEquals(Operation.INVALID.name, calculator.performEquation())
    }

    @Test
    fun modifyDisplayedResult() {
    }

    @Test
    fun isRootingPossible() {

    }

    @Test
    fun isApplyingPercentagePossible() {
    }

    @Test
    fun isPlainNumber() {
    }

    @Test
    fun reverseNumber() {
    }

    @Test
    fun hasOperatorAtEnd() {
    }

    @Test
    fun addParenthesis() {
    }

    @Test
    fun containsAlreadyOperatorAtPosition() {
    }

    @Test
    fun isNegative() {
    }

}