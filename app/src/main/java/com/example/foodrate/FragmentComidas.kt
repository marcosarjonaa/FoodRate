package com.example.foodrate

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodrate.controller.Controller
import com.example.foodrate.databinding.FragmentComidasBinding


class FragmentComidas() : Fragment() {
    lateinit var binding: FragmentComidasBinding
    lateinit var controller : Controller
    lateinit var activityContext : Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComidasBinding.inflate(inflater, container, false)
        activityContext = requireActivity()
        controller = Controller(activityContext, this)
        controller.initdata()
        return binding.root
    }

}