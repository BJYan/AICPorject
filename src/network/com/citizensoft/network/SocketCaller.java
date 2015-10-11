package network.com.citizensoft.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

import network.com.citizensoft.common.util.HexUtil;
import network.com.citizensoft.stream.InputStreamReader;

public class SocketCaller {
    public static Message executeMethod(String serverIp, int serverPort,String session, short type, String[] data, boolean compress, SocketCallTimeout timeout) throws IOException {
        Socket client = null;
        try {
            client = new Socket();
            client.connect(new InetSocketAddress(serverIp, serverPort), timeout == null ? 5000 : timeout.ConnectTimeoutSeconds * 1000);
            client.setReceiveBufferSize(8192);
            client.setSendBufferSize(8192);

            Message request = new Message(compress,session,type,data);

            client.getOutputStream().write(request.toByte(false));
            client.getOutputStream().flush();

            return readMessage(client.getInputStream());
        } finally {
            if (client != null)
                client.close();
        }
    }
    
    public static Message executeMethod(String serverIp, int serverPort, Message request, SocketCallTimeout timeout) throws IOException
    {
        return executeMethod(serverIp,serverPort,request.Session, request.Type,request.Data,request.Compress,timeout);
    }

    public static Message readMessage(InputStream stream) throws IOException {
        InputStreamReader reader = new InputStreamReader(stream, false);

        byte[] header = reader.readBytesLoop(5);
        int len = HexUtil.getInt(header);
        Message message = new Message();
        message.Compress = header[4] == 1;

        byte[] body = reader.readBytesLoop(len - 1);
        reader = new InputStreamReader(new ByteArrayInputStream(body), false);
        message.Session = reader.readString(false);
        message.Type = reader.readShort();
        message.Data = new String[reader.readInteger()];
        for (int i = 0; i < message.Data.length; i++) {
            message.Data[i] = reader.readString(message.Compress);
        }

        return message;
    }
}
