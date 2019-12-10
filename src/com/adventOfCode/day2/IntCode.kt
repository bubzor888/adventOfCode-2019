package com.adventOfCode.day2

import java.io.File

fun main(args: Array<String>) {
    var fileName = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day2\\input.txt"
    var program = File(fileName).readText().split(",").map { it.toInt() }

    println("Result: ${execute(program.toMutableList(), 12, 2)}")

    //Now we're trying for 19690720
    var found = false;
    for (i in 1..100) {
        for (j in 1..100) {
            val result = execute(program.toMutableList(), i, j)
            if (result == 19690720) {
                println("Noun: $i, Verb: $j, 100 * i + j: ${100*i+j}")
                found = true
                break;
            }
        }
    }

    if (!found) {
        println("Not found!")
    }
}

fun execute(program: MutableList<Int>, input1: Int, input2: Int): Int {
    var pointer = 0

    //Set inputs
    program[1] = input1
    program[2] = input2

    var numOfinstructions = 4

    execute@ while (pointer < program.size) {
        val operation = program.elementAt(pointer)

        if (operation == 99) {
            //println("Success: $program[0]")
            break@execute
        }

        val parameter1 = program.elementAt(pointer+1)
        val parameter2 = program.elementAt(pointer+2)

        when (operation) {
            1 -> program[program.elementAt(pointer+3)] = program.elementAt(parameter1) + program.elementAt(parameter2)
            2 -> program[program.elementAt(pointer+3)] = program.elementAt(parameter1) * program.elementAt(parameter2)
            else -> {
                println("Unknown operation found at $pointer. Terminating")
                break@execute
            }
        }

//        println("Program State: $program")
        pointer+=numOfinstructions
    }

    return program[0]
}