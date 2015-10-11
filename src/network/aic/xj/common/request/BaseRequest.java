package network.aic.xj.common.request;

import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import network.com.citizensoft.common.util.DateUtil;
import network.com.citizensoft.network.Message;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.response.BaseResponse;

public class BaseRequest<T extends BaseResponse> {
    public String Session = null;
    
    public short getType()
    {
        return 0;
    }
    
    //APP本机MAC
    public String Source_MAC = null;
    //APP本机IP
    public String Source_IP = null;
    //目标MAC，未使用
    public String Target_MAC = null;
    //目标IP，未使用
    public String Target_IP = null;
    //调用用户ID
    public String User = null;
    //创建时间
    public String CreateTime = null;
    //过期时间，未使用
    public String ExpireTime = null;
    
    
    public BaseRequest()
    {
        Session = UUID.randomUUID().toString();
        try {
            CreateTime = DateUtil.parseDate(new Date(System.currentTimeMillis()));
        } catch(Exception ex){}
    }
    
    private RequestInfo generateRequestInfo()
    {
        return new RequestInfo(Source_MAC, Source_IP, Target_MAC, Target_IP, User, CreateTime, ExpireTime);
    }
    
    public Message toMessage()
    {
        Message message = new Message();
        message.Compress = false;
        message.Session = Session;
        message.Type = this.getType();
        if(this instanceof QueryPlanResultRequest)
        {
            
        }
        else if(this instanceof QueryServerCommandRequest)
        {
            message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo()),
                    JSON.toJSONString(((QueryServerCommandRequest)this).Args)};
        }
        else if(this instanceof UploadMobilePhoneInfoRequest)
        {
            message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo()),
                    JSON.toJSONString(((UploadMobilePhoneInfoRequest)this).Args)};
        }
        else if(this instanceof UploadRFIDRequest)
        {
            message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo()),
                    JSON.toJSONString(((UploadRFIDRequest)this).Args)};
        }
        else if(this instanceof UploadNormalPlanResultRequest)
        {
            message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo()),
                    JSON.toJSONString(((UploadNormalPlanResultRequest)this).Args)};
            message.Compress = true;
        }
        else if(this instanceof UploadTempPlanResultRequest)
        {
            message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo()),
                    JSON.toJSONString(((UploadTempPlanResultRequest)this).Args)};
            message.Compress = true;
        }
        else if(this instanceof UploadWaveDataRequest)
        {
        	message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo()),
                    JSON.toJSONString(((UploadWaveDataRequest)this).Args)};
            message.Compress = true;
        }
        else if(this instanceof AckServerCommandRequest)
        {
        	message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo()),
                    JSON.toJSONString(((AckServerCommandRequest)this).Args)};
        }
        else
        {
            message.Data = new String[]{JSON.toJSONString(this.generateRequestInfo())};
        }
        
        return message;
    }
}
