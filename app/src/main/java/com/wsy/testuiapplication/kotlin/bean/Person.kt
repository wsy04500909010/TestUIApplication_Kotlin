package com.wsy.testuiapplication.kotlin.bean

class Person(name: String, surname: String) : Animal(name) {

    override var name: String = ""
        get() = field.toUpperCase()
        set(value) {
            field = "Name: $value"
        }

    var surname: String = ""


    init {
        this.name = name
        this.surname = surname
    }
}