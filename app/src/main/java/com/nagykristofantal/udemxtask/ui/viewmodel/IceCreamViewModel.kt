package com.nagykristofantal.udemxtask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nagykristofantal.udemxtask.data.CartItem
import com.nagykristofantal.udemxtask.data.ExtraCategory
import com.nagykristofantal.udemxtask.data.IceCream
import com.nagykristofantal.udemxtask.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IceCreamViewModel : ViewModel() {

    private val _iceCreams = MutableStateFlow<List<IceCream>>(emptyList())
    val iceCreams: StateFlow<List<IceCream>> = _iceCreams.asStateFlow()

    private val _basePrice = MutableStateFlow<Double>(0.0)
    val basePrice: StateFlow<Double> = _basePrice.asStateFlow()

    private val _extras = MutableStateFlow<List<ExtraCategory>>(emptyList())
    val extras: StateFlow<List<ExtraCategory>> = _extras.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val iceCreamResponse = RetrofitClient.apiService.getIceCreams()
                _iceCreams.value = iceCreamResponse.iceCreams
                _basePrice.value = iceCreamResponse.basePrice

                val extrasResponse = RetrofitClient.apiService.getExtras()
                _extras.value = extrasResponse

            } catch (e: Exception) {
                _errorMessage.value = "Hiba történt a letöltéskor: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortIceCreamsByStatus() {
        _iceCreams.value = _iceCreams.value.sortedBy { it.status.name }
    }

    fun addToCart(cartItem: CartItem) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.add(cartItem)
        _cartItems.value = currentCart
    }

    fun removeFromCart(cartItem: CartItem) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.remove(cartItem)
        _cartItems.value = currentCart
    }
}