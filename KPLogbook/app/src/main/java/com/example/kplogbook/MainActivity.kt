package com.example.kplogbook

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kplogbook.data.pref.UserPreference
import com.example.kplogbook.data.pref.dataStore
import com.example.kplogbook.ui.group.CreateGroupScreen
import com.example.kplogbook.ui.home.HomeScreen
import com.example.kplogbook.ui.login.LoginScreen
import com.example.kplogbook.ui.pendaftaran.PendaftaranScreen
import com.example.kplogbook.ui.registration.KPRegistrationScreen
import com.example.kplogbook.ui.requestDetail.EditRequestScreen
import com.example.kplogbook.ui.requestDetail.RequestDetailScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            EvelinApp()
        }
    }

}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EvelinApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreference = UserPreference.getInstance(context.dataStore)
    val userSession = userPreference.getSession().collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userSession.value) {
        val isLoggedIn = userSession.value?.isLogin == true

        if (isLoggedIn) {
            navController.navigate("pendaftaran") {
            }
        } else {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }



    AnimatedNavHost(navController = navController, startDestination = if (userSession.value?.isLogin == true) "home" else "login") {
//    AnimatedNavHost(navController = navController, startDestination = "home") {
        composable(
            "login",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { LoginScreen(context, navController) }
        composable(
            "home",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { HomeScreen(navController = navController) }
        composable(
            "pendaftaran",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { PendaftaranScreen(navController = navController) }
        composable(
            "registration",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { KPRegistrationScreen(navController = navController) }
        composable(
            "reg-group",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { CreateGroupScreen(navController = navController) }
        composable(
            "request",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { RequestDetailScreen(navController = navController) }
        composable(
            "edit-request",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { EditRequestScreen(navController = navController) }



    }
}