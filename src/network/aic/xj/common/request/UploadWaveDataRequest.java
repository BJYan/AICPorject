package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.UploadWaveDataRequestArgs;
import network.aic.xj.common.response.UploadWaveDataResponse;

public class UploadWaveDataRequest extends BaseRequest<UploadWaveDataResponse> {
	public UploadWaveDataRequestArgs Args = null;
	
	@Override
    public short getType()
    {
        return MessageType.UploadWaveDataRequest;
    }
}
