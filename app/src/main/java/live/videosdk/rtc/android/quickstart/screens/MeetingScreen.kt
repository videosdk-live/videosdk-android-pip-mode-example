package live.videosdk.rtc.android.quickstart.screens

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import live.videosdk.rtc.android.quickstart.MainApplication
import live.videosdk.rtc.android.quickstart.components.*
import live.videosdk.rtc.android.quickstart.model.MeetingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingScreen(
    viewModel: MeetingViewModel, navController: NavController, meetingId: String, context: Context
) {
    val app = context.applicationContext as MainApplication
    val activity = context as? Activity
    var isMeetingLeft = viewModel.isMeetingLeft
    val pipMode = viewModel.pipMode

    LaunchedEffect(isMeetingLeft) {
        if (isMeetingLeft) {
            navController.navigate("join_screen")
            viewModel.reset()
        }
    }

    if (!pipMode.value) {
        Column(modifier = Modifier.fillMaxSize()) {

            Header(meetingId)
            MySpacer()

            ParticipantsGrid(
                gridCells = GridCells.Fixed(2),
                participants = viewModel.participants, modifier = Modifier.weight(1f)
            )

            MySpacer()
            MediaControlButtons(
                onJoinClick = { viewModel.initMeeting(context, app.sampleToken, meetingId) },
                onMicClick = { viewModel.toggleMic() },
                onCamClick = { viewModel.toggleWebcam() },
                onLeaveClick = { viewModel.leaveMeeting() },
                onPiPClick = { pipMode.value = viewModel.meetingJoined.value })

        }
    }

    if (pipMode.value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            activity?.enterPictureInPictureMode(
                PictureInPictureParams.Builder().setAspectRatio(Rational(16, 9)).build()
            )
        }

        if (viewModel.participants.size == 1) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    ParticipantsGrid(
                        gridCells = GridCells.Fixed(1),
                        participants = viewModel.participants.take(1),
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You are alone \n in the \n meeting",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        } else {
            ParticipantsGrid(
                gridCells = GridCells.Fixed(2),
                participants = viewModel.participants.subList(0, 2),
                modifier = Modifier.fillMaxSize()
            )
        }
    }


}

@Composable
fun Header(meetingId: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Row {
            MyText("MeetingID: ", 28.sp)
            MyText(meetingId, 25.sp)
        }
    }
}

@Composable
fun MediaControlButtons(
    onJoinClick: () -> Unit,
    onMicClick: () -> Unit,
    onCamClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onPiPClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MyAppButton(onJoinClick, "Join")
            MyAppButton(onMicClick, "Toggle Mic")
            MyAppButton(onCamClick, "Toggle Cam")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MyAppButton(onLeaveClick, "Leave")
            MyAppButton(onPiPClick, "PiP Mode")
        }
    }
}