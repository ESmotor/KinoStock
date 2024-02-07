package com.itskidan.kinostock.domain

import com.itskidan.kinostock.data.entity.Film

interface OnItemClickListener {
    fun onItemClick(film: Film)
}