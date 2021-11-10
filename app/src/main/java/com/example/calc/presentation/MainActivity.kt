package com.example.calc.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calc.databinding.ActivityMainBinding

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
                    val tedText = updatedText.substring(0, updatedText.length - 1)
                    numberInputField.setText(tedText)
                    numberInputField.setSelection(tedText.length)
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
            }
        }

    }

}