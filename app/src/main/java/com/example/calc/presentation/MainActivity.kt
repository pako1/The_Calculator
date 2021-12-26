package com.example.calc.presentation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calc.R
import com.example.calc.data.Operation
import com.example.calc.databinding.ActivityMainBinding
import com.example.calc.domain.CalculatorHelper
import com.example.calc.domain.toPercent
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.sqrt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //By using inject here hilt will provide us a calculatorHelper instance by using the CalculatorModule to find out how..
    @Inject
    lateinit var calculatorHelper: CalculatorHelper

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_The_Calculator)
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initializeButtons()
    }

    private fun initializeButtons() {
        with(mainBinding) {

            numberInputField.apply {
                showSoftInputOnFocus = false
            }

            backspaceBtn.setOnClickListener {
                val text = numberInputField.text
                if (!text.isNullOrEmpty()) {
                    when (val cursorPosition = numberInputField.selectionStart) {
                        0 -> return@setOnClickListener
                        1 -> {
                            val remainingText = text.substring(1, text.length)
                            calculatorHelper.setTextInput(remainingText)
                            numberInputField.setText(remainingText)
                            numberInputField.setSelection(0)
                            result.text =
                                if (remainingText == Operation.PERCENTAGE.operatorSymbol.toString()) "" else remainingText
                            if (result.text.isNullOrEmpty()) {
                                calculatorHelper.setTextResult(remainingText)
                            }
                        }
                        text.length -> {
                            if (text.last() == '%') {
                                result.text = ""
                            }
                            val remainingText = text.substring(0, text.length - 1)
                            calculatorHelper.setTextInput(remainingText)
                            numberInputField.setText(remainingText)
                            numberInputField.setSelection(remainingText.length)
                        }
                        else -> {
                            val remainingTextFirstPart =
                                text.substring(0, cursorPosition - 1)
                            val remainingTextSecondPart =
                                text.substring(cursorPosition, text.length)
                            val finalText = remainingTextFirstPart + remainingTextSecondPart
                            if (finalText.contains(Operation.PERCENTAGE.toString()) && finalText.length > 1) {
                                result.text = calculatorHelper.modifyDisplayedResult(
                                    finalText.substring(
                                        0,
                                        finalText.length - 1
                                    ).toPercent()
                                )
                            }
                            calculatorHelper.setTextInput(finalText)
                            numberInputField.setText(finalText)
                            numberInputField.setSelection(remainingTextFirstPart.length)
                        }
                    }
                }
            }

            backspaceBtn.setOnLongClickListener {
                calculatorHelper.resetResult()
                calculatorHelper.resetTextInput()
                numberInputField.text?.clear()
                result.text = ""
                false
            }

            oneBtn.setOnClickListener { numberInputField.insertChar('1') }

            twoBtn.setOnClickListener { numberInputField.insertChar('2') }

            threeBtn.setOnClickListener { numberInputField.insertChar('3') }

            fourBtn.setOnClickListener { numberInputField.insertChar('4') }

            fiveBtn.setOnClickListener { numberInputField.insertChar('5') }

            sixBtn.setOnClickListener { numberInputField.insertChar('6') }

            sevenBtn.setOnClickListener { numberInputField.insertChar('7') }

            eightBtn.setOnClickListener { numberInputField.insertChar('8') }

            nineBtn.setOnClickListener { numberInputField.insertChar('9') }

            zeroBtn.setOnClickListener { numberInputField.insertChar('0') }

            dotBtn.setOnClickListener { applyDot(numberInputField, result) }

            acBtn.setOnClickListener {
                calculatorHelper.resetResult()
                calculatorHelper.resetTextInput()
                numberInputField.text?.clear()
                result.text = ""
            }

            multiplyBtn.setOnClickListener {
                applyOperatorOnEquation(
                    Operation.MULTIPLICATION.operatorSymbol,
                    numberInputField,
                    result
                )
            }

            divideBtn.setOnClickListener {
                applyOperatorOnEquation(
                    Operation.DIVISION.operatorSymbol,
                    numberInputField,
                    result
                )
            }

            minusBtn.setOnClickListener {
                applyOperatorOnEquation(
                    Operation.SUBTRACTION.operatorSymbol,
                    numberInputField,
                    result
                )
            }

            plusBtn.setOnClickListener {
                applyOperatorOnEquation(
                    Operation.ADDITION.operatorSymbol,
                    numberInputField,
                    result
                )
            }

            percentBtn.setOnClickListener {
                val isTherePreviousResult = calculatorHelper.doesResultExist()
                if (isTherePreviousResult) {
                    val calculationResult = calculatorHelper.getTextResult()
                    numberInputField.setText(calculationResult)
                    numberInputField.append("%")
                    calculatorHelper.setTextInput(calculationResult)
                    calculatorHelper.resetResult()
                    val percentageResult =
                        calculatorHelper.modifyDisplayedResult(calculationResult.toPercent())
                    result.text = percentageResult
                    calculatorHelper.setTextResult(percentageResult)
                    numberInputField.setSelection(numberInputField.selectionEnd)
                } else {
                    val inputFieldText = numberInputField.text.toString()
                    if (inputFieldText.isNotEmpty()
                        && !inputFieldText.contains("%")
                        && calculatorHelper.isApplyingPercentagePossible(inputFieldText)
                    ) {
                        calculatorHelper.setTextInput(inputFieldText)
                        val equationResult = calculatorHelper.performEquation()

                        when {
                            equationResult == Operation.SIGNED_NUMBER_REPRESENTATION.name ->{
                                val numberInPercentage =
                                    calculatorHelper.modifyDisplayedResult(calculatorHelper.getTextInput().toPercent())
                                numberInputField.append("%")
                                result.text = numberInPercentage
                                calculatorHelper.setTextResult(numberInPercentage)
                            }
                            equationResult == Operation.INVALID.name && calculatorHelper.isPlainNumber(
                                equationResult
                            ) -> return@setOnClickListener
                            equationResult == Operation.INVALID.name && calculatorHelper.isPlainNumber(
                                inputFieldText
                            ) -> {
                                val numberInPercentage =
                                    calculatorHelper.modifyDisplayedResult(inputFieldText.toPercent())
                                numberInputField.append("%")
                                result.text = numberInPercentage
                                calculatorHelper.setTextResult(numberInPercentage)
                            }
                            equationResult != Operation.INVALID.name && calculatorHelper.isPlainNumber(
                                equationResult
                            ) -> {
                                val numberInPercentage =
                                    calculatorHelper.modifyDisplayedResult(equationResult.toPercent())
                                numberInputField.append("%")
                                result.text = numberInPercentage
                                calculatorHelper.setTextResult(numberInPercentage)
                            }
                        }
                    }
                }
            }

            squareBtn.setOnClickListener {
                val isTherePreviousResult = calculatorHelper.doesResultExist()
                if (isTherePreviousResult) {
                    val calculationResult = calculatorHelper.getTextResult()
                    numberInputField.setText(calculationResult)
                    calculatorHelper.setTextInput(calculationResult)
                    calculatorHelper.resetResult()
                    result.text = sqrt(calculationResult.toDouble()).toString()
                    numberInputField.setSelection(numberInputField.selectionEnd)
                } else {
                    val inputFieldText = numberInputField.text.toString()
                    if (inputFieldText.isNotEmpty()) {
                        calculatorHelper.setTextInput(inputFieldText)
                        if (calculatorHelper.isRootingPossible()) {
                            val equationResult = calculatorHelper.performEquation()
                            when {
                                calculatorHelper.isNegative(equationResult) -> return@setOnClickListener
                                equationResult == Operation.INVALID.name && calculatorHelper.isPlainNumber(
                                    equationResult
                                ) -> return@setOnClickListener
                                equationResult == Operation.INVALID.name && calculatorHelper.isPlainNumber(
                                    inputFieldText
                                ) -> {
                                    val root = sqrt(inputFieldText.toDouble()).toString()
                                    result.text = root
                                    calculatorHelper.setTextResult(root)
                                }
                                equationResult != Operation.INVALID.name && calculatorHelper.isPlainNumber(
                                    equationResult
                                ) -> {
                                    val root = sqrt(equationResult.toDouble()).toString()
                                    result.text = root
                                    calculatorHelper.setTextResult(root)
                                }
                            }
                        }
                    }
                }
            }

            positiveNegativeConverterBtn.setOnClickListener {
                val currentInput = calculatorHelper.getTextInput()
                if (currentInput.isEmpty()) {
                    return@setOnClickListener
                }
                val isPlainNumber = calculatorHelper.isPlainNumber(currentInput)
                if (isPlainNumber) {
                    val reversedNumber = calculatorHelper.reverseNumber()
                    calculatorHelper.setTextInput(reversedNumber)
                    numberInputField.setText(reversedNumber)
                    numberInputField.setSelection(reversedNumber.length)
                } else {
                    val cursorPosition = numberInputField.selectionStart
                    val equation = calculatorHelper.addParenthesis(cursorPosition)
                    calculatorHelper.setTextInput(equation)
                    numberInputField.setText(equation)
                    numberInputField.setSelection(equation.length)
                }
            }

            equalsBtn.setOnClickListener {
                when (val operationResult = calculatorHelper.performEquation()) {
                    Operation.INVALID.name, Operation.INCOMPLETE.name, Operation.SIGNED_NUMBER_REPRESENTATION.name -> {
                        return@setOnClickListener
                    }
                    else -> {
                        result.text = operationResult
                    }
                }
            }
        }
    }

    private fun applyDot(numberInputField: TextInputEditText, result: TextView) {
        val isTherePreviousResult = calculatorHelper.doesResultExist()
        val calculationResult = calculatorHelper.getTextResult()
        if (isTherePreviousResult) {
            if (calculationResult[numberInputField.selectionStart - 1] == DOT_CHAR) {
                return
            }
            numberInputField.setText(calculationResult)
            calculatorHelper.setTextInput(calculationResult)
            numberInputField.setSelection(calculationResult.length)
            numberInputField.insertChar(DOT_CHAR)
            calculatorHelper.resetResult()
            result.text = ""
        } else {
            if ((numberInputField.length() >= 0 && numberInputField.selectionStart == 0) || numberInputField.doesContainOperator()) {
                return
            } else {
                if (calculatorHelper.getTextInput()[numberInputField.selectionStart - 1] == DOT_CHAR) {
                    return
                }
                numberInputField.insertChar(DOT_CHAR)
            }
        }
    }

    private fun applyOperatorOnEquation(
        operator: Char,
        numberInputField: TextInputEditText,
        result: TextView
    ) {
        val isTherePreviousResult = calculatorHelper.doesResultExist()
        if (isTherePreviousResult) {
            val calculationResult = calculatorHelper.getTextResult()
            numberInputField.setText(calculationResult)
            calculatorHelper.setTextInput(calculationResult)
            numberInputField.setSelection(calculationResult.length)
            numberInputField.insertChar(operator)
            calculatorHelper.resetResult()
            result.text = ""
        } else {
            if ((numberInputField.length() >= 0 && numberInputField.selectionStart == 0) || numberInputField.doesContainOperator()) {
                return
            } else {
                numberInputField.insertChar(operator)
            }
        }
    }

    private fun TextInputEditText.doesContainOperator(): Boolean {
        return calculatorHelper.containsAlreadyOperatorAtPosition(selectionStart)
    }

    private fun TextInputEditText.insertChar(newChar: Char) {
        val currentCursorPosition = selectionStart
        calculatorHelper.appendTextInputAtPosition(newChar, currentCursorPosition)
        text?.insert(currentCursorPosition, newChar.toString())
    }

    companion object {
        private const val DOT_CHAR = '.'
    }
}