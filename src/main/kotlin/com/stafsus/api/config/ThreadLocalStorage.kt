package com.stafsus.api.config

object ThreadLocalStorage {
	private val tenant = ThreadLocal<Long>()
	fun setTenant(tenantId: Long?) {
		if (tenantId != null)
			tenant.set(tenantId)
	}

	fun getTenantId(): Long? {
		return tenant.get()
	}
}