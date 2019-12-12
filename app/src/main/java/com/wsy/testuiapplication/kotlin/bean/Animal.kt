package com.wsy.testuiapplication.kotlin.bean

open class Animal(name: String) {

    open var name: String = ""

    init {
        this.name = name
    }

    fun getRandomNumber(): Int {
        return 0
    }
}