package com.traintycoon3d.trains

data class Train(var x: Float = 0f, var z: Float = 0f, var passengers: Int = 0)
class TrainAI { fun nextStop(train: Train) = train }
class TrainController(val trains: MutableList<Train> = mutableListOf(Train())) { fun update(delta: Float) { trains.forEach { it.x += delta } } }
