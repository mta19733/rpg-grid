package com.aau.rpg.core.json

import io.reactivex.Observable
import org.json.JSONObject

interface JsonStorageService {

    /**
     * Load [JSONObject] from storage.
     */
    fun load(): Observable<JSONObject>

    /**
     * Save [json] to storage.
     */
    fun save(json: JSONObject): Observable<Unit>
}
