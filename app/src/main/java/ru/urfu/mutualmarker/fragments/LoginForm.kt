package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.ClientCredentials
import ru.urfu.mutualmarker.client.LoginService
import ru.urfu.mutualmarker.dto.LoginRequest
import ru.urfu.mutualmarker.dto.LoginResponse

class LoginForm : Fragment() {

    lateinit var loginService: LoginService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginService = ClientCredentials.loginService
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailField = view.findViewById<TextInputEditText>(R.id.EmailField)
        val passwordField = view.findViewById<TextInputEditText>(R.id.PasswordField)

        view.findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val result = loginService.login(LoginRequest(emailField.getText().toString(),
                    passwordField.getText().toString()))
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        System.out.println("result " + t.message)
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        System.out.println("result " + response)
                    }
                })
            findNavController().navigate(R.id.action_Login_to_FirstFragment)
        }

        view.findViewById<Button>(R.id.SignupButton).setOnClickListener {
            System.out.println(String.format("email: %s, password: %s",
                emailField.getText().toString(),
                passwordField.getText().toString()))
        }
    }
}