package com.example.calc.domain

import org.junit.Test
import kotlin.test.assertEquals

class CalculatorTest {

    private val listOfOperations = listOf("3+3", "3", " ", "3+9+")

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
    fun is_Equation_Correct() {
        val calculator = Calculator()

        assertEquals(true, calculator.isEquationCorrect("3+2"))
        assertEquals(true, calculator.isEquationCorrect("3-3"))
        assertEquals(true, calculator.isEquationCorrect("3/3"))
        assertEquals(true, calculator.isEquationCorrect("3×3"))
        assertEquals(false, calculator.isEquationCorrect("1+(+)+3"))
        assertEquals(false, calculator.isEquationCorrect(")("))
        assertEquals(false, calculator.isEquationCorrect("3)-/(3"))
        assertEquals(false, calculator.isEquationCorrect("(3+3--"))
    }

    @Test
    fun isNegative() {
    }

}