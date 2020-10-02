//*********************************************************************
// Created by Ghous Khan on 2020-10-02.
// Innovative Quest Ltd
// Copyright (C) Innovative Quest Ltd All Rights Reserved
// Any copying or reproduction of this software in strictly prohibited.
//*********************************************************************


package com.innovativequest.base_mvvm_arch_app_github.repository

import androidx.lifecycle.LiveData
import com.innovativequest.base_mvvm_arch_app_github.AppExecutors
import com.innovativequest.base_mvvm_arch_app_github.api.ApiResponse
import com.innovativequest.base_mvvm_arch_app_github.api.DataService
import com.innovativequest.base_mvvm_arch_app_github.db.DataListItemResponseDao
import com.innovativequest.base_mvvm_arch_app_github.db.AppDb
import com.innovativequest.base_mvvm_arch_app_github.testing.OpenForTesting
import com.innovativequest.base_mvvm_arch_app_github.util.RateLimiter
import com.innovativequest.base_mvvm_arch_app_github.model.*
import com.innovativequest.base_mvvm_arch_app_github.util.AppConstants
import com.innovativequest.base_mvvm_arch_app_github.util.AppConstants.REFRESH_TIME_OUT_IN_MILLIS
import com.innovativequest.base_mvvm_arch_app_github.util.PreferencesManager
import com.innovativequest.base_mvvm_arch_app_github.util.Utils
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Repo instances.
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
@OpenForTesting
class DataListItemsRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val preferencesManager: PreferencesManager,
    private val dataListItemDao: DataListItemResponseDao,
    private val dataService: DataService
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadDataListItemResponses(): LiveData<Resource<DataListItemResponse>> {
        return object : NetworkBoundResource<DataListItemResponse, DataListItemResponse>(appExecutors) {
            override fun saveCallResult(item: DataListItemResponse) {
                dataListItemDao.insertDataListItemResponses(item)
            }

            override fun shouldFetch(data: DataListItemResponse?): Boolean {
                return data?.items == null ||data.items.isEmpty() ||
                        isOutdated(preferencesManager.getLong(AppConstants.GET_LIST_ITEMS))/*|| repoListRateLimit.shouldFetch()*/  // TODO: Enable this
            }

            override fun loadFromDb() = dataListItemDao.loadAllDataListItemResponses()

            override fun createCall()  : LiveData<ApiResponse<DataListItemResponse>> {
                preferencesManager.setLong(AppConstants.GET_LIST_ITEMS, Utils.getCurrentTime())
                return dataService.getDataItems(100, "desc", "reputation", "stackoverflow")
            }

            override fun onFetchFailed() {
                //repoListRateLimit.reset() // TODO: Enable this
            }
        }.asLiveData()
    }

    fun loadDataListItemResponseById(userId: String): LiveData<Resource<DataListItemResponse>> {
        return object : NetworkBoundResource<DataListItemResponse, DataListItemResponse>(appExecutors) {
            override fun saveCallResult(item: DataListItemResponse) {
                dataListItemDao.insertDataListItemResponses(item)
            }

            override fun shouldFetch(data: DataListItemResponse?): Boolean {
                return data?.items == null ||data.items.isEmpty() ||
                        isOutdated(preferencesManager.getLong(AppConstants.GET_LIST_ITEM_BY_ID + userId))/*|| repoListRateLimit.shouldFetch()*/  // TODO: Enable this
            }

            override fun loadFromDb() = dataListItemDao.loadAllDataListItemResponses()

            override fun createCall() : LiveData<ApiResponse<DataListItemResponse>> {
                preferencesManager.setLong(AppConstants.GET_LIST_ITEM_BY_ID + userId, Utils.getCurrentTime())

                return dataService.getDataItemById(userId,100, "desc", "reputation", "stackoverflow")
            }

            override fun onFetchFailed() {
                //repoListRateLimit.reset() // TODO: Enable this
            }
        }.asLiveData()
    }

    /**
     * Check if a time stamp from prefs is outdated
     */
    private fun isOutdated(lastUpdateTime: Long): Boolean = Utils.getCurrentTime() > lastUpdateTime + REFRESH_TIME_OUT_IN_MILLIS
}
