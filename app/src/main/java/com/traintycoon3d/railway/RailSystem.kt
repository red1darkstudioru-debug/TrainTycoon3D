package com.traintycoon3d.railway

data class RailNode(val x: Int, val z: Int, val neighbors: MutableSet<RailNode> = mutableSetOf())
class RailSystem { val nodes = mutableListOf<RailNode>(); fun addNode(x: Int, z: Int) = RailNode(x, z).also(nodes::add) }
class RailBuilder(private val rails: RailSystem) { fun place(x: Int, z: Int) = rails.addNode(x, z) }
