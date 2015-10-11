package network.aic.xj.common;

public class MessageType {
    public static final short QueryOrganizationRequest = 2001;
    public static final short QueryPlanResultRequest = 2002;
    public static final short QueryServerInfoRequest = 2003;
    public static final short QueryServerCommandRequest = 2004;
    public static final short AckServerCommandRequest = 2005;
    public static final short UploadFirstOpenTempPlanRequest = 3001;
    public static final short UploadMobilePhoneInfoRequest = 3002;
    public static final short UploadNormalPlanResultRequest = 3003;
    public static final short UploadRFIDRequest = 3004;
    public static final short UploadTempPlanResultRequest = 3005;
    public static final short UploadWaveDataRequest = 3006;
    
    public static final short QueryOrganizationResponse = -2001;
    public static final short QueryPlanResultResponse = -2002;
    public static final short QueryServerInfoResponse = -2003;
    public static final short QueryServerCommandResponse = -2004;
    public static final short AckServerCommandResponse = -2005;
    public static final short UploadFirstOpenTempPlanResponse = -3001;
    public static final short UploadMobilePhoneInfoResponse = -3002;
    public static final short UploadNormalPlanResultResponse = -3003;
    public static final short UploadRFIDResponse = -3004;
    public static final short UploadTempPlanResultResponse = -3005;
    public static final short UploadWaveDataResponse = -3006;
}
