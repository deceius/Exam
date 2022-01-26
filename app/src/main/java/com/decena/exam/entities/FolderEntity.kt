package com.decena.exam.entities

import java.io.Serializable

class FolderEntity constructor(
    var name: String,
    var papers: ArrayList<String>
): Serializable{

    override fun toString(): String {
        var s = ""
        for (paper in papers.distinct().asReversed()){
            s = "$s$paper, "
        }
        return "$s$name"
    }
}