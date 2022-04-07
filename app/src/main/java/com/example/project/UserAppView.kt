package com.example.project

import android.graphics.Paint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.AsyncImage

@Composable
fun MainContentViewUser(navController: NavHostController) {
    val bookVM = viewModel<BookViewModel>()

    NavHost(navController = navController, startDestination = HOME_ROUTE) {
        composable( route = HOME_ROUTE ){ SearchAndHome(bookVM) }
        composable( route = RESERVATION_ROUTE ){ ReservationView(bookVM) }
    }
}

@Composable
fun SearchAndHome(bookVM: BookViewModel) {
    val textVal = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        Search(textVal)
        HomeView(bookVM, textVal)
    }
}

@Composable
fun Search(textVal: MutableState<TextFieldValue>) {
    TextField(
        placeholder = { Text("Search for books") },
        value = textVal.value,
        onValueChange = { textVal.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp),
        textStyle = TextStyle(Color.DarkGray, fontSize = 18.sp),
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
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )

}

@Composable

fun HomeView(bookVM: BookViewModel, textVal: MutableState<TextFieldValue>) {

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
                            Text(text = it.price, color= Color.DarkGray, fontSize = 12.sp)
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
                            ) {

                                Icon(
                                    painter = painterResource(id = R.drawable.outline_add_24),
                                    contentDescription = "Add to reservations",
                                    modifier = Modifier .fillMaxWidth() )
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
                                    Row() {
                                        IconButton(onClick = {expanded = false}) {
                                            Icon(painter = painterResource(id = R.drawable.outline_expand_less_black_18), contentDescription = "Collapse", modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Column(verticalArrangement = Arrangement.Center) {
                                        Text(text = it.genre,  color = Color.DarkGray, fontSize = 12.sp)
                                        Text(text = it.condition,  color = Color.DarkGray, fontSize = 12.sp)
                                        Text(text = it.description,  color = Color.DarkGray, fontSize = 12.sp)
                                    }
                                }
                            } else {
                                Row() {
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
fun ReservationView(bookVM: BookViewModel) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally
    )
    {
        if(bookVM.reservedBooks.isEmpty()) {
            Text ( text = "Your Reservation List is empty", textAlign = TextAlign.Center)
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
                                Text(text = it.price, color= Color.DarkGray, fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.End) {
                                Button(onClick = {
                                    bookVM.deleteReservation(Book(
                                        image = "", name = "", author = "", description = "", price = "", condition = "", genre = ""
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
                    }
                }
            }
            Button(
                onClick = {
                    bookVM.confirmReservation(Book(
                        image = "", name = "", author = "", description = "", price = "", condition ="", genre =""
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
fun BottomBarViewUser(navController: NavHostController) {
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
    }
}