package com.example.toyopay.mainflow.bottombar.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.toyopay.commonComponents.TayoPayTexts
import com.example.toyopay.mainflow.bottombar.util.BottomBarScreen
import com.example.toyopay.naivgation.BottomNavGraph
import com.example.toyopay.ui.theme.LightBlack
import com.example.toyopay.ui.theme.LightBlue
import com.example.toyopay.ui.theme.LightGrey
import com.example.toyopay.ui.theme.White
import com.example.toyopay.util.fonts.TayoPayFonts

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomBarScreen() {
    val navController = rememberNavController()
    val showBottomBar = rememberSaveable {
        mutableStateOf(true)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        "home" -> showBottomBar.value = true
        "transfer" -> showBottomBar.value = true
        "account" -> showBottomBar.value = true
        else -> showBottomBar.value = false
    }

    Scaffold(bottomBar = {
        BottomBar(
            navController = navController,
            bottomBarState = showBottomBar
        )
    }) {
        BottomNavGraph(navHostController = navController)
    }


}

@Composable
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    val screens = listOf(
       BottomBarScreen.Home,
        BottomBarScreen.Transfer,
        BottomBarScreen.Account,
    )


    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomBarScreenContent(
                navController = navController,
//                bottomBarState = bottomBarState,
                screens = screens
            )
        }
    )
}


@Composable
fun BottomBarScreenContent(
    navController: NavHostController,
    screens: List<BottomBarScreen>
) {
    CompositionLocalProvider {
        Card(
                shape = RoundedCornerShape(10.dp , 10.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 20.dp)
        ) {
            NavigationBar(containerColor = White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { menuItem ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == menuItem.route } == true

                    // adding each item
                    NavigationBarItem(
                        selected = (selected),
                        onClick = {
                            if (selected) return@NavigationBarItem
                            navController.navigate(menuItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector =  menuItem.iconOutlined,
                                contentDescription = menuItem.route,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        label = {
                            TayoPayTexts.TextAsBoldHeader(
                                text = menuItem.title,
                                color = if (!selected) LightGrey else LightBlue,
                                size = if (!selected) 15 else 18
                            )
                        },
                        enabled = true,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = LightBlue
                        )
                    )
                }
            }
        }
    }
}


