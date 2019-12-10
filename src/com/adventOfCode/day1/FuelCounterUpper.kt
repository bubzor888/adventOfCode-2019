package com.adventOfCode.day1

import java.io.File

fun main(args: Array<String>) {
    var fileName = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day1\\input.txt"
    var num = File(fileName).readLines().map { (it.toInt() / 3) - 2 }.reduce { sum, element -> sum + element }
    println("Part 1 answer: $num")

    println("Test1: ${findFuelRecursive((14/3)-2, 0)}")
    println("Test2: ${findFuelRecursive((1969/3)-2, 0)}")
    println("Test3: ${findFuelRecursive((100756/3)-2, 0)}")


    var num2 = File(fileName).readLines().map { findFuelRecursive((it.toLong()/3)-2, 0) }.reduce { sum, element -> sum + element }
    println("Part 2 answer: $num2")
}

tailrec fun findFuelRecursive(newFuel: Long, sum: Long): Long {
    return when (newFuel) {
        -2L -> sum
        -1L -> sum
        0L -> sum
        else -> findFuelRecursive((newFuel / 3) - 2, sum + newFuel)
    }
}