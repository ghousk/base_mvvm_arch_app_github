package com.innovativequest.ayesha_naeem_catering.data

import com.google.gson.annotations.SerializedName
import com.innovativequest.ayesha_naeem_catering.MealType

data class StartersItem(

	@field:SerializedName("summary")
	override val summary: String? = null,

	@field:SerializedName("quantity")
	val quantity: String? = null,

	@field:SerializedName("img_url")
	override val imgUrl: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("description")
	override val description: String? = null,

	@field:SerializedName("id")
	override val id: String? = null,

	@field:SerializedName("title")
	override val title: String? = null,

	@field:SerializedName("hero_img")
	override val heroImg: String? = null,

	override val mealType: MealType
) : MealItem
