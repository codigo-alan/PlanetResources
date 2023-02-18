package com.example.planetresources.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.example.planetresources.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var jobPicoMetal : Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //crear channel metal
        val metalChannel = Channel<Int>(3)
        var metalQty = 0
        val metalQtyLiveData = MutableLiveData<Int>(0)

        //crear channel cristal
        val cristalChannel = Channel<Int>(3)
        val cristalQtyLiveData = MutableLiveData<Int>(0)

        //crear channel deuterio
        val deuterioChannel = Channel<Int>(3)
        //var deuterioQty = 0
        val deuterioQtyLiveData = MutableLiveData<Int>(0)

        //METAL
        jobPicoMetal = startNewJob(binding.picoMetalIv)

        metalQtyLiveData.observe(this){
            if (metalQtyLiveData.value!! >= 3) {
                jobPicoMetal.cancel()
                binding.picoMetalIv.visibility = View.GONE
            }
            else{
                if(!jobPicoMetal.isActive) {
                    binding.picoMetalIv.visibility = View.VISIBLE
                    jobPicoMetal = startNewJob(binding.picoMetalIv)
                }
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                withContext(Dispatchers.Main) {
                    binding.cantMetalTv.text = metalQtyLiveData.value!!.toString()
                }
                metalChannel.send(metalQtyLiveData.value!!)
                delay(1000)
                if (metalQtyLiveData.value!! < 3 ) {
                    metalQtyLiveData.postValue(metalQtyLiveData.value!! + 1)
                }
            }
        }



        binding.naveMetalIv.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                metalChannel.receive()
                if (metalQtyLiveData.value!! > 0 )  metalQtyLiveData.postValue(metalQtyLiveData.value!! - 1)
                withContext(Dispatchers.Main) {
                    binding.cantMetalTv.text = metalQtyLiveData.value!!.toString()
                    rotateNave(binding.naveMetalIv)
                    /*while (binding.naveMetalIv.y > 682.0) {
                        delay(500)
                        binding.naveMetalIv.y -= 50
                    }
                    binding.cantMetalTv.text = metalQty.toString()
                    metalChannel.receive()
                    if (metalQty > 0 ) metalQty--
                    while (binding.naveMetalIv.y < 822.0) {
                        delay(500)
                        binding.naveMetalIv.y += 50
                    }*/
                    //println(binding.naveMetalIv.y)//822.0
                }
            }
        }


        //CRISTAL
        var jobPicoCristal = startNewJob(binding.picoCristalIv)
        cristalQtyLiveData.observe(this){
            binding.cantCristalTv.text = cristalQtyLiveData.value!!.toString()
            if (cristalQtyLiveData.value!! >= 3) {
                jobPicoCristal.cancel()
                binding.picoCristalIv.visibility = View.GONE
            }else{
                if (!jobPicoCristal.isActive) {
                    binding.picoCristalIv.visibility = View.VISIBLE
                    jobPicoCristal = startNewJob(binding.picoCristalIv)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (true){
                withContext(Dispatchers.Main){
                    //binding.cantCristalTv.text = cristalQtyLiveData.value!!.toString()
                }
                cristalChannel.send(cristalQtyLiveData.value!!)
                delay(2000)
                if (cristalQtyLiveData.value!! < 3 ) cristalQtyLiveData.postValue(cristalQtyLiveData.value!! + 1)
            }
        }
        binding.naeCristalIv.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cristalChannel.receive()
                if (cristalQtyLiveData.value!! > 0 ) cristalQtyLiveData.postValue(cristalQtyLiveData.value!! - 1)
                withContext(Dispatchers.Main){
                    //binding.cantCristalTv.text = cristalQtyLiveData.value!!.toString()
                    rotateNave(binding.naeCristalIv)
                }
            }
        }


        //DEUTERIO
        var jobPicoDeuterio = startNewJob(binding.picoDeuterioIv)
        deuterioQtyLiveData.observe(this){
            binding.cantDeuterioTv.text = deuterioQtyLiveData.value!!.toString()
            if (deuterioQtyLiveData.value!! >= 3) {
                jobPicoDeuterio.cancel()
                binding.picoDeuterioIv.visibility = View.GONE
            }else{
                if (!jobPicoDeuterio.isActive) {
                    binding.picoDeuterioIv.visibility = View.VISIBLE
                    jobPicoDeuterio = startNewJob(binding.picoDeuterioIv)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (true){
                /*withContext(Dispatchers.Main){
                    //binding.cantDeuterioTv.text = deuterioQty.toString()
                    binding.cantDeuterioTv.text = deuterioQtyLiveData.value!!.toString()
                }*/
                //deuterioChannel.send(deuterioQty)
                deuterioChannel.send(deuterioQtyLiveData.value!!)
                delay(3000)
                //if (deuterioQty < 3 ) deuterioQty++
                if (deuterioQtyLiveData.value!! < 3)
                    deuterioQtyLiveData.postValue(deuterioQtyLiveData.value!! + 1)
            }
        }
        binding.naveDeuterioIv.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                deuterioChannel.receive()
                //if (deuterioQty > 0 ) deuterioQty--
                if (deuterioQtyLiveData.value!! > 0)
                    deuterioQtyLiveData.postValue(deuterioQtyLiveData.value!! - 1)
                withContext(Dispatchers.Main){
                    //binding.cantDeuterioTv.text = deuterioQty.toString()
                    binding.cantDeuterioTv.text = deuterioQtyLiveData.value!!.toString()
                    rotateNave(binding.naveDeuterioIv)
                }
            }
        }

    }

    private fun startNewJob(picoIv: ImageView): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            while (true){
                picoIv.rotation = 40f
                delay(200)
                picoIv.rotation = 0f
                delay(200)
            }
        }
    }

    private suspend fun rotateNave(naveIv: ImageView) {
        naveIv.rotation = 90f
        delay(50)
        naveIv.rotation = 180f
        delay(50)
        naveIv.rotation = 270f
        delay(50)
        naveIv.rotation = 360f
    }

}