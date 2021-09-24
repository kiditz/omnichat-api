package com.stafsus.api.config

import java.lang.ThreadLocal
import com.stafsus.api.config.ThreadLocalStorage

object ThreadLocalStorage {
	private val tenant = ThreadLocal<Long>()
	fun setTenant(tenantId: Long?) {
		if (tenantId != null)
			tenant.set(tenantId)
	}

	fun getTenantId(): Long? {
		return tenant.get()
	}
//	var tenantId: Long?
//		get() = tenant.get()
//		set(tenantId) {
//			tenant.set(tenantId)
//		}
}