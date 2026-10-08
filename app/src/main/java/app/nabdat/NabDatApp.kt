package app.nabdat

import android.app.Application
import app.nabdat.data.History
import app.nabdat.data.Prefs
import app.nabdat.engine.Engine

class NabDatApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
        History.init(this)
        Nabber.init(this)
        DownloadService.ensureChannels(this)
        // unpack python/ffmpeg/yt-dlp in the background right away, so the first
        // nab doesn't wait on it
        Engine.start(this)
    }
}
