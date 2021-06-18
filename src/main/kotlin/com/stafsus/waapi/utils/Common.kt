package com.stafsus.waapi.utils

import org.apache.commons.lang3.StringUtils

class Common {
    companion object{
        fun getUsernameByEmail(email: String): String {
            if(StringUtils.isEmpty(email) && !StringUtils.contains(email, "@")) return ""
            return email.split("@")[0]
        }
    }
}