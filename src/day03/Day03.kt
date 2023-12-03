package day03

import Coordinate
import neighbors
import println
import readInput

private fun String.findNumbers() = "\\d+".toRegex().findAll(this)

fun main() {

    fun findSymbolsWithPartNumbers(engine: List<String>): List<Pair<Char, List<Int>>> {

        val partNumberCandidates: Map<Coordinate, Pair<Coordinate, Int>> = engine.flatMapIndexed { y , engineLine ->
            engineLine.findNumbers().flatMap { number ->
                val numberPositionsInGrid = number.range

                numberPositionsInGrid.map { x ->
                    val startCoordinateX = numberPositionsInGrid.first
                    val partNumberCandidate = Coordinate(startCoordinateX, y) to number.value.toInt()

                    Coordinate(x, y) to partNumberCandidate
                }
            }
        }.toMap()

        val symbolsWithPartNumbers: List<Pair<Char, List<Int>>> = engine.flatMapIndexed { y, engineLine ->
            engineLine.mapIndexedNotNull { x, character ->
                if(character.isDigit() || character == '.') {
                    null
                } else {
                    val neighbors = Coordinate(x, y).neighbors()
                    val partNumbers = neighbors
                        .mapNotNull { partNumberCandidates[it] }
                        // Remove duplicates from partNumberCandidates generation
                        .toSet()
                        .map { (_, partNumber) -> partNumber }

                    character to partNumbers
                }
            }
        }

        return symbolsWithPartNumbers
    }

    fun part1(engine: List<String>): Int {
        return findSymbolsWithPartNumbers(engine).sumOf { (_, partNumbers) -> partNumbers.sum() }
    }

    fun part2(engine: List<String>): Long {
        fun isGear(symbolWithPartNumbers: Pair<Char, List<Int>>): Boolean {
            val (symbol, partNumbers) = symbolWithPartNumbers

            return partNumbers.size == 2 && symbol == '*'
        }

        return findSymbolsWithPartNumbers(engine)
            .filter(::isGear)
            .sumOf { (_, partNumbers) ->
                partNumbers.fold(1L) { acc, partNumber -> acc * partNumber }
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03", "Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("day03", "Day03")
    part1(input).println() // Solution: 559667

    val testInputPart2 = readInput("day03", "Day03_test")
    check(part2(testInputPart2) == 467835L)

    part2(input).println() // Solution: 86841457
}