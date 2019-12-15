package com.adventOfCode.day7

import java.io.File

const val debug = false

fun main(args: Array<String>) {

    val test1Input = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0".split(",").map { it.toInt() }
    val test1PhaseArray = "4,3,2,1,0".split(",").map { it.toInt() }
    println("Test1: ${runAmpArray(test1Input, test1PhaseArray, 0) == 43210}")

    val test2Input = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0".split(",").map { it.toInt() }
    val test2PhaseArray = "0,1,2,3,4".split(",").map { it.toInt() }
    println("Test2: ${runAmpArray(test2Input, test2PhaseArray, 0) == 54321}")

    val test3Input = "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0".split(",").map { it.toInt() }
    val test3PhaseArray = "1,0,4,3,2".split(",").map { it.toInt() }
    println("Test3: ${runAmpArray(test3Input, test3PhaseArray, 0) == 65210}")

    var fileName = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day7\\input.txt"
    var program = File(fileName).readText().split(",").map { it.toInt() }

    //Find all permutations
    var permutations = mutableListOf<String>()
    findPermutations(permutations, "", "01234")

    //Try them all and remember the highest result
    var thrusterSignal = 0
    for (test in permutations) {
        println(test)
        val phaseInput = test.split(",").map { it.toInt() }
        val testResult = runAmpArray(program.toMutableList(), phaseInput, 0)

        if (testResult > thrusterSignal) {
            thrusterSignal = testResult
        }
    }
    
    println("Part 1: $thrusterSignal")

}

private fun findPermutations(result: MutableList<String>, prefix: String, remaining: String) {
    when {
        remaining.isEmpty() -> { result.add(prefix.substring(1)) }
        else -> {
            remaining.forEachIndexed { index, element ->
                findPermutations(result, "$prefix,$element", remaining.substring(0, index) + remaining.substring(index + 1))
            }

        }
    }
}

private fun runAmpArray(program: List<Int>, phaseArray: List<Int>, input: Int): Int {
    var output = input
    for (phase in phaseArray) {
        output = execute(program.toMutableList(), listOf(phase, output))
    }

    return output
}

fun execute(program: MutableList<Int>, input: List<Int>): Int {
    var pointer = 0
    var numOfInstructions = 4
    var result = -1
    var inputPointer = 0;

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
                program[parameter] = input[inputPointer]
                inputPointer++
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