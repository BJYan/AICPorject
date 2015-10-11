package network.aic.xj.common.request;

public class RequestInfo {
    
    public String Source_MAC = null;
    public String Source_IP = null;
    public String Target_MAC = null;
    public String Target_IP = null;

    public String User = null;
    public String CreateTime = null;
    public String ExpireTime = null;
    
    public RequestInfo(){}

    public RequestInfo(String source_MAC, String source_IP, String target_MAC, String target_IP, String user, String createTime, String expireTime) {
        super();
        Source_MAC = source_MAC;
        Source_IP = source_IP;
        Target_MAC = target_MAC;
        Target_IP = target_IP;
        User = user;
        CreateTime = createTime;
        ExpireTime = expireTime;
    }
    
    
    
    
}
