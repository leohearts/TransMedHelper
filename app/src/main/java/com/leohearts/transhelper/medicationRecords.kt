package com.leohearts.transhelper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun medicationRecords(){

    var sampleData = ArrayList<HashMap<String, String>>()
    var d1 = HashMap<String,String>()
    d1.set("name", "Oestrogel")
    d1.set("doseStatus", "y")
    d1.set("doseTime", "2023/04/18")
    sampleData.add(d1)
    var d2 = HashMap<String,String>()
    d2.set("name", "DXM")
    d2.set("doseStatus", "f")
    d2.set("doseTime", "2023/04/18")
    sampleData.add(d2)
    var d3 = HashMap<String,String>()
    d3.set("name", "Oestrogel")
    d3.set("doseStatus", "y")
    d3.set("doseTime", "2023/04/18")
    sampleData.add(d3)
    var d4 = HashMap<String,String>()
    d4.set("name", "DXM")
    d4.set("doseStatus", "y")
    d4.set("doseTime", "2023/04/18")
    sampleData.add(d4)
    LazyColumn(
    ) {
        val data = sampleData
        items(sampleData) { entry ->
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
                                    /* TODO */
                                }
                            ) {
                                val primaryColor = MaterialTheme.colorScheme.primary
                                val errorColor = MaterialTheme.colorScheme.error
                                var currStatusBool by rememberSaveable { mutableStateOf(entry.get("doseStatus") == "y") }
                                var currStatus = if (currStatusBool) Icons.Rounded.Done else Icons.Rounded.HelpOutline
                                var currTint = if (currStatusBool) primaryColor else errorColor
                                ListItem(
                                    headlineContent = { entry.get("name")?.let { Text(it) } },
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
                                    overlineContent = { entry.get("doseTime")?.let { Text(it) } },
                                    trailingContent = {

                                        IconButton(
                                            onClick = {     //  Change doseTime status
                                                currStatusBool = ! currStatusBool
                                            }
                                        ){
                                            Icon(
                                                currStatus,
                                                contentDescription = "doseTime status",
                                                tint = currTint
                                            )
                                        }

                                    },
                                    supportingContent = {
                                        Text(if (currStatusBool) "Done" else "Skipped")
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