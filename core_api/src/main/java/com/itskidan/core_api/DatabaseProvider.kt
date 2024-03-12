package com.itskidan.core_api

import com.itskidan.core_api.dao.FilmDao

interface DatabaseProvider {
    fun filmDao(): FilmDao
}