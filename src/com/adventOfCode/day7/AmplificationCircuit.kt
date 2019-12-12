package com.adventOfCode.day7

import com.adventOfCode.day2.execute
import java.io.File

fun main(args: Array<String>) {
    val test1 = listOf("")
    if (execute(test1) == 1) {
        println("Test1 Pass")
    } else {
        println("Test1 Failed")
    }

    val test2 = listOf("")
    if (execute(test2) == 2) {
        println("Test2 Pass")
    } else {
        println("Test2 Failed")
    }

    val test3 = listOf("")
    if (execute(test3) == 3) {
        println("Test3 Pass")
    } else {
        println("Test3 Failed")
    }

    var fileName = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\dayX\\input.txt"
    var input = File(fileName).readLines()

}

private fun execute(input: List<String>): Int {
    return 0
}