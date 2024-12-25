package com.example.kplogbook.ui.component.utils


import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
//import com.example.kerjapraktek.ui.addEvent.InputField
import java.util.*

@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Date Picker Dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onDateSelected(formattedDate)
        },
        year, month, day
    )

//    // Field with Calendar Icon
//    InputField(
//        label = label,
//        value = selectedDate,
//        trailingIcon = {
//            Icon(
//                painter = painterResource(id = android.R.drawable.ic_menu_my_calendar),
//                contentDescription = "Calendar Icon",
//                tint = Green,
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable {
//                        datePickerDialog.show()
//                    }
//            )
//        },
//        onValueChange = {}
//    )
}
