package com.wsy.testuiapplication.kotlin

import com.wsy.testuiapplication.kotlin.bean.ForcastBean
import com.wsy.testuiapplication.kotlin.bean2.ForcastBean as FB

public class DataMapper {
    fun filterData(forcastBean: ForcastBean): FB {
        return FB(forcastBean.name, forcastBean.main.temp_max, forcastBean.main.temp_min)
    }
}