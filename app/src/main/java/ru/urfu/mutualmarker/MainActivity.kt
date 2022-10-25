package ru.urfu.mutualmarker

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.client.CustomCookieJar
import ru.urfu.mutualmarker.client.LoginService
import ru.urfu.mutualmarker.dto.LoginResponse
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var customCookieJar: CustomCookieJar
    @Inject
    lateinit var loginService: LoginService


    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = applicationContext.getSharedPreferences( "credentials", Context.MODE_PRIVATE)

        //todo delete
//        val edit = sharedPref?.edit()
//        edit?.putString("username", "iulia@mail.ru")
//        edit?.putString("password", "0000")
//        edit?.apply()


        val email = sharedPref.getString("username", null)
        val password = sharedPref.getString("password", null)
        super.onCreate(savedInstanceState)

        if (email != null && password != null){
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", password)
                .addFormDataPart("username", "ROLE_STUDENT\\$email")
                .build()
            loginService.login(requestBody)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        println("result FAIl" + t.message)
                    }


                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 200) {

                            //TODO get student's rooms

                            val edit = sharedPref.edit()
                            edit?.putString("username", email)
                            edit?.putString("password", password)
                            edit?.apply()


                        }
                        println("result OK" + response.errorBody())
                    }
                })

        }
        setContentView(R.layout.activity_main)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}