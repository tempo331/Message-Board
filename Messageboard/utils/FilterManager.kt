package com.neco_desarrollo.tabladeanuncioskotlinv2.utils

import com.neco_desarrollo.tabladeanuncioskotlinv2.model.Ad
import com.neco_desarrollo.tabladeanuncioskotlinv2.model.AdFilter
import java.lang.StringBuilder

object FilterManager {
    fun createFilter(ad: Ad): AdFilter{
        return AdFilter(
            ad.time,
            "${ad.category}_${ad.time}",
            "${ad.category}_${ad.country}_${ad.withSent}_${ad.time}",
            "${ad.category}_${ad.country}_${ad.city}_${ad.withSent}_${ad.time}",
            "${ad.category}_" +
                    "${ad.country}_" +
                    "${ad.city}_" +
                    "${ad.index}_" +
                    "${ad.withSent}_" +
                    ad.time,
            "${ad.category}_" +
                    "${ad.index}_" +
                    "${ad.withSent}_" +
                    ad.time,
            "${ad.category}_${ad.withSent}_${ad.time}",
            "${ad.country}_${ad.withSent}_${ad.time}",
            "${ad.country}_${ad.city}_${ad.withSent}_${ad.time}",
            "${ad.country}_" +
                    "${ad.city}_" +
                    "${ad.index}_" +
                    "${ad.withSent}_" +
                    ad.time,
            "${ad.index}_" +
                    "${ad.withSent}_" +
                    ad.time,
            "${ad.withSent}_${ad.time}"
        )
    }
    fun getFilter(filter: String): String{
        val sBuilderNode = StringBuilder()
        val sBuilderFilter = StringBuilder()
        val tempArray = filter.split("_")
        if(tempArray[0] != "empty"){
            sBuilderNode.append("country_")
            sBuilderFilter.append("${tempArray[0]}_")
        }
        if(tempArray[1] != "empty"){
            sBuilderNode.append("city_")
            sBuilderFilter.append("${tempArray[1]}_")
        }
        if(tempArray[2] != "empty"){
            sBuilderNode.append("index_")
            sBuilderFilter.append("${tempArray[2]}_")
        }
        sBuilderFilter.append(tempArray[3])
        sBuilderNode.append("withSent_time")
        return "$sBuilderNode|$sBuilderFilter"
    }
}