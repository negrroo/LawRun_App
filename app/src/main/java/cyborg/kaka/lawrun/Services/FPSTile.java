package cyborg.kaka.lawrun.Services;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class FPSTile extends TileService {

    @Override
    public void onStartListening() {
        // Enable Tile Service & Running
        if (FPS.serviceRunning) {
            Tile tile = getQsTile();
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
        }
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();

        // Start Service & Enable Tile If Not Running
        if (!FPS.serviceRunning) {
            this.startForegroundService(new Intent(this, FPS.class));
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
            return;
        }

        // Start Service & Disable Tile If Running
        this.stopService(new Intent(this, FPS.class));
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
    }
}
