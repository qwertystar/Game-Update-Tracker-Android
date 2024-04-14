/*
 * Copyright (c) 2024 qwertystar
 * Game Update Tracker is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.qwertystar.gameupdatetracker.ui

data class VersionCodeUiState(

    val localVersionCode: String = "",
    val onlineVersionCode: String = "",
    val isFetchingOnlineVersion: Boolean = false,

//    fetch结果状态控制
    val fetchResultUiState: FetchResultUiState = FetchResultUiState.NoneFetch
)

// 定义fetch后界面的四种互斥状态
sealed class FetchResultUiState {
    data class Success(val versionCode: String) : FetchResultUiState()
    data object VersionSame : FetchResultUiState()
    data class Failed(val reason: String) : FetchResultUiState()
    data object NoneFetch : FetchResultUiState()


}