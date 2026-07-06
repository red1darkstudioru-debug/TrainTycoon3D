package com.traintycoon3d

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.traintycoon3d.engine.TrainTycoonGame

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(TrainTycoonGame(), AndroidApplicationConfiguration().apply { useImmersiveMode = true; numSamples = 4 })
    }
}
