package com.example.toyopay.commonComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.example.toyopay.ui.theme.NavyBlue
import com.example.toyopay.ui.theme.White

@Preview
@Composable
fun GenerateFunctionalButton(
    text: String = "Get Started",
    horizontalPadding: Int = 15,
    verticalPadding: Int=10,
    isEnabled: Boolean = true,
    containerColor : Color = NavyBlue,
    textColor : Color = White,
    modifier: Modifier = Modifier,
    height : Int = 72,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick.invoke() },
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(horizontalPadding.dp, verticalPadding.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor.copy(alpha = 0.9f),
            disabledContainerColor = Color.Gray,
        ),
        shape = RoundedCornerShape(20.dp),
        enabled = isEnabled
    ) {
        TayoPayTexts.TextAsSemiBold(text = text , color = textColor)
    }
}
