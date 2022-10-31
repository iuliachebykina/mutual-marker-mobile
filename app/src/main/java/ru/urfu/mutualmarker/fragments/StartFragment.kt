package ru.urfu.mutualmarker.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.RoomsActivity
import ru.urfu.mutualmarker.client.AuthorizationService

import ru.urfu.mutualmarker.dto.LoginResponse
import javax.inject.Inject


@AndroidEntryPoint
class StartFragment : Fragment() {

    @Inject
    lateinit var authorizationService: AuthorizationService

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences( "credentials", Context.MODE_PRIVATE)


        //todo delete
        val edit = sharedPref?.edit()
        edit?.putString("username", null)
        edit?.putString("password", null)
        edit?.apply()





        val email = sharedPref?.getString("username", null)
        val password = sharedPref?.getString("password", null)
        super.onCreate(savedInstanceState)

        if (email != null && password != null){
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", password)
                .addFormDataPart("username", "ROLE_STUDENT\\$email")
                .build()
            authorizationService.login(requestBody)
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

                            val editor = sharedPref.edit()
                            editor?.putString("username", email)
                            editor?.putString("password", password)
                            editor?.apply()
                        }
                        println("result OK" + response.errorBody())
                    }
                })

            activity?.startActivity(Intent(activity, RoomsActivity::class.java))

        } else{
            findNavController().navigate(R.id.action_StartPage_to_LoginForm)
        }
    }
}