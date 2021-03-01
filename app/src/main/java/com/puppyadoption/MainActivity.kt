package com.puppyadoption

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.puppyadoption.ui.theme.PuppyAdoptionTheme

private val puppies = listOf(
    Puppy(PuppyId("c1"), "Milo",  4, "Fiesty",R.drawable.dog1),
    Puppy(PuppyId("c2"), "Hound",  2, "Attention seeking",R.drawable.dog2),
    Puppy(PuppyId("c3"), "Airedale",  8, "Chilled",R.drawable.dog3),
    Puppy(PuppyId("c4"), "Akbash",  7, "Chilled",R.drawable.dog4),
    Puppy(PuppyId("c5"), "Akita",  4, "Uncouth",R.drawable.dog5),
    Puppy(PuppyId("c6"), "Husky",  7, "Happy go lucky",R.drawable.dog6),
    Puppy(PuppyId("c7"), "Klee",  1, "Hunter",R.drawable.dog7),
    Puppy(PuppyId("c8"), "Malamute",  5, "Friendly",R.drawable.dog8),

)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuppyAdoptionTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "overview") {
                    composable("overview") { MyApp(navController) }
                    composable("details/{catId}") {
                        AdoptionDetails(PuppyId(it.arguments?.getString("catId")!!))
                    }
                }
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(navController: NavHostController) {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            Header("Puppy Adoption")
            PuppiesList(puppies = puppies, navController)
        }
    }
}

@Composable
fun Header(title: String) {
    Text(
        text = title,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(PaddingValues(20.dp))
    )
}

@Composable
fun PuppiesList(
    puppies: List<Puppy>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        LazyColumn(
            Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(20.dp)
        ) {
            // TODO should be in ViewModel
            val grouped: Map<String, List<Puppy>> = puppies.groupBy { it.name }

            grouped.forEach { (initial, puppies) ->
                item {
                    CharacterHeader(char = initial, Modifier.fillParentMaxWidth())
                }
                items(puppies) { puppy ->
                    PuppyListItem(
                        puppy = puppy,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        navController.navigate("details/${puppy.id.raw}")
                    }
                }
            }
        }
    }
}

@Composable
fun PuppyListItem(
    puppy: Puppy,
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClickAction.invoke() }
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(PaddingValues(0.dp, 10.dp, 0.dp, 10.dp))
        ) {
            Image(
                painter = painterResource(id = puppy.pic),
                contentDescription = "Picture of ${puppy.name}",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .padding(PaddingValues(end = 10.dp)),
                contentScale = ContentScale.Fit,
            )
            Column(Modifier.padding(PaddingValues(end = 5.dp))) {
                Text(text = puppy.name)
                Text(text = puppy.attitude)
            }

        }
    }
}

@Composable
fun CharacterHeader(
    char: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = char[0].toString()
    )
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    PuppyAdoptionTheme {
//        MyApp(navController)
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    PuppyAdoptionTheme(darkTheme = true) {
//        MyApp(navController)
    }
}

data class Puppy(
    val id: PuppyId,
    val name: String,
    val age: Int,
    val attitude: String,
    @DrawableRes val pic:Int
)

data class PuppyId(val raw: String)

@Composable
fun AdoptionDetails(
    puppyId: PuppyId,
) {
    val puppy = puppies.find { it.id == puppyId }!!
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Image(
            painter = painterResource(id = puppy.pic),
            contentDescription = null,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .padding(PaddingValues(bottom = 10.dp)),
            contentScale = ContentScale.Crop,

            )
        Text(text = "Name: ${puppy.name}")
        Text(text = "Age: ${puppy.age}")
        Text(text = "Attitude: ${puppy.attitude}")
    }
}

@Preview("Adoption Details", widthDp = 360, heightDp = 640)
@Composable
fun AdoptionDetailsPreview() {
    PuppyAdoptionTheme{
        AdoptionDetails(puppies[0].id)
    }
}