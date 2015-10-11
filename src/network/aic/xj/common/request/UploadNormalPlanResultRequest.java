package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.UploadNormalPlanResultRequestArgs;
import network.aic.xj.common.response.UploadNormalPlanResultResponse;

public class UploadNormalPlanResultRequest extends BaseRequest<UploadNormalPlanResultResponse> {
    public UploadNormalPlanResultRequestArgs Args = null;
    
    @Override
    public short getType()
    {
        return MessageType.UploadNormalPlanResultRequest;
    }
}
