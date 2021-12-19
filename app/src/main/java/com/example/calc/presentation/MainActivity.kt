package com.example.calc.presentation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calc.databinding.ActivityMainBinding
import com.example.calc.domain.Calculator
import com.example.calc.domain.CalculatorHelper
import com.example.calc.domain.Operation
import com.example.calc.domain.toPercent
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var calculatorHelper: CalculatorHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        calculatorHelper = Calculator()
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
                            result.text = if (remainingText == "%") "" else remainingText
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
                            if (finalText.contains("%") && finalText.length > 1) {
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

            oneBtn.setOnClickListener { numberInputField.insertChar("1") }

            twoBtn.setOnClickListener { numberInputField.insertChar("2") }

            threeBtn.setOnClickListener { numberInputField.insertChar("3") }

            fourBtn.setOnClickListener { numberInputField.insertChar("4") }

            fiveBtn.setOnClickListener { numberInputField.insertChar("5") }

            sixBtn.setOnClickListener { numberInputField.insertChar("6") }

            sevenBtn.setOnClickListener { numberInputField.insertChar("7") }

            eightBtn.setOnClickListener { numberInputField.insertChar("8") }

            nineBtn.setOnClickListener { numberInputField.insertChar("9") }

            zeroBtn.setOnClickListener { numberInputField.insertChar("0") }

            dotBtn.setOnClickListener { numberInputField.insertChar(".") }

            acBtn.setOnClickListener {
                calculatorHelper.resetResult()
                calculatorHelper.resetTextInput()
                numberInputField.text?.clear()
                result.text = ""
            }

            divideBtn.setOnClickListener { applyOperatorOnEquation("/", numberInputField, result) }

            multiplyBtn.setOnClickListener {
                applyOperatorOnEquation(
                    "×",
                    numberInputField,
                    result
                )
            }

            minusBtn.setOnClickListener { applyOperatorOnEquation("-", numberInputField, result) }

            plusBtn.setOnClickListener { applyOperatorOnEquation("+", numberInputField, result) }

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
                } else {
                    val inputFieldText = numberInputField.text?.toString()
                    if (!inputFieldText.isNullOrEmpty() && !inputFieldText.contains("%")) {
                        val numberInPercentage =
                            calculatorHelper.modifyDisplayedResult(inputFieldText.toPercent())
                        numberInputField.append("%")
                        result.text = numberInPercentage
                        calculatorHelper.setTextResult(numberInPercentage)
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
                } else {
                    val inputFieldText = numberInputField.text?.toString()
                    if (!inputFieldText.isNullOrEmpty()) {
                        val root = sqrt(inputFieldText.toDouble()).toString()
                        result.text = root
                        calculatorHelper.setTextResult(root)
                    }
                }
            }

            positiveNegativeConverterBtn.setOnClickListener {
                numberInputField.text?.toString()?.toInt()?.let {
                    if (it > 0) {
                        //todo add - from front
                        return@let
                    } else {
                        return@let
                        //todo remove - from front
                    }
                }
            }

            equalsBtn.setOnClickListener {
                when (val operationResult = calculatorHelper.performEquation()) {
                    Operation.INVALID.name, Operation.INCOMPLETE.name, Operation.DONATING_SIGN.name -> {
                        return@setOnClickListener
                    }
                    else -> {
                        result.text = operationResult
                    }
                }
            }
        }
    }

    private fun applyOperatorOnEquation(
        operator: String,
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

    private fun TextInputEditText.insertChar(number: String) {
        val currentCursorPosition = selectionEnd
        calculatorHelper.appendTextInputAtPosition(number, currentCursorPosition)
        text?.insert(currentCursorPosition, number)
    }
}