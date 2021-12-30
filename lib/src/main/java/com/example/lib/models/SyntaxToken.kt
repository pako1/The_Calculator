package com.example.lib.models

 data class SyntaxToken internal constructor(
    val syntaxKind: SyntaxKind,
    val position: Int,
    val text: String?,
    val value: Any?
)