package com.innovativequest.ayesha_naeem_catering.data

import com.innovativequest.ayesha_naeem_catering.MealType

interface MealItem{
    val summary: String?
    val imgUrl: String?
    val description: String?
    val id: String?
    val title: String?
    val heroImg: String?
    val mealType: MealType
}
