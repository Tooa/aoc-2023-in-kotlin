package day09

import println
import readInput

typealias History = List<Long>

//private fun History.last(index: Int) = get(size + index) // Python-like negative index access

private fun parseHistories(lines: List<String>): List<History> =
    lines.map { line -> line.split(" ").map { num -> num.toLong() } }

private fun History.differenceAtStep(): List<Long> = windowed(2, 1) { (l, r) -> r - l }

private fun History.predictNextValue(): Long {
    fun predict(acc: History): Long {
        return if(acc.all { it == 0L }) 0
        else acc.last() + predict(acc.differenceAtStep())
    }
    return predict(this)
}

private fun History.predictPreviousValue(): Long {
    fun predict(acc: History): Long {
        return if(acc.all { it == 0L }) 0
        else acc.first() - predict(acc.differenceAtStep())
    }
    return predict(this)
}

fun main() {

    fun part1(lines: List<String>): Long = parseHistories(lines).sumOf(History::predictNextValue)

    fun part2(lines: List<String>): Long = parseHistories(lines).sumOf(History::predictPreviousValue)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09", "Day09_test")
    check(part1(testInput) == 114L)

    val input = readInput("day09", "Day09")
    part1(input).println()

    val testInputPart2 = readInput("day09", "Day09_test")
    check(part2(testInputPart2) == 2L)

    part2(input).println()
}