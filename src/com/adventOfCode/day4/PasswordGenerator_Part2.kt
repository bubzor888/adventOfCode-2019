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
    var inMatch = false
    var numSameDigitsInARow = 0
    var newPassword = StringBuilder()
    var previousNumber = -1

    for (i in 0 until candidate.length) {
        val currentNumber = candidate[i].toString().toInt()
        if (currentNumber <= previousNumber) {
            if (!inMatch) {
                inMatch = true
                numSameDigitsInARow = 2
            } else {
                numSameDigitsInARow++
            }
            newPassword.append(previousNumber)
            //previousNumber stays the same
        } else {
            //We're switching to a new number, see if we just came out of a match
            if (inMatch && numSameDigitsInARow == 2) {
                sameDigitCheck = true
            }
            inMatch = false
            newPassword.append(currentNumber)
            previousNumber = currentNumber
        }
    }

    //See if the last digit satisfied the match
    if (inMatch && numSameDigitsInARow == 2) {
        sameDigitCheck = true
    }

    return when (sameDigitCheck) {
        true -> {
            //We have all pairs, we're good
            println("Found a valid password: $newPassword")
            return newPassword.toString()
        }
        false -> {
            //Increment the resulting password and try again
            println("Finished with $newPassword but no sameDigitCheck")
            return findNextValidPassword((newPassword.toString().toInt().inc()).toString())
        }
    }
}