// src/helpers/PasswordHelper.java
package helpers;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHelper {
    
    // Mã hóa password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    
    // So sánh password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}