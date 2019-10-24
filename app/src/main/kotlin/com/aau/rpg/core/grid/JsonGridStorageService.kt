package com.aau.rpg.core.grid

import com.aau.rpg.core.json.JsonStorageService
import com.aau.rpg.ui.grid.Tile
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject

class JsonGridStorageService(
    private val jsonService: JsonStorageService,
    private val size: Int
) : GridStorageService {

    override fun load() = Single.just(
        gridOf(size = size)
    )

    override fun list(): Observable<List<String>> = jsonService
        .load()
        .flatMap { json ->
            json
                .grids
                .map { jsonGrid -> jsonGrid.name }
                .let { names -> Observable.just(names) }
        }

    override fun load(name: String): Observable<Grid> = jsonService
        .load()
        .flatMap { json ->
            json
                .grids
                .firstOrNull { grid -> grid.name == name }
                ?.let { grid -> Observable.just(grid) }
                ?: Observable.empty()
        }

    override fun save(grid: Grid): Observable<Unit> = jsonService
        .load()
        .flatMap { json ->
            val updatedGrids = json
                .grids
                .filter { savedGrid -> savedGrid.name != grid.name }
                .plus(grid)

            json.grids = updatedGrids

            jsonService.save(json)
        }

    override fun delete(name: String): Observable<Unit> {
        return jsonService
            .load()
            .flatMap { json ->
                val updatedGrids = json
                    .grids
                    .filter { savedGrid -> savedGrid.name != name }

                json.grids = updatedGrids

                jsonService.save(json)
            }
    }
}

private fun JSONArray.toJsonObjectList() = (0 until length()).map(::getJSONObject)

private fun JSONArray.toJsonArrayList() = (0 until length()).map(::getJSONArray)

private val JSONObject.tile: Tile
    get() = Tile(
        value = getBoolean("value"),
        id = getInt("id")
    )

private val JSONObject.tiles: List<List<Tile>>
    get() = getJSONArray("tiles")
        .toJsonArrayList()
        .map { row ->
            row
                .toJsonObjectList()
                .map(JSONObject::tile)
        }

private val JSONObject.size: Int
    get() = getInt("size")

private val JSONObject.name: String
    get() = getString("name")

private val JSONObject.grid: Grid
    get() = Grid(
        tiles = tiles,
        size = size,
        name = name
    )

private var JSONObject.grids: List<Grid>
    set(value) {
        put("grids", value)
    }
    get() = optJSONArray("grids")
        ?.toJsonObjectList()
        ?.map { json -> json.grid }
        ?: emptyList()
