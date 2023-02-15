package com.example.planetresources.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.planetresources.R
import com.example.planetresources.databinding.ActivityMainBinding
import com.example.planetresources.models.Mineral
import com.example.planetresources.models.Nave
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //crear channel metal
        val metalChannel = Channel<Int>(3)
        var metalQty = 0

        //crear channel cristal
        val cristalChannel = Channel<Int>(3)
        var cristalQty = 0

        //crear channel deuterio
        val deuterioChannel = Channel<Int>(3)
        var deuterioQty = 0

        //METAL
        val metalJob = CoroutineScope(Dispatchers.Default).launch {
            //val metal = Mineral("Metal", 1000)
            //utilizar el channel metal para escribir
            while (true) {
                //metal.addResource()
                withContext(Dispatchers.Main) {
                    binding.cantMetalTv.text = metalQty.toString()//metal.quantity.toString()
                }
                metalChannel.send(metalQty)
                delay(1000)
                metalQty++
            }
        }

        binding.naveMetalIv.setOnClickListener {
            val metalNaveJob = CoroutineScope(Dispatchers.Default).launch {
                val nave = Nave("Metal")
                //leer channel metal

                metalChannel.receive()
                metalQty--
                withContext(Dispatchers.Main) {
                    binding.cantMetalTv.text = metalQty.toString()
                    /*while (binding.naveMetalIv.y > 682.0) {
                        delay(500)
                        binding.naveMetalIv.y -= 50
                    }*/


                    //println(binding.naveMetalIv.y)//822.0

                }
            }
        }


        //CRISTAL
        val cristalJob = CoroutineScope(Dispatchers.IO).launch {
            while (true){
                withContext(Dispatchers.Main){
                    binding.cantCristalTv.text = cristalQty.toString()
                }
                cristalChannel.send(cristalQty)
                delay(2000)
                cristalQty++
            }
        }
        binding.naeCristalIv.setOnClickListener {
            val naveCristalJob = CoroutineScope(Dispatchers.IO).launch {
                cristalChannel.receive()
                cristalQty--
                withContext(Dispatchers.Main){
                    binding.cantCristalTv.text = cristalQty.toString()
                }
            }
        }


        //DEUTERIO
        val deuterioJob = CoroutineScope(Dispatchers.IO).launch {
            while (true){
                withContext(Dispatchers.Main){
                    binding.cantDeuterioTv.text = deuterioQty.toString()
                }
                deuterioChannel.send(deuterioQty)
                delay(3000)
                deuterioQty++
            }
        }
        binding.naveDeuterioIv.setOnClickListener {
            val naveDeuterioJob = CoroutineScope(Dispatchers.IO).launch {
                deuterioChannel.receive()
                deuterioQty--
                withContext(Dispatchers.Main){
                    binding.cantDeuterioTv.text = deuterioQty.toString()
                }
            }
        }

    }

}