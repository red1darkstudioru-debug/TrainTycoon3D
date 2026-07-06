package com.traintycoon3d.buildings

data class Building(val id: String, val x: Int, val z: Int)
class BuildingManager { val buildings = mutableListOf<Building>(); fun place(id: String, x: Int, z: Int) = buildings.add(Building(id, x, z)) }
