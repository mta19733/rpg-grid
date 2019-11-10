package com.aau.rpg

import android.app.Application
import com.aau.rpg.core.bluetooth.BluetoothService
import com.aau.rpg.core.bluetooth.RxBluetoothService
import com.aau.rpg.core.grid.GridStorageService
import com.aau.rpg.core.grid.JsonGridStorageService
import com.aau.rpg.core.json.FileJsonStorageService
import com.aau.rpg.core.json.JsonStorageService
import com.aau.rpg.ui.connection.BluetoothViewModel
import com.aau.rpg.ui.connection.ConnectionBluetoothViewModel
import com.aau.rpg.ui.grid.GameGridViewModel
import com.aau.rpg.ui.grid.GridViewModel
import com.aau.rpg.ui.management.StoringManagementViewModel
import com.aau.rpg.ui.management.ManagementViewModel
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
        jsonService = get()
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
                ConnectionBluetoothViewModel(
                    bluetooth = get()
                )
            }

            viewModel<GridViewModel> {
                GameGridViewModel(
                    idDelimiter = resources.getString(R.string.grid_id_delimiter),
                    viewSize = resources.getInteger(R.integer.grid_view_size)
                )
            }

            viewModel<ManagementViewModel> {
                StoringManagementViewModel(
                    gridStorage = get(),
                    gridSize = resources.getInteger(R.integer.grid_full_size)
                )
            }
        }

        startKoin {
            androidContext(this@RpgGridApplication)
            modules(mainModule)
        }
    }
}
