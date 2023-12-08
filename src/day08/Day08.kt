package day08

import lcm
import println
import readInput

typealias LeftRightInstruction = String

data class Node(val left: String, val right: String)
data class Document(val instructions: LeftRightInstruction, val network: Map<String, Node>)

private fun parseDocument(lines: List<String>): Document {
    val lr = lines.first()
    val network = lines.drop(2).associate { l ->
        val src = l.substringBefore(" =")
        val (left, right) = l
            .substringAfter("= ")
            .split(", ")

        src to Node(left.removePrefix("("), right.removeSuffix(")"))
    }

    return Document(lr, network)
}

private tailrec fun Document.steps(currentCoordinate: String, acc: Long, recursionAnchor: (coordinate: String) -> Boolean): Long {
    if(recursionAnchor(currentCoordinate)) return acc
    else {
        val (l, r) = network[currentCoordinate] ?: error("lost in the desert")
        val nextLR = instructions[acc.mod(instructions.length)]
        return steps(if(nextLR == 'L') l else r, acc + 1, recursionAnchor)
    }
}

private fun Document.part1(start: String = "AAA", end: String = "ZZZ"): Long =
    steps(start, 0L) { coordinate -> coordinate == end }

private fun Document.part2(): Long {
    val startCoordinates = network.filterKeys { it.last() == 'A' }

    return startCoordinates.keys
        .map { coordinate -> steps(coordinate, 0) { it.endsWith("Z") } }
        .lcm()
}

fun main() {

    fun part1(lines: List<String>): Long = parseDocument(lines).part1()

    // LCM Problem:
    // After brute-forcing part 2, I discovered a rule by visually inspecting the given input in more detail
    // It seems the inputs are constructed in a way that each start hits a target node in a cyclic manner
    // i.e. each start hits 'Z' at every _multiple_ of the steps length, so the answer is just the lcm of the steps length
    // The solution then becomes lcm(steps to first 'Z' for each path)
    fun part2(lines: List<String>): Long = parseDocument(lines).part2()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08", "Day08_test")
    check(part1(testInput) == 2L)

    val testInput2 = readInput("day08", "Day08_test2")
    check(part1(testInput2) == 6L)

    val input = readInput("day08", "Day08")
    part1(input).println()
    part2(input).println()
}