
typealias BooleanGrid = Grid<Boolean>

interface Plane {
    val width: Int
    val height: Int

    // Order: up to down
    fun column(index: Int): List<Coordinate> {
        require(index in xRange) { "column out of bounds: '$index' in width $width" }
        return yRange.map { Coordinate(index, it) }
    }

    // Order: left to right
    fun row(index: Int): List<Coordinate> {
        require(index in yRange) { "row out of bounds: '$index' in height $height" }
        return xRange.map { Coordinate(it, index) }
    }

    fun Coordinate.toIndex() = x + y * width
}

val Plane.xRange get() = 0..<width
val Plane.yRange get() = 0..<height
val Plane.points get() = xRange.flatMap { x -> yRange.map { y -> Coordinate(x, y) } }
operator fun Plane.contains(coordinate: Coordinate) = coordinate.x in 0..<width && coordinate.y in 0..<height

interface GridDef<T>: Plane, Iterable<T> {
    val elements: List<T>

    fun rowValues(index: Int) = row(index).map { this[it] }
    fun columnValues(index: Int) = column(index).map { this[it] }

    operator fun get(key: Coordinate) = if (key !in this) error("Invalid key '$key'") else elements[key.toIndex()]
}

val <T> GridDef<T>.rowsValues get() = yRange.map { rowValues(it) }
val <T> GridDef<T>.columnsValues get() = xRange.map { columnValues(it) }

inline fun <T> GridDef<T>.findCoordinates(check: (T) -> Boolean) = points.filter { check(this[it]) }
fun <T> GridDef<T>.findCoordinatesWithValue(value: T): List<Coordinate> = findCoordinates { it == value }

data class Grid<T>(
    override val width: Int,
    override val height: Int,
    override val elements: List<T>
) : List<T> by elements, GridDef<T> {

    init {
        require(size == width * height)
    }
}

inline fun <T> List<String>.asGrid(transform: (Char) -> T) = Grid(
    width = this[0].length,
    height = size,
    elements = flatMap { it.map(transform) }.toMutableList()
)