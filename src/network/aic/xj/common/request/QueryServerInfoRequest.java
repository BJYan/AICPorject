package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.response.QueryServerInfoResponse;

public class QueryServerInfoRequest extends BaseRequest<QueryServerInfoResponse> {
    @Override
    public short getType()
    {
        return MessageType.QueryServerInfoRequest;
    }
}
