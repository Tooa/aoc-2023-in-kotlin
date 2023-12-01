fun main() {
    fun part1(input: List<String>): Int {
        fun calibrationValue(line: String): Int {
            val numbers = line.filter(Char::isDigit)

            val sum = "${numbers.first()}${numbers.last()}"
            return sum.toInt()
        }

        return input.sumOf(::calibrationValue)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
