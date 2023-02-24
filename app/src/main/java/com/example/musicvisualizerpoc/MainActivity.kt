package com.example.musicvisualizerpoc

import android.media.audiofx.Visualizer
import android.opengl.ETC1.getHeight
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicvisualizerpoc.ui.theme.MusicVisualizerPOCTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.*
import kotlin.math.ceil


var recordAudioPermissionGranted = false
var modifyAudioSettingsPermissionGranted = false


class MainActivity : ComponentActivity(), Visualizer.OnDataCaptureListener {
    var bytes:ByteArray = byteArrayOf(0x01, 0x02, 0x03)
    val density:Float = 50f
    val gap:Int = 4
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MusicVisualizerPOCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        getRecordAudioPermission()
                        getModifyAudioSettingsPermission()
                        startVisualizer()

                        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            val barWidth = canvasWidth / density
                            val div = bytes!!.size / density
                            Log.d("here",Arrays.toString(bytes))
                            for (i in 0 until density.toInt()) {
                                val bytePosition = ceil((i * div).toDouble()).toInt()
                                val top = canvasHeight +
                                        (Math.abs(bytes!![bytePosition]+ 128) ) * canvasHeight / 128
                                val barX = i * barWidth + barWidth / 2
                                drawLine(
                                    start = Offset(x = barX, y = canvasHeight),
                                    end = Offset(x = barX, y = top),
                                    color = Color.Blue,
                                    strokeWidth = barWidth - gap
                                )
                            }

                        } )

                    }
                }
            }
        }
    }



    fun startVisualizer() {

        if(recordAudioPermissionGranted and modifyAudioSettingsPermissionGranted)
        {
            val visualizer = Visualizer(0)
            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(),true, false)
            val capture_size = 256
            visualizer.setCaptureSize(capture_size)
            visualizer.setEnabled(true);

        }
    }

    override fun onWaveFormDataCapture(
        visualizer: Visualizer?,
        waveform: ByteArray?,
        samplingRate: Int
    ) {
        //Log.d("Visualizer", "Here in waveform data capture")
        bytes = waveform!!

    }

    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {
        Log.d("Visualizer", "Here in Fft data capture")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicVisualizerPOCTheme {
        Greeting("Android")
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun getRecordAudioPermission() {
    val recordAudioPermissionState = rememberPermissionState(permission =
    android.Manifest.permission.RECORD_AUDIO
    )
    if (recordAudioPermissionState.status.isGranted) {
        Text("Record Audio permission Granted")
        recordAudioPermissionGranted = true
    } else {
        Column {
            val textToShow = if (recordAudioPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Record Audio is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Record Audio permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { recordAudioPermissionState.launchPermissionRequest() }) {
                Text("Request Record Audio permission")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun getModifyAudioSettingsPermission() {
    val modifyAudioSettingsPermissionState = rememberPermissionState(permission =
    android.Manifest.permission.MODIFY_AUDIO_SETTINGS
    )
    if (modifyAudioSettingsPermissionState.status.isGranted) {
        Text("Modify Audio Settings permission Granted")
        modifyAudioSettingsPermissionGranted = true;
    } else {
        Column {
            val textToShow = if (modifyAudioSettingsPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Modify Audio Settings is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Modify Audio Settings permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { modifyAudioSettingsPermissionState.launchPermissionRequest() }) {
                Text("Request Modify Audio Settings permission")
            }
        }
    }
}

fun byteArrayToUnsignedInt(byteArray: ByteArray): Int {
    var result = 0
    for (i in byteArray.indices) {
        result = result or ((byteArray[i].toInt() and 0xFF) shl (8 * i))
    }
    return result
}
@Composable
fun drawWaveForm(bytes: ByteArray)  {

}