package com.example.calc.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calc.databinding.ActivityMainBinding
import com.example.calc.domain.Calculator
import com.example.calc.domain.CalculatorHelper
import com.example.calc.domain.toPercent
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

            backspaceBtn.setOnClickListener {
                val text = numberInputField.text
                if (!text.isNullOrEmpty()) {
                    val updatedText = text.toString().trim()
                    when (val cursorPosition = numberInputField.selectionStart) {
                        0 -> return@setOnClickListener
                        1 -> {
                            val remainingText = updatedText.substring(1, updatedText.length)
                            numberInputField.setText(remainingText)
                            numberInputField.setSelection(0)
                            result.text = if (remainingText == "%") "" else remainingText
                        }
                        updatedText.length -> {
                            if (updatedText.last() == '%') {
                                result.text = ""
                            }
                            val remainingText = updatedText.substring(0, updatedText.length - 1)
                            numberInputField.setText(remainingText)
                            numberInputField.setSelection(remainingText.length)
                        }
                        else -> {
                            val remainingTextFirstPart =
                                updatedText.substring(0, cursorPosition - 1)
                            val remainingTextSecondPart =
                                updatedText.substring(cursorPosition, updatedText.length)
                            val finalText = remainingTextFirstPart + remainingTextSecondPart
                            if (finalText.contains("%") && finalText.length > 1) {
                                result.text =
                                    finalText.substring(0, finalText.length - 1).toPercent()
                            }
                            numberInputField.setText(finalText)
                            numberInputField.setSelection(remainingTextFirstPart.length)
                        }
                    }
                }
            }

            oneBtn.setOnClickListener {
                calculatorHelper.appendTextInput("1")
                numberInputField.append("1")
            }

            twoBtn.setOnClickListener {
                calculatorHelper.appendTextInput("2")
                numberInputField.append("2")
            }

            threeBtn.setOnClickListener {
                calculatorHelper.appendTextInput("3")
                numberInputField.append("3")
            }

            fourBtn.setOnClickListener {
                calculatorHelper.appendTextInput("4")
                numberInputField.append("4")
            }

            fiveBtn.setOnClickListener {
                calculatorHelper.appendTextInput("5")
                numberInputField.append("5")
            }

            sixBtn.setOnClickListener {
                calculatorHelper.appendTextInput("6")
                numberInputField.append("6")
            }

            sevenBtn.setOnClickListener {
                calculatorHelper.appendTextInput("7")
                numberInputField.append("7")
            }

            eightBtn.setOnClickListener {
                calculatorHelper.appendTextInput("8")
                numberInputField.append("8")
            }

            nineBtn.setOnClickListener {
                calculatorHelper.appendTextInput("9")
                numberInputField.append("9")
            }

            zeroBtn.setOnClickListener {
                calculatorHelper.appendTextInput("0")
                numberInputField.append("0")
            }

            acBtn.setOnClickListener {
                calculatorHelper.resetResult()
                calculatorHelper.resetTextInput()
                numberInputField.text?.clear()
                result.text = ""
            }

            divideBtn.setOnClickListener {
                val isTherePreviousResult = calculatorHelper.doesPreviousResultExist("/")
                if (isTherePreviousResult) {
                    numberInputField.setText(calculatorHelper.getTextInput())
                    result.text = ""
                } else {
                    numberInputField.setText(calculatorHelper.getTextInput())
                }
            }

            multiplyBtn.setOnClickListener {
                val isTherePreviousResult = calculatorHelper.doesPreviousResultExist("×")
                if (isTherePreviousResult) {
                    numberInputField.setText(calculatorHelper.getTextInput())
                    result.text = ""
                } else {
                    numberInputField.setText(calculatorHelper.getTextInput())
                }
            }

            minusBtn.setOnClickListener {
                val isTherePreviousResult = calculatorHelper.doesPreviousResultExist("-")
                if (isTherePreviousResult) {
                    numberInputField.setText(calculatorHelper.getTextInput())
                    result.text = ""
                } else {
                    numberInputField.setText(calculatorHelper.getTextInput())
                }
            }

            plusBtn.setOnClickListener {
                val isTherePreviousResult = calculatorHelper.doesPreviousResultExist("+")
                if (isTherePreviousResult) {
                    numberInputField.setText(calculatorHelper.getTextInput())
                    result.text = ""
                } else {
                    numberInputField.setText(calculatorHelper.getTextInput())
                }
            }

            percentBtn.setOnClickListener {
                val inputFieldText = numberInputField.text?.toString()
                if (!inputFieldText.isNullOrEmpty() && !inputFieldText.contains("%")) {
                    val numberInPercentage = inputFieldText.toPercent()
                    numberInputField.append("%")
                    result.text = numberInPercentage
                }
            }

            squareBtn.setOnClickListener {
                val inputFieldText = numberInputField.text?.toString()?.toDouble()
                if (inputFieldText != null) {
                    result.text = sqrt(inputFieldText).toString()
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
                val mathematicalOperation = numberInputField.text.toString()
                result.text = calculatorHelper.performEquals()
                calculatorHelper.setTextResult(result.text.toString())
            }
        }
    }
}