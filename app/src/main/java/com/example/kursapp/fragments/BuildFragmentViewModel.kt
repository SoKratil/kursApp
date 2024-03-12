package com.example.kursapp.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kursapp.Product

class BuildFragmentViewModel : ViewModel() {
    private val _selectedGraphicsCard = MutableLiveData<Product?>()
    val selectedGraphicsCard: LiveData<Product?> = _selectedGraphicsCard

    private val _selectedMotherboard = MutableLiveData<Product?>()
    val selectedMotherboard: LiveData<Product?> = _selectedMotherboard

    private val _selectedProcessor = MutableLiveData<Product?>()
    val selectedProcessor: LiveData<Product?> = _selectedProcessor

    private val _selectedRAM = MutableLiveData<Product?>()
    val selectedRAM: LiveData<Product?> = _selectedRAM

    private val _selectedSSD = MutableLiveData<Product?>()
    val selectedSSD: LiveData<Product?> = _selectedSSD

    fun setSelectedGraphicsCard(product: Product?) {
        _selectedGraphicsCard.value = product
    }

    fun setSelectedMotherboard(product: Product?) {
        _selectedMotherboard.value = product
    }

    fun setSelectedProcessor(product: Product?) {
        _selectedProcessor.value = product
    }

    fun setSelectedRAM(product: Product?) {
        _selectedRAM.value = product
    }

    fun setSelectedSSD(product: Product?) {
        _selectedSSD.value = product
    }
}
