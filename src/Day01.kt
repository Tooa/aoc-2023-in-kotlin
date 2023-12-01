fun main() {
    // Alternative Solution:
    // We may use calibrationValue(line, textualDigitsToDigits.values) from part2
    fun part1(input: List<String>): Int {
        fun calibrationValue(line: String): Int {
            val numbers = line.filter(Char::isDigit)

            val sum = "${numbers.first()}${numbers.last()}"
            return sum.toInt()
        }

        return input.sumOf(::calibrationValue)
    }

    fun part2(input: List<String>): Int {
        val textualDigitsToDigits = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9"
        )

        fun calibrationValue(line: String, numbers: Collection<String>): Int {
            val (_, firstTextualNumber) = line.findAnyOf(numbers)!!
            val (_, secondTextualNumber) = line.findLastAnyOf(numbers)!!

            val firstNumber = textualDigitsToDigits[firstTextualNumber] ?: firstTextualNumber
            val lastNumber = textualDigitsToDigits[secondTextualNumber] ?: secondTextualNumber

            val sum = "${firstNumber}${lastNumber}"
            return sum.toInt()
        }

        val numbers = textualDigitsToDigits.keys + textualDigitsToDigits.values
        return input.sumOf { calibrationValue(it, numbers)}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val testInputPart2 = readInput("Day01_test_part2")
    check(part2(testInputPart2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
