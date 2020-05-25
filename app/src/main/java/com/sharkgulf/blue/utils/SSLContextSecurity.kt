package com.sharkgulf.blue.utils

import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier
import javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import android.annotation.SuppressLint
import java.security.SecureRandom


class SSLContextSecurity {
    companion object{
//        /**
//         * 绕过验证
//         * @return
//         */
//        fun createIgnoreVerifySSL(sslVersion: String): SSLSocketFactory {
//            var sc = SSLContext.getInstance(sslVersion)
//            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
//                @Throws(CertificateException::class)
//                override fun checkClientTrusted(
//                    chain: Array<java.security.cert.X509Certificate>, authType: String) {
//                }
//
//                @Throws(CertificateException::class)
//                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
//                }
//
//                override fun getAcceptedIssuers(): Array<X509Certificate?> {
//                    return arrayOfNulls(0)
//                }
//            })
//
//            sc.init(null, trustAllCerts, java.security.SecureRandom())
//
//            // Create all-trusting host name verifier
//            val allHostsValid = HostnameVerifier { _, _ -> true }
//            /***
//             * 如果 hostname in certificate didn't match的话就给一个默认的主机验证
//             */
//            setDefaultSSLSocketFactory(sc.getSocketFactory());
//            setDefaultHostnameVerifier(allHostsValid);
//            return sc.socketFactory;
//        }
        @SuppressLint("TrulyRandom")
         fun createSSLSocketFactory(): SSLSocketFactory? {
            var sSLSocketFactory: SSLSocketFactory? = null
            try {
                var sc = SSLContext.getInstance("SSL")
                sc.init(
                    null, arrayOf(MyTrustManager()),
                    SecureRandom()
                )
                sSLSocketFactory = sc.socketFactory
            } catch (e: Exception) {
            }

            return sSLSocketFactory
        }
    }


}