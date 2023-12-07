package day07

import println
import readInput

enum class Card(val representation: Char, val score: Int, val jokerScore: Int = score) {
    A('A',14),
    K('K',13),
    Q('Q', 12),
    J('J', 11, 1),
    T('T', 10),
    NINE('9', 9),
    EIGHT('8', 8),
    SEVEN('7', 7),
    SIX('6', 6),
    FIVE('5', 5),
    FOUR('4', 4),
    THREE('3', 3),
    TWO('2', 2);

    companion object {
        private val map = Card.entries.associateBy { it.representation }
        infix fun from(value: Char) = map[value] ?: throw IllegalArgumentException("Unknown Card")
    }
}

// Implements comparable by default with natural order
enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_KIND,
    FULL_HOUSE,
    FOUR_KIND,
    FIVE_KIND
}

sealed interface Hand: Comparable<Hand> {
    val cards: List<Card>
    val bid: Int

    fun type(): HandType

    fun score(): Int

    fun typeFromFrequencyMap(frequencyMap: Map<Card, Int>): HandType {
        return when(frequencyMap.size) {
            5 -> HandType.HIGH_CARD
            4 -> HandType.ONE_PAIR
            3 -> if(frequencyMap.containsValue(3)) HandType.THREE_KIND else HandType.TWO_PAIR
            2 -> if(frequencyMap.containsValue(4)) HandType.FOUR_KIND else HandType.FULL_HOUSE
            1 -> HandType.FIVE_KIND
            else -> throw IllegalArgumentException("no valid hand")
        }
    }

    override fun compareTo(other: Hand): Int {
        // Compare ordinal enum values
        val typeComparison = type().compareTo(other.type())
        return if(typeComparison != 0) {
            typeComparison
        } else {
            // Tie, do card by card comparison via score value
            score().compareTo(other.score())
        }
    }
}

data class ClassicHand(override val cards: List<Card>, override val bid: Int) : Hand {

    override fun type(): HandType {
        val frequencyMap = cards.groupingBy { it }.eachCount()
        return typeFromFrequencyMap(frequencyMap)
    }

    override fun score(): Int = cards.map(Card::score).reduce { acc, score -> acc * 15 + score }
}

private data class JokerHand(override val cards: List<Card>, override val bid: Int) : Hand{

    override fun type(): HandType {
        val frequencyMap = cards.groupingBy { it }.eachCount()

        val jokerCount = frequencyMap[Card.J] ?: 0
        val frequencyMapWithoutJoker = frequencyMap.toMutableMap()
        if (jokerCount < 5) {
            frequencyMapWithoutJoker.remove(Card.J)
            val (mostFreqCard, frequency) = frequencyMapWithoutJoker.maxBy { it.value }
            frequencyMapWithoutJoker[mostFreqCard] = jokerCount + frequency
        }
        check(frequencyMapWithoutJoker.values.sum() == 5)

        return typeFromFrequencyMap(frequencyMapWithoutJoker)
    }

    override fun score(): Int = cards.map(Card::jokerScore).reduce { acc, jokerScore -> acc * 15 + jokerScore }

}
private fun List<Hand>.totalWinnings(): Int = sorted().withIndex().sumOf { (index, hand) -> hand.bid * (index + 1) }

private fun parseClassicHand(hand: String, bid: Int): Hand = ClassicHand(hand.map { Card from it }, bid)
private fun parseJokerHand(hand: String, bid: Int): Hand = JokerHand(hand.map { Card from it }, bid)

private fun parseListOfHands(lines: List<String>, withJoker: Boolean = false): List<Hand> =
    lines.map {
        val elements = it.split(" ")
        require(elements.size == 2)

        val parser = if(withJoker) ::parseJokerHand else ::parseClassicHand
        parser(elements[0], elements[1].toInt())
    }

fun main() {

    fun part1(lines: List<String>): Int = parseListOfHands(lines).totalWinnings()

    fun part2(lines: List<String>): Int = parseListOfHands(lines, withJoker = true).totalWinnings()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07", "Day07_test")
    check(part1(testInput) == 6440)

    val input = readInput("day07", "Day07")
    part1(input).println()

    check(part2(testInput) == 5905)
    part2(input).println()
}