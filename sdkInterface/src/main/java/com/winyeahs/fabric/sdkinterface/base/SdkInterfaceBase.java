package com.winyeahs.fabric.sdkinterface.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.helper.Utils;

import java.io.*;
import java.net.URLDecoder;
import java.security.PrivateKey;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkInterfaceBase {
    private static final Log log = LogFactory.getLog(SdkInterfaceBase.class);

    /**
     * 获取channel-artifacts配置路径
     *
     * @return /WEB-INF/classes/fabric/channel-artifacts/
     */
        /*
    protected String getChannleArtifactsPath(String module) {
        String directorys = SdkInterfaceBase.class.getClassLoader().getResource("fabric").getFile();
        File directory = new File(directorys);
        return directory.getPath() + "/" + module + "/channel-artifacts/";
    }

    /**
     * 获取crypto-config配置路径
     *
     * @return /WEB-INF/classes/fabric/crypto-config/
     */
    /*
    protected String getCryptoConfigPath(String module) {
        String directorys = SdkInterfaceBase.class.getClassLoader().getResource("fabric").getFile();
        try {
            directorys = URLDecoder.decode(directorys, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File directory = new File(directorys);
        return directory.getPath() + "/" + module + "/crypto-config/";
    }
    */
    /**
     * 将日期转换为字符串
     *
     * @param date
     *            date日期
     *
     * @return 日期字符串
     */
    protected String parseDateFormat(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 通过字节数组信息获取私钥
     *
     * @param data 字节数组
     * @return 私钥
     */
    protected PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException {
        final Reader pemReader = new StringReader(new String(data));
        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }
//        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
//        return privateKey;
        return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
    }

    /**
     * 从指定路径中获取后缀为 _sk 的文件，且该路径下有且仅有该文件
     *
     * @param directory 指定路径
     * @return File
     */
    protected File findFileSk(File directory) {
        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));
        if (null == matches) {
            throw new RuntimeException(String.format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }
        if (matches.length != 1) {
            throw new RuntimeException(String.format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }
        return matches[0];
    }

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String grpcTLSify(boolean openTLS, String location) {
        location = location.trim();
        Exception e = Utils.checkGrpcUrl(location);
        if (e != null) {
            throw new RuntimeException(String.format("Bad TEST parameters for grpc url %s", location), e);
        }
        return openTLS ? location.replaceFirst("^grpc://", "grpcs://") : location;

    }

    protected String httpTLSify(boolean openCATLS, String location) {
        location = location.trim();
        return openCATLS ? location.replaceFirst("^http://", "https://") : location;
    }
}
