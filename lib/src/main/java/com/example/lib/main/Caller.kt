package com.example.lib.main


fun main() {
    val parser = Parser()
    val line = readLine()

    if (line.isNullOrEmpty()) {
        println("Does not work with Empty or Null text")
        return
    }
    parser.setTextInput(line)
    parser.startProcess()
    val syntaxTree = parser.parse()
    if (syntaxTree.diagnostics.any()) {
        syntaxTree.diagnostics.forEach {
            println(it)
        }
    } else {
        val evaluator = Evaluator(syntaxTree.root)
        val result = evaluator.evaluate()
        println("The Result is: $result")
    }
}