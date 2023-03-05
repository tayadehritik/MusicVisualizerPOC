package com.example.musicvisualizerpoc

import android.content.Intent
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicvisualizerpoc.ui.theme.MusicVisualizerPOCTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.*


var recordAudioPermissionGranted = false
var modifyAudioSettingsPermissionGranted = false


class MainActivity : ComponentActivity(), Visualizer.OnDataCaptureListener {

    var bytes:ByteArray? = null
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
                        foregroundServiceButtons()

                        if(bytes==null)
                        {
                            startVisualizer()
                        }


                        val data = remember {
                            mutableStateListOf<ByteArray>()
                        }
                        if(!data.isEmpty())
                        {
                            //Log.d("name", Arrays.toString(data.first()))

                        }

                        Spacer(
                            modifier = Modifier
                                .drawWithCache {
                                    val nonEmpty = byteArrayOf(0x01, 0x02, 0x03)
                                    data.clear()
                                    if (bytes != null) {
                                        data.add(bytes!!)
                                    } else {
                                        data.add(nonEmpty)
                                    }

                                    val canvasWidth = size.width
                                    val canvasHeight = size.height

                                    val xIncrement: Float = canvasWidth / data.first().size
                                    val yIncrement: Float = canvasHeight / 0xFF
                                    val halfHeight = (canvasHeight * 0.5)
                                    val path = Path()
                                    path.moveTo(0f, halfHeight.toFloat())
                                    val waveform = data.first()
                                    for (i in 1 until waveform.size) {
                                        val yPosition: Float =
                                            if (waveform.get(i) > 0) canvasHeight - yIncrement * waveform.get(
                                                i
                                            ) else -(yIncrement * waveform.get(
                                                i
                                            ))
                                        path.lineTo(xIncrement * i, yPosition)
                                    }
                                    path.lineTo(canvasWidth, halfHeight.toFloat())

                                    path.close()
                                    onDrawBehind {
                                        drawPath(path, Color.Magenta, style = Stroke(width = 10f))
                                    }
                                }
                                .fillMaxSize()
                        )




                        //Initial composition

                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // If the screen is off then the device has been locked
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val isScreenOn: Boolean = powerManager.isInteractive

        if (!isScreenOn) {
            // The screen has been locked
            // do stuff...
            Log.d("screen", "screen is off")
        }
    }



    fun startVisualizer() {

        if(recordAudioPermissionGranted and modifyAudioSettingsPermissionGranted)
        {
            val visualizer = Visualizer(0)
            visualizer.setEnabled(false)
            visualizer.setDataCaptureListener(this,Visualizer.getMaxCaptureRate(), true, false)
            visualizer.setCaptureSize(256)
            visualizer.setEnabled(true);

        }
    }


    override fun onWaveFormDataCapture(
        visualizer: Visualizer?,
        waveform: ByteArray?,
        samplingRate: Int
    ) {
        bytes = waveform
    }

    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {

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
fun drawWaveForm(bytes:ByteArray, width:Float, height:Float)  {

}

@Composable
fun foregroundServiceButtons() {
    Column {
        val currentContext = LocalContext.current
        Button(onClick = {
            val serviceIntent = Intent(currentContext, ExampleService::class.java)
            currentContext.startService(serviceIntent)
        }) {
            Text(text = "Start Foreground Service")
        }
        Button(onClick = {
            val serviceIntent = Intent(currentContext, ExampleService::class.java)
            currentContext.stopService(serviceIntent)
        }) {
            Text(text = "Stop Foreground Service")
        }
    }
}