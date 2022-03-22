package com.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


const val HOME_ROUTE = "home"
const val NOTE_ROUTE = "note"
const val LOGIN_ROUTE = "login"
const val SIGNUP_ROUTE = "signup"

@Composable
fun MainView() {
    val userVM = viewModel<UserViewModel>()

    if(userVM.username.value.isEmpty()) {
        StartView()
    } else {
        MainScaffoldView()
    }
}

@Composable
fun MainScaffoldView() {

    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBarView() },
        bottomBar = { BottomBarView(navController) },
        content = { MainContentView(navController) })
}

@Composable
fun MainContentView(navController: NavHostController) {
    val noteVM = viewModel<NoteViewModel>()
    val bookVM = viewModel<BookViewModel>()

    NavHost(navController = navController, startDestination = HOME_ROUTE) {
        composable( route = HOME_ROUTE ){ HomeView(bookVM) }
        composable( route = NOTE_ROUTE ){ NoteView(noteVM) }
    }
}

@Composable
fun HomeView(bookVM: BookViewModel) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF64BCDF))
        .height(400.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        bookVM.books.value.forEach {
            Text(text = it)
        }
        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
fun NoteView(noteVM: NoteViewModel) {

    var note by remember {mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF9BD5EB))
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text(text = "Grocery List") })
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = { noteVM.addNote( Note(note) ) }
        ) {
            Text(text = "Add Item")
        }

        Spacer(modifier = Modifier.height(10.dp))
        noteVM.notes.value.forEach {
            Divider(thickness = 2.dp)
            Text(text = it.message)
        }
        Divider(thickness = 2.dp)
    }
}

@Composable
fun BottomBarView(navController: NavHostController) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF7CCEEE)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = "home",
            modifier = Modifier.clickable { navController.navigate(HOME_ROUTE)}
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_grocery),
            contentDescription = "note",
            modifier = Modifier.clickable { navController.navigate(NOTE_ROUTE)}
        )
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
        verticalAlignment = Alignment.CenterVertically
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
            .height(400.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = emailRegister,
            onValueChange = { emailRegister = it },
            label = { Text(text = "Email") },
            shape = RoundedCornerShape(50)
        )
        OutlinedTextField(
            value = pwRegister,
            onValueChange = { pwRegister = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(50),

        )
        Button(
            onClick = { userVM.createUser(emailRegister, pwRegister)},
            modifier = Modifier.clip(
                RoundedCornerShape(50)
            )
        ) {
            Text(text = "Register", modifier = Modifier.padding(6.dp))


        }

        Text(text = "Already have an account? Sign in",
            modifier = Modifier.clickable(onClick = {navController.navigate(LOGIN_ROUTE)})
        )
    }
}

@Composable
fun LoginView(userVM: UserViewModel, navController: NavHostController) {

    var emailLogin by remember { mutableStateOf("") }
    var pwLogin by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = emailLogin,
            onValueChange = { emailLogin = it },
            label = { Text(text = "Email") },
            shape = RoundedCornerShape(50))
        OutlinedTextField(
            value = pwLogin,
            onValueChange = { pwLogin = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(50)
        )
        Button(onClick = { userVM.loginUser(emailLogin, pwLogin)}, modifier = Modifier.clip(
            RoundedCornerShape(36.dp))
        ) {
            Text(text = "Login", modifier = Modifier.padding(6.dp))
        }
        Text(text = "Don't have an account? Sign up",
            modifier = Modifier.clickable(onClick = {navController.navigate(SIGNUP_ROUTE)})

        )

    }
}