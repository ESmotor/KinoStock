package com.itskidan.kinostock.domain

import com.itskidan.core_api.entity.Film


interface OnItemClickListener {
    fun onItemClick(film: Film)
}