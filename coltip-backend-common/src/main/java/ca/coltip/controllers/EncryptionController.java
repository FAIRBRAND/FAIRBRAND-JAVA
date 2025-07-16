package ca.coltip.controllers;

import ca.coltip.apiData.request.EncryptionRequest;
import ca.coltip.apiData.response.EncryptionResponse;
import ca.coltip.services.impl.VaultService;
import ca.coltip.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class EncryptionController {
    //private final String key = "DRZtDUquBvQk3o6LZYb/kuyKyT+VVWM3LQIaGjxs/Ik=";

    @Autowired
    VaultService vaultService;

    private final String key = vaultService.getEncKey();

    @PostMapping("/test/encrypt")
    public EncryptionResponse encrypt(@RequestBody EncryptionRequest request){
        EncryptionResponse response = new EncryptionResponse();
        try{
            response.encryptedValue = EncryptionUtils.encrypt(request.valueToEncrypt,key);
            return response;
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException
               | IllegalBlockSizeException | BadPaddingException
               | NoSuchAlgorithmException | NoSuchPaddingException e){
            response.encryptedValue = "not encrypted";
            return response;
        }
        catch (Exception e){
            response.encryptedValue = "not encrypted";
            return response;
        }

    }

    @PostMapping("/test/testing/decrypt")
    public EncryptionResponse decrypt(@RequestBody EncryptionRequest request){
        EncryptionResponse response = new EncryptionResponse();
        try{
            response.encryptedValue = EncryptionUtils.decrypt(request.valueToEncrypt,key);
            return response;
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException
               | IllegalBlockSizeException | BadPaddingException
               | NoSuchAlgorithmException | NoSuchPaddingException e){
            response.encryptedValue = "not decrypted";
            return response;
        }
        catch (Exception e){
            response.encryptedValue = "not decrypted";
            return response;
        }
    }
}
