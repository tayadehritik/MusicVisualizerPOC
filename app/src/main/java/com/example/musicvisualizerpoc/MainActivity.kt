package com.example.musicvisualizerpoc

import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
                        if(bytes==null)
                        {
                            startVisualizer()
                        }


                        val data = remember {
                            mutableStateListOf<ByteArray>()
                        }
                        if(!data.isEmpty())
                        {
                            Log.d("name", Arrays.toString(data.first()))


                        }
                        Canvas(modifier = Modifier.fillMaxSize()) {

                            val nonEmpty = byteArrayOf(0x01, 0x02, 0x03)
                            data.clear()
                            if(bytes!=null)
                            {
                                data.add(bytes!!)
                            }
                            else{
                                data.add(nonEmpty)
                            }

                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            drawLine(
                                start = Offset(x = canvasWidth, y = 0f),
                                end = Offset(x = 0f, y = canvasHeight),
                                color = Color.Blue
                            )
                        }

                        //Initial composition

                    }
                }
            }
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
fun drawWaveForm()  {

}