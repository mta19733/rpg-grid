package com.aau.rpg.data.json

import io.reactivex.Observable
import org.json.JSONObject

interface JsonRepository {

    /**
     * Load [JSONObject] from storage.
     */
    fun load(): Observable<JSONObject>

    /**
     * Save [json] to storage.
     */
    fun save(json: JSONObject): Observable<Unit>
}
