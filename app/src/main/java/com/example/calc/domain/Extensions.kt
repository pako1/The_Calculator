package com.example.calc.domain

fun String.toPercent(): Double {
    return (toDouble() / 100f)
}