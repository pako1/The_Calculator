package com.example.calc.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calc.databinding.ActivityMainBinding
import com.example.calc.domain.doesPreviousResultExist
import com.example.calc.domain.toPercent
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
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
                numberInputField.append("1")
            }

            twoBtn.setOnClickListener {
                numberInputField.append("2")
            }

            threeBtn.setOnClickListener {
                numberInputField.append("3")
            }

            fourBtn.setOnClickListener {
                numberInputField.append("4")
            }

            fiveBtn.setOnClickListener {
                numberInputField.append("5")
            }

            sixBtn.setOnClickListener {
                numberInputField.append("6")
            }

            sevenBtn.setOnClickListener {
                numberInputField.append("7")
            }

            eightBtn.setOnClickListener {
                numberInputField.append("8")
            }

            nineBtn.setOnClickListener {
                numberInputField.append("9")
            }

            zeroBtn.setOnClickListener {
                numberInputField.append("0")
            }

            acBtn.setOnClickListener {
                numberInputField.text?.clear()
                result.text = ""
            }

            divideBtn.setOnClickListener {
                val isTherePreviousResult =
                    numberInputField.doesPreviousResultExist(result.text, "/")
                if (isTherePreviousResult) {
                    result.text = ""
                }
            }

            multiplyBtn.setOnClickListener {
                val isTherePreviousResult =
                    numberInputField.doesPreviousResultExist(result.text, "×")
                if (isTherePreviousResult) {
                    result.text = ""
                }
            }

            minusBtn.setOnClickListener {
                val isTherePreviousResult =
                    numberInputField.doesPreviousResultExist(result.text, "-")
                if (isTherePreviousResult) {
                    result.text = ""
                }
            }

            plusBtn.setOnClickListener {
                val isTherePreviousResult =
                    numberInputField.doesPreviousResultExist(result.text, "+")
                if (isTherePreviousResult) {
                    result.text = ""
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
                when {
                    mathematicalOperation.contains("+") -> {
                        val (firstNumber, secondNumber) = getNumbersFromOperation(
                            mathematicalOperation,
                            "+"
                        )
                        result.text = (firstNumber + secondNumber).toString()
                    }
                    mathematicalOperation.contains("-") -> {
                        val (firstNumber, secondNumber) = getNumbersFromOperation(
                            mathematicalOperation,
                            "-"
                        )
                        result.text = (firstNumber - secondNumber).toString()
                    }
                    mathematicalOperation.contains("×") -> {
                        val (firstNumber, secondNumber) = getNumbersFromOperation(
                            mathematicalOperation,
                            "×"
                        )
                        result.text = (firstNumber * secondNumber).toString()
                    }
                    mathematicalOperation.contains("/") -> {
                        val (firstNumber, secondNumber) = getNumbersFromOperation(
                            mathematicalOperation,
                            "/"
                        )
                        result.text = (firstNumber.toFloat() / secondNumber.toFloat()).toString()
                    }
                }

            }
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
}