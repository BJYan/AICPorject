package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.UploadTempPlanResultRequestArgs;
import network.aic.xj.common.response.UploadTempPlanResultResponse;

public class UploadTempPlanResultRequest extends BaseRequest<UploadTempPlanResultResponse> {
    public UploadTempPlanResultRequestArgs Args = null;
    
    @Override
    public short getType()
    {
        return MessageType.UploadTempPlanResultRequest;
    }
}
