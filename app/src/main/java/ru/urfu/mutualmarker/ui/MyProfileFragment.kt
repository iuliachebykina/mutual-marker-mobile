package ru.urfu.mutualmarker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
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
        profileFillingService.fillProfile(profileService, null, null, view)

        view.findViewById<Button>(R.id.UpdateProfileButton).setOnClickListener {

            //TODO пример вызова с аргументами
//            val bundle = Bundle()
//            bundle.putString("email", "iulia@mail.ru")
//            findNavController().navigate(R.id.action_navigation_profile_to_student_profile, bundle)

        }

    }



}