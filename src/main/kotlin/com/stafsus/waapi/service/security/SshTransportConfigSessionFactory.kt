package com.stafsus.waapi.service.security

import com.jcraft.jsch.JSch
import org.eclipse.jgit.api.TransportConfigCallback
import org.eclipse.jgit.transport.*
import org.eclipse.jgit.util.FS
import java.io.File


class SshTransportConfigCallback(
        val privateKeyPath: String
) : TransportConfigCallback {
    private val sshSessionFactory: SshSessionFactory = object : JschConfigSessionFactory() {
        override fun createDefaultJSch(fs: FS?): JSch {
            val defaultJSch = super.createDefaultJSch(fs)
            defaultJSch.removeAllIdentity()
            defaultJSch.addIdentity(File(privateKeyPath).absolutePath)
            return defaultJSch
        }

        override fun configure(hc: OpenSshConfig.Host?, session: com.jcraft.jsch.Session) {
            session.setConfig("StrictHostKeyChecking", "no")
        }
    }

    override fun configure(transport: Transport) {
        val sshTransport = transport as SshTransport
        sshTransport.sshSessionFactory = sshSessionFactory
    }
}