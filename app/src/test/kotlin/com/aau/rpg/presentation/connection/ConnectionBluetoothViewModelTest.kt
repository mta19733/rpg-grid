package com.aau.rpg.presentation.connection

import com.aau.rpg.core.bluetooth.BluetoothConnection
import com.aau.rpg.core.bluetooth.BluetoothService
import com.aau.rpg.core.bluetooth.ConnectionState
import com.aau.rpg.test.InstantExecutorExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.Observable
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class ConnectionBluetoothViewModelTest {

    private val bluetooth = mockk<BluetoothService>()
    private val model = ConnectionBluetoothViewModel(bluetooth)

    @Test
    fun `should send grid in correct format`() {
        val data = slot<String>()
        val conn = mockk<BluetoothConnection> {
            every { send(capture(data)) } answers { Single.just(firstArg()) }
            every { observeState() } returns Observable.just(ConnectionState.CONNECTED)

            every { name } returns ""
            every { mac } returns ""
        }

        every { bluetooth.connect() } returns Observable.just(conn)

        val rawData = "0,4,8"
        model.connect()
        model.send(rawData)

        assertThat(data.captured).isEqualTo(rawData)
    }
}
