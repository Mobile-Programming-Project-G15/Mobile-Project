package com.example.project

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.ui.theme.RedA700

const val HOME_ROUTE = "home"
const val ADDBOOK_ROUTE = "addBook"
const val LOGIN_ROUTE = "login"
const val SIGNUP_ROUTE = "signup"
const val RESERVATION_ROUTE = "reservation"

@Composable
fun MainView() {
    val userVM = viewModel<UserViewModel>()

    if(userVM.username.value.isEmpty()) {
        StartView()
    } else {
        MainScaffoldView(userVM)
    }
}

@Composable
fun MainScaffoldView(userVM: UserViewModel) {

           if( userVM.isAdmin.value ) {
               val navController = rememberNavController()
               Scaffold(
                   topBar = { TopBarView() },
                   bottomBar = { BottomBarViewAdmin(navController) },
                   content = { MainContentViewAdmin(navController) })
           } else {
               val navController = rememberNavController()
               Scaffold(
                   topBar = { TopBarView() },
                   bottomBar = { BottomBarViewUser(navController) },
                   content = { MainContentViewUser(navController) })
           }
}

@Composable
fun TopBarView() {
    val userVM = viewModel<UserViewModel>()

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFD50000))
        .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Text(text = userVM.username.value)
        OutlinedButton(onClick = { userVM.logoutUser() }) {
            Text(text = "Log out")
        }
    }
}

@Composable
fun StartView() {
    val userVM = viewModel<UserViewModel>()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LOGIN_ROUTE) {
        composable( route = LOGIN_ROUTE ){ LoginView(userVM, navController = navController) }
        composable( route = SIGNUP_ROUTE ){ SignupView(userVM, navController = navController) }
    }
}

@Composable
fun SignupView(userVM: UserViewModel, navController: NavHostController) {
    var emailRegister by remember { mutableStateOf("") }
    var pwRegister by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(38.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        SPRLogo()

        OutlinedTextField(
            value = emailRegister,
            onValueChange = { emailRegister = it },
            label = { Text(text = "Email") },
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(12.dp)
        )
        OutlinedTextField(
            value = pwRegister,
            onValueChange = { pwRegister = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(12.dp)

            )
        Checkbox(checked = userVM.isAdmin.value, onCheckedChange = { userVM.isAdmin.value = it })
        Text(text="Register as Admin",modifier = Modifier.padding(6.dp))

        Button(
            onClick = { userVM.createUser(emailRegister, pwRegister)},
            modifier = Modifier.clip(
                RoundedCornerShape(50),

            )
        ) {
            Text(text = "Register", modifier = Modifier.padding(8.dp))
        }

        Row(verticalAlignment = CenterVertically, modifier = Modifier.padding(12.dp)) {

            Text(text = "Already have an account? ")
            Text(text = "Sign in", color = RedA700, modifier = Modifier.clickable(onClick = {navController.navigate(LOGIN_ROUTE)}))
        }

    }
}

@Composable
fun SPRLogo() {
    val image: Painter = painterResource(id = R.drawable.composelogo)
    Image(painter = image,contentDescription = "Default logo",
        modifier = Modifier.width(250.dp))
}

@Composable
fun LoginView(userVM: UserViewModel, navController: NavHostController) {

    var emailLogin by remember { mutableStateOf("") }
    var pwLogin by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(38.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        SPRLogo()

        OutlinedTextField(
            value = emailLogin,
            onValueChange = { emailLogin = it },
            label = { Text(text = "Email") },
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(12.dp))
        OutlinedTextField(
            value = pwLogin,
            onValueChange = { pwLogin = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(12.dp)
        )

        Checkbox(checked = userVM.isAdmin.value, onCheckedChange = { userVM.isAdmin.value = it })
        Text(text="I'm an Admin",modifier = Modifier.padding(6.dp))

        Button(onClick = { userVM.loginUser(emailLogin, pwLogin)}, modifier = Modifier.clip(
            RoundedCornerShape(36.dp))
        ) {
            Text(text = "Login", modifier = Modifier.padding(8.dp))
        }

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = CenterVertically) {
            Text(text = "Don't have an account? ")
            Text(text = "Sign up", color = RedA700, modifier = Modifier.clickable(onClick = {navController.navigate(SIGNUP_ROUTE)}))

        }
    }
}