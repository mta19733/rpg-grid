package com.aau.rpg.core.grid

import com.aau.rpg.core.json.JsonStorageService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.Test

class JsonGridStorageServiceTest {

    private val jsonService = mockk<JsonStorageService>()
    private val gridService = JsonGridStorageService(
        jsonService = jsonService,
        size = 123
    )

    @Test
    fun `should list grid names`() {
        val name = "test grid"
        val json = """
            {
                "grids": [
                    {
                        "size": 0,
                        "name": "$name",
                        "tiles": []
                    }
                ]
            }
        """.trimIndent()

        every { jsonService.load() } returns Observable.just(JSONObject(json))

        val names = gridService
            .list()
            .blockingFirst()

        assertThat(names).containsOnly(name)
    }

    @Test
    fun `should load grid`() {
        val grid = gridOf(
            size = 1,
            name = "test grid"
        )

        val tile = grid.tiles.flatten().first()

        val json = """
            {
                "grids": [
                    {
                        "size": ${grid.size},
                        "name": "${grid.name}",
                        "tiles": [
                            [
                                {
                                    "value": ${tile.value},
                                    "id": ${tile.id}
                                }
                            ]
                        ]
                    }
                ]
            }
        """.trimIndent()

        every { jsonService.load() } returns Observable.just(JSONObject(json))

        val loadedGrid = gridService
            .load(grid.name)
            .blockingFirst()

        assertThat(loadedGrid).isEqualTo(grid)
    }

    @Test
    fun `should save grid`() {
        val grid = gridOf(
            size = 1,
            name = "test grid"
        )

        val tile = grid.tiles.flatten().first()

        val slot = slot<JSONObject>()
        every { jsonService.save(capture(slot)) } returns Observable.just(Unit)
        every { jsonService.load() } returns Observable.just(JSONObject())

        gridService
            .save(grid)
            .blockingFirst()

        val exp = """
            {
                "grids": [
                    {
                        "size": ${grid.size},
                        "name": "${grid.name}",
                        "tiles": [
                            [
                                {
                                    "value": ${tile.value},
                                    "id": ${tile.id}
                                }
                            ]
                        ]
                    }
                ]
            }
        """.trimIndent()

        assertThat(slot.captured.toString())
            .isEqualTo(JSONObject(exp).toString())
    }

    @Test
    fun `should delete grid`() {
        val grid = gridOf(
            size = 1,
            name = "test grid"
        )

        val tile = grid.tiles.flatten().first()

        val json = """
            {
                "grids": [
                    {
                        "size": ${grid.size},
                        "name": "${grid.name}",
                        "tiles": [
                            [
                                {
                                    "value": ${tile.value},
                                    "id": ${tile.id}
                                }
                            ]
                        ]
                    }
                ]
            }
        """.trimIndent()

        val slot = slot<JSONObject>()
        every { jsonService.save(capture(slot)) } returns Observable.just(Unit)
        every { jsonService.load() } returns Observable.just(JSONObject(json))

        gridService
            .delete(grid.name)
            .blockingFirst()

        val exp = """
            {
                "grids": []
            }
        """.trimIndent()

        assertThat(slot.captured.toString())
            .isEqualTo(JSONObject(exp).toString())
    }
}
