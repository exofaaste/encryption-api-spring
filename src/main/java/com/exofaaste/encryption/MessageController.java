package com.exofaaste.encryption;
//Begin Imports
//import org.springframework.web;
import org.springframework.web.bind.annotation.RestController;

import com.exofaaste.encryption.PGPEncryptionDataObject;
import com.exofaaste.encryption.PgpProcessingUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.util.Base64;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Value;
//end imports


@RestController
public class MessageController {
	
	//Starting Properties
	private static boolean isArmored = true;
	private static boolean integrityCheck = true;
	@Value("${pgp.public.key.path}")
	private  String publicKeyPath;
	private  String privateKeyPath = "C:\\Users\\exofa\\Documents\\encryptionKeys\\privateKey.gpg";
	private String errorMessage= "Unable to Encrypt your message at this moment.";
	
	//Starting Methods
	@GetMapping("/encrypt")
	public EncryptionResponseObject encryptedMessage(@RequestParam (value = "message") String message) {
		System.out.println("Starting Encryption Main");
		String someStringToEncrypt = "Some text to encrypt";
		PgpProcessingUtil pgpProcessingUtil = new PgpProcessingUtil();
		byte[] finalOutput = null;
		InputStream publicKeyInputStream;
		try {
			publicKeyInputStream = new FileInputStream(publicKeyPath);
			PGPPublicKey pgpPublicKey = pgpProcessingUtil.readPublicKey(publicKeyInputStream);
			PGPEncryptionDataObject pgpEncryptionDataObject = new PGPEncryptionDataObject(someStringToEncrypt,
					pgpPublicKey, null, Boolean.TRUE, Boolean.TRUE);
			finalOutput = pgpProcessingUtil.encryptData(pgpEncryptionDataObject);
			message = Base64.getEncoder().encodeToString(finalOutput);
		} catch (FileNotFoundException e) {
			message = errorMessage;
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			message = errorMessage;
			e.printStackTrace();
		} catch (IOException e) {
			message = errorMessage;
			e.printStackTrace();
		} catch (PGPException e) {
			message = errorMessage;
			e.printStackTrace();
		}
		return new EncryptionResponseObject(message);
	}
}
