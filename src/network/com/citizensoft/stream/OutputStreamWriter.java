package network.com.citizensoft.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import network.com.citizensoft.common.util.ArrayUtil;
import network.com.citizensoft.common.util.HexUtil;

public class OutputStreamWriter {
    private OutputStream _stream = null;
    private boolean _isLittleOrder = false;

    public OutputStreamWriter(OutputStream stream, boolean isLittleOrder) {
        this._stream = stream;
        this._isLittleOrder = isLittleOrder;
    }

    public void writeInteger(int value) throws IOException {
        byte[] data = HexUtil.getBytes(value);
        if (this._isLittleOrder) {
            data = ArrayUtil.reverse(data);
        }
        writeBytes(data);
    }

    public void writeShort(short value) throws IOException {
        byte[] data = HexUtil.getBytes(value);
        if (this._isLittleOrder) {
            data = ArrayUtil.reverse(data);
        }
        writeBytes(data);
    }

    public void writeLong(long value) throws IOException {
        byte[] data = HexUtil.getBytes(value);
        if (this._isLittleOrder) {
            data = ArrayUtil.reverse(data);
        }
        writeBytes(data);
    }

    public void writeBoolean(boolean value) throws IOException {
        if (value) {
            writeBytes(new byte[] { 1 });
        } else {
            writeBytes(new byte[] { 0 });
        }
    }

    public void writeString(String value,boolean compress) throws IOException {
        byte[] data = HexUtil.getBytes(value,compress);
        writeInteger(data.length);
        writeBytes(data);
    }

    public void writeFloat(float value) throws IOException {
        byte[] data = HexUtil.getBytes(value);
        if (this._isLittleOrder) {
            data = ArrayUtil.reverse(data);
        }
        writeBytes(data);
    }

    public void writeDouble(double value) throws IOException {
        byte[] data = HexUtil.getBytes(value);
        if (this._isLittleOrder) {
            data = ArrayUtil.reverse(data);
        }
        writeBytes(data);
    }

    public void writeBytes(byte[] value) throws IOException {
        _stream.write(value, 0, value.length);
    }
    
    public void writeByte(byte value) throws IOException {
        _stream.write(new byte[]{value},0,1);
    }
}
