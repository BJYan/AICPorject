package network.com.citizensoft.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import network.com.citizensoft.common.util.ArrayUtil;
import network.com.citizensoft.common.util.HexUtil;

public class InputStreamReader {
    private InputStream _stream = null;
    private boolean _isLittleOrder = false;

    public InputStreamReader(InputStream stream, boolean isLittleOrder) {
        this._stream = stream;
        this._isLittleOrder = isLittleOrder;
    }

    public int readInteger() throws IOException {
        byte[] bytes = readBytes(4);
        if (_isLittleOrder) {
            bytes = ArrayUtil.reverse(bytes);
        }
        return HexUtil.getInt(bytes);
    }

    public short readShort() throws IOException {
        byte[] bytes = readBytes(2);
        if (_isLittleOrder) {
            bytes = ArrayUtil.reverse(bytes);
        }
        return HexUtil.getShort(bytes);
    }

    public long readLong() throws IOException {
        byte[] bytes = readBytes(8);
        if (_isLittleOrder) {
            bytes = ArrayUtil.reverse(bytes);
        }
        return HexUtil.getLong(bytes);
    }

    public boolean readBoolean() throws IOException {
        return readBytes(1)[0] == 1;
    }

    public String readString(boolean compress) throws IOException {
        int len = readInteger();
        return HexUtil.getString(readBytes(len), compress);
    }

    public float readFloat() throws IOException {
        byte[] bytes = readBytes(4);
        if (_isLittleOrder) {
            bytes = ArrayUtil.reverse(bytes);
        }
        return HexUtil.getFloat(bytes);
    }

    public double readDouble() throws IOException {
        byte[] bytes = readBytes(8);
        if (_isLittleOrder) {
            bytes = ArrayUtil.reverse(bytes);
        }
        return HexUtil.getDouble(bytes);
    }

    public byte[] readBytes(int len) throws IOException {
        byte[] buffer = new byte[len];
        int readlen = _stream.read(buffer, 0, len);
        if (readlen == len) {
            return buffer;
        } else
            throw new IOException("lack data");
    }
    
    public byte[] readBytesLoop(int len) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(len);
        byte[] buffer = new byte[len < 8192? len : 8192];
        int retrycount = 5;
        while(retrycount > 0)
        {
            int readlen = _stream.read(buffer, 0, buffer.length);
            if(readlen == 0)
            {
                if(stream.size() != len)
                {
                    retrycount--;
                }
                else break;
            }
            else
            {
                stream.write(buffer, 0, readlen);
                if(stream.size() == len)
                {
                    break;
                }
            }
        }
        if(stream.size() != len) throw new IOException("lack data");
        else return stream.toByteArray();
    }
    
    public byte readByte() throws IOException 
    {
        return (byte)_stream.read();
    }
}
