package network.com.citizensoft.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {
    public static byte[] compress(byte[] data) throws IOException
    {
        GZIPOutputStream gzip = null;
        ByteArrayOutputStream stm = null;
        try
        {
            stm = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(stm);
            int count = data.length / 4096;
            for(int i = 0;i < count;i ++)
            {
            	gzip.write(data,i * 4096,4096);
            }
            int left = data.length % 4096;
            if(left != 0)
            {
            	gzip.write(data,count * 4096,left);
            }
            gzip.flush();
            gzip.finish();
            return stm.toByteArray();
        }
        finally
        {
            if(gzip != null) gzip.close();
            if(stm != null) stm.close();
        }
    }
    
    public static byte[] decompress(byte[] data) throws IOException
    {
        GZIPInputStream gzip = null;
        ByteArrayInputStream in = null;
        ByteArrayOutputStream out = null;
        try
        {
            in = new ByteArrayInputStream(data);
            out = new ByteArrayOutputStream();
            gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[4096];
            int len = 0;
            while((len = gzip.read(buffer)) > 0)
            {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        }
        finally
        {
            if(gzip != null) gzip.close();
            if(in != null) in.close();
            if(out != null) out.close();
        }
        
    }
}
