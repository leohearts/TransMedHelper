package com.leohearts.transhelper

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.leohearts.transhelper.room.Converters
import com.leohearts.transhelper.room.dao.MedicationItemDao
import com.leohearts.transhelper.room.entity.MedicationItemEntiry
import java.util.Date
import java.time.LocalDate


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun medicationTab(){        // impl of meditation tab to navigate

    val data = db!!.medicationItemDao().getAll()
    if (data.isEmpty()){
        db!!.medicationItemDao().insertAll(MedicationItemEntiry(name="Oestrogel",
            dose="1",
            creationDate = Date(),
            remindTime = arrayListOf(arrayListOf(9,0,1)),
            unit = "ruler",
            id = 0      // id = 0 -> auto set by engine
        ))

        db!!.medicationItemDao().insertAll(MedicationItemEntiry(name="DXM",
            dose="16",
            creationDate = Date(),
            remindTime = arrayListOf(arrayListOf(23,0,1)),
            unit = "tabs * 15 mg",
            id = 0
        ))
    }
    LazyColumn(
    ) {
        items(data) { entry ->
                BoxWithConstraints(
                    content = {
                        val _maxWidth = maxWidth
                        Row(modifier = Modifier
                            .width(_maxWidth),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedCard(modifier = Modifier
                                .width(_maxWidth * 0.85f)
                                .padding(vertical = 10.dp)
                            ) {
                                Surface(    // Card body
                                    onClick = {
                                        topLevelNavHostController!!.navigate("addMedicineDialog/" + entry.id.toString())
                                    }
                                ) {
                                    ListItem(
                                        headlineContent = { Text(entry.name) },
                                        leadingContent = {
                                            Box(
                                                modifier = Modifier
                                                    .size(35.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        FloatingActionButtonDefaults.containerColor
                                                    ),
                                                contentAlignment = Alignment.Center

                                            ){
                                                Icon(
                                                    Icons.Rounded.Medication,
                                                    contentDescription = "icon_medication"
                                                )
                                            }
                                        },
                                        supportingContent = { Text(entry.dose + " " + entry.unit) },
                                        overlineContent = { Text(Converters().remindTimeToString(entry.remindTime)) },
                                        trailingContent = {
                                            Row (   // trailing body, contains a dose status and a edit/info button
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                val primaryColor = MaterialTheme.colorScheme.primary
                                                val errorColor = MaterialTheme.colorScheme.error
                                                var currStatusInt by rememberSaveable { mutableStateOf(0) }
                                                var currStatus = listOf(Icons.Rounded.HelpOutline, Icons.Rounded.Done, Icons.Rounded.Close)[currStatusInt]
                                                var currTint = listOf(primaryColor, primaryColor, errorColor)[currStatusInt]
                                                IconButton(
                                                    onClick = {     //  Change dose status
                                                        currStatusInt = (currStatusInt + 1 ) % 3
                                                    }
                                                ){
                                                    Icon(
                                                        currStatus,
                                                        contentDescription = "Dose status",
                                                        tint = currTint
                                                    )
                                                }

                                                Box(        //  My vertical "Divider" impl !
                                                    modifier = Modifier
                                                        .height(40.dp)
                                                        .width(DividerDefaults.Thickness)
                                                        .background(color = DividerDefaults.color)
                                                )
                                                IconButton(
                                                    onClick = {
                                                        topLevelNavHostController!!.navigate("addMedicineDialog/" + entry.id.toString())
                                                    }
                                                ){
                                                    Icon(
                                                        Icons.Rounded.ChevronRight,
                                                        contentDescription = "Details and edit"
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }

                    }
                )
        }

    }
}