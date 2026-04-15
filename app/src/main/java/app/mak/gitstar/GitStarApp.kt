package app.mak.gitstar

import android.app.Application
import app.mak.gitstar.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GitStarApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GitStarApp)
            androidLogger(Level.ERROR)
            modules(appModules)
        }
    }
}