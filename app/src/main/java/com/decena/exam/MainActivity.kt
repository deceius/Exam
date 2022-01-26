package com.decena.exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(){

    lateinit var viewModel: MainActivityVieModel
    lateinit var rvInventory: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = MainActivityVieModel()
        rvInventory = findViewById(R.id.rvInventory)
        rvInventory.layoutManager = LinearLayoutManager(this)

        val inventoryAdapter = InventoryAdapter(this)
        viewModel.getInput().observe(this, {
            Log.d("0", "$it")
            inventoryAdapter.mData?.add(it)
            rvInventory.adapter = inventoryAdapter
        })

    }


}