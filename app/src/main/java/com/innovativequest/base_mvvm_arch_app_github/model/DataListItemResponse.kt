package com.innovativequest.base_mvvm_arch_app_github.model

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
	indices = [
		Index("hasMore"),
		Index("quotaMax")],
	primaryKeys = ["quotaRemaining"]
)
data class DataListItemResponse(

	@field:SerializedName("quota_max")
	val quotaMax: Int? = null,

	@field:SerializedName("quota_remaining")
	val quotaRemaining: Int? = null,

	@field:SerializedName("has_more")
	val hasMore: Boolean? = null,

	@field:SerializedName("items")
	val items: List<DataListItem>? = null
)