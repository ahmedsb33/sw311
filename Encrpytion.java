import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrpytion {
	public byte[] encmessgage(String messgage,SecretKey deskey) {
		byte[] et = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] t = messgage.getBytes();
			et = cipher.doFinal(t);
			
		} catch (Exception e) {

		}
		return et;
	}
}
class Decrpytion {
	public String decmessgage(byte[] enmessgage,SecretKeySpec deskey) {
		byte[] dt = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			dt = cipher.doFinal(enmessgage);
		} catch (Exception e) {

		}
		return new String(dt);
	}
}
