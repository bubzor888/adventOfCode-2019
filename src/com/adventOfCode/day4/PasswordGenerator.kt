package com.adventOfCode.day4

import java.io.File
import java.lang.StringBuilder

fun main(args: Array<String>) {
    println("Number of valid passwords: ${execute(178416, 676461)}")
}

private fun execute(min: Int, max: Int): Int {
    var count = 0
    var result = findNextValidPassword(min.toString())

    while(result.toInt() <= max) {
        count++
        result = findNextValidPassword((result.toString().toInt().inc()).toString())
    }

    return count
}

private fun findNextValidPassword(candidate: String): String {
    var sameDigitCheck = false
    var newPassword = StringBuilder()
    var previousNumber = -1

    for (i in 0 until candidate.length) {
        val currentNumber = candidate[i].toString().toInt()
        if (currentNumber <= previousNumber) {
            sameDigitCheck = true
            //Whether they match or we're resplacing, either way we put in the previous number
            newPassword.append(previousNumber)
            //previousNumber stays the same
        } else {
            //It's higher so we keep it
            newPassword.append(currentNumber)
            previousNumber = currentNumber
        }
    }

    return when (sameDigitCheck) {
        true -> {
            println("Found a valid password: $newPassword")
            newPassword.toString()
        }
        false -> {
            //Increment the resulting password and try again
            println("Finished with $newPassword but no sameDigitCheck")
            return findNextValidPassword((newPassword.toString().toInt().inc()).toString())
        }
    }
}