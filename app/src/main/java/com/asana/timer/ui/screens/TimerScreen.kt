package com.asana.timer.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.asana.timer.ui.TimerUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    state: TimerUiState,
    onBack: () -> Unit,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSkipForward: () -> Unit,
    onSkipBackward: () -> Unit,
    onErrorDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var lastError by remember { mutableStateOf<String?>(null) }
    val view = LocalView.current

    // Keep screen on when timer is running
    LaunchedEffect(state.isRunning) {
        val activity = view.context as? Activity ?: return@LaunchedEffect
        val window = activity.window
        if (state.isRunning) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    LaunchedEffect(state.errorMessage) {
        val newError = state.errorMessage
        if (newError != null && newError != lastError) {
            snackbarHostState.showSnackbar(newError)
            lastError = newError
            onErrorDismiss()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.sequence?.name ?: "Timer") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (!state.isReady) {
            EmptyTimerState(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding))
        } else {
            TimerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                state = state,
                onStart = onStart,
                onPause = onPause,
                onReset = onReset,
                onSkipForward = onSkipForward,
                onSkipBackward = onSkipBackward
            )
        }
    }
}

@Composable
private fun EmptyTimerState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Keine Sequenz ausgewählt",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bitte wähle eine Liste und starte den Timer erneut.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TimerContent(
    modifier: Modifier = Modifier,
    state: TimerUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSkipForward: () -> Unit,
    onSkipBackward: () -> Unit
) {
    val currentAsana = state.currentAsana ?: state.sequence?.asanas?.firstOrNull()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = currentAsana?.title ?: "Bereit",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            if (!currentAsana?.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentAsana?.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = formatTime(state.remainingSeconds.takeIf { state.currentIndex >= 0 } ?: currentAsana?.durationSeconds ?: 0),
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fortschritt: ${state.currentIndex + 1} / ${state.sequence?.asanas?.size ?: 0}",
                style = MaterialTheme.typography.bodyMedium
            )

            AnimatedVisibility(visible = state.nextAsana != null && !state.isCompleted) {
                AssistChip(
                    modifier = Modifier.padding(top = 12.dp),
                    onClick = {},
                    enabled = false,
                    label = {
                        Text(text = "Als Nächstes: ${state.nextAsana?.title ?: ""}")
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }

            AnimatedVisibility(visible = state.isCompleted) {
                Text(
                    text = "Sequenz abgeschlossen!",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSkipBackward, enabled = state.currentIndex > 0) {
                    Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Zurück")
                }

                if (state.isRunning) {
                    IconButton(onClick = onPause) {
                        Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                    }
                } else {
                    IconButton(onClick = onStart, enabled = state.isReady) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start")
                    }
                }

                IconButton(onClick = onSkipForward, enabled = state.sequence?.asanas?.let { state.currentIndex < it.lastIndex } == true) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Weiter")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onReset) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Zurücksetzen")
                }
            }
        }
    }
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds.coerceAtLeast(0) / 60
    val seconds = totalSeconds.coerceAtLeast(0) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

