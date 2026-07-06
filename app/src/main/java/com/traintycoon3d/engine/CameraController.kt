package com.traintycoon3d.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3

class CameraController(private val camera: PerspectiveCamera) {
    private val target = Vector3.Zero.cpy()
    fun update(delta: Float) {
        val speed = 12f * delta
        if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.translate(0f, 0f, -speed)
        if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.translate(0f, 0f, speed)
        if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.translate(-speed, 0f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.translate(speed, 0f, 0f)
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) camera.rotateAround(target, Vector3.Y, -Gdx.input.deltaX * 0.35f)
        camera.update()
    }
    fun zoom(amount: Float) { camera.translate(camera.direction.cpy().scl(amount)); camera.update() }
}
