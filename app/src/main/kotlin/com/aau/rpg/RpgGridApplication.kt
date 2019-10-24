package com.aau.rpg

import android.app.Application
import com.aau.rpg.core.bluetooth.BluetoothService
import com.aau.rpg.core.bluetooth.RxBluetoothService
import com.aau.rpg.core.grid.JsonGridStorageService
import com.aau.rpg.core.grid.GridStorageService
import com.aau.rpg.core.json.FileJsonStorageService
import com.aau.rpg.core.json.JsonStorageService
import com.aau.rpg.ui.connection.BluetoothViewModel
import com.aau.rpg.ui.connection.DefaultBluetoothViewModel
import com.aau.rpg.ui.grid.DefaultGridViewModel
import com.aau.rpg.ui.grid.GridViewModel
import com.polidea.rxandroidble2.RxBleClient
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.UUID

@Suppress("unused")
class RpgGridApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    private fun createJsonStorageService(): JsonStorageService = FileJsonStorageService(
        fileName = getString(R.string.storage_name),
        context = applicationContext
    )

    private fun createGridStorageService(): GridStorageService = JsonGridStorageService(
        jsonService = get(),
        size = resources.getInteger(R.integer.grid_full_size)
    )

    private fun createBluetoothService(): BluetoothService = RxBluetoothService(
        connectDelayMillis = resources.getInteger(R.integer.ble_connect_delay_millis).toLong(),
        scanTimeoutMillis = resources.getInteger(R.integer.ble_scan_timeout_millis).toLong(),
        connectRetries = resources.getInteger(R.integer.ble_connect_retries),
        writeReties = resources.getInteger(R.integer.ble_write_retries),
        characteristicId = UUID.fromString(getString(R.string.ble_characteristic_id)),
        serviceId = UUID.fromString(getString(R.string.ble_service_id)),
        pin = getString(R.string.ble_pin),
        client = RxBleClient.create(baseContext)
    )

    private fun startKoin() {
        val mainModule = module {
            single { createJsonStorageService() }
            single { createGridStorageService() }
            single { createBluetoothService() }

            viewModel<BluetoothViewModel> {
                DefaultBluetoothViewModel(
                    bluetooth = get()
                )
            }

            viewModel<GridViewModel> {
                DefaultGridViewModel(
                    gridStorageService = get(),
                    viewSize = resources.getInteger(R.integer.grid_view_size)
                )
            }
        }

        startKoin {
            androidContext(this@RpgGridApplication)
            modules(mainModule)
        }
    }
}
