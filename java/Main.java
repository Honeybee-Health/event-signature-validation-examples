import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.util.Base64;
import java.util.Map;

public class Main {
    public static void main(String[] args)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException,
            ArrayStoreException, NullPointerException {
        Map<String, String> request = Map.of(
                "url", "https://webhook.site/bafb9675-e60a-4db3-ab3c-377061a12ed5",
                "method", "POST",
                "body",
                "{\"event_id\":\"5d62dca6-1ce3-4807-afc6-8cfaa47577c5\",\"event_type\":\"RX_RECEIVED\",\"patient_id\":\"v3y4od\",\"medication_requests\":[{\"id\":82,\"prescription_id\":82,\"active\":true,\"patient_id\":\"v3y4od\",\"prescriber_name\":\"Dr. Jane Foster\",\"receive_date\":\"2023-05-10T16:16:58.469+00:00\",\"drug_name\":\"ATORVASTATIN 10MG TABLET\",\"ndc\":\"5976201551\",\"sig_text\":\"Take 1 tablet daily\",\"written_qty\":90,\"days_supply\":null,\"refills_left\":2,\"expire_date\":\"2023-06-12T14:15:22Z\",\"drug_schedule\":4}]}");

        String CLIENT_SECRET = "[CLIENT_SECRET_HERE]";

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(CLIENT_SECRET.getBytes(StandardCharsets.UTF_8));
        String HASHED_CLIENT_SECRET = bytesToHex(hash);

        String encodedUrl = URLEncoder.encode(request.get("url"), StandardCharsets.UTF_8.toString());
        String encodedMethod = URLEncoder.encode(request.get("method"), StandardCharsets.UTF_8.toString());
        String encodedBody = URLEncoder.encode(request.get("body"), StandardCharsets.UTF_8.toString());

        String base = encodedMethod + encodedUrl + encodedBody;

        Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret_key = new SecretKeySpec(HASHED_CLIENT_SECRET.getBytes(), "HmacSHA1");
        sha1_HMAC.init(secret_key);

        byte[] hashBase = sha1_HMAC.doFinal(base.getBytes());
        byte[] hashBaseWithNewline = new byte[hashBase.length + 1];
        System.arraycopy(hashBase, 0, hashBaseWithNewline, 0, hashBase.length);
        hashBaseWithNewline[hashBase.length] = '\n';

        String base64HashBase = Base64.getEncoder().encodeToString(hashBaseWithNewline);

        System.out.println(base64HashBase);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
