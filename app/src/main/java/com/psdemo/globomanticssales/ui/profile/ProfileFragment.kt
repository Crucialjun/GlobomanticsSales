package com.psdemo.globomanticssales.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.psdemo.globomanticssales.MainActivity
import com.psdemo.globomanticssales.R
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.txtName

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = requireActivity().getSharedPreferences(getString(R.string.profile_preference_key),
            Context.MODE_PRIVATE)

        txtName.setText(
            sharedPref.getString(getString(R.string.profile_name_key),getString(R.string.profile_name_key_default))
        )

        btnSave.setOnClickListener{
            with(sharedPref.edit()){
                putString(getString(R.string.profile_name_key),txtName.text.toString())
                apply()
            }

            (activity as MainActivity).hideKeyboard()
            Snackbar.make(it,"Changes succesfully saved",Snackbar.LENGTH_LONG).show()

        }
    }
}
