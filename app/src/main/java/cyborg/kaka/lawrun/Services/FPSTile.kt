package cyborg.kaka.lawrun.services

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class FPSTile : TileService() {

    object Service {
        var isRunning = false
    }

    override fun onStartListening() {}

    override fun onClick() {
        val tile = qsTile

        // Start Service & Enable Tile If Not Running
        if (qsTile.state == Tile.STATE_INACTIVE) {
            startForegroundService(Intent(this, FPS::class.java))
            tile.state = Tile.STATE_ACTIVE
            Service.isRunning = true
            tile.updateTile()
            return
        }

        // Start Service & Disable Tile If Running
        stopService(Intent(this, FPS::class.java))
        tile.state = Tile.STATE_INACTIVE
        Service.isRunning = false
        tile.updateTile()
    }
}