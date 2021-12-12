package com.brian_sjtu.hw211205

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.brian_sjtu.hw211205.ui.theme.HW211205Theme
import com.google.android.exoplayer2.ui.PlayerView
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : ComponentActivity() {
    var title = mutableStateOf("title")
    var imgUrl =  mutableStateOf("")
    var videoUrl = mutableStateOf("")

    val dyList = Retrofit.Builder()
        .baseUrl("https://creator.douyin.com/")
        .build().create(DYList::class.java)
    val dyUrl = Retrofit.Builder()
        .baseUrl("https://api.oick.cn/")
        .build().create(DYList::class.java)

    class DYItem {
        var title = ""
        var imgUrl = ""
        var shareUrl = ""
        var playUrl = ""
    }
//    var Items: Array<DYItem>
    var dyItem = DYItem()

    object ListCallback : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val jsObj = JSONObject(response.body()?.string()?:"")
            val obj = jsObj.getJSONArray("billboard_data")
                .getJSONObject(0)
                .getJSONArray("extra_list")
                .getJSONObject(0)
            obj.getString("title")
            obj.getString("img_url")
            obj.getString("link")
        }
    }

    object UrlCallback : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val jsObj = JSONObject(response.body()?.string()?:"")
            jsObj.getString("play")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HW211205Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(title.value, imgUrl.value, videoUrl.value)
                }
            }
        }
    }
}

interface DYList {
    @GET("aweme/v1/creator/data/billboard/?billboard_type=1")
    fun getList() : Call<ResponseBody>
}

interface DYUrl {
    @GET("douyin/api.php")
    fun getUrl(@Query("url") url: String) : Call<ResponseBody>
}

@Composable
fun Greeting(title: String, imgUrl: String, videoUrl: String) {
    Column() {
        Row() {
            Image(painter = rememberImagePainter(data = imgUrl), contentDescription = "")
            Text(text = title)
            Button(onClick = {}) {
                Text(text = "refresh")
            }
        }
        AndroidView(factory = {
            PlayerView(it)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HW211205Theme {
        Greeting("", "", "")
    }
}