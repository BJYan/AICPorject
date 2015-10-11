package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.response.QueryPlanResultResponse;

public class QueryPlanResultRequest extends BaseRequest<QueryPlanResultResponse> {
    @Override
    public short getType()
    {
        return MessageType.QueryPlanResultRequest;
    }
}
