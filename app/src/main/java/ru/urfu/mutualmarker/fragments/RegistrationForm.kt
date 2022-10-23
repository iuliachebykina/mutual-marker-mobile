package ru.urfu.mutualmarker.fragments

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
import ru.urfu.mutualmarker.client.AuthorizationService
import ru.urfu.mutualmarker.dto.LoginResponse
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationForm : Fragment() {
    @Inject
    lateinit var authorizationService : AuthorizationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.registration_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailField = view.findViewById<TextInputEditText>(R.id.EmailField)
        val passwordField = view.findViewById<TextInputEditText>(R.id.PasswordField)
        val firstNameField = view.findViewById<TextInputEditText>(R.id.FirstNameField)

        view.findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val requestBody : RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", passwordField.getText().toString())
                .addFormDataPart("username", emailField.getText().toString())
                .build()
            val result = authorizationService.login(requestBody)
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