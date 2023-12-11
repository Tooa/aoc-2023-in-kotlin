import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: String, name: String) = Path("src/$day/$name.txt").readLines()
fun readInputAsText(day: String, name: String) = Path("src/$day/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun String.findNumbers(): Sequence<MatchResult> = "\\d+".toRegex().findAll(this)

fun Int.isEven() = this % 2 == 0

fun List<Long>.lcm(): Long = reduce { a, b -> lcm(a, b) }

fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)


// r-length tuples, in sorted order, no repeated elements
// listOf('A', 'B', 'C', 'D').combinations(2) --> AB AC AD BC BD CD
fun <T> List<T>.combinations(r: Int = size): Sequence<List<T>> = indices
    .toList()
    .permutations(r)
    .filter { it.sorted() == it }
    .map { c -> c.map { this[it] } }

// r-length tuples, all possible orderings, no repeated elements
// listOf('A', 'B', 'C', 'D').permutations(2) --> AB AC AD BA BC BD CA CB CD DA DB DC
fun <T> List<T>.permutations(r: Int = size): Sequence<List<T>> = sequence {
    if (r > size || isEmpty()) return@sequence

    val ind = indices.toMutableList()
    val cyc = (size downTo size - r).toMutableList()
    yield(take(r + 1))

    while (true) {
        for (i in r - 1 downTo 0) {
            if (--cyc[i] == 0) {
                ind.add(ind.removeAt(i))
                cyc[i] = size - i
            } else {
                Collections.swap(ind, i, size - cyc[i])
                yield(slice(ind.take(r)))
                break
            }

            if (i == 0) return@sequence
        }
    }
}