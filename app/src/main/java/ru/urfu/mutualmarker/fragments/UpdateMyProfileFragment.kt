package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.ProfileService
import ru.urfu.mutualmarker.dto.Name
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.service.ProfileFillingService
import javax.inject.Inject


private const val PROFILE = "profile"

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateMyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class UpdateMyProfileFragment : Fragment() {
    private var profile: Profile? = null
    var profileFillingService = ProfileFillingService()

    @Inject
    lateinit var profileService: ProfileService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profile = Gson().fromJson(it.getString(PROFILE), Profile::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        profileFillingService.fillProfile(profileService, view)

        val firstName = view.findViewById<TextView>(R.id.FirstName)
        val lastName = view.findViewById<TextView>(R.id.LastName)
        val patronymic = view.findViewById<TextView>(R.id.Patronymiс)

        val phoneNumber = view.findViewById<TextView>(R.id.PhoneNumber)
        val university = view.findViewById<TextView>(R.id.University)
        val institute = view.findViewById<TextView>(R.id.Institute)
        val socialNetwork = view.findViewById<TextView>(R.id.SocialNetwork)


        val studyGroup = view.findViewById<TextView>(R.id.StudyGroup)



        view.findViewById<Button>(R.id.SaveProfileButton).setOnClickListener {
            val updatedProfile = Profile(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                university.text.toString(),
                institute.text.toString(),
                studyGroup.text.toString(),
                phoneNumber.text.toString(),
                socialNetwork.text.toString(),
                Name(
                    firstName.text.toString(),
                    lastName.text.toString(),
                    patronymic.text.toString()
                )

            )
            val updateProfile: Call<Profile> = profileService.updateProfile(updatedProfile)
            updateProfile.enqueue(object : Callback<Profile> {
                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    println("result FAIl" + t.message)
                }

                override fun onResponse(
                    call: Call<Profile>,
                    response: Response<Profile>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Профиль успешно обновлен", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(context, "Не удалось обновить профиль", Toast.LENGTH_LONG)
                            .show()
                    }
                    println("result OK" + response)
                }
            })

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdateMyProfileFragment.
         */
        @JvmStatic
        fun newInstance(profile: String) =
            UpdateMyProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(profile, profile)
                }
            }
    }
}