package com.aau.dnd.util

import com.aau.dnd.device.Device
import kotlin.random.Random

fun generateDevices() = (1..random.nextInt(MAX_DEVICES)).map {
    val partialName = deviceNames.random(random)
    val name = if (random.nextBoolean()) {
        "$partialName v${random.nextInt(MAX_DEVICES)}.${random.nextInt(MAX_DEVICES)}"
    } else {
        partialName
    }

    Device(
        name = name,
        id = System.nanoTime().toString()
    )
}

fun generateDelay() = random.nextLong(MAX_DELAY)

private const val MAX_DEVICES = 50
private const val MAX_DELAY = 5000L

private val random = Random(System.nanoTime())

private val deviceNames = listOf(
    "Cool device",
    "D&D board",
    "Cool Board",
    "D&D",
    "New board",
    "Peters Headphones",
    "Carls Phone",
    "Train",
    "scooter",
    "big man",
    "super",
    "suuuh"
)
