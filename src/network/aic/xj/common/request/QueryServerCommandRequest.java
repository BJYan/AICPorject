package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.context.request.QueryServerCommandRequestArgs;
import network.aic.xj.common.response.QueryServerCommandResponse;

public class QueryServerCommandRequest extends BaseRequest<QueryServerCommandResponse> {
    public QueryServerCommandRequestArgs Args = null;
    
    @Override
    public short getType()
    {
        return MessageType.QueryServerCommandRequest;
    }
}
