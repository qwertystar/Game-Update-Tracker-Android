/*
 * Copyright (c) 2023-2024 qwertystar
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

// 定义fetch后界面的四种互斥状态
sealed class FetchResultUiState {
    data class Success(val versionCode: String) : FetchResultUiState()
    data object VersionSame : FetchResultUiState()
    data class Failed(val reason: String) : FetchResultUiState()
    data object NoneFetch : FetchResultUiState()


}

data class VersionCodeUiState(

    val localVersionCode: String = "",
    val onlineVersionCode: String = "",
    val isFetchingOnlineVersion: Boolean = false,

//    fetch结果状态控制
    val fetchResultUiState: FetchResultUiState = FetchResultUiState.NoneFetch
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


        VersionCodeLayout(
            localVersion = versionCodeUiState.localVersionCode,
            onLocalVersionChange = { versionCodeViewModel.handleLocalVersionCodeValueChanged(it) },
            updateNewVersion = { versionCodeViewModel.handleUpdateNewVersionCodeButtonClicked() },
            checkNewVersion = { versionCodeViewModel.checkNewVersionButtonClicked() },
            isFetching = versionCodeUiState.isFetchingOnlineVersion,
            clearFailedFetchedResult = { versionCodeViewModel.clearFailedFetchedResult() },
            fetchResult = versionCodeUiState.fetchResultUiState
        )
    }
}

@Composable
fun VersionCodeLayout(
    localVersion: String,
    onLocalVersionChange: (String) -> Unit,
    updateNewVersion: () -> Unit,
    checkNewVersion: () -> Unit,
    isFetching: Boolean,
    clearFailedFetchedResult: () -> Unit,
    modifier: Modifier = Modifier,
    fetchResult: FetchResultUiState
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
            when (fetchResult) {
                is FetchResultUiState.Success -> {
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
                                text = fetchResult.versionCode,
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

                is FetchResultUiState.Failed -> {
                    SelectionContainer {
                        Text(
                            text = fetchResult.reason,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Magenta
                        )
                    }
                    Button(onClick = clearFailedFetchedResult) {
                        Text(text = "清空错误记录")
                    }
                }

                is FetchResultUiState.VersionSame -> {
                    //        Toast是系统级提示，未来应该使用SnackBar

                    Toast.makeText(LocalContext.current, "无更新", Toast.LENGTH_LONG).show()
                }

                is FetchResultUiState.NoneFetch -> {

                }
            }
//

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