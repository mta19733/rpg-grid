package com.aau.rpg.ui.connection

import com.aau.rpg.core.bluetooth.BluetoothConnection
import com.aau.rpg.core.bluetooth.BluetoothService
import com.aau.rpg.core.bluetooth.ConnectionState
import com.aau.rpg.ui.grid.Grid
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
class ObservingBluetoothViewModelTest {

    private val bluetooth = mockk<BluetoothService>()
    private val model = ObservingBluetoothViewModel(bluetooth)

    @Test
    fun `should send grid in correct format`() {
        val data = slot<String>()
        val conn = mockk<BluetoothConnection> {
            every {
                observeState()
            } answers {
                Observable.just(ConnectionState.CONNECTED)
            }

            every {
                send(capture(data))
            } answers {
                Single.just(firstArg())
            }

            every { name } returns ""
            every { mac } returns ""
        }

        every {
            bluetooth.connect()
        } answers {
            Observable.just(conn)
        }

        model.connect()
        model.send(
            Grid(
                listOf(
                    listOf(true, false, false),
                    listOf(false, true, false),
                    listOf(false, false, true)
                )
            )
        )

        assertThat(data.captured).isEqualTo("0,4,8")
    }
}
