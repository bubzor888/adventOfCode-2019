package com.adventOfCode.day5

import java.io.File

const val debug = false

fun main(args: Array<String>) {
    var fileName = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day5\\input.txt"
    var program = File(fileName).readText().split(",").map { it.toInt() }


    val test1Input = "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9".split(",").map { it.toInt() }
    println("Test1A: ${execute(test1Input.toMutableList(), 0) == 0}")
    println("Test1B: ${execute(test1Input.toMutableList(), 1) == 1}")

    val test2Input = "3,3,1105,-1,9,1101,0,0,12,4,12,99,1".split(",").map { it.toInt() }
    println("Test2A: ${execute(test1Input.toMutableList(), 0) == 0}")
    println("Test2B: ${execute(test1Input.toMutableList(), 1) == 1}")

    val test3Input = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".split(",").map { it.toInt() }
    for (i in 1..15) {
        val testResult = execute(test3Input.toMutableList(), i)
        if (i < 8) {
            println("Test3.$i: Result $testResult : ${testResult == 999}")
        } else if (i == 8) {
            println("Test3.$i: Result $testResult : ${testResult == 1000}")
        } else {
            println("Test3.$i: Result $testResult : ${testResult == 1001}")
        }
    }

    println("Part 1: ${execute(program.toMutableList(), 1)}")

    println("Part 2: ${execute(program.toMutableList(), 5)}")
}

fun execute(program: MutableList<Int>, input: Int): Int {
    var pointer = 0
    var numOfInstructions = 4
    var result = -1

    if (debug) {
        println("Initial State: $program")
    }

    execute@ while (pointer < program.size) {
        val operation = program.elementAt(pointer)

        //The opcode is the 2 right most digits
        val opCode = operation % 100

        var isJump = false

        if (debug) {
            println("Operation: $operation, opCode: $opCode")
        }

        if (operation == 99) {
            //println("Success: $program[0]")
            break@execute
        }

        val modeTypes = (operation / 100).toString().reversed()
        if (debug) {
            println("ModeInstructions: $modeTypes")
        }

        when (opCode) {
            1 -> {
                val parameter1 = getParameter(modeTypes, 0, program, pointer+1)
                val parameter2 = getParameter(modeTypes, 1, program, pointer+2)
                //write parameters are always parameter mode
                val parameter3 = program.elementAt(pointer+3)
                program[parameter3] = parameter1 + parameter2
                numOfInstructions = 4
                if (debug) {
                    println("Operation 1 - Add: Setting $parameter3 to $parameter1 + $parameter2")
                }
            }
            2 -> {
                val parameter1 = getParameter(modeTypes, 0, program, pointer+1)
                val parameter2 = getParameter(modeTypes, 1, program, pointer+2)
                //write parameters are always parameter mode
                val parameter3 = program.elementAt(pointer+3)
                program[parameter3] = parameter1 * parameter2
                numOfInstructions = 4
                if (debug) {
                    println("Operation 2 - Multiply: Setting $parameter3 to $parameter1 * $parameter2")
                }
            }
            3 -> {
                val parameter = program[pointer+1]
                program[parameter] = input
                numOfInstructions = 2
                if (debug) {
                    println("Operation 3 - Save: Setting $parameter to $input")
                }
            }
            4 -> {
                result = getParameter(modeTypes,0, program, pointer+1)
                numOfInstructions = 2
                if (debug) {
                    println("Test output from position ${pointer + 1}: $result")
                    if (result != 0) {
                        println("Failure, aborting")
                        break@execute
                    }
                }
            }
            5 -> {
                val parameter1 = getParameter(modeTypes,0, program, pointer+1)
                if (parameter1 != 0) {
                    val parameter2 = getParameter(modeTypes,1, program, pointer+2)
                    pointer = parameter2
                    isJump = true
                    if (debug) {
                        println("Operation 5 - Jump if True: $parameter1 is non-zero. Successful jump to position $pointer")
                    }
                } else {
                    numOfInstructions = 3
                    if (debug) {
                        println("Operation 5 - Jump if True: $parameter1 is zero. No jump")
                    }
                }
            }
            6 -> {
                val parameter1 = getParameter(modeTypes,0, program, pointer+1)
                if (parameter1 == 0) {
                    val parameter2 = getParameter(modeTypes,1, program, pointer+2)
                    pointer = parameter2
                    isJump = true
                    if (debug) {
                        println("Operation 6 - Jump if false: $parameter1 is zero. Successful jump to position $pointer")
                    }
                } else {
                    numOfInstructions = 3
                    if (debug) {
                        println("Operation 6 - Jump if false: $parameter1 is non-zero. No jump")
                    }
                }
            }
            7 -> {
                val parameter1 = getParameter(modeTypes, 0, program, pointer+1)
                val parameter2 = getParameter(modeTypes, 1, program, pointer+2)
                //write parameters are always parameter mode
                val parameter3 = program.elementAt(pointer+3)
                numOfInstructions = 4
                if (parameter1 < parameter2) {
                    program[parameter3] = 1
                    if (debug) {
                        println("Operation 7 - Less Than: $parameter1 < $parameter2: Setting position $parameter3 to 1")
                    }
                } else {
                    program[parameter3] = 0
                    if (debug) {
                        println("Operation 7 - Less Than: $parameter1 >= $parameter2: Setting position $parameter3 to 0")
                    }
                }
            }
            8 -> {
                val parameter1 = getParameter(modeTypes, 0, program, pointer+1)
                val parameter2 = getParameter(modeTypes, 1, program, pointer+2)
                //write parameters are always parameter mode
                val parameter3 = program.elementAt(pointer+3)
                numOfInstructions = 4
                if (parameter1 == parameter2) {
                    program[parameter3] = 1
                    if (debug) {
                        println("Operation 8 - Equals: $parameter1 == $parameter2: Setting position $parameter3 to 1")
                    }
                } else {
                    program[parameter3] = 0
                    if (debug) {
                        println("Operation 8 - Equals: $parameter1 != $parameter2: Setting position $parameter3 to 0")
                    }
                }
            }
            else -> {
                println("Unknown operation found at $pointer. Terminating")
                break@execute
            }
        }

        if (debug) {
            println("Program State: $program")
            println("Pointer: $pointer -> ${pointer+numOfInstructions}")
        }

        if (!isJump) {
            pointer += numOfInstructions
        }
    }

    return result
}

private fun getParameter(modeTypes: String, modeIndex: Int, program: List<Int>, pointer: Int): Int {
    return when(modeTypes.elementAtOrElse(modeIndex) { '0' }.toString().toInt()) {
        1 -> {
            if (debug) {
                println("getParameter for mode 1: program[$pointer] = ${program[pointer]}")
            }
            program[pointer]
        }
        else -> {
            if (debug) {
                println("getParameter for mode 0: program[$pointer] = ${program[pointer]} -> program[${program[pointer]}] = ${program[program[pointer]]}")
            }
            program[program[pointer]]
        }
    }
}