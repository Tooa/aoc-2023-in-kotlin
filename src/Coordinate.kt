

typealias Polygon = List<Coordinate>

data class Coordinate(val x: Int, val y: Int)

// Includes diagonal neighbors
fun Coordinate.neighbors(): Set<Coordinate> = setOf(
    Coordinate(x - 1, y - 1),
    Coordinate(x - 1, y),
    Coordinate(x - 1, y + 1),
    Coordinate(x, y - 1),
    Coordinate(x, y),
    Coordinate(x, y + 1),
    Coordinate(x + 1, y - 1),
    Coordinate(x + 1, y),
    Coordinate(x + 1, y + 1)
)

fun Coordinate.move(
    moveTo: Direction,
) = Coordinate(
    x = x + when (moveTo) {
        Direction.LEFT -> -1
        Direction.RIGHT -> 1
        Direction.UP, Direction.DOWN -> 0
    },
    y = y + when (moveTo) {
        Direction.UP -> -1
        Direction.DOWN -> 1
        Direction.LEFT, Direction.RIGHT -> 0
    }
)

// Ray Casting Algorithm - determine whether a point is inside a complex polygon
// https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
fun Coordinate.isInPolygon(polygon: Polygon): Boolean {
    fun checkIntersection(c1: Coordinate, c2: Coordinate): Boolean
        = c2.y > y != c1.y > y && x < (c1.x - c2.x) * (y - c2.y) / (c1.y - c2.y) + c2.x

    var rayIntersectionCount = 0
    var previous = polygon.last()

    polygon.forEach { current ->
        if (checkIntersection(current, previous)) rayIntersectionCount++
        previous = current
    }
    // If the point is on the outside of the polygon the ray will intersect its edge an even number of times.
    // If the point is on the inside of the polygon then it will intersect the edge an odd number of times
    return !rayIntersectionCount.isEven()
}