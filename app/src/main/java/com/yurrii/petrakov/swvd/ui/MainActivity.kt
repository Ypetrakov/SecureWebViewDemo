package com.yurrii.petrakov.swvd.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yurrii.petrakov.swvd.ui.navigation.AppTopBar
import com.yurrii.petrakov.swvd.ui.navigation.Navigation
import com.yurrii.petrakov.swvd.ui.theme.SecureWebViewDemoTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val rawIntentData = intent?.dataString
        Log.d("Intent", rawIntentData ?: "")
        setContent {
            SecureWebViewDemoTheme {
                val commonViewModel: CommonViewModel = koinViewModel()
                val topBarState = commonViewModel.topBarState.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        topBarState.value.let {
                            if (it.showTopBar) {
                                AppTopBar(it)
                            }
                        }

                    }
                ) { innerPadding ->
                    Navigation(
                        Modifier.padding(innerPadding),
                        intent,
                        commonViewModel
                    )
                }
            }
        }
    }
}