package ru.urfu.mutualmarker.service

import android.view.View
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.ProfileService
import ru.urfu.mutualmarker.dto.Name
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.util.Role


class ProfileFillingService {

    fun fillProfile(profileService: ProfileService, email: String?, role: Role?, view: View) {
        val profileResponse: Call<Profile> = if (email == null)
            profileService.getStudentProfile()
        else if (role != null && role == Role.STUDENT)
            profileService.getStudentProfile(email)
        else if (role != null && role == Role.TEACHER)
            profileService.getTeacherProfile(email)
        else
            return
        profileResponse.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                println("result FAIl" + t.message)
            }

            override fun onResponse(
                call: Call<Profile>,
                response: Response<Profile>
            ) {
                if (response.code() == 200) {
                    val profile = response.body() ?: return

                    val name = getName(profile.name)
                    view.findViewById<TextView>(R.id.Name).text = name
                    view.findViewById<TextView>(R.id.Email).text = profile.email
                    view.findViewById<TextView>(R.id.PhoneNumber).text = profile.phoneNumber
                    view.findViewById<TextView>(R.id.Study).text = profile.institute
                    view.findViewById<TextView>(R.id.StudyGroup).text = profile.studentGroup
                    view.findViewById<TextView>(R.id.SocialNetwork).text = profile.socialNetwork

                }
                println("result OK" + response.errorBody())
            }
        })
    }

    fun getName(name: Name): String {
        var fullName = ""
        fullName += name.lastName
        fullName += " " + name.firstName
        if (!name.patronymic.isNullOrBlank())
            fullName += " " + name.patronymic
        return fullName

    }

}