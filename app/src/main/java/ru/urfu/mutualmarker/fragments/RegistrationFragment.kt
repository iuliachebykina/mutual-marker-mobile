package ru.urfu.mutualmarker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.RoomsActivity
import ru.urfu.mutualmarker.client.AuthorizationService
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.dto.RegistrationRequest
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    @Inject
    lateinit var authorizationService : AuthorizationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("Creat")
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("Created")
        getRegisterOnClickListener()
    }

    private fun getRegisterOnClickListener(){
        val view = requireView();
        val emailField = view.findViewById<TextInputEditText>(R.id.EmailField)
        val passwordField = view.findViewById<TextInputEditText>(R.id.PasswordField)
        val firstNameField = view.findViewById<TextInputEditText>(R.id.FirstNameField)
        val lastNameField = view.findViewById<TextInputEditText>(R.id.LastNameField)
        val patronymicField = view.findViewById<TextInputEditText>(R.id.PatronymicField)
        val numberField = view.findViewById<TextInputEditText>(R.id.PhoneField)

        view.findViewById<Button>(R.id.RegistrationButton).setOnClickListener {
            println("Onclick")
            val requestBody = RegistrationRequest(
                passwordField.text.toString(), numberField.text.toString(),
                firstNameField.text.toString(), lastNameField.text.toString(),
                patronymicField.text.toString(), emailField.text.toString()
            )
            val result = authorizationService.registerStudent(requestBody)
                .enqueue(object : Callback<Profile> {
                    override fun onFailure(call: Call<Profile>, t: Throwable) {
                        System.out.println("result " + t.message)
                    }

                    override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                        if(response.code() == 200){
                            activity?.startActivity(Intent(activity, RoomsActivity::class.java))
                        }
                        System.out.println("result " + response)
                    }
                })
        }
    }
}