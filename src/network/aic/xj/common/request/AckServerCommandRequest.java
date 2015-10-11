package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.AckServerCommandRequestArgs;
import network.aic.xj.common.response.AckServerCommandResponse;

public class AckServerCommandRequest extends BaseRequest<AckServerCommandResponse> {
    public AckServerCommandRequestArgs Args = null;
    
    @Override
    public short getType()
    {
        return MessageType.AckServerCommandRequest;
    }
}
