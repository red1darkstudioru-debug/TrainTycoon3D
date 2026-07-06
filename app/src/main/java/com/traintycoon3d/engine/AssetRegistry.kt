package com.traintycoon3d.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.utils.JsonReader

data class GameAsset(val id: String, val path: String, val type: AssetType)
enum class AssetType { TRAIN_MODEL, BUILDING_MODEL, DECORATION_MODEL, TEXTURE, SOUND, CONFIG }

class AssetRegistry(private val manager: AssetManager = AssetManager()) {
    val assets = linkedMapOf<String, GameAsset>()

    fun scanAndLoad() {
        scan("models", "glb") { path, id ->
            val type = when {
                id.contains("train", true) -> AssetType.TRAIN_MODEL
                id.contains("station", true) || id.contains("building", true) -> AssetType.BUILDING_MODEL
                else -> AssetType.DECORATION_MODEL
            }
            manager.load(path, Model::class.java); GameAsset(id, path, type)
        }
        scan("textures", "png") { path, id -> manager.load(path, Texture::class.java); GameAsset(id, path, AssetType.TEXTURE) }
        scan("sounds", "wav") { path, id -> manager.load(path, Sound::class.java); GameAsset(id, path, AssetType.SOUND) }
        scan("configs", "json") { path, id -> JsonReader().parse(Gdx.files.internal(path)); GameAsset(id, path, AssetType.CONFIG) }
        manager.finishLoading()
    }

    fun model(id: String): Model? = assets[id]?.takeIf { manager.isLoaded(it.path) }?.let { manager.get(it.path, Model::class.java) }
    fun dispose() = manager.dispose()

    private fun scan(folder: String, ext: String, register: (String, String) -> GameAsset) {
        val dir = Gdx.files.internal(folder)
        if (!dir.exists() || !dir.isDirectory) return
        dir.list().filter { it.extension().equals(ext, true) }.forEach { file ->
            assets[file.nameWithoutExtension()] = register(file.path(), file.nameWithoutExtension())
        }
    }
}
