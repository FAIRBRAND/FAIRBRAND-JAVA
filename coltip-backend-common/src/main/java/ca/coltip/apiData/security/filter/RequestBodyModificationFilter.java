package ca.coltip.apiData.security.filter;

import ca.coltip.apiData.security.wrapper.ModifiedRequestWrapper;
import ca.coltip.utils.EncryptionUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class RequestBodyModificationFilter implements Filter {

    private final String key = "DRZtDUquBvQk3o6LZYb/kuyKyT+VVWM3LQIaGjxs/Ik=";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        if(servletRequest instanceof HttpServletRequest httpRequest){
            if(httpRequest.toString().contains("/api/")){
                // Read the original body
                String originalBody = getRequestBody(httpRequest);
                String modifiedBody = "";
                try{
                    // Modify the body as needed
                    modifiedBody = EncryptionUtils.decrypt(originalBody, key);
                }
                catch (InvalidKeyException | InvalidAlgorithmParameterException
                       | IllegalBlockSizeException | BadPaddingException
                       | NoSuchAlgorithmException | NoSuchPaddingException e){
                    //throw some sort of error
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                // Wrap the request to allow reading the modified body
                ModifiedRequestWrapper requestWrapper = new ModifiedRequestWrapper(httpRequest, modifiedBody);

                // Continue with the chain using the modified body
                chain.doFilter(requestWrapper, servletResponse);
            }
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        return body.toString();
    }
}
