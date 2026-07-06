package com.traintycoon3d.economy

class MoneySystem(var money: Int = 1000) { fun spend(cost: Int) = (money >= cost).also { if (it) money -= cost }; fun earn(value: Int) { money += value } }
class EconomyManager(val money: MoneySystem = MoneySystem()) { fun tick(passengers: Int) = money.earn(passengers * 2 - 1) }
