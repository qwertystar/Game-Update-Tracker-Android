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

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertystar.gameupdatetracker.R
import com.qwertystar.gameupdatetracker.ui.theme.GameUpdateTrackerTheme

data class VersionCodeUiState(

    val localVersionCode: String = "",
    val onlineVersionCode: String = "",
    val isFetchingOnlineVersion: Boolean = false,
    //   成功获取到新的版本号
    val hasNewOnlineVersion: Boolean = false,
    //    爬到错误的逻辑值和错误原因
    val hasFailedFetchOnlineResult: Boolean = false,
    val failedFetchedOnlineResult: String = "",

//    线上版本与本地版本相同时提醒用户的逻辑值
    val hasSameVersionCode: Boolean = false
)

@Composable
fun VersionCodeScreen(
    versionCodeViewModel: VersionCodeViewModel = viewModel()
) {
    val versionCodeUiState by versionCodeViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .padding(25.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.gamename), style = typography.titleLarge
        )
        SelectionContainer {
            Text(
                text = stringResource(R.string.mindustry_anukendev_pvp),
                style = typography.bodyLarge
            )
        }


        VersionCodeLayout(hasNeedUpdate = versionCodeUiState.hasNewOnlineVersion,
            localVersion = versionCodeUiState.localVersionCode,
            onlineVersion = versionCodeUiState.onlineVersionCode,
            onLocalVersionChange = { versionCodeViewModel.handleLocalVersionCodeValueChanged(it) },
            updateNewVersion = { versionCodeViewModel.handleUpdateNewVersionCodeButtonClicked() },
            checkNewVersion = { versionCodeViewModel.checkNewVersionButtonClicked() },
            isFetching = versionCodeUiState.isFetchingOnlineVersion,
            hasFailedFetchOnlineResult = versionCodeUiState.hasFailedFetchOnlineResult,
            failedFetchedOnlineResult = versionCodeUiState.failedFetchedOnlineResult,
            clearFailedFetchedResult = { versionCodeViewModel.clearFailedFetchedResult() })

//        Toast是系统级提示，未来应该使用SnackBar
        if (versionCodeUiState.hasSameVersionCode) {
            Toast.makeText(LocalContext.current, "无更新", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun VersionCodeLayout(
    hasNeedUpdate: Boolean,
    localVersion: String,
    onlineVersion: String,
    onLocalVersionChange: (String) -> Unit,
    updateNewVersion: () -> Unit,
    checkNewVersion: () -> Unit,
    isFetching: Boolean,
    hasFailedFetchOnlineResult: Boolean,
    failedFetchedOnlineResult: String,
    clearFailedFetchedResult: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {
            OutlinedTextField(value = localVersion, onValueChange = onLocalVersionChange, label = {
                Text(text = "本地版本号")
            })
            Row {
                Button(onClick = checkNewVersion) {
                    Text(text = "现在查询最新版本号")
                }
                if (isFetching) {
                    CircularProgressIndicator()
                }
            }
            if (hasNeedUpdate) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "在线版本号",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                            .wrapContentWidth(Alignment.Start),
                        style = typography.titleSmall
                    )
                    SelectionContainer {
                        Text(
                            text = onlineVersion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp)
                                .wrapContentWidth(Alignment.End)
                        )
                    }
                }
                Button(onClick = updateNewVersion) {
                    Text(text = "已确认本地版本更新")
                }
            }
            if (hasFailedFetchOnlineResult) {
                SelectionContainer {
                    Text(
                        text = failedFetchedOnlineResult,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Magenta
                    )
                }
                Button(onClick = clearFailedFetchedResult) {
                    Text(text = "清空错误记录")
                }
            }

        }
    }
}


@Composable
@Preview
fun VersionCodeScreenPreview() {
    GameUpdateTrackerTheme {
        VersionCodeScreen()
    }
}