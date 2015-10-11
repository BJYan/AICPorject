package network.com.citizensoft.common.util;

import java.util.Arrays;

public class ArrayUtil {
    public static <T> T[] reverse(T[] objs) {
        for (int i = 0; i < objs.length / 2; i++) {
            T temp = objs[i];
            objs[i] = objs[objs.length - i - 1];
            objs[objs.length - i - 1] = temp;
        }
        return objs;
    }
    
    public static byte[] reverse(byte[] objs) {
        for (int i = 0; i < objs.length / 2; i++) {
            byte temp = objs[i];
            objs[i] = objs[objs.length - i - 1];
            objs[objs.length - i - 1] = temp;
        }
        return objs;
    }
    
    public static byte[] subArray(byte[] bytes,int position,int length)
    {
        return Arrays.copyOfRange(bytes, position, position + length);
    }
}
