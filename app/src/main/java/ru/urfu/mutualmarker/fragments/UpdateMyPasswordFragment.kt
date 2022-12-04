package ru.urfu.mutualmarker.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.client.ProfileService
import ru.urfu.mutualmarker.dto.Profile
import ru.urfu.mutualmarker.dto.UpdatePassword
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [UpdateMyPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class UpdateMyPasswordFragment : Fragment() {

    @Inject
    lateinit var profileService: ProfileService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_my_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences( "credentials", Context.MODE_PRIVATE)

        val email = sharedPref?.getString("username", null)

        val oldPassword = view.findViewById<TextInputEditText>(R.id.OldPassword)
        val newPassword = view.findViewById<TextInputEditText>(R.id.NewPassword)


        view.findViewById<Button>(R.id.SavePasswordButton).setOnClickListener {
            val updatePasswordDto =
                UpdatePassword(email!!, oldPassword.text.toString(), newPassword.text.toString())
            updatePassword(updatePasswordDto, sharedPref)
        }
    }

    private fun updatePassword(updatePasswordDto: UpdatePassword, sharedPref: SharedPreferences) {
        val updatePassword = profileService.updatePassword(updatePasswordDto)
        updatePassword.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                println("result FAIl" + t.message)
            }

            override fun onResponse(
                call: Call<Profile>,
                response: Response<Profile>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Пароль успешно обновлен", Toast.LENGTH_LONG).show()
                    sharedPref.edit().putString("password", updatePasswordDto.newPassword).apply()
                } else{
                    Toast.makeText(context, "Не удалось обновить пароль", Toast.LENGTH_LONG).show()
                }
                println("result OK" + response)
            }
        })
    }


}