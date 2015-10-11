package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.UploadFirstOpenTempPlanRequestArgs;
import network.aic.xj.common.response.UploadFirstOpenTempPlanResponse;

public class UploadFirstOpenTempPlanRequest extends BaseRequest<UploadFirstOpenTempPlanResponse> {
	public UploadFirstOpenTempPlanRequestArgs Args = null;
	
    @Override
    public short getType()
    {
        return MessageType.UploadFirstOpenTempPlanRequest;
    }
}
