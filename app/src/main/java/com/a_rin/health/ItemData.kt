package com.a_rin.health

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ItemData : RealmObject() {

    @PrimaryKey
    var year : Int ?= null
    var month : Int ?= null
    var day : Int ?= null
    var lucky : Int ?= null
    var satiety : Int ?= null
    var fitness : Int ?= null
    var sleep : Int ?= null
    var average : Int ?= null

}