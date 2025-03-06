package com.example.top_academy_lab1.ui.theme.weeks

import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

data class Task(
    val title: String,
    val description: String,
    var isCompleted: Boolean = false,
    var priority: Int = 0, //0 - green, 1 - yellow, 2 - red
    val time: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManager(){
    val tasks = remember {mutableStateListOf<Task>()}
    val archiveTasks = remember {mutableStateListOf<Task>()}

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("")}
    var time by remember { mutableStateOf("")}

    var showArchiveTasks by remember { mutableStateOf(false) } //Если false - отображаем таски, иначе архивные

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true; },  containerColor= Color.LightGray)
            { Text("A",color = Color(0xFFE571F6),fontSize = 36.sp)}
        },
        topBar = {
            TopAppBar(
            title = {Text("Task manager", fontSize = 36.sp)},
                colors=TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray,
                    titleContentColor = Color.DarkGray))
        }){
        padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,


            ){

                Row(modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                ){
                    Spacer(modifier = Modifier.width(154.dp))
                    Button(onClick = { showArchiveTasks = false}, enabled = showArchiveTasks,
                        colors = ButtonDefaults.buttonColors(
                        contentColor = Color(0xFF353538),       // цвет текста
                        containerColor = Color(0x00434343),
                            disabledContainerColor = Color(0x00FF0000),
                            disabledContentColor = Color(0xFF000000)
                    )){
                        Text("active", fontSize = 24.sp)
                    }



                    Spacer(modifier = Modifier.width(24.dp))

                    Button(onClick = {showArchiveTasks = true}, enabled = !showArchiveTasks,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color(0xFF353538),       // цвет текста
                            containerColor = Color(0x00434343),
                            disabledContainerColor = Color(0x00FF0000),
                            disabledContentColor = Color(0xFF000000)
                        )){
                        Row(){
                            Text("archiv", fontSize = 24.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!showArchiveTasks){
                    tasks.sortWith(compareByDescending{it.priority})
                    tasks.forEach {
                            task -> TaskItem(task){
                            tasks.remove(task)
                            archiveTasks.add(task)
                        }
                    }
                }
                else{
                    archiveTasks.forEach {
                            task -> TaskItem(task){
                            archiveTasks.remove(task)
                        }
                    }
                }

            }
            if(showDialog){
                var priority: Int = 0
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Добавить новую задачу") },
                    text = {
                        Column {
                            TextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Название задачи") }
                            )
                            TextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Описание задачи") }
                            )
                            TextField(
                                value = time,
                                onValueChange = { time = it },
                                label = { Text("date + time ") }
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){

                                Column(horizontalAlignment = Alignment.CenterHorizontally){
                                    var isChecked by remember { mutableStateOf(false) }
                                    RadioButton (
                                        selected  = isChecked,
                                        onClick  = {
                                            isChecked = true
                                            priority = 0
                                        }
                                    )
                                    Text("0")
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally){
                                    var isChecked by remember { mutableStateOf(false) }
                                    RadioButton(
                                        selected = isChecked,
                                        onClick = {
                                            isChecked = true
                                            priority = 1
                                        }
                                    )
                                    Text("1")
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally){
                                    var isChecked by remember { mutableStateOf(false) }
                                    RadioButton(
                                        selected = isChecked,
                                        onClick = {
                                            isChecked = true
                                            priority = 2
                                        }
                                    )
                                    Text("2")
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            //tasks.sortedBy { it.priority }
                            if (title.isNotEmpty() && description.isNotEmpty()) {
                                tasks.add(Task(title, description, false, priority, time))
                                title = ""
                                description = ""
                                time = ""
                                showDialog = false

                            }
                        }) {
                            Text("Добавить")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Отмена")
                        }
                    }
                )
            }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: () -> Unit){
    var isChecked by remember { mutableStateOf(task.isCompleted) }

    val color: Color = when(task.priority){
        0 -> Color.Green
        1 -> Color.Yellow
        2 -> Color.Red
        else -> {Color.Cyan}
    }


    val colorList = listOf(color, Color.LightGray)
    val brush = Brush.horizontalGradient(
        colors = colorList,
        startX = 0f,
        endX = 500f,
    )
    Card(

        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black,
        ),
        modifier = Modifier
            .padding(8.dp)
    )
    {
        Box(
            modifier = Modifier
                .background(brush)
                .padding(start = 2000.dp, top = 8.dp)
        )

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            RadioButton(
                selected = isChecked,
                onClick = {
                    isChecked = true
                    task.isCompleted = true
                    onDelete();
                }

            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ){
                Text(task.title)
                Text(task.description)
                Text(task.time, textAlign = TextAlign.End )
            }

            IconButton(onClick = {
                onDelete();
            }, modifier = Modifier) { Icon(Icons.Default.Delete, contentDescription = null) }
        }
    }



}
