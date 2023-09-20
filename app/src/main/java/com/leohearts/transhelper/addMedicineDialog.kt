package com.leohearts.transhelper

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.TimePicker
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leohearts.transhelper.room.entity.MedicationItemEntiry
import java.util.*

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineDialog(id: Int){

    Log.i("Tag", id.toString())
    var isUsing by rememberSaveable { mutableStateOf(false) }       // prevent recomposition
    var currEntry by remember {mutableStateOf( MedicationItemEntiry(0, "", "", "", Date(), arrayListOf(arrayListOf(0,0,0))))}
    if (id != 0 && !isUsing)
        currEntry = db!!.medicationItemDao().loadById(id)

    var name by rememberSaveable { mutableStateOf(currEntry.name) }
    var dose by rememberSaveable { mutableStateOf(currEntry.dose) }
    var unit by rememberSaveable { mutableStateOf(currEntry.unit) }
    var remindTime by rememberSaveable { mutableStateOf(currEntry.remindTime) }


    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()
    var timePickerTarget by rememberSaveable { mutableStateOf(0) }
    var helper by rememberSaveable { mutableStateOf(0) }

    if (showTimePicker) {
        val confirmEnabled = derivedStateOf { state.minute != null }
        DatePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, state.hour)
                cal.set(Calendar.MINUTE, state.minute)
                cal.isLenient = false
                TextButton(
                    onClick = {
                        isUsing = true
                        showTimePicker = false
                        Log.i("TAG", timePickerTarget.toString())
                        remindTime[timePickerTarget].set(0, state.hour)
                        remindTime[timePickerTarget].set(1, state.minute)

                        currEntry.remindTime[timePickerTarget].set(0, state.hour)
                        currEntry.remindTime[timePickerTarget].set(1, state.minute)
                        helper = 1
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }

            },
        ) {
            TimePicker(state = state,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .padding(horizontal = 40.dp)
            )
        }
    }


    var openDeleteWarningDialog = remember { mutableStateOf(false) }

    if (openDeleteWarningDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDeleteWarningDialog.value = false
            },
            title = {
                Text(text = "Confirm")
            },
            text = {
                Text(text = "Confirm delete?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        db!!.medicationItemDao().delete(currEntry)
                        openDeleteWarningDialog.value = false
                        topLevelNavHostController!!.popBackStack()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDeleteWarningDialog.value = false
                    }
                ) {
                    Text("No!")
                }
            }
        )
    }

    Scaffold (
        topBar = {
            TopAppBar (
                title = { Text("Edit") },
                navigationIcon = {
                    IconButton(onClick = {
                        topLevelNavHostController!!.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },

                actions = {
                    IconButton(onClick = {
                        if (id != 0)
                            db!!.medicationItemDao().update(currEntry)
                        else
                            db!!.medicationItemDao().insertAll(currEntry)
                        topLevelNavHostController!!.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = "save edit"

                        )
                    }
                }


            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .padding(horizontal = 50.dp)
        ) {
            item {

                BoxWithConstraints(
                    content = {
                        val _maxWidth = maxWidth
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                currEntry.name = it
                                            },
                            label = { Text("Name") },
                            modifier = Modifier.width(_maxWidth)
                        )
                    }
                )
                BoxWithConstraints(
                    content = {
                        val _maxWidth = maxWidth
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(_maxWidth * 0.04f)
                        ) {
                            OutlinedTextField(
                                value = dose,
                                onValueChange = {
                                    dose = it
                                    currEntry.dose = it
                                                },
                                label = { Text("Dose") },
                                modifier = Modifier.width(_maxWidth * 0.48f)
                            )
                            OutlinedTextField(
                                value = unit,
                                onValueChange = {
                                    unit = it
                                    currEntry.unit = it
                                                },
                                label = { Text("Unit") },
                                modifier = Modifier.width(_maxWidth * 0.48f)
                            )
                        }
                    }
                )
            }
            item{
                for (i in 0..remindTime.size-1) {
                    if (remindTime[i].size == 3) {      //  errorsafe
                        BoxWithConstraints(
                            content = {
                                val _maxWidth = maxWidth
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(_maxWidth * 0.04f)
                                ) {
                                    OutlinedTextField(
                                        value = remindTime[i][0].toString() + ":" + remindTime[i][1].toString(),
                                        onValueChange = { },
                                        label = {
                                            Text("Remind Time")
                                                },
                                        modifier = Modifier
                                            .width(_maxWidth * 0.48f + 0.000001.dp * helper ),
                                        interactionSource = remember { MutableInteractionSource() }
                                            .also { interactionSource ->
                                                LaunchedEffect(interactionSource) {
                                                    interactionSource.interactions.collect {
                                                        if (it is PressInteraction.Release) {
                                                            // works like onClick
                                                            timePickerTarget = i
                                                            showTimePicker = true
                                                            helper = 0
                                                        }
                                                    }
                                                }
                                            },
                                        readOnly = true
                                    )
                                    var tmp by remember { mutableStateOf(remindTime[i][2].toString()) }
                                    OutlinedTextField(
                                        value = tmp,
                                        onValueChange = {
                                            try {
                                                tmp = it
                                                remindTime[i].set(2,it.trim().toInt())
                                                currEntry.remindTime[i].set(2,it.trim().toInt())
                                            }catch(e: java.lang.Exception){}
                                        },
                                        label = { Text("Every n days") },
                                        modifier = Modifier.width(_maxWidth * 0.48f)
                                    )
                                }
                            }
                        )
                    }
                }
            }

            item {          //  Add / Remove remind row button
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){

                    ElevatedButton(
                        onClick = {
                            isUsing = true  //  Prevent state lose
                            currEntry.remindTime.add(arrayListOf(0,0,0))
                            showTimePicker = true  //  recompose UI
                            showTimePicker = false
                        },
                    ) {
                        Text("Add row")
                    }

                    ElevatedButton(
                        onClick = {
                            if (currEntry.remindTime.size > 0) {
                                isUsing = true  //  Prevent state lose
                                currEntry.remindTime.removeLast()
                                showTimePicker = true  //  recompose UI
                                showTimePicker = false
                            }
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text("Remove row")
                    }
                }
            }
            item {          //  Delete medication button
                if (id != 0) {      //  not adding
                    ElevatedButton(
                        onClick = {
                            openDeleteWarningDialog.value = true
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}