package com.nagykristofantal.udemxtask.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nagykristofantal.udemxtask.data.CartItem
import com.nagykristofantal.udemxtask.data.ExtraCategory
import com.nagykristofantal.udemxtask.data.IceCream
import com.nagykristofantal.udemxtask.data.OrderPayload
import com.nagykristofantal.udemxtask.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IceCreamViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _cart = MutableStateFlow<List<CartItem>>(loadCart())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

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

    private val _selectedIceCream = MutableStateFlow<IceCream?>(null)
    val selectedIceCream: StateFlow<IceCream?> = _selectedIceCream.asStateFlow()

    init {
        fetchData()
    }

    private fun saveCart(cartList: List<CartItem>) {
        val json = gson.toJson(cartList)
        prefs.edit().putString("cart_data", json).apply()
    }

    private fun loadCart(): List<CartItem> {
        val json = prefs.getString("cart_data", null)
        if (json != null) {
            val type = object : TypeToken<List<CartItem>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }

    fun selectIceCream(iceCream: IceCream) {
        _selectedIceCream.value = iceCream
    }

    fun addToCart(cartItem: CartItem) {
        _cart.value = _cart.value + cartItem
        saveCart(_cart.value)
    }

    fun removeFromCart(cartItem: CartItem) {
        _cart.value = _cart.value - cartItem
        saveCart(_cart.value)
    }

    fun placeOrder(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val payload = _cart.value.map { cartItem ->
                    OrderPayload(
                        id = cartItem.iceCream.name.hashCode().toLong(),
                        extra = cartItem.extras.map { it.id }
                    )
                }

                val response = RetrofitClient.apiService.placeOrder(payload)
                if (response.isSuccessful) {
                    _cart.value = emptyList()
                    saveCart(emptyList())
                    onSuccess()
                } else {
                    onError("Hiba a szervertől: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Hálózati hiba történt!")
            }
        }
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
                _errorMessage.value = "Hiba: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortIceCreamsByStatus() {
        _iceCreams.value = _iceCreams.value.sortedBy { iceCream ->
            when (iceCream.status.name.lowercase()) {
                "available" -> 0
                "melted" -> 1
                "unavailable" -> 2
                else -> 3
            }
        }
    }
}