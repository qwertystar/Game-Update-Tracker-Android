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
import kotlinx.coroutines.delay
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
        _uiState.value = _uiState.value.copy(fetchResultUiState = FetchResultUiState.NoneFetch)
    }

    sealed class SealedFetchVersionCodeResult {
        data class Success(val versionCode: String) : SealedFetchVersionCodeResult()
        data class Failed(val reason: String) : SealedFetchVersionCodeResult()
    }

    fun checkNewVersionButtonClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(fetchResultUiState = FetchResultUiState.NoneFetch)
            when (val result = fetchOnlineVersionCode()) {
                is SealedFetchVersionCodeResult.Success -> {
                    val onlineVersionCode = result.versionCode
                    _uiState.value = _uiState.value.copy(onlineVersionCode = onlineVersionCode)
                    if (_uiState.value.localVersionCode == onlineVersionCode) {
                        println("无更新")

                        _uiState.value =
                            _uiState.value.copy(fetchResultUiState = FetchResultUiState.VersionSame)
//                        让Toast有时间先生成一个消息框
//                        Note: Toast的延时由Compose函数中的Toast来控制
//                        这里只能保证hasSameVersionCode在相对长的时间内发生变化就足够了
                        delay(100)

                        _uiState.value =
                            _uiState.value.copy(fetchResultUiState = FetchResultUiState.NoneFetch)
                    } else {

                        _uiState.value = _uiState.value.copy(
                            fetchResultUiState = FetchResultUiState.Success(onlineVersionCode)
                        )
                    }
                }

                is SealedFetchVersionCodeResult.Failed -> {
                    _uiState.value =
                        _uiState.value.copy(fetchResultUiState = FetchResultUiState.Failed(result.reason))
                }
            }
        }
    }

    private fun updateFetchingState(isFetching: Boolean) {
        _uiState.value = _uiState.value.copy(isFetchingOnlineVersion = isFetching)
    }

    fun clearFailedFetchedResult() {
        _uiState.value = _uiState.value.copy(fetchResultUiState = FetchResultUiState.NoneFetch)
    }

    private suspend fun fetchOnlineVersionCode(): SealedFetchVersionCodeResult {
        updateFetchingState(true)
        return withContext(Dispatchers.IO) {
            try {
                val connect = Jsoup.connect("https://mindustrygame.github.io/wiki/")
//                爬虫必备请求头
                    .userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36"
                    ).timeout(3000)

                val document: Document = connect.get()
                val elements: Elements =
                    document.select("a[href^=https://github.com/Anuken/Mindustry/releases/tag/]")
                if (elements.size == 1) {
                    println(elements.first())
                    val version = elements.first()?.text()
                    if (version != null) {
                        SealedFetchVersionCodeResult.Success(version)
                    } else {
                        throw Exception("网站改版提示：找到版本信息位置却未找到信息")
                    }
                } else {
                    throw Exception("网站改版提示：未找到版本位置")
                }
            } catch (e: Exception) {
                SealedFetchVersionCodeResult.Failed("错误原因: ${e.message}")
            } finally {
                updateFetchingState(false)
            }
        }
    }

}