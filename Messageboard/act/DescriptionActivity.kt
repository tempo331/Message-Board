package com.neco_desarrollo.tabladeanuncioskotlinv2.act

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import com.neco_desarrollo.tabladeanuncioskotlinv2.adapters.ImageAdapter
import com.neco_desarrollo.tabladeanuncioskotlinv2.databinding.ActivityDescriptionBinding
import com.neco_desarrollo.tabladeanuncioskotlinv2.model.Ad
import com.neco_desarrollo.tabladeanuncioskotlinv2.utils.ImageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.fbTel.setOnClickListener { call() }
        binding.fbEmail.setOnClickListener { sendEmail() }
    }

    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            viewPager.adapter = adapter
        }
        getIntentFromMainAct()
        imageChangeCounter()
    }

    private fun getIntentFromMainAct(){
        ad = intent.getSerializableExtra("AD") as Ad
        if(ad != null)updateUI(ad!!)
    }

    private fun updateUI(ad: Ad){
        ImageManager.fillImageArray(ad, adapter)
        fillTextViews(ad)
    }

    private fun fillTextViews(ad: Ad) = with(binding){
       tvTitle.text = ad.title
       tvDescription.text = ad.description
       tvEmail.text = ad.email
       tvPrice.text = ad.price
       tvTel.text = ad.tel
       tvCountry.text = ad.country
       tvCity.text = ad.city
       tvIndex.text = ad.index
       tvWithSent.text = isWithSent(ad.withSent.toBoolean())
    }

    private fun isWithSent(withSent: Boolean): String{
        return if(withSent) "Да" else "Нет"
    }

    private fun call(){
        val callUri = "tel:${ad?.tel}"
        val iCall = Intent(Intent.ACTION_DIAL)
        iCall.data = callUri.toUri()
        startActivity(iCall)
    }

    private fun sendEmail(){
        val iSendEmail = Intent(Intent.ACTION_SEND)
        iSendEmail.type = "message/rfc822"
        iSendEmail.apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(ad?.email))
            putExtra(Intent.EXTRA_SUBJECT, "Объявление")
            putExtra(Intent.EXTRA_TEXT, "Меня интересует ваше объявление!")
        }
        try{
          startActivity(Intent.createChooser(iSendEmail, "Открыть с"))
       }catch (e: ActivityNotFoundException){

       }
    }

    private fun imageChangeCounter(){
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCounter = "${position + 1}/${binding.viewPager.adapter?.itemCount}"
                binding.tvImageCounter.text = imageCounter
            }
        })
    }
}