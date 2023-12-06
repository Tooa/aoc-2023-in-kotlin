package day06

import findNumbers
import println
import readInput

data class Race(val timeInMs: Long, val distanceRecord: Long)

private fun String.toListOfLong() = findNumbers().map { it.value.toLong() }.toList()

private fun parseRaceTable(lines: List<String>): Pair<List<Long>, List<Long>> {
    require(lines.size == 2)

    val times = lines[0].toListOfLong()
    val distances = lines[1].toListOfLong()

    return times to distances
}

private fun parseRaceTableAsMultiple(lines: List<String>): List<Race> {
    val (times, distances) = parseRaceTable(lines)

    require(times.size == distances.size)

    return times.zip(distances).map { (t, d) -> Race(t, d) }
}

private fun parseRaceTableAsSingle(lines: List<String>): Race {
    val (times, distances) = parseRaceTable(lines)
    return Race(
        times.joinToString(separator = "").toLong(),
        distances.joinToString(separator = "").toLong()
    )
}

fun main() {

    fun Race.numWinningOptions(): Long {
        val travelSpeedPerMs = 1
        return (1..timeInMs)
            .fold(0L) { acc, buttonPressedInMs ->
                val timeLeftInRaceInMs = timeInMs - buttonPressedInMs
                val travelDistance = buttonPressedInMs * timeLeftInRaceInMs * travelSpeedPerMs

                if(travelDistance > distanceRecord) acc + 1L else acc + 0L
            }
    }

    fun part1(lines: List<String>): Long {
        val races = parseRaceTableAsMultiple(lines)
        return races
            .map(Race::numWinningOptions)
            .fold(1L) { acc, i -> acc * i }
    }

    fun part2(lines: List<String>): Long {
        val race = parseRaceTableAsSingle(lines)
        return race.numWinningOptions()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06", "Day06_test")
    check(part1(testInput) == 288L)

    val input = readInput("day06", "Day06")
    part1(input).println() // Solution: 160816

    check(part2(testInput) == 71503L)
    part2(input).println() // Solution: 46561107
}