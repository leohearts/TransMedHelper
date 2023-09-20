package com.leohearts.transhelper


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun recordsTab(){        // impl of meditation tab to navigate

    Column(
    ) {
        var state by rememberSaveable { mutableStateOf(0) }
        val titles = listOf("Lab Result", "Medication")
        val navValues = listOf("labRecords", "medicationRecords")
        val tabIcons = listOf(Icons.Rounded.Medication, Icons.Rounded.Science)

        var navController = rememberNavController()
        val startDestination = "labRecords"


        TabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                LeadingIconTab(
                    selected = state == index,
                    onClick = {
                        state = index
                        navController.navigate(navValues[index]) {
                            popUpTo(0)
                        }

                              },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                    icon = {Icon(tabIcons[index], contentDescription = title)}
                )
            }
        }


        RecordsNavController(navController, startDestination)
    }
}

@Composable
fun RecordsNavController(    // Navigation Controller
    navController: NavHostController,
    startDestination: String
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("labRecords"){ labRecords() }
        composable("medicationRecords"){ medicationRecords() }
    }
}