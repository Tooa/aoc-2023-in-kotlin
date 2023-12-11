package day11

import BooleanGrid
import CoordinateL
import asGrid
import columnsValues
import combinations
import findCoordinatesWithValue
import manhattanDistanceTo
import println
import readInput
import rowsValues

private typealias GalaxyImage = BooleanGrid

private fun GalaxyImage.sumShortestDistance(weight: Long): Long {
    val galaxies = findCoordinatesWithValue(true)

    val emptyIndex = { l: List<List<Boolean>> -> l.withIndex().filter { it.value.all { g -> !g } }.map { it.index }.toSet() }
    val emptyRowIndices = emptyIndex(rowsValues)
    val emptyColumnIndices = emptyIndex(columnsValues)

    return galaxies.map { galaxy ->
        // Move galaxy coordinates further away that are affected by space extended rows/columns
        CoordinateL(
            emptyColumnIndices.count { it < galaxy.x } * weight + galaxy.x,
            emptyRowIndices.count { it < galaxy.y } * weight + galaxy.y
        )
    }
        .combinations(2)
        .sumOf { (a, b) -> a manhattanDistanceTo b }
}


fun main() {

    fun part1(input: List<String>): Long {
        val galaxyImage: GalaxyImage = input.asGrid { it == '#' }
        return galaxyImage.sumShortestDistance(1)
    }

    fun part2(input: List<String>): Long {
        val galaxyImage: GalaxyImage = input.asGrid { it == '#' }
        return galaxyImage.sumShortestDistance(1000000 - 1)
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11", "Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("day11", "Day11")
    part1(input).println()

//   check(part2(testInput) == 8410L) // weight: 100 - 1
    part2(input).println()
}