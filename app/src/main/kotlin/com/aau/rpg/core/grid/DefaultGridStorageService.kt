package com.aau.rpg.core.grid

class DefaultGridStorageService(private val size: Int) : GridStorageService {

    override fun load() = gridOf(size)
}
