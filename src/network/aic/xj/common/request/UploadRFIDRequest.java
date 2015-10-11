package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.UploadRFIDRequestArgs;
import network.aic.xj.common.response.UploadRFIDResponse;

public class UploadRFIDRequest extends BaseRequest<UploadRFIDResponse> {
    public UploadRFIDRequestArgs Args = null;
    
    @Override
    public short getType()
    {
        return MessageType.UploadRFIDRequest;
    }
}
