package com.aau.rpg.core.bluetooth

import com.aau.rpg.core.grid.Grid
import com.polidea.rxandroidble2.RxBleConnection
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.UUID

class RxBluetoothConnectionTest {

    @Test
    fun `should send pin with diagonal grid`() {
        val characteristic = UUID.randomUUID()
        val pin = "pin-code"

        val bleConnection = mockk<RxBleConnection>()
        val payload = slot<ByteArray>()

        every {
            bleConnection.writeCharacteristic(
                eq(characteristic),
                capture(payload)
            )
        } answers {
            Single.just(ByteArray(0))
        }

        val connection = RxBluetoothConnection(
            characteristicId = characteristic,
            writeReties = 0,
            pin = pin,
            connection = bleConnection,
            device = mockk()
        )

        val grid = Grid(
            listOf(
                listOf(true, false, false),
                listOf(false, true, false),
                listOf(false, false, true)
            )
        )

        connection
            .send(grid)
            .blockingGet()

        val (sentPin, sentIds) = String(payload.captured).split(":")
        assertThat(sentPin).isEqualTo(pin)
        assertThat(sentIds).isEqualTo("0,4,8")
    }
}
