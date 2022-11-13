package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.ProfileService
import ru.urfu.mutualmarker.service.ProfileFillingService
import ru.urfu.mutualmarker.util.Role
import javax.inject.Inject

private const val EMAIL = "email"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class StudentProfileFragment : Fragment() {
    var profileFillingService = ProfileFillingService()

    @Inject
    lateinit var profileService: ProfileService

    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(EMAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email?.let { profileFillingService.fillProfile(profileService, it, Role.STUDENT, view) }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param email Parameter 1.
         * @return A new instance of fragment StudentProfileFragment.
         */
        @JvmStatic
        fun newInstance(email: String) =
            StudentProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(EMAIL, email)
                }
            }
    }
}