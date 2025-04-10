/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.desencryption;

/**
 *
 * @author patry
 */
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class DesEncryption {

private static final String ALGORITHM = "DES";
    private final SecretKey secretKey;

    public DesEncryption(byte[] keyBytes) {
        if (keyBytes.length != 8) {
            throw new IllegalArgumentException("A DES kulcsnak 8 bájtosnak kell lennie.");
        }
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static DesEncryption generate() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(56);
        SecretKey secretKey = keyGen.generateKey();
        return new DesEncryption(secretKey.getEncoded());
    }

    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, "UTF8");
    }

    public byte[] getRawKey() {
        return secretKey.getEncoded();
    }

   public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
        System.out.print("Kulcs generálás (G) vagy meglévő kulcs beolvasás (B)? [G/B]: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        DesEncryption des;

        if ("B".equals(choice)) {
    System.out.print("Add meg a DES kulcsot (base64 formátumban): ");
    String base64Key = scanner.nextLine();

    try {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        if (keyBytes.length != 8) {
            System.err.println("⚠️ A kulcsnak pontosan 8 bájtosnak kell lennie (jelenleg " + keyBytes.length + " bájtos).");
            return;
        }

        des = new DesEncryption(keyBytes);

    } catch (IllegalArgumentException e) {
        System.err.println("❌ Hibás Base64 kód! Ellenőrizd, hogy jól adtad-e meg a kulcsot.");
        return;
    }
}
        //Egyszerű teszt A6cuW8S43Gk= kiCsiKe 
 else {
            des = DesEncryption.generate();
            System.out.println("✅ Új kulcs generálva (base64): " +
                    Base64.getEncoder().encodeToString(des.getRawKey()));
        }

        System.out.print("\nÍrd be a titkosítandó szöveget: ");
        String inputText = scanner.nextLine();
        String encrypted = des.encrypt(inputText);
        System.out.println("🔒 Titkosított szöveg: " + encrypted);

        System.out.print("\nÍrd be a titkosított szöveget visszafejtéshez: ");
        String encryptedInput = scanner.nextLine();
        String decrypted = des.decrypt(encryptedInput);
        System.out.println("🔓 Visszafejtett szöveg: " + decrypted);

    } catch (Exception e) {
        System.err.println("⚠️ Hiba: " + e.getMessage());
    }
}


}