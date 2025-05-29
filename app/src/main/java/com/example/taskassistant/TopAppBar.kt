package com.example.taskassistant

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.ui.theme.bodyFontFamily
import com.example.taskassistant.ui.theme.displayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    showBackButton: Boolean = false,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 22.sp,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = displayFontFamily
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_small_left),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    // Handle notifications
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.bell),
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                onClick = {
                    // Handle settings
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}


@Preview
@Composable
fun PreviewAppTopBar(){
    AppTopBar("Mindly", true, rememberNavController())
}