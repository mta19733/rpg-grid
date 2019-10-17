package com.aau.rpg

import android.app.Application
import com.aau.rpg.core.bluetooth.BluetoothService
import com.aau.rpg.core.bluetooth.RxBluetoothService
import com.polidea.rxandroidble2.RxBleClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.UUID

class RpgGridApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    private fun createBluetoothService() = RxBluetoothService(
        connectDelayMillis = resources.getInteger(R.integer.connect_delay_millis).toLong(),
        scanTimeoutMillis = resources.getInteger(R.integer.scan_timeout_millis).toLong(),
        connectRetries = resources.getInteger(R.integer.connect_retries),
        writeReties = resources.getInteger(R.integer.write_retries),
        characteristicId = UUID.fromString(getString(R.string.characteristic_id)),
        serviceId = UUID.fromString(getString(R.string.service_id)),
        pin = getString(R.string.bluetooth_pin),
        client = RxBleClient.create(baseContext)
    )

    private fun startKoin() {
        val mainModule = module {
            single<BluetoothService> {
                createBluetoothService()
            }
        }

        startKoin {
            androidContext(this@RpgGridApplication)
            modules(mainModule)
        }
    }
}
