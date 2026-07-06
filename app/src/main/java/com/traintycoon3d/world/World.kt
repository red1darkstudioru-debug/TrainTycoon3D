package com.traintycoon3d.world

data class Tile(val x: Int, val z: Int, var occupiedBy: String? = null)
class Grid(val width: Int = 32, val height: Int = 32) {
    private val tiles = Array(width) { x -> Array(height) { z -> Tile(x, z) } }
    fun tile(x: Int, z: Int) = tiles.getOrNull(x)?.getOrNull(z)
}
class World(val grid: Grid = Grid())
