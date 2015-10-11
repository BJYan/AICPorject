package network.aic.xj.client;

import network.com.citizensoft.network.Message;
import network.com.citizensoft.network.SocketCallTimeout;
import network.com.citizensoft.network.SocketCaller;

import network.aic.xj.common.request.BaseRequest;
import network.aic.xj.common.response.BaseResponse;

public class ServiceProvider {
    public String ServerIP = null;
    public int Port = 0;
    
    public ServiceProvider()
    {
        ServerIP = "127.0.0.1";
    }
    
    @SuppressWarnings("unchecked")
    public <T extends BaseResponse> T Execute(BaseRequest<T> request,SocketCallTimeout timeout) throws Exception 
    {
        Message message = SocketCaller.executeMethod(ServerIP, Port, request.toMessage(), timeout);
        return (T) message.toResponse();
    }
}
