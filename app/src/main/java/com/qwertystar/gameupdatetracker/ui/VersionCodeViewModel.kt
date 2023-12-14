/*
 * Copyright (c) 2023 qwertystar
 * Game Update Tracker is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.qwertystar.gameupdatetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class VersionCodeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VersionCodeUiState())
    val uiState: StateFlow<VersionCodeUiState> = _uiState.asStateFlow()

    //    localVersionCode输入变化的操作
    fun handleLocalVersionCodeValueChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(localVersionCode = newValue)
    }

    fun handleUpdateNewVersionCodeButtonClicked() {
        _uiState.value = _uiState.value.copy(localVersionCode = _uiState.value.onlineVersionCode)
        _uiState.value = _uiState.value.copy(hasNewOnlineVersion = false)
    }


    fun checkNewVersionButtonClicked() {
        viewModelScope.launch {
            val onlineVersionCode = fetchOnlineVersionCode()
            _uiState.value = _uiState.value.copy(onlineVersionCode = onlineVersionCode)
            _uiState.value =
                _uiState.value.copy(hasNewOnlineVersion = (_uiState.value.localVersionCode != onlineVersionCode))
        }
    }

    private fun updateFetchingState(isFetching: Boolean) {
        _uiState.value = _uiState.value.copy(isFetchingOnlineVersion = isFetching)
    }

    private suspend fun fetchOnlineVersionCode(): String {
        updateFetchingState(true)
        return withContext(Dispatchers.IO) {
            try {
                val document: Document =
                    Jsoup.connect("https://mindustrygame.github.io/wiki/").get()
                println(document)
                val elements: Elements =
                    document.select("a[href^=https://github.com/Anuken/Mindustry/releases/tag/]")
                println(elements)
                if (elements.size == 1) {
                    println(elements.first())
                    elements.first()?.text() ?: "第一个元素没有……"
                } else {
                    println("爬虫失败！")
                    "Not Found"
                }
            } catch (e: Exception) {
                "Error occurred: ${e.message}"
            } finally {
                updateFetchingState(false)
            }
        }
    }

}