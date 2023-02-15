package com.example.planetresources.models

class Mineral(val name: String, val elapseTime: Int) {

    var quantity = 0

    fun addResource(){
        quantity++
    }

    override fun toString(): String {
        return "Mineral($name,$elapseTime)"
    }
}