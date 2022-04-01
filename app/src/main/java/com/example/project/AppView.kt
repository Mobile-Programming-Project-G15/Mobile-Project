package com.example.project

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.project.ui.theme.RedA700

const val HOME_ROUTE = "home"
const val NOTE_ROUTE = "note"
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
    val userVM = viewModel<UserViewModel>()

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
fun ExpandableCard(title: String, body: String) {
    var expanded by remember { mutableStateOf(false)}

    Card{
        Column{
            Text(text = title)
            //Content
            if(expanded) {
                Text(text = body)
                IconButton(onClick = {expanded = false}) {
                    Icon(painter = painterResource(id = R.drawable.outline_expand_less_black_18), contentDescription = "Collapse")
                }
            } else {
                IconButton(onClick = {expanded = true}) {
                    Icon(painter = painterResource(id = R.drawable.outline_expand_more_black_18), contentDescription = "Expand")

                }
            }
        }
    }

/*
    Card(modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp
    ) {
        Row(verticalAlignment = CenterVertically) {
            AsyncImage(model = it.image, contentDescription = "", modifier = Modifier
                .padding(12.dp)
                .width(60.dp)
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = it.name, color = Color.Black, fontSize = 16.sp)
                Text(text = it.author, color= Color.DarkGray, fontSize = 12.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End){

                Button(
                    onClick = {
                        bookVM.addReservation(Book(
                            name = it.name, author = it.author, image = it.image
                        ))
                    },
                    modifier= Modifier.size(50.dp),
                    shape = CircleShape,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24),
                        contentDescription = "Add to reservations",
                        modifier = Modifier .fillMaxWidth() )
                }
            }
        }
    }

 */
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
            .height(400.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
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

        Row(verticalAlignment = CenterVertically) {
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
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        SPRLogo()

        OutlinedTextField(
            value = emailLogin,
            onValueChange = { emailLogin = it },
            label = { Text(text = "Email") },
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(6.dp))
        OutlinedTextField(
            value = pwLogin,
            onValueChange = { pwLogin = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(6.dp)
        )
        Button(onClick = { userVM.loginUser(emailLogin, pwLogin)}, modifier = Modifier.clip(
            RoundedCornerShape(36.dp))
        ) {
            Text(text = "Login", modifier = Modifier.padding(6.dp))
        }

        Checkbox(checked = userVM.isAdmin.value, onCheckedChange = { userVM.isAdmin.value = it })
        Text(text="Admin",modifier = Modifier.padding(5.dp))

        if(userVM.isAdmin.value) {
            Text(text= "ok works")
        }

        Row(verticalAlignment = CenterVertically) {
            Text(text = "Don't have an account? ")
            Text(text = "Sign up", color = RedA700, modifier = Modifier.clickable(onClick = {navController.navigate(SIGNUP_ROUTE)}))

        }

    }
}