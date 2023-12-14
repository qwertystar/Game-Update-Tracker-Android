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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertystar.gameupdatetracker.R
import com.qwertystar.gameupdatetracker.ui.theme.GameUpdateTrackerTheme

data class VersionCodeUiState(
    val hasNewOnlineVersion: Boolean = false,
    val localVersionCode: String = "",
    val onlineVersionCode: String = "",
    val isFetchingOnlineVersion: Boolean = false
)

@Composable
fun VersionCodeScreen(
    versionCodeViewModel: VersionCodeViewModel = viewModel()
) {
    val versionCodeUiState by versionCodeViewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.gamename))
        SelectionContainer {
            Text(text = stringResource(R.string.mindustry_anukendev_pvp))
        }


        VersionCodeLayout(
            hasNeedUpdate = versionCodeUiState.hasNewOnlineVersion,
            localVersion = versionCodeUiState.localVersionCode,
            onlineVersion = versionCodeUiState.onlineVersionCode,
            onLocalVersionChange = { versionCodeViewModel.handleLocalVersionCodeValueChanged(it) },
            updateNewVersion = { versionCodeViewModel.handleUpdateNewVersionCodeButtonClicked() },
            checkNewVersion = { versionCodeViewModel.checkNewVersionButtonClicked() },
            isFetching = versionCodeUiState.isFetchingOnlineVersion
        )

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
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {
            OutlinedTextField(value = localVersion, onValueChange = onLocalVersionChange, label = {
                Text(text = "本地版本号")
            })

            Button(onClick = checkNewVersion) {
                Text(text = "现在查询最新版本号")

            }
            if (hasNeedUpdate) {
                Row {
                    Text("在线版本号")
                    Text(onlineVersion)
                }


                Button(onClick = updateNewVersion) {
                    Text(text = "已确认本地版本更新")
                }
            }
            if (isFetching) {
                CircularProgressIndicator()
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