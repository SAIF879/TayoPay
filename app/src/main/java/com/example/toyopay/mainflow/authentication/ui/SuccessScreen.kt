package com.example.toyopay.mainflow.authentication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toyopay.commonComponents.GenerateFunctionalButton
import com.example.toyopay.commonComponents.TayoPayBackground
import com.example.toyopay.commonComponents.TayoPayTexts
import com.example.toyopay.naivgation.AuthenticationScreens
import com.example.toyopay.ui.theme.LightBlue


@Composable
fun SuccessScreen(navController: NavController) {
    TayoPayBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

   Column(modifier = Modifier.weight(0.6f) , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
       TayoPayTexts.TextAsBoldHeader("SignUp Success !!" , color = Color.Black)
       Spacer(modifier = Modifier.height(10.dp))
       TayoPayTexts.TextAsMedium(text = "Your account has been \n created successfully" , color = LightBlue)
       Spacer(modifier = Modifier.height(30.dp))
   }
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GenerateFunctionalButton("Login") {
                    navController.navigate(AuthenticationScreens.LoginScreen.route)
                }
            }

        }
    }
}