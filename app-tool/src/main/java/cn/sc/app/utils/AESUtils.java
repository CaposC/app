package cn.sc.app.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * @author xiaodi on 2021/9/13 0013.
 */
public class AESUtils {

    private static final String ALGORITHM="AES/CBC/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String decrypt(String content,String key,String iv){
        try{
            Cipher cipher=Cipher.getInstance(ALGORITHM);
            byte[] sessionKey= Base64.getDecoder().decode(key);
            SecretKey secretKey=new SecretKeySpec(sessionKey,"AES");
            byte [] ivByte=Base64.getDecoder().decode(iv);
            AlgorithmParameterSpec parameterSpec=new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE,secretKey,parameterSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(content)),"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
