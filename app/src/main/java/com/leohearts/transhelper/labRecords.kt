package com.leohearts.transhelper

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.*


val sampleData = "[{'time': '2021-03-17 (HRT-1m)',\n" +
        "  '睾酮 (T: ng/ml)': '2.95',\n" +
        "  '雌二醇 (E2: pg/ml)': '32.11',\n" +
        "  '泌乳素 (PROL: ng/ml)': '14.93',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.378',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '2.94',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '1.37'},\n" +
        " {'time': '2021-06-06 (+4d)',\n" +
        "  '睾酮 (T: ng/ml)': '5.24',\n" +
        "  '雌二醇 (E2: pg/ml)': '52.56',\n" +
        "  '泌乳素 (PROL: ng/ml)': '6.81',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.42',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '4.67',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '1.11'},\n" +
        " {'time': '2021-08-07 (+2m)',\n" +
        "  '睾酮 (T: ng/ml)': '2.33',\n" +
        "  '雌二醇 (E2: pg/ml)': '86.07',\n" +
        "  '泌乳素 (PROL: ng/ml)': '8.60',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.097',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '3.71',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '0.63'},\n" +
        " {'time': '2021-09-14 (色+1m)',\n" +
        "  '睾酮 (T: ng/ml)': '0.117',\n" +
        "  '雌二醇 (E2: pg/ml)': '73.30',\n" +
        "  '泌乳素 (PROL: ng/ml)': '22.11',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.129',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '<0.1',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '<0.1'},\n" +
        " {'time': '2021-10-26 (色6.25+1m)',\n" +
        "  '睾酮 (T: ng/ml)': '0.41',\n" +
        "  '雌二醇 (E2: pg/ml)': '126',\n" +
        "  '泌乳素 (PROL: ng/ml)': '27.85',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.16',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '<',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '<'},\n" +
        " {'time': '2021-12-01 (停色换诺4d)',\n" +
        "  '睾酮 (T: ng/ml)': '0.30',\n" +
        "  '雌二醇 (E2: pg/ml)': '36',\n" +
        "  '泌乳素 (PROL: ng/ml)': '28.27',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.30',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '<',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '0.23'},\n" +
        " {'time': '2021-12-28 (停色1m:6mgE2)',\n" +
        "  '睾酮 (T: ng/ml)': '2.81',\n" +
        "  '雌二醇 (E2: pg/ml)': '71.37',\n" +
        "  '泌乳素 (PROL: ng/ml)': '25.31',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.496',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '2.36',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '0.496'},\n" +
        " {'time': '2022-04-23 (色12.5/2d半个月)',\n" +
        "  '睾酮 (T: ng/ml)': '0.185',\n" +
        "  '雌二醇 (E2: pg/ml)': '73.68',\n" +
        "  '泌乳素 (PROL: ng/ml)': '-',\n" +
        "  '孕酮 (PROG: ng/ml)': '-',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '-',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '-'},\n" +
        " {'time': '2022-06-05 (色12.5/3d)',\n" +
        "  '睾酮 (T: ng/ml)': '0.174',\n" +
        "  '雌二醇 (E2: pg/ml)': '68.17',\n" +
        "  '泌乳素 (PROL: ng/ml)': '24.71',\n" +
        "  '孕酮 (PROG: ng/ml)': '0.12',\n" +
        "  '促黄体生成素 (LH: mIU/ml)': '0.31',\n" +
        "  '促卵泡成熟激素 (FSH: mIU/ml)': '0.26'}]"


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun labRecords(){

    val labResultData = JSONArray2ArrayConverter(sampleData).sortedByDescending { T->T.get("time") }        //  Sort by time desc

    var producer = HashMap<String, ArrayList<Float>>()
    for (dataOne in labResultData.sortedBy { T->T.get("time") }){       //  Sort it back by time to asc
        dataOne.forEach{ entry ->                   //  Flatten structured data
            if (producer.get(entry.key) == null){
                producer.set(entry.key, ArrayList<Float>())
            }
            try {
                producer.get(entry.key)?.add(entry.value.toFloat())
            } catch (e: java.lang.NumberFormatException){
                producer.get(entry.key)?.add(0f)        //  set to 0 if target is not valid float
            } catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    //  init mapChartItemEnabled to true
    var mapChartItemEnabled = HashMap<String, Boolean>()
    for (i in producer) {
        mapChartItemEnabled.set(i.key, true)
    }

    LazyColumn(content = {

        item {

            val chartEntryModelProducer = ChartEntryModelProducer(ArrayList<FloatEntry>())

            //  Filter Chips
            FlowRow(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            ){
                for (i in producer) {
                    if (i.key != "time") {
                        var selected by rememberSaveable { mutableStateOf(true) }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp, vertical = 0.dp)
                        ) {

                            FilterChip(
                                selected = selected,
                                onClick = {
                                    selected = !selected
                                    mapChartItemEnabled.set(i.key, selected)
                                    updateChart(chartEntryModelProducer, producer, mapChartItemEnabled)
                                          },
                                label = { Text(i.key) },
                                leadingIcon = if (selected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Rounded.Done,
                                            contentDescription = "icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                }
            }

            // Main data chart
            Chart(
                chart = lineChart(),
                chartModelProducer = chartEntryModelProducer,
                startAxis = startAxis(),
                bottomAxis = bottomAxis(),
            )
            updateChart(chartEntryModelProducer, producer, mapChartItemEnabled)

        }

        //  Details list
        items(labResultData){ eMap ->
            ListItem(
                headlineContent = { eMap.get("time")?.let { Text(it) } },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Science,
                        contentDescription = "icon",
                    )
                },
                supportingContent = {
                    LazyColumn(
                        Modifier.heightIn(30.dp, 300.dp),
                        content = {
                            items(eMap.keys.toList()){ e ->
                                if (e != "time")
                                    Text(e + ": " + eMap[e])
                            }
                        }
                    )
                }

            )
        }
    })
}

//  Convert imported JSON data to structured Java object
fun JSONArray2ArrayConverter(data: String): ArrayList<HashMap<String, String>> {
    val typeToken = object : TypeToken<ArrayList<HashMap<String, String>>>() {}.type
    val res =  Gson().fromJson<ArrayList<HashMap<String, String>>>(data, typeToken)
    return res;
}

//  Chip chick trigger function to update chart
fun updateChart(chartEntryModelProducer: ChartEntryModelProducer, producer: HashMap<String, ArrayList<Float>>, filter: HashMap<String, Boolean>) {
    var dynEntries = ArrayList<ArrayList<FloatEntry>>()
    producer.forEach { entry ->
        if (filter.get(entry.key) == true) {
            var count = 0
            var tmp = ArrayList<FloatEntry>();
            entry.value.forEach { it ->
                tmp.add(FloatEntry(count.toFloat(), it))
                count++
            }
            dynEntries.add(tmp)
        }
    }
    chartEntryModelProducer.setEntries(dynEntries)
}