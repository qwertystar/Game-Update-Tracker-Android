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
                val connect = Jsoup.connect("https://mindustrygame.github.io/wiki/")
//                爬虫必备请求头
                val connectHeader = connect.header(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36"
                )
                val document: Document = connectHeader.get()
                val elements: Elements =
                    document.select("a[href^=https://github.com/Anuken/Mindustry/releases/tag/]")
                if (elements.size == 1) {
                    println(elements.first())
                    elements.first()?.text() ?: "已找到对应元素，但是好像网站没有提供对应信息"
                } else {
                    println("爬虫失败！")
                    "未能爬到有效信息，可能是网站改版……"
                }
            } catch (e: Exception) {
                "错误原因: ${e.message}"
            } finally {
                updateFetchingState(false)
            }
        }
    }

}