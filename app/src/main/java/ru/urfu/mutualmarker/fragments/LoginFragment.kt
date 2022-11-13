package ru.urfu.mutualmarker.fragments

import android.content.Context
import android.content.Intent
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
import ru.urfu.mutualmarker.RoomsActivity
import ru.urfu.mutualmarker.client.AuthorizationService
import ru.urfu.mutualmarker.client.CustomCookieJar
import ru.urfu.mutualmarker.dto.Login
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject
    lateinit var authorizationService: AuthorizationService

    @Inject
    lateinit var customCookieJar: CustomCookieJar





    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("Create")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("Created")
        getLoginOnClickListener()

        view.findViewById<Button>(R.id.SignupButton).setOnClickListener {
            findNavController().navigate(R.id.action_Login_to_Registration)
        }
    }

    private fun getLoginOnClickListener(){
        val emailField = requireView().findViewById<TextInputEditText>(R.id.EmailField)
        val passwordField = requireView().findViewById<TextInputEditText>(R.id.PasswordField)

        requireView().findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", passwordField.text.toString())
                .addFormDataPart("username", "ROLE_STUDENT\\" + emailField.text.toString())
                .build()

            authorizationService.login(requestBody)
                .enqueue(object : Callback<Login> {
                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        println("result FAIl" + t.message)
                    }


                    override fun onResponse(
                        call: Call<Login>,
                        response: Response<Login>
                    ) {
                        if (response.code() == 200) {

                            //TODO get student's rooms
                            val sharedPref = activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)

                            val edit = sharedPref?.edit()
                            edit?.putString("username", emailField.text.toString())
                            edit?.putString("password", passwordField.text.toString())
                            edit?.apply()

                            activity?.startActivity(Intent(activity, RoomsActivity::class.java))


                        }
                        println("result OK" + response)
                    }
                })

        }

    }
}