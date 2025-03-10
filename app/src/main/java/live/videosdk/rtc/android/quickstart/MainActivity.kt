package live.videosdk.rtc.android.quickstart

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import live.videosdk.rtc.android.quickstart.model.MeetingViewModel
import live.videosdk.rtc.android.quickstart.navigation.NavigationGraph
import live.videosdk.rtc.android.quickstart.ui.theme.Videosdk_android_compose_quickstartTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MeetingViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MeetingViewModel::class.java]

        requestPermissions()

        setContent {
            Videosdk_android_compose_quickstartTheme {
                MyApp(this, viewModel)
            }
        }
    }

    private fun requestPermissions() {
        val permissionsToRequest = REQUESTED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("Permissions", "${it.key} = ${it.value}")
            }
        }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        viewModel.pipMode.value = isInPictureInPictureMode
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    override fun onPictureInPictureRequested(): Boolean {
        viewModel.pipMode.value = viewModel.meetingJoined.value
        return super.onPictureInPictureRequested()
    }

    companion object {
        private val REQUESTED_PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(context: Context, viewModel: MeetingViewModel) {
    NavigationGraph(context = context, meetingViewModel = viewModel)
}
