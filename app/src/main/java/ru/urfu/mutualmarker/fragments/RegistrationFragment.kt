package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.AuthorizationService
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.dto.Registration
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
        val view = requireView()
        val emailField = view.findViewById<TextInputEditText>(R.id.EmailField)
        val passwordField = view.findViewById<TextInputEditText>(R.id.PasswordField)
        val repeatPasswordField = view.findViewById<TextInputEditText>(R.id.RepeatPasswordField)
        val registrationButton = view.findViewById<Button>(R.id.RegistrationButton)

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val shortPasswordFlag = view.findViewById<TextView>(R.id.ShortPassword)
                if(passwordField.text?.length!! < 8){
                    shortPasswordFlag.visibility = View.VISIBLE
                } else {
                    shortPasswordFlag.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        repeatPasswordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val diffPasswordFlag = view.findViewById<TextView>(R.id.DiffPassword)
                if(repeatPasswordField.text.toString() != passwordField.text.toString()){
                    diffPasswordFlag.visibility = View.VISIBLE
                } else {
                    diffPasswordFlag.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        val firstNameField = view.findViewById<TextInputEditText>(R.id.FirstNameField)
        val lastNameField = view.findViewById<TextInputEditText>(R.id.LastNameField)
        val patronymicField = view.findViewById<TextInputEditText>(R.id.PatronymicField)
        val numberField = view.findViewById<TextInputEditText>(R.id.PhoneField)



        registrationButton.setOnClickListener {
            val fillFieldsFlag = view.findViewById<TextView>(R.id.FillFields)
//            if(emailField.text.isNullOrBlank() ||
//                passwordField.text.isNullOrBlank()  ||
//                passwordField.text != repeatPasswordField.text ||
//                firstNameField.text.isNullOrBlank() ||
//                lastNameField.text.isNullOrBlank() ||
//                patronymicField.text.isNullOrBlank() ||
//                numberField.text.isNullOrBlank() ){
//                //fillFieldsFlag.visibility = View.VISIBLE
//
//            } else{
                fillFieldsFlag.visibility = View.INVISIBLE
                val requestBody = Registration(
                    passwordField.text.toString(), numberField.text.toString(),
                    firstNameField.text.toString(), lastNameField.text.toString(),
                    patronymicField.text.toString(), emailField.text.toString()
                )
            authorizationService.registerStudent(requestBody)
                .enqueue(object : Callback<Profile> {
                    override fun onFailure(call: Call<Profile>, t: Throwable) {
                        System.out.println("result " + t.message)
                    }

                    override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                        if(response.code() == 200){
                            findNavController().navigate(R.id.action_RegistrationForm_to_navigation_my_rooms)
                        }
                        System.out.println("result " + response)
                    }
                })
//            }
            println("Onclick")

        }
    }
}