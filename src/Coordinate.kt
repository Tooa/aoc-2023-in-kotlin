

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