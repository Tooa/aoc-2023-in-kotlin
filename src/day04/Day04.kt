package day04

import println
import readInput
import kotlin.math.pow

typealias ScratchCard = String

private fun String.findNumbers() = "\\d+".toRegex().findAll(this)

val ScratchCard.cardNumber: Int
    get() = """Card\s+(?<id>\d+):.+""".toRegex().matchEntire(this)!!.groups["id"]!!.value.toInt()

val ScratchCard.winningNumbers: Set<Int>
    get() {
        return substringAfter(": ")
            .substringBefore(" |")
            .findNumbers().map { it.value.toInt() }.asIterable()
            .toSet()
    }

val ScratchCard.numbers: Set<Int>
    get() {
        return substringAfter("| ")
            .findNumbers().map { it.value.toInt() }.asIterable()
            .toSet()
    }

val ScratchCard.matchingNumbers: Set<Int>
    get() = numbers intersect winningNumbers

fun main() {

    fun part1(cards: List<ScratchCard>): Int {
        return cards.sumOf { card ->
            2f.pow(card.matchingNumbers.size - 1).toInt()
        }
    }

    fun part2(cards: List<ScratchCard>): Int {
        fun recursivePart2(acc: List<ScratchCard>) : Int {
            return acc.sumOf { card ->
                val numMatches = card.matchingNumbers.size
                if (numMatches == 0) 1
                else {
                    val copies = (card.cardNumber..<card.cardNumber + numMatches).map {
                        cards[it]
                    }
                    1 + recursivePart2(copies)
                }
            }
        }
        return recursivePart2(cards)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04", "Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("day04", "Day04")
    part1(input).println() // Solution: 25651

    check(part2(testInput) == 30)
    part2(input).println() // Solution: 19499881
}