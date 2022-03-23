package br.com.dgc.businesscard

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import br.com.dgc.businesscard.data.AppDatabase
import br.com.dgc.businesscard.data.BusinessCardRepository
import br.com.dgc.businesscard.ui.AddBusinessCardActivity

class App : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { BusinessCardRepository(database.businessDao()) }

}