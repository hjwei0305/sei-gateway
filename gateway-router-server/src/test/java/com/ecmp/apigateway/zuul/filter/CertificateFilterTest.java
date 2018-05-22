package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.model.vo.JwtVo;
import com.ecmp.util.JsonUtils;
import com.ecmp.util.RSAUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/22;ProjectName:api-gateway;
 */
public class CertificateFilterTest {

    CertificateFilter certificateFilter = new CertificateFilter();

    // 私钥
    String prKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCVVpYPaHKAhc0QHMPsuYH1Eja/r5lgRrA90uh9fWOG9waDwVWFkBiC/G7c1KGpVI2hXvZW1hebQKWWPkdfkmFpoLLxK0x9yjNtdEXmF3NveBSvWUFw3R1ZWSvjut6SnQhhsm7zlQN7I18OUSKNCElSr3F1wf1meqzGKtxwmslUJuT0Qf7bkEDoHmnslHSoOlfwDC4TQjkz0GgYy8BJJI8w1vAYpzhleKu1zG8KGklu7lco4pds2rABlDMiS6OZtNGCOSbZVHvlgUjr9K/hFrjqxb+CwDYhrwgnvLf0XeTHTm110TIHKynQBBM71x8so1bTaegk90bHrUvP+qv3K8j5AgMBAAECggEAawOQJtxu6OB40Sn70EAi+24g432/B/m/w53GUl6vGoEcHwBE+6AFoJ+qBf8tFX1svI2jMxjZ87wArMhWNwtbWOj6Mc4YUy+5aJ7glACMDP3cvMbWGT2qEWPQCrHXWvLCldfaoBxRUUVIkabcfnnIqULlqD/qCSyxcJQ4QUz6Jp1aAjWKDDpGOp2K15k5wwslOUrs9kIl3oyr3NJTtRRxJpD/VHCtaeTpqu9Mry/Zvf1k8m9omIQWpgUhhG0mWKXNZcorbHX2irb9sVqEX2KCoY1qkpB6xEKSKw74/Sletz+QB2Z2bNYo9sAnXKE7MVX5xY3d6epjWbg5KiDD1J3E7QKBgQDeJ8VH0Gknm8Map2w3vlfVeHrDb/SV3uUOGYyOuG+EKy3YSor5vFC9kaFev6MuhhDeFZQHcqb3hk5x9bztgL+hvT7H0LhlIgTbCQc7t2rct2MHTYJwS3qseoftb3AvDI456N4f9oho2b6xGCXRyZqkuzcgVQnFj586nQQCeMJFGwKBgQCsFuRGCyVA/QL/1JuiwUp855DFnaIvEoLXP+8bzsXJnqlDNbgnCFdfoOlPIOXP1+lVuz4Pz77BZ4Y7k1FmCc2KXnNc2CRSooJq8615xyd8Tfyaek/YyqhBzsSTEecnIuGyZdti8PpgNf5mBZPVqUgIC2l8Nkw6IW4EoH4r09gPewKBgBEzHxEKT7njU12y7Kv8Lqy2YTrUZilcUnue+sTp/qhK94exbcUcEVw79DRzvdySQKgRH57b/5VWbqhgEDqLJ0sMbdnmjQA3fAzBTDARaVKhWCdultS+40aLTg7R++uIm1JcdyYeXj1P7nWjmvn9E94BKKl2LAWBsyBEm9TdKJ1VAoGAXDFvXe7+qFBj2Pbn4ku3Qi80qgoHJzy9h9+ik700zQ0JFQ6kd5TKalrkYxX2yGCdiuZjG0lqomZ5zVThGM9LpENxfl31J6gUQoQFfeYpyzhkicXZHQWFGqxRN5Exyw+u5koXPGxpVj3W6oU2bBLimhyzCG52a/YY4LWyonw9lh0CgYBOL0LCz8UnrR4iIdGIdXYtCNy07R40oT4MV7aTo25OVMGo8u+e3Y6gnjqUEDlZWYNykjTi/i/2c6fiWi9k49lLKK5uExq8Q5IbRpvslzbCaqO0z3KBbboFdb0t3hC3CM6f4rEpkbg9P1KfPMotWRbiLQsqDwV3QI0GaBWQUIyWlg==";
    //公钥
    String puKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlVaWD2hygIXNEBzD7LmB9RI2v6+ZYEawPdLofX1jhvcGg8FVhZAYgvxu3NShqVSNoV72VtYXm0Cllj5HX5JhaaCy8StMfcozbXRF5hdzb3gUr1lBcN0dWVkr47rekp0IYbJu85UDeyNfDlEijQhJUq9xdcH9ZnqsxirccJrJVCbk9EH+25BA6B5p7JR0qDpX8AwuE0I5M9BoGMvASSSPMNbwGKc4ZXirtcxvChpJbu5XKOKXbNqwAZQzIkujmbTRgjkm2VR75YFI6/Sv4Ra46sW/gsA2Ia8IJ7y39F3kx05tddEyBysp0AQTO9cfLKNW02noJPdGx61Lz/qr9yvI+QIDAQAB";


    @Test
    public void run() throws Exception {
        JwtVo jwtVo = new JwtVo();
        String val = "{userId:1,account:2,name:liusonglin}";
        jwtVo.setAppId("DC4C6C2C-C4FC-11E7-9A52-005056930C6B");
        jwtVo.setVal(RSAUtils.encryptByPrivateKey(val,prKey));
        jwtVo.setKey(val);
        jwtVo.setUri("/test");

//        certificateFilter.setCertificationCenterUrl("http://10.4.68.77:9099/oauth2-server/api/clientSystem/findOneByAppId");
//        run(buildJWT(jwtVo));
    }


    //**测试用例，抛开前端请求
//    public String run(String jwt){
//        JwtVo jwtVo = certificateFilter.parseJwt(jwt);
//        String result = certificateFilter.getPublicKey(jwtVo.getAppId());
//        Map<String,Object> resultMap = JsonUtils.fromJson(result, Map.class);
//        String publicKey = String.valueOf(resultMap.get("publicKey"));
//        String valJson = null;
//        try {
//            valJson = RSAUtils.decryptByPublicKey(jwtVo.getVal(), publicKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return valJson;
//    }

    private static final long DEFAULT_EXPIRATION = 30*1000L*100;


    public static String buildJWT(JwtVo jwtVo){

        Date now = new Date();
        long expMillis = now.getTime() + DEFAULT_EXPIRATION;

        String jwtVoStr = JsonUtils.toJson(jwtVo);

        JwtBuilder builder = Jwts.builder().setId(jwtVo.getAppId())
                .setIssuedAt(now)
                .setSubject(jwtVoStr)
                .setIssuer(jwtVo.getAppId());

        builder.setExpiration(new Date(expMillis));
        return builder.compact();
    }

}