package ru.urfu.mutualmarker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.CustomCookieJar
import ru.urfu.mutualmarker.client.LoginService
import ru.urfu.mutualmarker.dto.LoginResponse
import javax.inject.Inject

@AndroidEntryPoint
class LoginForm : Fragment() {
    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var customCookieJar: CustomCookieJar





    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_form, container, false)
    }

    val sharedPref = activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailField = view.findViewById<TextInputEditText>(R.id.EmailField)
        val passwordField = view.findViewById<TextInputEditText>(R.id.PasswordField)

        view.findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", passwordField.text.toString())
                .addFormDataPart("username", "ROLE_STUDENT\\" + emailField.text.toString())
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

                            val edit = sharedPref?.edit()
                            edit?.putString("username", emailField.text.toString())
                            edit?.putString("password", passwordField.text.toString())
                            edit?.apply()

                            //preferences.edit().putString("cred", cred.toString())
                            if (false) { //if room's count > 0
                                findNavController().navigate(R.id.action_Login_to_FirstFragment)

                            } else { //if room's count = 0
                                findNavController().navigate(R.id.action_Login_to_AddRoomFragment)
                            }


                        }
                        println("result OK" + response.errorBody())
                    }
                })

        }

        view.findViewById<Button>(R.id.SignupButton).setOnClickListener {
            println(
                String.format(
                    "email: %s, password: %s",
                    emailField.text.toString(),
                    passwordField.text.toString()
                )
            )
        }
    }
}