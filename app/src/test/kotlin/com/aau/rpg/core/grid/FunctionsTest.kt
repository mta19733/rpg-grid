package com.aau.rpg.core.grid

import com.aau.rpg.ui.grid.Tile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FunctionsTest {

    @Test
    fun `should create grid`() {
        val grid = gridOf(2)

        assertThat(grid).isEqualTo(
            gridOf(
                tiles = listOf(
                    listOf(
                        Tile(0, false), Tile(1, false)
                    ),
                    listOf(
                        Tile(2, false), Tile(3, false)
                    )
                ),
                size = 2,
                name = grid.name
            )
        )
    }

    @Test
    fun `should create sub grid from start`() {
        val fullGrid = gridOf(
            tiles = listOf(
                listOf(
                    Tile(0, true), Tile(1, true), Tile(2, false)
                ),
                listOf(
                    Tile(3, true), Tile(4, true), Tile(5, false)
                ),
                listOf(
                    Tile(6, false), Tile(7, false), Tile(8, false)
                )
            ),
            size = 3
        )

        val subGrid = fullGrid.view(
            Position(
                row = 0,
                col = 0
            ),
            size = 2
        )

        assertThat(subGrid).isEqualTo(
            gridOf(
                tiles = listOf(
                    listOf(
                        Tile(0, true), Tile(1, true)
                    ),
                    listOf(
                        Tile(3, true), Tile(4, true)
                    )
                ),
                size = 2,
                name = fullGrid.name
            )
        )
    }

    @Test
    fun `should create sub grid from middle`() {
        val fullGrid = gridOf(
            tiles = listOf(
                listOf(
                    Tile(0, false), Tile(1, false), Tile(2, false)
                ),
                listOf(
                    Tile(3, false), Tile(4, true), Tile(5, true)
                ),
                listOf(
                    Tile(6, false), Tile(7, true), Tile(8, true)
                )
            ),
            size = 3
        )

        val subGrid = fullGrid.view(
            position = Position(
                row = 1,
                col = 1
            ),
            size = 2
        )

        assertThat(subGrid).isEqualTo(
            gridOf(
                tiles = listOf(
                    listOf(
                        Tile(4, true), Tile(5, true)
                    ),
                    listOf(
                        Tile(7, true), Tile(8, true)
                    )
                ),
                size = 2,
                name = fullGrid.name
            )
        )
    }
}
