package com.a_rin.health

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/*
 * タイトル : HealthApplication
 * 説明 : KibunChanのアプリケーションクラス
 *
 * @author Ayaka Yoshizawa
 */

class HealthApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}