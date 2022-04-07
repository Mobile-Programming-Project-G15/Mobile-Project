package com.example.project

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.AsyncImage

@Composable
fun MainContentViewAdmin(navController: NavHostController) {
    val bookVM = viewModel<BookViewModel>()

    NavHost(navController = navController, startDestination = HOME_ROUTE) {
        composable( route = HOME_ROUTE ){ SearchAndHomeAdmin(bookVM) }
        composable( route = ADDBOOK_ROUTE ){ AddBooks(bookVM) }
    }
}

@Composable
fun SearchAndHomeAdmin(bookVM: BookViewModel) {
    val textVal = remember { mutableStateOf(TextFieldValue("")) }

    Column {
        SearchAdmin(textVal)
        HomeViewAdmin(bookVM, textVal)
    }
}

@Composable
fun SearchAdmin(textVal: MutableState<TextFieldValue>) {
    TextField(
        placeholder = { Text("Search for books") },
        value = textVal.value,
        onValueChange = { textVal.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp),
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
            textColor = Color.DarkGray,
            cursorColor = Color.Red,
            backgroundColor = Color.White,
            trailingIconColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun HomeViewAdmin(bookVM: BookViewModel, textVal: MutableState<TextFieldValue>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally

    ) {
        bookVM.books.forEach {
            Column(modifier = Modifier
                .padding(16.dp, 6.dp, 12.dp, 16.dp),
                horizontalAlignment = CenterHorizontally,
            ) {
                Card(modifier = Modifier
                    .fillMaxWidth(),
                    elevation = 8.dp
                ) {
                    Column {
                        Row(verticalAlignment = CenterVertically) {
                            AsyncImage(model = it.image, contentDescription = "", modifier = Modifier
                                .padding(12.dp, 12.dp, 12.dp, 0.dp)
                                .width(60.dp)
                            )
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(text = it.name, color = Color.Black, fontSize = 16.sp)
                                Text(text = it.author, color= Color.DarkGray, fontSize = 12.sp)
                                Text(text = it.price, color= Color.Black, fontSize = 16.sp)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp, 6.dp, 12.dp, 2.dp),
                                horizontalArrangement = Arrangement.End)
                            {
                                Button(
                                    onClick = {
                                        bookVM.addReservation(Book(
                                            name = it.name, author = it.author, image = it.image,
                                            price = it.price, genre = it.genre,
                                            condition = it.condition,description = it.description
                                        ))
                                    },
                                    modifier= Modifier.size(50.dp),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                    elevation = null

                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_clear_white_18),
                                        contentDescription = "Delete",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        Row(verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier .fillMaxWidth(),
                        ) {
                            var expanded by remember { mutableStateOf(false)}

                            if (expanded){
                                Column(horizontalAlignment = CenterHorizontally) {
                                    Row {
                                        IconButton(onClick = {expanded = false}) {
                                            Icon(painter = painterResource(id = R.drawable.outline_expand_less_black_18), contentDescription = "Collapse", modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Column(modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 12.dp)) {
                                        Row(modifier = Modifier.padding(4.dp)) {
                                            Text(text = "Genre: ",  color = Color.DarkGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(text = it.genre,  color = Color.DarkGray, fontSize = 12.sp)
                                        }
                                        Row(modifier = Modifier.padding(4.dp)) {
                                            Text(text = "Condition: ",  color = Color.DarkGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(text = it.condition,  color = Color.DarkGray, fontSize = 12.sp)
                                        }
                                        Row(modifier = Modifier.padding(4.dp)) {
                                            Text(text = "Description: ",  color = Color.DarkGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Text(text = it.description,  color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.padding(4.dp))

                                    }
                                }
                            } else {
                                Row {
                                    IconButton(onClick = {expanded = true}) {
                                        Icon(painter = painterResource(id = R.drawable.outline_expand_more_black_18), contentDescription = "Expand", modifier = Modifier.size(18.dp))
                                    }
                                }
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
fun AddBooks(bookVM: BookViewModel) {
    var bookName by remember {mutableStateOf("")}
    var bookAuthor by remember {mutableStateOf("")}
    var bookImage by remember {mutableStateOf("")}
    var bookPrice by remember {mutableStateOf("")}
    var bookCondition by remember {mutableStateOf("")}
    var bookGenre by remember {mutableStateOf("")}
    var bookDescription by remember {mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
    ){
        OutlinedTextField(
            value = bookName,
            onValueChange = { bookName = it },
            label = { Text(text = "Book Name") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bookAuthor,
            onValueChange = { bookAuthor = it },
            label = { Text(text = "Book Author") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bookPrice,
            onValueChange = { bookPrice = it },
            label = { Text(text = "Book Price") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bookGenre,
            onValueChange = { bookGenre = it },
            label = { Text(text = "Book Genre") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bookCondition,
            onValueChange = { bookCondition = it },
            label = { Text(text = "Book Condition") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bookDescription,
            onValueChange = { bookDescription = it },
            label = { Text(text = "Book Description") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = bookImage,
            onValueChange = { bookImage = it },
            label = { Text(text = "Book Image") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = { bookVM.addBookByAdmin( Book(bookName, bookAuthor, bookPrice, bookGenre, bookCondition , bookDescription, bookImage) ) }
        ) {
            Text(text = "Add Book")
        }
    }
}

@Composable
fun BottomBarViewAdmin(navController: NavHostController) {
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
            painter = painterResource(id = R.drawable.ic_grocery),
            contentDescription = "addBook",
            modifier = Modifier.clickable { navController.navigate(ADDBOOK_ROUTE)}
        )
    }
}