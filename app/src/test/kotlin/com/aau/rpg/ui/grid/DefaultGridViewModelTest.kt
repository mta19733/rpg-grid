package com.aau.rpg.ui.grid

import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.GridStorageService
import com.aau.rpg.test.InstantExecutorExtension
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class DefaultGridViewModelTest {

    private val gridStorageService = mockk<GridStorageService> {
        every { load() } answers {
            Grid(
                tiles = listOf(
                    listOf(
                        Tile(0, true), Tile(1, false)
                    ),
                    listOf(
                        Tile(2, false), Tile(3, true)
                    )
                ),
                size = 2
            )
        }
    }

    private val view = DefaultGridViewModel(
        gridStorageService = gridStorageService,
        viewSize = 1
    )

    @Test
    fun `should move right`() {
        view.move(Direction.RIGHT)

        assertThat(view.grid.value).isEqualTo(
            Tile(1, false).asGrid()
        )
    }

    @Test
    fun `should move right and left`() {
        view.move(Direction.RIGHT)
        view.move(Direction.LEFT)

        assertThat(view.grid.value).isEqualTo(
            Tile(0, true).asGrid()
        )
    }

    @Test
    fun `should move down`() {
        view.move(Direction.DOWN)

        assertThat(view.grid.value).isEqualTo(
            Tile(2, false).asGrid()
        )
    }

    @Test
    fun `should move down and up`() {
        view.move(Direction.DOWN)
        view.move(Direction.UP)

        assertThat(view.grid.value).isEqualTo(
            Tile(0, true).asGrid()
        )
    }


    @Test
    fun `should move right and down`() {
        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        assertThat(view.grid.value).isEqualTo(
            Tile(3, true).asGrid()
        )
    }

    @Test
    fun `should set bottom right tile`() {
        view.set(3, false)

        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        val tile = Tile(3, false)
        assertThat(view.tile.value).isEqualTo(tile)
        assertThat(view.grid.value).isEqualTo(
            tile.asGrid()
        )
    }

    private fun Tile.asGrid() = Grid(
        tiles = listOf(listOf(this)),
        size = 1
    )
}
