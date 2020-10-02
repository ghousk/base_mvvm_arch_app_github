//*********************************************************************
// Created by Ghous Khan on 2020-10-02.
// Innovative Quest Ltd
// Copyright (C) Innovative Quest Ltd All Rights Reserved
// Any copying or reproduction of this software in strictly prohibited.
//*********************************************************************


package com.innovativequest.base_mvvm_arch_app_github.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.innovativequest.base_mvvm_arch_app_github.repository.DataListItemsRepository
import com.innovativequest.base_mvvm_arch_app_github.testing.OpenForTesting
import com.innovativequest.base_mvvm_arch_app_github.util.AbsentLiveData
import com.innovativequest.base_mvvm_arch_app_github.model.DataListItemResponse
import com.innovativequest.base_mvvm_arch_app_github.model.Resource
import javax.inject.Inject

@OpenForTesting
class ItemDetailViewModel @Inject constructor(repository: DataListItemsRepository) : ViewModel() {
    private val _repoId: MutableLiveData<RepoId> = MutableLiveData()
    val repoId: LiveData<RepoId>
        get() = _repoId
    val dataListItemResponseById: LiveData<Resource<DataListItemResponse>> = Transformations
        .switchMap(_repoId) { input ->
            input.ifExists { userId ->
                repository.loadDataListItemResponseById(userId)
            }
        }

    val dataListItems: LiveData<Resource<DataListItemResponse>> = repository.loadDataListItemResponses()

    fun retry() {
        val userId = _repoId.value?.userId
        if (userId != null) {
            _repoId.value = RepoId(userId)
        }
    }

    fun setId(userId: String) {
        val update = RepoId(userId)
        if (_repoId.value == update) {
            return
        }
        _repoId.value = update
    }

    data class RepoId(val userId: String) {
        fun <T> ifExists(f: (String) -> LiveData<T>): LiveData<T> {
            return if (userId.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(userId)
            }
        }
    }
}
