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

    fun fillProfile(profileService: ProfileService, view: View) {
        val myProfileResponse: Call<Profile> = profileService.getStudentProfile()
        myProfileResponse.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                println("result FAIl" + t.message)
            }

            override fun onResponse(
                call: Call<Profile>,
                response: Response<Profile>
            ) {
                if (response.code() == 200) {
                    val profile = response.body() ?: return
                    fillProfile(profile, view)


                }
                println("result OK" + response)
            }
        })


    }

    fun fillProfile(profile: Profile, view: View){
        val name: String
        if(profile.name != null){
            name = getName(profile.name)
            if( view.findViewById<TextView>(R.id.FirstName) != null)
                view.findViewById<TextView>(R.id.FirstName).text = profile.name.firstName
            if(view.findViewById<TextView>(R.id.LastName)!=null)
                view.findViewById<TextView>(R.id.LastName).text = profile.name.lastName
            if(view.findViewById<TextView>(R.id.Patronymiс)!=null)
                view.findViewById<TextView>(R.id.Patronymiс).text = profile.name.patronymic

        } else{
            name = getName(profile)
        }
        if(view.findViewById<TextView>(R.id.Name) != null)
            view.findViewById<TextView>(R.id.Name).text = name

        if(view.findViewById<TextView>(R.id.Email) != null)
            view.findViewById<TextView>(R.id.Email).text = profile.email
        if(view.findViewById<TextView>(R.id.PhoneNumber) != null)
            view.findViewById<TextView>(R.id.PhoneNumber).text = profile.phoneNumber
        if(view.findViewById<TextView>(R.id.University) != null)
            view.findViewById<TextView>(R.id.University).text = profile.university
        if(view.findViewById<TextView>(R.id.Institute) != null)
            view.findViewById<TextView>(R.id.Institute).text = profile.institute
        if(view.findViewById<TextView>(R.id.SocialNetwork) != null)
            view.findViewById<TextView>(R.id.SocialNetwork).text = profile.socialNetwork
        if(view.findViewById<TextView>(R.id.Subject) != null)
            view.findViewById<TextView>(R.id.Subject).text = profile.subject
        if(view.findViewById<TextView>(R.id.StudyGroup) != null)
            view.findViewById<TextView>(R.id.StudyGroup).text = profile.studentGroup
    }

    fun fillProfile(profileService: ProfileService, email: String, role: Role, view: View) {
        val profileResponse: Call<Profile>
        if (role == Role.STUDENT)
            profileResponse = profileService.getStudentProfile(email)
        else if (role == Role.TEACHER)
            profileResponse = profileService.getTeacherProfile(email)
        else
            return
        profileResponse.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                println("result FAIl" + t.message)
            }

            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.code() == 200) {
                    val profile = response.body() ?: return

                    fillProfile(profile, view)

                }
                println("result OK" + response)
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

    fun getName(profile: Profile): String {
        var fullName = ""
        fullName += profile.lastName
        fullName += " " + profile.firstName
        if (!profile.patronymic.isNullOrBlank())
            fullName += " " + profile.patronymic
        return fullName

    }

}