package com.decena.exam

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decena.exam.entities.BoxEntity
import com.decena.exam.entities.FolderEntity
import com.example.grouping_exam.DataSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivityVieModel : ViewModel(), Observer<List<String>> {

    var deliveryData: ArrayList<BoxEntity> =  arrayListOf()
    var delivery = MutableLiveData<ArrayList<BoxEntity>>()
    init {
        DataSource.emit()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this)
    }

    fun getInput(): MutableLiveData<ArrayList<BoxEntity>>{
        return delivery
    }

    override fun onSubscribe(d: Disposable) {
        Log.d("TAG", "onSubscribe")
    }

    override fun onNext(t: List<String>) {
        Log.d("I", "$t")
        val deliveries = receiveDelivery(t)
        for (delivery in deliveries){
            deliveryData.add(delivery)
        }

        val a = sortInventory(deliveryData)
        val output: List<String> = a.toString().replace("[", "").replace("]", "").split(", ").distinct()
        Log.d("0", "$output")

        delivery.postValue(sortInventory(receiveDelivery(output)))
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onComplete() {
        Log.d("TAG", "completed")
    }

    private fun folderParser(contents: List<String>): ArrayList<FolderEntity>{
        var papers = arrayListOf<String>()
        var folders = arrayListOf<FolderEntity>()
        for (item in contents){
            if (item.contains("F")){
                if (papers.size > 0) folders.add(FolderEntity(item, papers))
                papers = arrayListOf()
            }
            else if (item.contains("P")) papers.add(item)
        }

        return folders

    }

    private fun receiveDelivery(list: List<String>): ArrayList<BoxEntity> {
        var contents = arrayListOf<String>()
        var boxes = arrayListOf<BoxEntity>()
        for (item in list){
            if (item.contains("B")){
                boxes.add(BoxEntity(item, folderParser(contents)))
                contents = arrayListOf()
            }
            else contents.add(item)
        }

        return boxes
    }



    private fun sortInventory(deliveries: ArrayList<BoxEntity>): ArrayList<BoxEntity>{
        var resultList = arrayListOf<BoxEntity>()
        for (delivery in deliveries){
            if (resultList.find { search -> search == delivery } == null) resultList.add(delivery)
        }
        var boxesForRemoval = arrayListOf<BoxEntity>()
        for (result in resultList){
            var duplicateBoxes = resultList.filter { search -> search.name == result.name }
            if (duplicateBoxes.size > 1){
                var firstBox = duplicateBoxes[0]
                for (i in 1 until duplicateBoxes.size){
                    val folderList = duplicateBoxes[i].folders
                    for (folder in sortFolders(firstBox.folders, folderList)){
                        if (firstBox.folders.find { s -> s.name == folder.name } == null) firstBox.folders.add(folder)
                    }

                    boxesForRemoval.add(duplicateBoxes[i])
                }

            }
            if ( result.folders.size == 0 || result.getPaperCount() == 0) boxesForRemoval.add(result)
        }
        for (box in boxesForRemoval){
            resultList.remove(box)
        }

        return resultList

    }

    private fun sortFolders(
        destinationFolder: ArrayList<FolderEntity>,
        otherFolders: ArrayList<FolderEntity>
    ): ArrayList<FolderEntity> {
        var result = arrayListOf<FolderEntity>()
        var allFolders = arrayListOf<FolderEntity>()
        allFolders.addAll(destinationFolder)
        allFolders.addAll(otherFolders)
        var folderForRemoval = arrayListOf<FolderEntity>()
        for (otherFolder in allFolders){
            var duplicateFolders = allFolders.filter { search -> search.name == otherFolder.name }
            if (duplicateFolders.size > 1) {
                var firstFolderEntity = duplicateFolders[0]
                for (i in 1 until duplicateFolders.size) {
                    firstFolderEntity.papers += duplicateFolders[i].papers.distinct()
                    folderForRemoval.add(duplicateFolders[i])
                }

                result.add(firstFolderEntity)
            }
            else{
                result.add(otherFolder)
            }
        }

        for (folder in folderForRemoval){
            result.remove(folder)
        }

        return result
    }
}