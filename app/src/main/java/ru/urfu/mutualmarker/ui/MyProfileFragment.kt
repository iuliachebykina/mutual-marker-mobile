package ru.urfu.mutualmarker.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.ProfileService
import ru.urfu.mutualmarker.service.ProfileFillingService
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : Fragment() {
    var profileFillingService = ProfileFillingService()

    @Inject
    lateinit var profileService: ProfileService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileFillingService.fillSelfProfile(profileService, view)

        setUpdateListener(view)
        setLogoutListener(view)

    }

    private fun setUpdateListener(view: View){
        view.findViewById<Button>(R.id.UpdateProfileButton).setOnClickListener {

            //TODO пример вызова с аргументами
            val bundle = Bundle()
            bundle.putLong("roomId", 152)
            findNavController().navigate(R.id.action_navigation_profile_to_roomMembers, bundle)

        }
    }

    private fun setLogoutListener(view: View){
        view.findViewById<Button>(R.id.LogoutButton).setOnClickListener {
            //logout + empty cookie jar
            profileService.logout().enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    println("result FAIl" + t.message)
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    val sharedPref = activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)

                    val edit = sharedPref?.edit()
                    edit?.remove("username")
                    edit?.remove("password")
                    edit?.apply()
                    findNavController().navigate(R.id.Logout_Redirect)
                }
            })
        }
    }

}