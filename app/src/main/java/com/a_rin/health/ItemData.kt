package com.a_rin.health

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ItemData(
    @PrimaryKey open var date: String ?= null,
        open var year : Int ?= null,
        open var month : Int ?= null,
        open var day : Int ?= null,
        open var lucky : Int ?= null,
        open var satiety : Int ?= null,
        open var fitness : Int ?= null,
        open var sleep : Int ?= null,
        open var average : Int ?= null,
        open var message : String ? = null
) : RealmObject()
