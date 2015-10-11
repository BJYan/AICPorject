package network.com.citizensoft.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.ResponseCode;
import network.aic.xj.common.context.response.*;
import network.aic.xj.common.response.*;

import com.alibaba.fastjson.JSONObject;
import network.com.citizensoft.stream.OutputStreamWriter;

public class Message {

    public boolean Compress = false;
    public String Session = null;
    public short Type = 0;

    public String[] Data = null;

    public Message()
    {
        
    }
    
    public Message(boolean compress,String session, short type, String[] data) {
        this.Compress = compress;
        this.Session = session;
        this.Type = type;
        this.Data = data;
    }

    public byte[] toByte(boolean isLittleOrder) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(stream, isLittleOrder);
        writer.writeString(Session, Compress);
        writer.writeShort(Type);
        writer.writeInteger(Data.length);
        for (String data : Data) {
            writer.writeString(data, Compress);
        }
        stream.close();
        byte[] body = stream.toByteArray();
        stream = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(stream, isLittleOrder);
        writer.writeInteger(body.length + 1);
        writer.writeBoolean(Compress);
        writer.writeBytes(body);
        return stream.toByteArray();
    }

    public BaseResponse toResponse() throws Exception
    {
        if(this.Type == MessageType.AckServerCommandResponse)
        {
            AckServerCommandResponse response = new AckServerCommandResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else if(this.Type == MessageType.QueryOrganizationResponse)
        {
            QueryOrganizationResponse response = new QueryOrganizationResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            if(response.Info.Code.equals(ResponseCode.OK))
            {
            	response.Args = JSONObject.parseObject(this.Data[1], QueryOrganizationResponseArgs.class);
            }
            return response;
        }
        else if(this.Type == MessageType.QueryPlanResultResponse)
        {
            QueryPlanResultResponse response = new QueryPlanResultResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else if(this.Type == MessageType.QueryServerCommandResponse)
        {
            QueryServerCommandResponse response = new QueryServerCommandResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            response.Args = JSONObject.parseObject(this.Data[1], QueryServerCommandResponseArgs.class);
            return response;
        }
        else if(this.Type == MessageType.QueryServerInfoResponse)
        {
            QueryServerInfoResponse response = new QueryServerInfoResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            response.Args = JSONObject.parseObject(this.Data[1], QueryServerInfoResponseArgs.class);
            return response;
        }
        else if(this.Type == MessageType.UploadFirstOpenTempPlanResponse)
        {
            UploadFirstOpenTempPlanResponse response = new UploadFirstOpenTempPlanResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else if(this.Type == MessageType.UploadMobilePhoneInfoResponse)
        {
            UploadMobilePhoneInfoResponse response = new UploadMobilePhoneInfoResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else if(this.Type == MessageType.UploadNormalPlanResultResponse)
        {
            UploadNormalPlanResultResponse response = new UploadNormalPlanResultResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else if(this.Type == MessageType.UploadRFIDResponse)
        {
            UploadRFIDResponse response = new UploadRFIDResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else if(this.Type == MessageType.UploadTempPlanResultResponse)
        {
            UploadTempPlanResultResponse response = new UploadTempPlanResultResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else if(this.Type == MessageType.UploadWaveDataResponse)
        {
        	UploadWaveDataResponse response = new UploadWaveDataResponse();
            response.Session = this.Session;
            response.Info = JSONObject.parseObject(this.Data[0], ResponseInfo.class);
            return response;
        }
        else throw new Exception("unknow response type");
    }
}
