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
        MainScaffoldView()
    }
}

@Composable
fun MainScaffoldView() {
    /* var isAdmin by remember { mutableStateOf(false) } */

    /* Checkbox(checked = isAdmin, onCheckChange = { isAdmin = !isAdmin })
           if( isSaved ) {
           } else {}
        */
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
        composable( route = HOME_ROUTE ){ searchAndHome(bookVM) }
        composable( route = NOTE_ROUTE ){ NoteView(noteVM) }
        composable( route = RESERVATION_ROUTE ){ ReservationView(bookVM) }

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
fun searchAndHome(bookVM: BookViewModel) {
    val textVal = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        search(textVal)
        HomeView(bookVM, textVal)
    }
}

@Composable
fun search(textVal: MutableState<TextFieldValue>) {
    TextField(
        value = textVal.value,
        onValueChange = { textVal.value = it },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(Color.Black, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if(textVal.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        textVal.value = TextFieldValue("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            leadingIconColor = Color.Black,
            trailingIconColor = Color.Black,
            backgroundColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun HomeView(bookVM: BookViewModel, textVal: MutableState<TextFieldValue>) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally

    ) {
        bookVM.books.forEach {
            Column(modifier = Modifier
                .padding(16.dp, 6.dp, 12.dp, 16.dp),
                horizontalAlignment = CenterHorizontally,
            ) {

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
                                    name = it.name, author = it.author, image = it.image, description = it.description
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
        }

        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ReservationView(bookVM: BookViewModel) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally
    )
    {
        if(bookVM.reservedBooks.isEmpty()) {
            Text ( text = "Your Reservation List is empty")
        } else {
            bookVM.reservedBooks.forEach {
                Column(modifier = Modifier
                    .padding(16.dp, 6.dp, 12.dp, 16.dp),
                    horizontalAlignment = CenterHorizontally,
                ) {
                    Card(modifier = Modifier.fillMaxWidth(),
                        elevation = 8.dp,

                    ) {
                        Row(verticalAlignment = CenterVertically) {
                            AsyncImage(model = it.image, contentDescription = "", modifier = Modifier
                                .padding(12.dp)
                                .width(60.dp)
                            )
                            Column(
                                modifier = Modifier,
                            verticalArrangement = Arrangement.Center,
                            ) {
                                Text(text = it.name, color = Color.Black, fontSize = 16.sp)
                                Text(text = it.author, color= Color.DarkGray, fontSize = 12.sp)
                            }
                            Row(
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(12.dp),
                                     horizontalArrangement = Arrangement.End) {
                                Button(onClick = {
                                    bookVM.deleteReservation(Book(
                                        image = "", name = "", author = "", description = ""
                                    ))
                                },
                                    modifier= Modifier.size(50.dp),
                                    shape = CircleShape

                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_clear_white_18),
                                        contentDescription = "Delete",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Button(
                onClick = {
                    bookVM.confirmReservation(Book(
                        image = "", name = "", author = "", description = ""
                    ))
                },
                shape = RoundedCornerShape(36.dp),
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    text = "Confirm Reservation",
                    modifier = Modifier.padding(6.dp)
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
        }

    }
}


@Composable
fun NoteView(noteVM: NoteViewModel) {

    var note by remember {mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF9BD5EB))
        .padding(10.dp),
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
        .background(Color(0xFFD50000)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = "home",
            modifier = Modifier.clickable { navController.navigate(HOME_ROUTE)}
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_reserved),
            contentDescription = "reservation",
            modifier = Modifier.clickable { navController.navigate(RESERVATION_ROUTE)}
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

        Row(verticalAlignment = CenterVertically) {
            Text(text = "Don't have an account? ")
            Text(text = "Sign up", color = RedA700, modifier = Modifier.clickable(onClick = {navController.navigate(SIGNUP_ROUTE)}))

        }

    }
}