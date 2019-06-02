/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.gateway.util;

import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Named;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


@Named
public class KeystoreGateway{
	
	@Value("${keystorePassword}")
	private String keystorePassword ;
	
	@Value("${keystoreKeyPass}")
	private String keyPass; 
	private Logger logger=LoggerFactory.getLogger(KeystoreGateway.class);

	public String getAESDecryptedValue(String encryptedInput){

		String decryptedValue = null ;
		FileInputStream stream = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			stream = new FileInputStream("mykeystore.jks");
			if(stream!=null){
				keyStore.load(stream, keystorePassword.toCharArray());
				Key key = keyStore.getKey("mykey", keyPass.toCharArray());
				decryptedValue = decryptWithAESKey(encryptedInput, key.getEncoded());
			}
		} catch (Exception e) {
			logger.debug("2---exception occured in catch{}",e.getMessage());
		}finally{
			if(stream!=null){
				try{
					stream.close();
				}catch(Exception e){
					logger.debug("2---exception occured in finally catch block{}",e.getMessage());
				}
			}
		}
		return decryptedValue;
	}

	 public static String decryptWithAESKey(String inputData, byte[] key) throws NoSuchAlgorithmException,
	   NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	  Cipher cipher = Cipher.getInstance("AES");
	  SecretKey secKey = new SecretKeySpec(key, "AES");

	  cipher.init(Cipher.DECRYPT_MODE, secKey);
	  byte[] newData = cipher.doFinal(Base64.decodeBase64(inputData.getBytes()));
	  return new String(newData);

	 }

}

