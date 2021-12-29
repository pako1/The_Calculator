package com.example.lib.models

data class SyntaxToken(
    val syntaxKind: SyntaxKind,
    val position: Int,
    val text: String?,
    val value: Any?
)