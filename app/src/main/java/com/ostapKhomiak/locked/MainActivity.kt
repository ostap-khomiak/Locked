package com.ostapKhomiak.locked

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ostapKhomiak.locked.ui.theme.LockedTheme
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.collectAsState


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LockedTheme {
                Surface {
                    MainScreen(
                        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    )
                }

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(sharedPreferences: SharedPreferences) {
    val viewModel: AppViewModel = remember { AppViewModel(sharedPreferences) }
    val isLocked by viewModel.isLocked.collectAsState()
    val lastAccess by viewModel.lastAccess.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLocked) {
                Image(
                    painter = painterResource(R.drawable.lock),
                    contentDescription = "Locked"
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.unlock),
                    contentDescription = "Unlocked"
                )
            }
            Spacer(Modifier.padding(32.dp))

            Text("Last access: ${lastAccess}")

            Spacer(Modifier.padding(32.dp))

            Button(
                onClick = { viewModel.toggleLock() },
                modifier = Modifier.size(128.dp)
            ) {
                Text(if (isLocked) "Unlock" else "Lock")
            }

        }
    }

}
