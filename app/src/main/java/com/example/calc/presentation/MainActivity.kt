package com.example.calc.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calc.databinding.ActivityMainBinding
import com.example.calc.domain.toPercent

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
            acBtn.setOnClickListener {
                numberInputField.text?.clear()
                result.text = ""
            }
            multiplyBtn.setOnClickListener {
                numberInputField.append("*")
            }
            plusBtn.setOnClickListener {
                numberInputField.append("+")
            }
            equalsBtn.setOnClickListener {
                val mathematicalOperation = numberInputField.text.toString()
                when {
                    mathematicalOperation.contains("+") -> {
                        val firstNumber = mathematicalOperation.substringBefore("+").toInt()
                        val secondNumber = mathematicalOperation.substringAfter("+").toInt()
                        result.text = (firstNumber + secondNumber).toString()
                    }
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
        }
    }
}