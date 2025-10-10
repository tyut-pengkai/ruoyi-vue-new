package com.common.domain;

import com.trs.hybase.client.TRSConnection;
import com.trs.hybase.client.params.ConnectParams;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HybaseConnectParams {
    private String url;
    private String table;
    private String userId;
    private String pwd;
    private long timeout;

    public TRSConnection createConnection() {
        ConnectParams connectParams = new ConnectParams();
        connectParams.setTimeout(this.timeout);
        return new TRSConnection(this.url, this.userId, this.pwd, connectParams);
    }
}
