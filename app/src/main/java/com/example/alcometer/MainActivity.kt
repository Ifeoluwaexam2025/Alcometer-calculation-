package com.example.alcometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alcometer.ui.theme.AlcometerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcometerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AlcometerCalc(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AlcometerCalc(modifier: Modifier= Modifier){
    var weight by remember { mutableStateOf("") }
    var bottlesDrunk by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var hoursDrunk by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(0.0) }
    Column(modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = weight,
            onValueChange = {weight = it},
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = bottlesDrunk,
            onValueChange = { bottlesDrunk = it },
            label = { Text("Bottles") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = hoursDrunk,
            onValueChange = { hoursDrunk = it },
            label = { Text("Time (hours)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            RadioButton(
                selected = isMale,
                onClick = { isMale = true }
            )
            Text("Male",
                modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = !isMale,
                onClick = { isMale = false }
            )
            Text("Female")
        }

        Button(
            onClick = {
                val weightValue = weight.toDoubleOrNull() ?: 0.0
                val bottleValue = bottlesDrunk.toIntOrNull() ?: 0
                val timeValue = hoursDrunk.toDoubleOrNull() ?: 0.0

                result = calculateAlcometer(
                    weightValue,
                    bottleValue,
                    timeValue,
                    isMale
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        Text(
            text = "Blood Alcohol Level: %.2f".format(result)
        )
    }
}




fun calculateAlcometer(
    weight: Double,
    bottles: Int,
    time: Double,
    isMale: Boolean
): Double {

    val litres = bottles * 0.33
    val grams = litres * 8 * 4.5
    val burningAlco = weight / 10
    val gramsLeft = grams - (burningAlco * time)

    if (gramsLeft <= 0) return 0.0

    return if (isMale) {
        gramsLeft / (weight * 0.7)
    } else {
        gramsLeft / (weight * 0.6)
    }
}
