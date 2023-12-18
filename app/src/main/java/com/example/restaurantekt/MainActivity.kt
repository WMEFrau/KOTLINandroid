package com.example.restaurantekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import java.util.Objects
import java.text.NumberFormat
import java.util.*


class ItemMenu constructor(val nombre: String, val precio: String) {
}

class ItemMesa constructor(val cantidad: Int, val itemMenu: ItemMenu) {
    fun calcularSubtotal(): Int {
        //resumenComida = resumenPastel + resumenCazuela
        val cant = cantidad
        val precio = itemMenu.precio.toInt()
        return cantidad * precio
    }
}

class CuentaMesa constructor(var aceptaPropina: Boolean = true, val _items: MutableList<ItemMesa> = mutableListOf()) {

    fun agregarItem(im: ItemMenu, cantidad: Int) {
        val ime = ItemMesa(cantidad, im)
        val index = _items.indexOfFirst { it.itemMenu.nombre == ime.itemMenu.nombre }
        if( index != -1 ){
            _items[index] = ime
        }else {
            _items.add(ime)
        }
    }

    fun agregarItem(ime: ItemMesa) {

        val index = _items.indexOfFirst { it.itemMenu.nombre == ime.itemMenu.nombre }
        if( index != -1 ){
            _items[index] = ime
        }else {
            _items.add(ime)
        }

        //Log.i("pruebatxt3", index.toString())
    }

    fun calcularTotalSinPropina(): Int {
        var montoBruto = 0
        for (mesa in _items) {
            montoBruto += mesa.calcularSubtotal()
            Log.i("pruebatxt2", mesa.itemMenu.nombre)
        }
        return montoBruto
    }

    fun calcularPropina(): Int {
        val montoBruto = calcularTotalSinPropina()
        return (montoBruto * 0.1).toInt()

    }

    fun calcularTotalConPropina(): Int {
        var montoBruto = calcularTotalSinPropina()
        var montoPropina = calcularPropina()
        if(aceptaPropina){
            return montoBruto + montoPropina
        }else{
            return montoBruto
        }
    }


}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val valorPastel = 36000
        val valorCazuela = 10000
        var resumenComida = 0
        var resumenPropina = 0
        var resumenTotal = 0
        var resumenPastel = 0
        var resumenCazuela = 0
        val txtPastel = findViewById<EditText>(R.id.txtPastel)
        val txtCazuela = findViewById<EditText>(R.id.txtCazuela)
        val valComida = findViewById<TextView>(R.id.valComida)
        val valPropina = findViewById<TextView>(R.id.valPropina)
        val valTotal = findViewById<TextView>(R.id.valTotal)
        val switchPropina = findViewById<Switch>(R.id.switchPropina) as Switch
        val Pastelsubtotal = findViewById<TextView>(R.id.Pastelsubtotal)
        val cazuelaSubtotal = findViewById<TextView>(R.id.cazuelaSubtotal)
        /* val clases externas  */
        val cm = CuentaMesa()


        fun formatMonto(numero: Int): String {
            val formato = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            return formato.format(numero)
        }

        fun calculaComida(monto: Int) {
            valComida.setText(formatMonto(monto))
        }

        fun calculaPropina(monto: Int) {
            valPropina.setText(formatMonto(monto))
        }

        fun calculaTotal(monto: Int) {
            valTotal.setText(formatMonto(monto))
        }

        fun recalculaPropina(){
            if (switchPropina.isChecked) {
                resumenPropina = cm.calcularPropina()
                cm.aceptaPropina = true
            } else {
                resumenPropina = 0
                cm.aceptaPropina = false
            }
            calculaPropina(resumenPropina)
            calculaTotal(cm.calcularTotalConPropina())
        }

        txtPastel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var valor = s.toString()
                var cantidad: Int = 0
                if (valor.isNotEmpty()) {
                    cantidad = valor.toInt()
                }

                val im = ItemMenu("Pastel de choclo", "12000")
                val ime = ItemMesa(cantidad, im)
                cm.agregarItem(ime)

                resumenPastel = ime.calcularSubtotal()
                Pastelsubtotal.setText(formatMonto(resumenPastel))



                calculaComida(cm.calcularTotalSinPropina())
                recalculaPropina()
                calculaTotal(cm.calcularTotalConPropina())
            }
        })

        txtCazuela.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var valor = s.toString()
                var cantidad: Int = 0
                if (valor.isNotEmpty()) {
                    cantidad = valor.toInt()
                }

                val im = ItemMenu("Cazuela", "10000")
                val ime = ItemMesa(cantidad, im)

                resumenCazuela = ime.calcularSubtotal()
                cazuelaSubtotal.setText(formatMonto(resumenCazuela))

                cm.agregarItem(ime)

                //Log.i("pruebatxt",resumenComida.toString())
                calculaComida(cm.calcularTotalSinPropina())
                recalculaPropina()
                calculaTotal(cm.calcularTotalConPropina())
            }
        })


        switchPropina.setOnClickListener {
            recalculaPropina()
        }


    }
}