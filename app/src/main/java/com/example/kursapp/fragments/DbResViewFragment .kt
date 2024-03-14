package com.example.kursapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kursapp.databinding.FragmentDbResviewBinding


class DbResViewFragment : Fragment() {

    // Объявление свойства для хранения экземпляра Binding
    private var _binding: FragmentDbResviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализация binding
        _binding = FragmentDbResviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Теперь вы можете использовать binding для доступа к представлениям из разметки
        binding.recyclerView2 // здесь можно выполнять операции с RecyclerView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Очистка binding при уничтожении представления фрагмента
        _binding = null
    }
}
