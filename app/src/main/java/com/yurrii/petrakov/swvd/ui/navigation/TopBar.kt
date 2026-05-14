package com.yurrii.petrakov.swvd.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.yurrii.petrakov.swvd.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(state: TopBarState) {

    var menuExpanded by remember { mutableStateOf(false) }


    TopAppBar(
        title = { Text(state.title,
            fontWeight = FontWeight.SemiBold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF1EEFF),
            titleContentColor = Color(0xFF090029),
            actionIconContentColor = Color(0xFF090029),
            navigationIconContentColor = Color(0xFF090029)
        ),
        navigationIcon = {
            if (state.showBack) {
                IconButton(onClick = state.onBack) {
                    Icon(painter = painterResource(R.drawable.back), contentDescription = "Back")
                }
            }
        },

        actions = {
            if (state.showOptions) {
                IconButton(onClick = {menuExpanded = !menuExpanded}) {
                    Icon(painter = painterResource(R.drawable.menu), contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {

                    state.actions.forEach { action ->
                        DropdownMenuItem(
                            text = { Text(action.text) },
                            onClick = {
                                menuExpanded = false
                                action.onClick()
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(action.icon),
                                    contentDescription = action.text
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

data class TopBarState(
    val showTopBar: Boolean,
    val showBack: Boolean,
    val title: String = "",
    val showOptions: Boolean = true,
    val onBack: () -> Unit = {},
    val onOptions: () -> Unit = {},
    val actions: List<TopBarAction> = emptyList()

)

data class TopBarAction(
    @param:DrawableRes val icon: Int,
    val text: String,
    val onClick: () -> Unit,
)