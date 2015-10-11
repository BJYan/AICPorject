package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.UploadMobilePhoneInfoRequestArgs;
import network.aic.xj.common.response.UploadMobilePhoneInfoResponse;

public class UploadMobilePhoneInfoRequest extends BaseRequest<UploadMobilePhoneInfoResponse> {
    public UploadMobilePhoneInfoRequestArgs Args = null;
    
    @Override
    public short getType()
    {
        return MessageType.UploadMobilePhoneInfoRequest;
    }
}
