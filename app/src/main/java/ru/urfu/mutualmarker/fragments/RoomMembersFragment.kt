package ru.urfu.mutualmarker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.urfu.mutualmarker.R
import ru.urfu.mutualmarker.adapter.MembersAdapter
import ru.urfu.mutualmarker.client.ProfileService
import ru.urfu.mutualmarker.dto.MyProfile
import ru.urfu.mutualmarker.dto.Profile
import javax.inject.Inject


private const val ROOM_ID = "roomId"

/**
 * A simple [Fragment] subclass.
 * Use the [RoomMembersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RoomMembersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var roomId: Long? = null

    @Inject
    lateinit var profileService: ProfileService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            roomId = it.getLong(ROOM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_members, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val teachers: List<MyProfile>

        roomId?.let {
            profileService.getTeachers(roomId = it, 0, 1000).enqueue(object : Callback<List<Profile>> {
                override fun onFailure(call: Call<List<Profile>>, t: Throwable) {
                    println("result FAIl" + t.message)
                }

                override fun onResponse(
                    call: Call<List<Profile>>,
                    response: Response<List<Profile>>
                ) {
                    if (response.code() == 200) {
                        val profiles = response.body() ?: return
                        val recyclerView = view.findViewById(R.id.teachersRecyclerView) as RecyclerView
                        recyclerView.layoutManager = LinearLayoutManager(activity)
                        recyclerView.adapter = MembersAdapter(profiles)

                    }
                    println("result OK" + response.errorBody())
                }
            })

            profileService.getStudents(roomId = it, 0, 1000).enqueue(object : Callback<List<Profile>> {
                override fun onFailure(call: Call<List<Profile>>, t: Throwable) {
                    println("result FAIl" + t.message)
                }

                override fun onResponse(
                    call: Call<List<Profile>>,
                    response: Response<List<Profile>>
                ) {
                    if (response.code() == 200) {
                        val profiles = (response.body() ?: return).toMutableList()
                        profiles.addAll(profiles)
                        profiles.addAll(profiles)
                        val recyclerView = view.findViewById(R.id.studentsRecyclerView) as RecyclerView
                        recyclerView.layoutManager = LinearLayoutManager(activity)
                        recyclerView.adapter = MembersAdapter(profiles)

                    }
                    println("result OK" + response.errorBody())
                }
            })



        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param roomId Room's Id.
         * @return A new instance of fragment RoomMembersFragment.
         */
        @JvmStatic
        fun newInstance(roomId: String) =
            RoomMembersFragment().apply {
                arguments = Bundle().apply {
                    putString(ROOM_ID, roomId)
                }
            }
    }
}