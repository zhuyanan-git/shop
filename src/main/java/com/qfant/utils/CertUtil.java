package com.qfant.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import com.qfant.config.WxMpConfig;
import com.sun.javafx.fxml.expression.KeyPath;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;


/**
 * 加载证书的类
 * @author Administrator
 *
 */
@Component
public class CertUtil {




    /**
     * 加载证书
     */
    public static SSLConnectionSocketFactory initCert(String mchid) throws Exception {

        FileInputStream instream = null;
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        instream = new FileInputStream(new File("F:/apiclient_cert.p12"));
         keyStore.load(instream, mchid.toCharArray());
        if (null != instream) {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,mchid.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        return sslsf;
    }
}
