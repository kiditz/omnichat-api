package com.stafsus.api.service

import com.stafsus.api.entity.Company
import com.stafsus.api.entity.UserPrincipal


interface CompanyService {
	fun getCompaniesByUser(userPrincipal: UserPrincipal): List<Company>
}