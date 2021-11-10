package com.example.calc.domain

import com.google.android.material.textfield.TextInputEditText

fun String.toPercent(): String {
    return (toInt() / 100f).toString()
}

fun TextInputEditText.doesPreviousResultExist(
    previousResult: CharSequence,
    mathematicalOperation: String
): Boolean {
    return if (doesResultExist(previousResult)) {
        apply {
            setText(previousResult)
            append(mathematicalOperation)
        }
        true
    } else {
        append(mathematicalOperation)
        false
    }
}

private fun doesResultExist(result: CharSequence): Boolean = result.isNotEmpty()
