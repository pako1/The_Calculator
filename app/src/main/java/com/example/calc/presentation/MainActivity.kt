package com.example.calc.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.calc.R
import com.example.calc.data.Operation
import com.example.calc.databinding.ActivityMainBinding
import com.example.calc.domain.CalculatorHelper
import com.example.calc.domain.DataStoreManager
import com.example.calc.domain.toPercent
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.sqrt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //By using inject here hilt will provide us a calculatorHelper instance by using the CalculatorModule to find out how..
    @Inject
    lateinit var calculatorHelper: CalculatorHelper

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var mainBinding: ActivityMainBinding

    private var currentCursorPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_The_Calculator)
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.lifecycleOwner = this
        initializeButtons()

        lifecycleScope.launch {

            viewModel.result.observe(this@MainActivity, { resultText ->
                mainBinding.result.text = resultText
                calculatorHelper.setTextResult(resultText)
            })

            viewModel.equation.observe(this@MainActivity, { updatedEquation ->
                with(mainBinding) {
                    numberInputField.setText(updatedEquation)
                    calculatorHelper.setTextInput(updatedEquation)
                    val cursorPosition = if (updatedEquation.isEmpty()) {
                        0
                    } else {
                        currentCursorPosition + 1
                    }
                    try {
                        numberInputField.setSelection(cursorPosition)
                    } catch (e: Exception) {
                        numberInputField.setSelection(updatedEquation.length)
                    }
                }
            })
            pickDarkOrLightMode()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(CURSOR_POSITION_KEY, currentCursorPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentCursorPosition = savedInstanceState.getInt(CURSOR_POSITION_KEY, 0)
    }

    private suspend fun pickDarkOrLightMode() {
        dataStoreManager.retrieveDarkLightMode()
            .collect {
                withContext(Main) {
                    with(mainBinding) {
                        when (it) {
                            true -> {
                                enableDarkMode()
                                darkModeButton.isClickable = false
                                darkModeButton.isPressed = false
                                lightModeButton.isClickable = true
                                lightModeButton.isPressed = true
                                lightModeButton.setIconTintResource(R.color.iconModePickedColor)
                            }
                            false -> {
                                enableLightMode()
                                darkModeButton.isClickable = true
                                darkModeButton.isPressed = true
                                lightModeButton.isClickable = false
                                lightModeButton.isPressed = false
                                darkModeButton.setIconTintResource(R.color.iconModePickedColor)
                            }
                            else -> {
                                when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                                    Configuration.UI_MODE_NIGHT_YES -> {
                                        darkModeButton.isClickable = false
                                        darkModeButton.isPressed = false
                                        lightModeButton.isClickable = true
                                        lightModeButton.isPressed = true
                                        lightModeButton.setIconTintResource(R.color.iconModePickedColor)
                                    }
                                    Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                                        darkModeButton.isClickable = true
                                        darkModeButton.isPressed = true
                                        lightModeButton.isClickable = false
                                        lightModeButton.isPressed = false
                                        darkModeButton.setIconTintResource(R.color.iconModePickedColor)
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

    private fun isManuallyHandlingOfDarkModePossible(): Boolean =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun enableLightMode() =
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    private fun initializeButtons() {
        with(mainBinding) {
            lifecycleOwner = this@MainActivity

            numberInputField.apply {
                showSoftInputOnFocus = false
            }

            if (isManuallyHandlingOfDarkModePossible()) {
                lightModeButton.setOnClickListener {
                    lifecycleScope.launch {
                        dataStoreManager.saveDarkLightMode(isNightModeEnabled = false)
                        enableLightMode()
                    }
                }

                darkModeButton.setOnClickListener {
                    lifecycleScope.launch {
                        dataStoreManager.saveDarkLightMode(isNightModeEnabled = true)
                        enableDarkMode()
                    }
                }
            } else {
                lightModeButton.visibility = View.GONE
                darkModeButton.visibility = View.GONE
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

            backspaceBtn.setOnLongClickListener { clearAll(); false }

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

            acBtn.setOnClickListener { clearAll() }

            multiplyBtn.setOnClickListener {
                numberInputField applyOperatorOnEquation Operation.MULTIPLICATION.operatorSymbol
            }

            divideBtn.setOnClickListener {
                numberInputField applyOperatorOnEquation Operation.DIVISION.operatorSymbol
            }

            minusBtn.setOnClickListener {
                numberInputField applyOperatorOnEquation Operation.SUBTRACTION.operatorSymbol
            }

            plusBtn.setOnClickListener {
                numberInputField applyOperatorOnEquation Operation.ADDITION.operatorSymbol
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
                            equationResult == Operation.SIGNED_NUMBER_REPRESENTATION.name -> {
                                val numberInPercentage =
                                    calculatorHelper.modifyDisplayedResult(
                                        calculatorHelper.getTextInput().toPercent()
                                    )
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

            rootBtn.setOnClickListener {
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
                                calculatorHelper.isNegative(equationResult) -> return@setOnClickListener
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
                    if (cursorPosition == calculatorHelper.getTextInput().length && calculatorHelper.hasOperatorAtEnd()) {
                        return@setOnClickListener
                    }
                    val equation = calculatorHelper.addParenthesis(cursorPosition)
                    calculatorHelper.setTextInput(equation)
                    numberInputField.setText(equation)
                    numberInputField.setSelection(equation.length)
                }
            }

            factorialBtn.setOnClickListener { numberInputField.insertChar('!') }

            equalsBtn.setOnClickListener {
                when (val operationResult = calculatorHelper.performEquation()) {
                    Operation.INVALID.name, Operation.INCOMPLETE.name, Operation.SIGNED_NUMBER_REPRESENTATION.name -> {
                        Snackbar.make(
                            mainBinding.root,
                            "Not valid operation, check your equation",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                    else -> {
                        viewModel.result.value = operationResult
                    }
                }
            }

        }
    }

    private fun clearAll() {
        viewModel.result.value = ""
        viewModel.equation.value = ""
    }

    private fun applyDot(numberInputField: TextInputEditText, result: TextView) {
        val isTherePreviousResult = calculatorHelper.doesResultExist()
        val calculationResult = calculatorHelper.getTextResult()
        if (isTherePreviousResult) {
            if (calculationResult[result.length() - 1] == DOT_CHAR) {
                return
            }
            viewModel.equation.value = calculationResult
            numberInputField.insertChar(DOT_CHAR)
            viewModel.result.value = ""
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

    private infix fun TextInputEditText.applyOperatorOnEquation(operator: Char) {
        val isTherePreviousResult = calculatorHelper.doesResultExist()
        val calculationResult = calculatorHelper.getTextResult()
        if (isTherePreviousResult) {
            viewModel.equation.value = calculationResult
            insertChar(operator)
            viewModel.result.value = ""
        } else {
            if ((length() >= 0 && selectionStart == 0) || doesContainOperator()) {
                return
            } else {
                insertChar(operator)
            }
        }
    }

    private fun TextInputEditText.doesContainOperator(): Boolean {
        return calculatorHelper.containsAlreadyOperatorAtPosition(selectionStart)
    }

    private fun TextInputEditText.insertChar(newChar: Char) {
        currentCursorPosition = selectionStart
        calculatorHelper.appendTextInputAtPosition(newChar, currentCursorPosition)
        viewModel.equation.value =
            text?.insert(currentCursorPosition, newChar.toString()).toString()
    }

    companion object {
        private const val DOT_CHAR = '.'
        private const val CURSOR_POSITION_KEY = "CursorPosition_KEY"
    }
}