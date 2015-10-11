package network.aic.xj.common.request;

import network.aic.xj.common.MessageType;
import network.aic.xj.common.response.QueryOrganizationResponse;

public class QueryOrganizationRequest extends BaseRequest<QueryOrganizationResponse> {
    @Override
    public short getType()
    {
        return MessageType.QueryOrganizationRequest;
    }
}
