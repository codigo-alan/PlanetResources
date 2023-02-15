package com.example.planetresources.models

class Nave(val usedTo: String) {

    //quita un valor de recurso
    fun extractResource() : Int{
        return -1
    }

    override fun toString(): String {
        return "Nave($usedTo)"
    }
}