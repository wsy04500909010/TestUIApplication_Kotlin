package com.wsy.testuiapplication.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.wsy.testuiapplication.R

class CollectionActivity : AppCompatActivity() {

    companion object {
        val TAG: String = "CollectionActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_collection)

        var list: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6)
        //drop 返回包含去掉前n个元素的所有元素的列表。
        Log.d(TAG, list.size.toString())

//        var list2: MutableList<Int> = list.drop(4) as MutableList<Int>
        Log.d(TAG, list.drop(4).size.toString())

        //dropWhile 返回根据给定函数从第一项开始去掉指定元素的列表。
        Log.d(TAG, list.dropWhile { it < 3 }.size.toString())

        //filter 过滤所有符合给定函数条件的元素
        Log.d(TAG, list.filter { it % 2 == 0 }.size.toString())

        //filterNot 过滤所有不符合给定函数条件的元素
        Log.d(TAG, list.filterNot { it < 3 }.size.toString())


    }

}