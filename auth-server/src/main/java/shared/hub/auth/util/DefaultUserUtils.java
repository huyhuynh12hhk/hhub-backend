package shared.hub.auth.util;

import java.util.UUID;

public class DefaultUserUtils {
    public static String generateFullName(){

        return "User"+UUID.randomUUID().toString().substring(0,8);
    }
}
