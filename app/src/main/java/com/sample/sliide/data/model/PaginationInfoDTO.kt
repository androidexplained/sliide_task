package com.sample.sliide.data.model

data class PaginationInfoDTO(
	val total: Int,
	val pages: Int,
	val page: Int,
	val limit: Int
)