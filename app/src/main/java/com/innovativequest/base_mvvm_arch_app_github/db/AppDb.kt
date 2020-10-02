//*********************************************************************
// Created by Ghous Khan on 2020-10-02.
// Innovative Quest Ltd
// Copyright (C) Innovative Quest Ltd All Rights Reserved
// Any copying or reproduction of this software in strictly prohibited.
//*********************************************************************


package com.innovativequest.base_mvvm_arch_app_github.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.innovativequest.base_mvvm_arch_app_github.model.*

/**
 * Main database description.
 */
@Database(
    entities = [
        DataListItemResponse::class,
        Contributor::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun dataListItemResponseDao(): DataListItemResponseDao
}
