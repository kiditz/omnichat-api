package com.stafsus.api.entity

enum class Authority(val text: String) {
	ADMIN("Administrator"), STAFF("Staff"), SUPERVISOR("Supervisor"), UNKNOWN("Does not have any access"),
}