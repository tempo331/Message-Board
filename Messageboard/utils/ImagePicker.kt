package com.neco_desarrollo.tabladeanuncioskotlinv2.utils

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import com.neco_desarrollo.tabladeanuncioskotlinv2.R
import com.neco_desarrollo.tabladeanuncioskotlinv2.act.EditAdsAct
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImagePicker {
    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
   private fun getOptions(imageCounter : Int): Options {
        val options = Options().apply {
            count = imageCounter
            isFrontFacing = false
            mode = Mode.Picture
            path = "/pix/images"
        }
       return options
    }

    fun getMultiImages(edAct: EditAdsAct, imageCounter: Int){
       edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)){ result->
           when (result.status) {
               PixEventCallback.Status.SUCCESS -> {
                  getMultiSelectedImages(edAct, result.data)
               }
           }
       }
    }
    fun addImages(edAct: EditAdsAct, imageCounter: Int){
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)){ result->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    openChooseImageFrag(edAct)
                    edAct.chooseImageFrag?.updateAdapter(result.data as ArrayList<Uri>, edAct)
                }
            }
        }
    }
    fun getSingleImage(edAct: EditAdsAct){
        edAct.addPixToActivity(R.id.place_holder, getOptions(1)){ result->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                   openChooseImageFrag(edAct)
                   singleImage(edAct, result.data[0])
                }
            }
        }
    }

    private fun openChooseImageFrag(edAct: EditAdsAct){
        edAct.supportFragmentManager.beginTransaction().replace(R.id.place_holder, edAct.chooseImageFrag!!).commit()
    }

    private fun closePixFrag(edAct: EditAdsAct){
        val fList = edAct.supportFragmentManager.fragments
        fList.forEach {
            if(it.isVisible) edAct.supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    fun getMultiSelectedImages(edAct: EditAdsAct, uris: List<Uri>) {
        if (uris.size > 1 && edAct.chooseImageFrag == null) {

            edAct.openChooseImageFrag(uris as ArrayList<Uri>)

        } else if (uris.size == 1 && edAct.chooseImageFrag == null) {
            CoroutineScope(Dispatchers.Main).launch {
                edAct.binding.pBarLoad.visibility = View.VISIBLE
                val bitMapArray =
                    ImageManager.imageResize(uris as ArrayList<Uri>, edAct) as ArrayList<Bitmap>
                edAct.binding.pBarLoad.visibility = View.GONE
                edAct.imageAdapter.update(bitMapArray)
                closePixFrag(edAct)
            }

        }
    }

    private fun singleImage(edAct: EditAdsAct, uri: Uri){
       edAct.chooseImageFrag?.setSingleImage(uri, edAct.editImagePos)
    }
}