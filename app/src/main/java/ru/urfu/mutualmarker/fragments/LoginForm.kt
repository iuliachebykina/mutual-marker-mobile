package ru.urfu.mutualmarker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Display.Mode
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
import ru.urfu.mutualmarker.client.LoginService
import ru.urfu.mutualmarker.client.CustomCookieJar
import ru.urfu.mutualmarker.dto.LoginResponse
import javax.inject.Inject

@AndroidEntryPoint
class LoginForm : Fragment() {
    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var customCookieJar: CustomCookieJar

    private val preferences: SharedPreferences = requireContext().getSharedPreferences("cred", Context.MODE_PRIVATE)





    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        loginService = ClientCredentials.loginService
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailField = view.findViewById<TextInputEditText>(R.id.EmailField)
        val passwordField = view.findViewById<TextInputEditText>(R.id.PasswordField)

        view.findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", passwordField.getText().toString())
                .addFormDataPart("username", "ROLE_STUDENT\\" + emailField.getText().toString())
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

                            val cred = StringBuilder()
                            cred.append(emailField.text).append(":").append(passwordField.text)


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
                    emailField.getText().toString(),
                    passwordField.getText().toString()
                )
            )
        }
    }
}