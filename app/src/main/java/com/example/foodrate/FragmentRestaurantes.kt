package com.example.foodrate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.adapter.AdapterRestaurantes
import com.example.foodrate.dao.DaoRestaurantes
import com.example.foodrate.databinding.FragmentRestaurantesBinding
import com.example.foodrate.models.Restaurantes

class FragmentRestaurantes : Fragment() {
    lateinit var binding: FragmentRestaurantesBinding
    private lateinit var listRestaurantes: MutableList<Restaurantes>
    lateinit var adapterAnuncio: AdapterRestaurantes
    private var layoutManagerRestaurantes: LinearLayoutManager = LinearLayoutManager(context)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantesBinding.inflate(inflater, container, false)
        initData()
        return binding.root
    }

    fun initData(){
        listRestaurantes = DaoRestaurantes.myDao.getDataRestaurantes().toMutableList()
        setAdapter()

    }
    fun setAdapter() {
        adapterAnuncio = AdapterRestaurantes(listRestaurantes)
        binding.restaurantes.layoutManager = layoutManagerRestaurantes
        binding.restaurantes.adapter = adapterAnuncio
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.restaurantes.adapter = null
        binding.restaurantes.layoutManager = null
    }
}