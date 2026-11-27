package com.cristo.escanf.features.scanner.presentation.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.cristo.escanf.features.scanner.data.analyzer.BarcodeAnalyzer
import kotlinx.coroutines.delay
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun ScannerScreen(
    onBarcodeScanned: (String) -> Unit,
    onLogout: () -> Unit

) {
    val context = LocalContext.current

    var hasPermission by remember { mutableStateOf(hasCameraPermission(context)) }
    var showManualDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Si el usuario concede el permiso desde Ajustes y regresa a la app
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        hasPermission = hasCameraPermission(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasPermission) {
            CameraWithAnalysis(onBarcodeScanned = onBarcodeScanned)
            ScanOverlay()
        } else {
            PermissionRequired(
                onRequest = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                onOpenSettings = {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                }
            )
        }

        Surface(
            color = Color.Black.copy(alpha = 0.55f),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) { IconButton(onClick = onLogout) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White
                )
            }
        }

        FilledTonalButton(
            onClick = { showManualDialog = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Text("Escribir código manualmente")
        }
    }

    if (showManualDialog) {
        ManualBarcodeDialog(
            onDismiss = { showManualDialog = false },
            onConfirm = { code ->
                showManualDialog = false
                onBarcodeScanned(code)
            }
        )
    }
}

@Composable
private fun CameraWithAnalysis(onBarcodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnScanned by rememberUpdatedState(onBarcodeScanned)

    // Empieza en true: se ignoran lecturas el primer 1.5 s para que, al volver
    // del detalle, no se vuelva a leer el mismo producto de inmediato.
    val handled = remember { AtomicBoolean(true) }
    LaunchedEffect(Unit) {
        delay(1500)
        handled.set(false)
    }

    val executor = remember { Executors.newSingleThreadExecutor() }
    val previewView = remember {
        PreviewView(context).apply { scaleType = PreviewView.ScaleType.FILL_CENTER }
    }
    val analyzer = remember {
        BarcodeAnalyzer { code ->
            if (handled.compareAndSet(false, true)) {
                vibrate(context)
                currentOnScanned(code)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val providerFuture = ProcessCameraProvider.getInstance(context)
        var provider: ProcessCameraProvider? = null

        providerFuture.addListener({
            provider = providerFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { it.setAnalyzer(executor, analyzer) }

            try {
                provider?.unbindAll()
                provider?.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            } catch (e: Exception) {
                Log.e("Escanf", "No se pudo iniciar la cámara", e)
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            provider?.unbindAll()
            analyzer.close()
            executor.shutdown()
        }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}

@Composable
private fun BoxScope.ScanOverlay() {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .size(width = 300.dp, height = 170.dp)
            .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
    )
    Surface(
        color = Color.Black.copy(alpha = 0.55f),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Apunta al código de barras",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun PermissionRequired(
    onRequest: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Escanf necesita la cámara para leer códigos de barras",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequest) { Text("Dar permiso") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onOpenSettings) { Text("Abrir ajustes") }
    }
}

@Composable
private fun ManualBarcodeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var code by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Código de barras") },
        text = {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it.filter(Char::isDigit).take(14) },
                label = { Text("8 a 14 dígitos") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(code) }, enabled = code.length in 8..14) {
                Text("Buscar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private fun hasCameraPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED

private fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Vibrator::class.java) ?: return
    vibrator.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
}