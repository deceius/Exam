package com.decena.exam.entities

import java.io.Serializable

class BoxEntity constructor(
    val name: String,
    var folders: ArrayList<FolderEntity>
)
: Serializable {

    init {

    }
    override fun toString(): String {
        var s = ""
        for (folder in folders){
            s = "$s${folder}, "
        }
        return "$s$name"
    }

    fun getPaperCount(): Int{
        var count = 0
        for (folder in folders){
            count += folder.papers.size
        }
        return count
    }

      fun getAllPapers(): ArrayList<String>{
        var papers = arrayListOf<String>()
        for (folder in folders){
            for (paper in folder.papers){
                papers.add(paper)
            }
        }
        return papers
    }

    fun mergeFolders(boxEntity: BoxEntity): BoxEntity {
        val boxFolders = boxEntity.folders.asReversed()
        val result: BoxEntity = boxEntity
        result.folders = arrayListOf()
        for (boxFolder in boxFolders){
            if (!containsPaper(boxEntity, boxFolder)){
                result.folders.add(boxFolder)
            }
        }

        return result
    }

    private fun containsPaper(boxEntity: BoxEntity, boxFolderEntity: FolderEntity): Boolean {
        return  boxEntity.folders.find { s -> s == boxFolderEntity } != null
    }




}