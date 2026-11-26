package com.cristo.escanf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.cristo.escanf.ui.theme.EscanfTheme
import com.cristo.escanf.ui.theme.EscanfTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EscanfTheme() {
                // Pantalla temporal: en el commit de navegación se cambia por MainAppScreen()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(text = "ScanFood", modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}