package com.olaf.squishyspaces.ui.screens

enum class ResultImpactMode { GREAT, MIXED, DISASTER, NONE }

fun scoreToImpactMode(score: Int): ResultImpactMode = when (score) {
    in 8..10 -> ResultImpactMode.GREAT
    in 5..7  -> ResultImpactMode.MIXED
    in 1..4  -> ResultImpactMode.DISASTER
    else     -> ResultImpactMode.NONE
}
