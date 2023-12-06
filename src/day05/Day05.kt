package day05

import println
import readInputAsText

data class MapEntry(val dstRangeStart: Long, val srcRangeStart: Long, val rangeLength: Long)

private fun MapEntry.inRange(src: Long): Boolean = src in srcRangeStart..<(srcRangeStart + rangeLength)
private fun MapEntry.inRangeReverse(dst: Long): Boolean = dst in dstRangeStart ..(dstRangeStart + rangeLength)

private typealias Almanac = String

private fun Almanac.seeds(): List<Long> =
    substringBefore("\n\n")
    .substringAfter(":")
    .split(" ")
    .filter { it.isNotEmpty() }
    .map { it.toLong() }

private fun Almanac.mapEntries(): List<List<MapEntry>> =
    split("\n\n").drop(1).map { block ->
        block.lines().drop(1).map { line ->
            val (dst, src, range) = line
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toLong() }

            MapEntry(dst, src, range)
        }
    }

fun main() {
    fun part1(almanac: Almanac): Long {
        fun getDstValue(num: Long, mapEntries: List<MapEntry>): Long = mapEntries
                .find { it.inRange(num) }
                ?.let { (dst, src, _) -> num - src + dst }
                // map identity when no range mapping exists for value
                ?: num

        return almanac.seeds().minOf { seed ->
            almanac
                .mapEntries()
                .fold(seed) { acc, blockAsList -> getDstValue(acc, blockAsList) }
        }
    }

    fun part2(almanac: Almanac): Long {
        fun getDstValueReverse(num: Long, mapEntries: List<MapEntry>): Long = mapEntries
            .find { it.inRangeReverse(num) }
            ?.let { (dst, src, _) -> src + (num - dst) }
            // map identity when no range mapping exists for value
            ?: num

        val seedAsRanges = almanac.seeds()
            .chunked(2)
            .map { (start, len) -> start..(start + len) }

        val reversedMapEntries = almanac.mapEntries().reversed()

        // Start at location 0 and find first valid location by reversing the mappings and then doing a reverse lookup
        return generateSequence(0L) { it + 1 }.filter { location ->
            val seed = reversedMapEntries.fold(location) { acc, blockAsList -> getDstValueReverse(acc, blockAsList)}

            seedAsRanges
                .any { seedRange -> seed in seedRange }
        }.first()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("day05", "Day05_test")
    check(part1(testInput) == 35L)

    val input = readInputAsText("day05", "Day05")

    val testInputPart2 = readInputAsText("day05", "Day05_test")
    check(part2(testInputPart2) == 46L)

    part1(input).println() // Solution: 31599214
    part2(input).println() // Solution: 20358599
}