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

@Composable
fun VersionCodeScreen(
    versionCodeViewModel: VersionCodeViewModel = viewModel()
) {
    val versionCodeUiState by versionCodeViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .padding(25.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
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
                Text(text = stringResource(R.string.local_versioncode))
            })
            Row {
                Button(onClick = checkNewVersion) {
                    Text(text = stringResource(R.string.check_new_online_versioncode))
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
                            text = stringResource(R.string.online_versioncode),
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
                        Text(text = stringResource(R.string.comfirmed_update_local_versioncode))
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
                        Text(text = stringResource(R.string.clear_error_result))
                    }
                }

                is FetchResultUiState.VersionSame -> {
                    //        Toast是系统级提示，未来应该使用SnackBar

                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.no_update), Toast.LENGTH_LONG
                    ).show()
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