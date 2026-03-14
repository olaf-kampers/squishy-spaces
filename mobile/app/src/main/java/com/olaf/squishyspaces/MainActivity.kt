package com.olaf.squishyspaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olaf.squishyspaces.ui.AppState
import com.olaf.squishyspaces.ui.SquishyViewModel
import com.olaf.squishyspaces.ui.screens.ErrorScreen
import com.olaf.squishyspaces.ui.screens.HomeScreen
import com.olaf.squishyspaces.ui.screens.LoadingScreen
import com.olaf.squishyspaces.ui.screens.PreviewScreen
import com.olaf.squishyspaces.ui.screens.ResultScreen
import com.olaf.squishyspaces.ui.theme.SquishySpacesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SquishySpacesTheme {
                val viewModel: SquishyViewModel = viewModel()
                val state by viewModel.state.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (val s = state) {
                            is AppState.Home -> HomeScreen(viewModel)
                            is AppState.Preview -> PreviewScreen(
                                uri = s.uri,
                                contentResolver = contentResolver,
                                viewModel = viewModel,
                            )
                            is AppState.Loading -> LoadingScreen()
                            is AppState.Result -> ResultScreen(
                                analysis = s.analysis,
                                viewModel = viewModel,
                            )
                            is AppState.Error -> ErrorScreen(
                                message = s.message,
                                viewModel = viewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}
