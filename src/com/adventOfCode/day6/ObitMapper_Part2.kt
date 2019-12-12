package com.adventOfCode.day6

import java.io.File

private var debug = true

fun main(args: Array<String>) {
    var testFile = "C:\\Users\\bubzo\\git\\AdventOfCode_2019\\src\\com\\adventOfCode\\day6\\testInput2.txt"
    var testInput = File(testFile).readLines().map { it.split(")") }
    var testOutput = execute(testInput)
    if (testOutput == 4) {
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

    //First, build a list of the path we need to take to each object
    var sanPath = mutableListOf<String>()
    var youPath = mutableListOf<String>()

    var needle = "YOU"
    do {
        needle = findParent(spaceObjects, needle)
        youPath.add(needle)
    } while(needle != "COM" && needle != "NOT_FOUND")
    //Reverse the order so we can start with COM
    youPath.reverse()

    needle = "SAN"
    do {
        needle = findParent(spaceObjects, needle)
        sanPath.add(needle)
    } while(needle != "COM" && needle != "NOT_FOUND")
    sanPath.reverse()

    if (debug) {
        println("SAN path: $sanPath")
        println("YOU path: $youPath")
    }

    //Next figure out where the 2 paths diverge
    var divergeIndex = -1
    for ((index, value) in sanPath.withIndex()) {
        if (value != youPath[index]) {
            //Diverged, go back 1
            divergeIndex = index
            if (debug) {
                println("Diverged at index $divergeIndex")
            }
            break
        }
    }

    //So the distance should be the number of nodes after they diverge
    return (sanPath.size - divergeIndex) + (youPath.size - divergeIndex)
}

private fun findParent(spaceObjects: Map<String, List<String>>, item: String): String {
    spaceObjects.forEach {
        if (it.value.contains(item)) {
            return it.key
        }
    }
    return "NOT_FOUND"
}