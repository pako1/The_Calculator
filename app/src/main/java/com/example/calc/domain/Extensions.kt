package com.example.calc.domain

fun String.toPercent(): String {
    return (toInt() / 100f).toString()
}