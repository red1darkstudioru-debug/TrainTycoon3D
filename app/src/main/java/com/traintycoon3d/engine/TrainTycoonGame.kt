package com.traintycoon3d.engine

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.traintycoon3d.buildings.BuildingManager
import com.traintycoon3d.economy.EconomyManager
import com.traintycoon3d.railway.RailBuilder
import com.traintycoon3d.railway.RailSystem
import com.traintycoon3d.ui.HUD
import com.traintycoon3d.world.World

class TrainTycoonGame : ApplicationAdapter(), InputProcessor {
    private lateinit var camera: PerspectiveCamera
    private lateinit var cameraController: CameraController
    private lateinit var batch: ModelBatch
    private lateinit var env: Environment
    private lateinit var hud: HUD
    private val registry = AssetRegistry()
    private val instances = mutableListOf<ModelInstance>()
    private val ownedModels = mutableListOf<Model>()
    private val world = World()
    private val buildings = BuildingManager()
    private val economy = EconomyManager()
    private val rails = RailSystem()
    private val railBuilder = RailBuilder(rails)
    private val attrs = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    private var buildMode = true

    override fun create() {
        registry.scanAndLoad()
        camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).apply { position.set(10f, 10f, 10f); lookAt(0f, 0f, 0f); near = .1f; far = 200f; update() }
        cameraController = CameraController(camera)
        batch = ModelBatch(); env = Environment().apply { set(ColorAttribute(ColorAttribute.AmbientLight, .45f, .45f, .5f, 1f)); add(DirectionalLight().set(Color.WHITE, -1f, -2f, -1f)) }
        buildBaseScene()
        hud = HUD({ buildMode = true }, { buildMode = false })
        Gdx.input.inputProcessor = InputMultiplexer(hud, this)
    }

    private fun buildBaseScene() {
        val mb = ModelBuilder()
        fun model(m: Model) = m.also(ownedModels::add)
        instances += ModelInstance(model(mb.createBox(64f, .15f, 64f, Material(ColorAttribute.createDiffuse(Color(0.2f, .55f, .23f, 1f))), attrs)))
        for (i in -8..8) {
            instances += ModelInstance(model(mb.createBox(.12f, .08f, 1.2f, Material(ColorAttribute.createDiffuse(Color.BROWN)), attrs)), Vector3(i.toFloat(), .14f, 0f))
        }
        instances += ModelInstance(model(mb.createBox(18f, .06f, .08f, Material(ColorAttribute.createDiffuse(Color.GRAY)), attrs)), Vector3(0f, .24f, -.45f))
        instances += ModelInstance(model(mb.createBox(18f, .06f, .08f, Material(ColorAttribute.createDiffuse(Color.GRAY)), attrs)), Vector3(0f, .24f, .45f))
        placeStation(0, -3)
    }

    private fun placeStation(x: Int, z: Int) {
        if (!economy.money.spend(50)) return
        buildings.place("station", x, z); world.grid.tile(x + 16, z + 16)?.occupiedBy = "station"
        val assetModel = registry.model("station")
        instances += if (assetModel != null) ModelInstance(assetModel, x.toFloat(), 0f, z.toFloat()) else {
            val m = ModelBuilder().createBox(2.5f, 1f, 1.6f, Material(ColorAttribute.createDiffuse(Color.SKY)), attrs); ownedModels += m; ModelInstance(m, x.toFloat(), .55f, z.toFloat())
        }
    }

    override fun render() {
        val dt = Gdx.graphics.deltaTime; cameraController.update(dt); economy.tick(1); hud.setMoney(economy.money.money)
        ScreenUtils.clear(.52f, .78f, .95f, 1f, true)
        batch.begin(camera); instances.forEach { batch.render(it, env) }; batch.end()
        hud.act(dt); hud.draw()
    }
    override fun resize(width: Int, height: Int) { camera.viewportWidth = width.toFloat(); camera.viewportHeight = height.toFloat(); camera.update(); hud.viewport.update(width, height, true) }
    override fun scrolled(amountX: Float, amountY: Float): Boolean { cameraController.zoom(amountY); return true }
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean { if (button == Input.Buttons.LEFT && buildMode) { val x = (screenX / 64) % 16 - 8; val z = (screenY / 64) % 16 - 8; placeStation(x, z) } else railBuilder.place(0, 0); return false }
    override fun dispose() { batch.dispose(); registry.dispose(); ownedModels.forEach(Model::dispose); hud.dispose() }
    override fun keyDown(keycode: Int)=false; override fun keyUp(keycode: Int)=false; override fun keyTyped(character: Char)=false; override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int)=false; override fun touchDragged(screenX: Int, screenY: Int, pointer: Int)=false; override fun mouseMoved(screenX: Int, screenY: Int)=false; override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int)=false
}
