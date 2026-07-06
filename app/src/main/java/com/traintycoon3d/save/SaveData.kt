package com.traintycoon3d.save

import com.google.gson.Gson
import com.traintycoon3d.buildings.Building
import com.traintycoon3d.trains.Train

data class SaveData(val money: Int, val buildings: List<Building>, val trains: List<Train>)
class SaveManager(private val gson: Gson = Gson()) { fun encode(data: SaveData): String = gson.toJson(data); fun decode(json: String): SaveData = gson.fromJson(json, SaveData::class.java) }
