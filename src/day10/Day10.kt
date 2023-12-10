package day10

import Coordinate
import Direction
import Polygon
import isInPolygon
import move
import neighbors
import println
import readInputAsText

private typealias PipeMap = Map<Coordinate, Tile>

private enum class Tile(val symbol: Char, val connection: List<Direction>) {
    VERTICAL('|', listOf(Direction.UP, Direction.DOWN)),
    HORIZONTAL('-', listOf(Direction.LEFT, Direction.RIGHT)),
    NORTH_EAST('L', listOf(Direction.UP, Direction.RIGHT)),
    NORTH_WEST('J', listOf(Direction.UP, Direction.LEFT)),
    SOUTH_WEST('7', listOf(Direction.DOWN, Direction.LEFT)),
    SOUTH_EAST('F', listOf(Direction.DOWN, Direction.RIGHT)),
    GROUND('.', emptyList()),
    START('S', Direction.entries);

    companion object {
        private val symbolToTile = Tile.entries.associateBy { it.symbol }
        infix fun from(symbol: Char) = symbolToTile[symbol] ?: throw IllegalArgumentException("Unknown symbol")
    }
}

private fun PipeMap.startCoordinate(): Coordinate =
    entries.first { it.value == Tile.START }.key

private fun PipeMap.tile(coordinate: Coordinate): Tile = (this[coordinate] ?: Tile.GROUND)

// Identifies the coordinates of the loop
private fun PipeMap.findLoop(): List<Coordinate> {
    val start = startCoordinate()

    tailrec fun rec(path: List<Coordinate>): List<Coordinate>? {
        val coordinate = path.last()
        val nextCoordinate = tile(coordinate).connection
            .map { direction -> coordinate.move(direction) }
            .singleOrNull { c -> c != path.getOrNull(path.lastIndex - 1) }

        return when (nextCoordinate) {
            null -> null
            start -> path
            else -> rec(path + nextCoordinate)
        }
    }

    return start
        .neighbors()
        .firstNotNullOf { rec(listOf(start, it)) }
}

// Longest path is half the number of tiles in the loop
private fun PipeMap.findLongestPath(): Int = findLoop().size / 2

private fun PipeMap.numEnclosedTiles(): Int  {
    val loop: Polygon = findLoop()

    val minX = loop.minOf { it.x }
    val maxX = loop.maxOf { it.x }
    val minY = loop.minOf { it.y }
    val maxY = loop.maxOf { it.y }
    return (minX..maxX)
        .flatMap { x ->
            (minY..maxY).map { y -> Coordinate(x, y) }
        }
        .filter { it !in loop } // do not include loop coordinate borders
        .count { it.isInPolygon(loop) }
}

private fun parsePipeMap(input: String): PipeMap {
    return input.lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, symbol ->
            Coordinate(x, y) to (Tile from symbol)
        }
    }.toMap()
}

fun main() {

    fun part1(input: String): Int = parsePipeMap(input).findLongestPath()

    fun part2(input: String): Int = parsePipeMap(input).numEnclosedTiles()

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("day10", "Day10_test")
    check(part1(testInput) == 4)

    val testInput2 = readInputAsText("day10", "Day10_test2")
    check(part1(testInput2) == 8)


    val input = readInputAsText("day10", "Day10")
    part1(input).println()

    val testInputPart2 = readInputAsText("day10", "Day10_test_part2")
    check(part2(testInputPart2) == 4)

    part2(input).println()
}