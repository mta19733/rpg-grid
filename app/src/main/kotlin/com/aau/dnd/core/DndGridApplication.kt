package com.aau.dnd.core

import com.aau.dnd.R
import com.aau.dnd.bluetooth.RxBluetoothService
import com.polidea.rxandroidble2.RxBleClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.UUID

class DndGridApplication : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        startCoin()
    }

    private fun createBluetoothService() = RxBluetoothService(
        connectDelayMillis = resources.getInteger(R.integer.connect_delay_millis).toLong(),
        scanTimeoutMillis = resources.getInteger(R.integer.scan_timeout_millis).toLong(),
        connectRetries = resources.getInteger(R.integer.connect_retries),
        writeReties = resources.getInteger(R.integer.write_retries),
        stringCharacteristicId = UUID.fromString(getString(R.string.string_characteristic_id)),
        serviceId = UUID.fromString(getString(R.string.service_id)),
        client = RxBleClient.create(baseContext)
    )

    private fun startCoin() {
        val mainModule = module {
            single {
                createBluetoothService()
            }
        }

        startKoin {
            androidContext(this@DndGridApplication)
            modules(mainModule)
        }
    }
}
