package com.aau.dnd

import com.aau.dnd.R
import com.aau.dnd.core.bluetooth.BluetoothService
import com.aau.dnd.core.bluetooth.RxBluetoothService
import com.polidea.rxandroidble2.RxBleClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.UUID

class DndGridApplication : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin()
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

    private fun startKoin() {
        val mainModule = module {
            single<BluetoothService> {
                createBluetoothService()
            }
        }

        startKoin {
            androidContext(this@DndGridApplication)
            modules(mainModule)
        }
    }
}
