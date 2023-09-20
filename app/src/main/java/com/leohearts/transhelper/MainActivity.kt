package com.leohearts.transhelper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.Room
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.leohearts.transhelper.room.AppDatabase
import com.leohearts.transhelper.ui.theme.TransHelperTheme

var db: AppDatabase? = null

@SuppressLint("StaticFieldLeak")
var topLevelNavHostController: NavHostController? = null

class MainActivity : ComponentActivity() {      //  Activity init for old-school Android things
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            TransHelperTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {    //  make system things transparent and immersive
                    systemUiController.setSystemBarsColor(Color.Transparent)
                    systemUiController.setNavigationBarColor(Color.Transparent)
                    db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "transHelperDb"
                    ).allowMainThreadQueries().build()

                }

                topLevelNavHostController = rememberNavController()
                val startDestination = "mainPage"
                // A surface container using the 'background' color from the theme, and Composer starts :)
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TopLevelNavController(topLevelNavHostController!!, startDestination)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Main(){         //  UI structure

    var tabAddName by remember { mutableStateOf("Medicine") }
    var tabAddNavName by remember {mutableStateOf("addMedicineDialog/0")}


    var navController = rememberNavController()
    val startDestination = "meditationTab"

    Scaffold(
        topBar = { MainTopBar() },
        content = {
            innerPadding -> MyNavController(navController, startDestination, innerPadding)
        },
        bottomBar = {
            val items = listOf("Medicate", "Records", "Community")
            val tabAddNames = listOf("Medicine", "Record", "Friend")
            val tabAddNavNames = listOf("addMedicineDialog/0", "addMedicineDialog/0", "addMedicineDialog/0")
            val tabNaviNames = listOf("meditationTab", "recordsTab", "communityTab")
            val icons = listOf(Icons.Rounded.Medication, Icons.Rounded.ShowChart, Icons.Rounded.People)

            var selectedItem by rememberSaveable { mutableStateOf(0) }

            NavigationBar (){
                items.forEachIndexed { i, e ->
                    NavigationBarItem(selected = selectedItem == i, label = {Text(e)},onClick =
                    {
                        selectedItem = i
                        tabAddName = tabAddNames[i]
                        tabAddNavName = tabAddNavNames[i]
                        navController.navigate(tabNaviNames[i]) {
                            popUpTo(0)
                        }
                    },
                        icon = {Icon(icons[i], contentDescription = e)})
                }
            }
        },
        floatingActionButton =
        {
            ExtendedFloatingActionButton(
                onClick = {
                          topLevelNavHostController!!.navigate(tabAddNavName)
                },
                icon = { Icon(Icons.Rounded.Add, "Add Medicine") },
                text = { Text("Add $tabAddName")}
            )
        }
    )
}


@Composable
fun MyNavController(    // MainPage Navigation Controller
    navController: NavHostController,
    startDestination: String,
    innerPadding: PaddingValues
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("meditationTab"){ medicationTab() }
        composable("recordsTab"){ recordsTab() }
        composable("communityTab"){ communityTab() }
    }
}

@Composable
fun TopLevelNavController(    // MainPage Navigation Controller
    navController: NavHostController,
    startDestination: String
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("mainPage"){ Main() }
        composable("addMedicineDialog/{id}"){ entry-> entry.arguments?.getString("id")?.toInt()?.let { AddMedicineDialog(it) } }
    }
}



val transFlag = "\uD83C\uDFF3️\u200D⚧️"             //  Transgender flag for android 12+ (Don't worry, targetSDK is android 13 :)

@Composable
@ExperimentalMaterial3Api
fun MainTopBar(){       //  App top bar
    CenterAlignedTopAppBar(
        title = {
            Text(text = transFlag + " TransHelper️")
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.PersonOutline, contentDescription = "User")
            }
        }
    )
}



