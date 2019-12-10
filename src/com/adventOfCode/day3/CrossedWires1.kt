package com.adventOfCode.day3

import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val test1 = listOf("R8,U5,L5,D3", "U7,R6,D4,L4")
    if (execute(test1) == 6) {
        println("Test1 Pass")
    }

    val test2 = listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
    if (execute(test2) == 159) {
        println("Test2 Pass")
    }

    val test3 = listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
    if (execute(test3) == 135) {
        println("Test3 Pass")
    }

    val fileName = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day3\\input.txt"
    println("Final Result: ${execute(File(fileName).readLines())}")

}

private fun execute(input: List<String>): Int {
    //Track a list of every grid space wire1 travels
    var wire1Grid = mutableListOf<Pair<Int, Int>>()
    var currentPosition = Pair(0, 0)
    input[0].split(",").forEach {
        val movement = move(currentPosition, it)
        currentPosition = movement.last()
        wire1Grid.addAll(movement)
    }

    var wire2Grid = mutableListOf(Pair(0, 0))
    currentPosition = Pair(0, 0)
    input[1].split(",").forEach {
        val movement = move(currentPosition, it)
        currentPosition = movement.last()
        wire2Grid.addAll(movement)
    }

    //Now we have all the positions they moved to, lets find intersections and keep track of the lowest one
    var bestDistance = -1
    for (pair in wire1Grid) {
        if (wire2Grid.contains(pair)) {
            val distance = calcDistance(pair)
            if (bestDistance == -1 || distance < bestDistance) {
                bestDistance = distance
            }
        }
    }

    return bestDistance
}

private fun move(start: Pair<Int, Int>, instruction: String): List<Pair<Int, Int>> {
    val direction = instruction[0]
    val distance = instruction.substring(1).toInt()

    var result = mutableListOf<Pair<Int, Int>>()

    when (direction) {
        'U' -> {
            for (i in 1..distance) {
                result.add(Pair(start.first, start.second + i))
            }
        }
        'D' -> {
            for (i in 1..distance) {
                result.add(Pair(start.first, start.second - i))
            }
        }
        'L' -> {
            for (i in 1..distance) {
                result.add(Pair(start.first - i, start.second))
            }
        }
        'R' -> {
            for (i in 1..distance) {
                result.add(Pair(start.first + i, start.second))
            }
        }
    }

    return result
}

private fun calcDistance(position: Pair<Int, Int>): Int {
    return position.first.absoluteValue + position.second.absoluteValue
}