package day02

import arrow.core.fold
import println
import readInput

private typealias Game = String

private enum class Cube(private val color: String) {
    RED("red"), GREEN("green"), BLUE("blue");

    companion object {
        private val map = entries.associateBy { it.color }
        infix fun from(value: String) = map[value] ?: throw IllegalArgumentException("Unknown cube")
    }
}
private typealias Subset = Map<Cube, Int>
private fun parseSubsets(line: String): List<Subset> = line.split("; ").map(::parseSubset)
private fun parseSubset(line: String): Subset = line.split(", ").associate(::parseNumCube)

private fun parseNumCube(line: String): Pair<Cube, Int> {
    val split = line.split(" ")
    check(split.size == 2)

    return (Cube from split[1]) to split[0].toInt()
}

private fun Game.subsets(): List<Subset> = parseSubsets(substringAfter(": "))
private fun Game.id(): Int = substringAfter("Game ").substringBefore(":").toInt()

fun main() {

    fun part1(games: List<Game>, bag: Subset): Int {
        fun subsetIsPossible(subset: Subset): Boolean = subset.entries
            .all { (cube, count) ->
                count <= checkNotNull(bag[cube])
            }

        fun gameIsPossible(game: Game): Boolean {
            val subsets = game.subsets()
            return subsets.all(::subsetIsPossible)
        }
        return games.filter(::gameIsPossible).sumOf(Game::id)
    }

    fun part2(games: List<Game>): Long {
        fun fewestCubes(game: Game): Subset {
            // Optimization:
            return Cube.entries.associateWith { cube -> game.subsets().maxOf { subset -> subset[cube] ?: 0 } }
//            val minimumCubes = mutableMapOf(Cube.RED to 0, Cube.GREEN to 0, Cube.BLUE to 0)
//            game.subsets().forEach { subset ->
//                subset.entries.forEach { (cube, count) ->
//                    if (checkNotNull(minimumCubes[cube]) < count) minimumCubes[cube] = count
//                }
//            }
//            return minimumCubes
        }

       return games.sumOf { fewestCubes(it).fold(1L) { acc, (_, count) -> count * acc} }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02","Day02_test")
    check(part1(testInput, mapOf(Cube.RED to 12, Cube.GREEN to 13, Cube.BLUE to 14)) == 8)

    val input = readInput("day02","Day02")
    val resultPart1 = part1(input, mapOf(Cube.RED to 12, Cube.GREEN to 13, Cube.BLUE to 14))
    resultPart1.println() // Solution: 1734

    val testInputPart2 = readInput("day02","Day02_test")
    check(part2(testInputPart2) == 2286L)

    val resultPart2 = part2(input)
    resultPart2.println() // Solution: 70387
}