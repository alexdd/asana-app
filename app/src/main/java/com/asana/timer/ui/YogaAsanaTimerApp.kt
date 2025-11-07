package com.asana.timer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asana.timer.AppViewModelProvider
import com.asana.timer.AsanaTimerApplication
import com.asana.timer.ui.TimerViewModel
import com.asana.timer.ui.screens.AsanaEditorScreen
import com.asana.timer.ui.screens.MainListScreen
import com.asana.timer.ui.screens.SplashScreen
import com.asana.timer.ui.screens.TimerScreen

object SplashDestination {
    const val route = "splash"
}

object ListsDestination {
    const val route = "lists"
}

object EditorDestination {
    const val route = "editor"
    const val sequenceIdArg = "sequenceId"
    const val routeWithArgs = "$route?$sequenceIdArg={$sequenceIdArg}"

    fun createRoute(sequenceId: String?): String =
        if (sequenceId.isNullOrBlank()) route else "$route?$sequenceIdArg=$sequenceId"
}

@Composable
fun YogaAsanaTimerApp() {
    val navController = rememberNavController()
    val asanaViewModel: AsanaViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val listState by asanaViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = SplashDestination.route) {
        composable(SplashDestination.route) {
            SplashScreen(onFinished = {
                navController.navigate(ListsDestination.route) {
                    popUpTo(SplashDestination.route) { inclusive = true }
                }
            })
        }

        composable(ListsDestination.route) {
            MainListScreen(
                sequences = listState.sequences,
                onCreate = { navController.navigate(EditorDestination.createRoute(null)) },
                onEdit = { sequenceId -> navController.navigate(EditorDestination.createRoute(sequenceId)) },
                onDelete = asanaViewModel::delete,
                onStart = { sequenceId -> navController.navigate(TimerDestination.createRoute(sequenceId)) }
            )
        }

        composable(
            route = EditorDestination.routeWithArgs,
            arguments = listOf(
                navArgument(EditorDestination.sequenceIdArg) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val sequenceId = backStackEntry.arguments?.getString(EditorDestination.sequenceIdArg)
            val currentSequence = listState.sequences.find { it.id == sequenceId }

            AsanaEditorScreen(
                sequence = currentSequence,
                onSave = { asanaViewModel.upsert(it); navController.popBackStack() },
                onCancel = { navController.popBackStack() },
                onDelete = currentSequence?.let { seq ->
                    { asanaViewModel.delete(seq.id); navController.popBackStack() }
                }
            )
        }

        composable(
            route = TimerDestination.route,
            arguments = listOf(
                navArgument(TimerDestination.SEQUENCE_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val application = LocalContext.current.applicationContext as AsanaTimerApplication
            val sequenceId = backStackEntry.arguments?.getString(TimerDestination.SEQUENCE_ID_ARG) ?: ""
            
            // Manually set the sequenceId in SavedStateHandle
            val defaultArgs = android.os.Bundle().apply {
                putString(TimerDestination.SEQUENCE_ID_ARG, sequenceId)
            }
            
            val timerViewModel: TimerViewModel = viewModel(
                backStackEntry,
                factory = TimerViewModel.provideFactory(
                    repository = application.repository,
                    owner = backStackEntry,
                    defaultArgs = defaultArgs
                )
            )
            val timerState by timerViewModel.uiState.collectAsStateWithLifecycle()

            TimerScreen(
                state = timerState,
                onBack = { navController.popBackStack() },
                onStart = timerViewModel::startOrResume,
                onPause = timerViewModel::pause,
                onReset = timerViewModel::reset,
                onSkipForward = timerViewModel::skipForward,
                onSkipBackward = timerViewModel::skipBackward,
                onErrorDismiss = timerViewModel::acknowledgeError
            )
        }
    }
}

