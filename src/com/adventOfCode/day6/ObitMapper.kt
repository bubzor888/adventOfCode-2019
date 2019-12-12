package com.adventOfCode.day6

import com.adventOfCode.day2.execute
import java.io.File

//It's gross but we'll just use a global variable for this
//Be sure to reset it each execution
private var total = 0
private var debug = false

fun main(args: Array<String>) {
    var testFile = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day6\\testInput.txt"
    var testInput = File(testFile).readLines().map { it.split(")") }
    var testOutput = execute(testInput)
    if (testOutput == 42) {
        println("Test Passed")
    } else {
        println("Test Failed: $testOutput")
    }

    var fileName = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day6\\input.txt"
    var input = File(fileName).readLines().map { it.split(")") }

    println("Number of orbits: ${execute(input)}")
}

private fun execute(input: List<List<String>>): Int {
    var spaceObjects = mutableMapOf<String, MutableList<String>>()

    //Reset that global variable!
    total = 0

    //First, build a map of pointers
    input.forEach {
        //Add known pairs
        spaceObjects.getOrPut(it[0]) { mutableListOf() }.add(it[1])

        //Also add an edge placeholder since they will not appear as a pair
        spaceObjects.putIfAbsent(it[1], mutableListOf())
    }

    if (debug) {
        println("Graph: $spaceObjects")
    }

    //Now we can start with COM and "read" the graph
    parseGraph("COM",0, spaceObjects, spaceObjects.getOrDefault("COM", mutableListOf()))

    return total
}

private fun parseGraph(objName: String, depth: Int, spaceObjects: Map<String, List<String>>, orbits: List<String>) {
    total += depth
    if (debug) {
        println("Object $objName : DepthTotal: $total, Depth: $depth, List: $orbits")
    }

    orbits.forEach {
        parseGraph(it,depth + 1, spaceObjects, spaceObjects.getOrDefault(it, mutableListOf() ) )
    }
}