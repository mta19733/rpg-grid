package com.aau.rpg.data.grid

import com.aau.rpg.data.json.JsonRepository
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject

class JsonGridRepository(private val jsonRepository: JsonRepository) : GridRepository {

    override fun loadGridInfos(): Observable<List<GridInfo>> = jsonRepository
        .load()
        .map(JSONObject::info)

    override fun loadGrid(name: String): Observable<Grid> = jsonRepository
        .load()
        .flatMap { json ->
            json
                .grids
                .firstOrNull { grid -> grid.name == name }
                ?.let { grid -> Observable.just(grid) }
                ?: Observable.empty()
        }

    override fun saveGrid(grid: Grid): Observable<Unit> = jsonRepository
        .load()
        .flatMap { json ->
            val updatedGrids = json
                .grids
                .filter { savedGrid -> savedGrid.name != grid.name }
                .plus(grid)

            json.grids = updatedGrids

            jsonRepository.save(json)
        }

    override fun deleteGrid(name: String): Observable<Unit> = jsonRepository
        .load()
        .flatMap { json ->
            val updatedGrids = json
                .grids
                .filter { savedGrid -> savedGrid.name != name }

            json.grids = updatedGrids

            jsonRepository.save(json)
        }
}

private fun JSONArray.toJsonObjectList() = (0 until length()).map(::getJSONObject)

private fun JSONArray.toJsonArrayList() = (0 until length()).map(::getJSONArray)

private var JSONObject.tile: Tile
    set(value) {
        put("value", value.value)
        put("id", value.id)
    }
    get() = Tile(
        value = getBoolean("value"),
        id = getInt("id")
    )

private var JSONObject.tiles: List<List<Tile>>
    set(value) {
        val jsonTileRows = JSONArray()
        value.forEach { tileRow ->
            val jsonTileCols = JSONArray()
            tileRow.forEach { tileCol ->
                val json = JSONObject()
                json.tile = tileCol
                jsonTileCols.put(json)
            }

            jsonTileRows.put(jsonTileCols)
        }

        put("tiles", jsonTileRows)
    }
    get() = getJSONArray("tiles")
        .toJsonArrayList()
        .map { row ->
            row
                .toJsonObjectList()
                .map(JSONObject::tile)
        }

private var JSONObject.size: Int
    set(value) {
        put("size", value)
    }
    get() = getInt("size")

private var JSONObject.name: String
    set(value) {
        put("name", value)
    }
    get() = getString("name")

private var JSONObject.grid: Grid
    set(value) {
        tiles = value.tiles
        size = value.size
        name = value.name
    }
    get() = Grid(
        tiles = tiles,
        size = size,
        name = name
    )

private val JSONObject.info: List<GridInfo>
    get() = optJSONArray("grids")
        ?.toJsonObjectList()
        ?.map { grid ->
            GridInfo(
                size = grid.size,
                name = grid.name
            )
        }
        ?: emptyList()

private var JSONObject.grids: List<Grid>
    set(value) {
        val grids = JSONArray()
        value.forEach { grid ->
            val json = JSONObject()
            json.grid = grid
            grids.put(json)
        }
        put("grids", grids)
    }
    get() = optJSONArray("grids")
        ?.toJsonObjectList()
        ?.map { json -> json.grid }
        ?: emptyList()
