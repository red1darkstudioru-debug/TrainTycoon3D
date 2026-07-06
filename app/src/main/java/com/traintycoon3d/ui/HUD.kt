package com.traintycoon3d.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.ScreenViewport

class HUD(private val onBuild: () -> Unit, private val onDelete: () -> Unit) : Stage(ScreenViewport()) {
    private val skin = Skin().apply { add("default-font", com.badlogic.gdx.graphics.g2d.BitmapFont()) }
    private val moneyLabel = Label("$1000", Label.LabelStyle(skin.getFont("default-font"), com.badlogic.gdx.graphics.Color.WHITE))
    init {
        val table = Table(); table.setFillParent(true); table.top().left().pad(12f); addActor(table)
        listOf("Build" to onBuild, "Train" to {}, "Delete" to onDelete, "Pause" to {}).forEach { (text, action) ->
            val btn = TextButton(text, TextButton.TextButtonStyle(null, null, null, skin.getFont("default-font")))
            btn.addListener(object : com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() { override fun changed(event: ChangeEvent?, actor: com.badlogic.gdx.scenes.scene2d.Actor?) = action() })
            table.add(btn).pad(6f)
        }
        table.row(); table.add(moneyLabel).pad(6f)
    }
    fun setMoney(value: Int) { moneyLabel.setText("$$value") }
}
class MainMenu
class BuildMenu
