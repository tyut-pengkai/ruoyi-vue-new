package com.ruoyi.api.v1.domain;

import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Api {

    private String api;
    private String name;
    private boolean checkToken;
    private String[] tags;
    private String description;
    private AuthType[] authTypes;
    private BillType[] billTypes;
    private List<Param> params;
    private Resp resp;

//    public Api(String api, String name, boolean checkToken, String description) {
//        this(api, name, checkToken, Constants.API_TAG_ALL, description, Constants.AUTH_TYPE_ALL,
//                Constants.BILL_TYPE_ALL);
//    }

//    public Api(String api, String name, boolean checkToken, String tag, String description) {
//        this(api, name, checkToken, new String[]{tag}, description, Constants.AUTH_TYPE_ALL,
//                Constants.BILL_TYPE_ALL);
//    }

//    public Api(String api, String name, boolean checkToken, String[] tags, String description) {
//        this(api, name, checkToken, tags, description, Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL);
//    }

//    public Api(String api, String name, boolean checkToken, String tag, String description, Param[] params) {
//        this(api, name, checkToken, new String[]{tag}, description, Constants.AUTH_TYPE_ALL,
//                Constants.BILL_TYPE_ALL, params);
//    }

//    public Api(String api, String name, boolean checkToken, String[] tags, String description, Param[] params) {
//        this(api, name, checkToken, tags, description, Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, params);
//    }

    public Api(String api, String name, boolean checkToken, String tag, String description, AuthType loginTypes,
               BillType chargeTypes) {
        this(api, name, checkToken, new String[]{tag}, description, loginTypes, chargeTypes, null, null);
    }

    public Api(String api, String name, boolean checkToken, String[] tags, String description, AuthType loginTypes,
               BillType chargeTypes) {
        this(api, name, checkToken, tags, description, loginTypes, chargeTypes, null, null);
    }

    public Api(String api, String name, boolean checkToken, String tag, String description, AuthType[] loginTypes,
               BillType[] chargeTypes, Param[] params) {
        this(api, name, checkToken, new String[]{tag}, description, loginTypes, chargeTypes, params, null);
    }

    public Api(String api, String name, boolean checkToken, String tag, String description, AuthType loginTypes,
               BillType chargeTypes, Param[] params) {
        this(api, name, checkToken, new String[]{tag}, description, new AuthType[]{loginTypes},
                new BillType[]{chargeTypes}, params, null);
    }

    public Api(String api, String name, boolean checkToken, String[] tags, String description, AuthType loginTypes,
               BillType chargeTypes, Param[] params) {
        this(api, name, checkToken, tags, description, new AuthType[]{loginTypes}, new BillType[]{chargeTypes},
                params, null);
    }

    public Api(String api, String name, boolean checkToken, String tag, String description, AuthType[] loginTypes,
               BillType[] chargeTypes) {
        this(api, name, checkToken, new String[]{tag}, description, loginTypes, chargeTypes, null, null);
    }

    public Api(String api, String name, boolean checkToken, String[] tags, String description, AuthType[] loginTypes,
               BillType[] chargeTypes) {
        this(api, name, checkToken, tags, description, loginTypes, chargeTypes, null, null);
    }

    public Api(String api, String name, boolean checkToken, String tag, String description, AuthType[] loginTypes,
               BillType[] chargeTypes, Param[] params, Resp resp) {
        this(api, name, checkToken, new String[]{tag}, description, loginTypes, chargeTypes, params, resp);
    }

    public Api(String api, String name, boolean checkToken, String tag, String description, AuthType loginTypes,
               BillType chargeTypes, Param[] params, Resp resp) {
        this(api, name, checkToken, new String[]{tag}, description, new AuthType[]{loginTypes},
                new BillType[]{chargeTypes}, params, resp);
    }

    public Api(String api, String name, boolean checkToken, String[] tags, String description, AuthType loginTypes,
               BillType chargeTypes, Param[] params, Resp resp) {
        this(api, name, checkToken, tags, description, new AuthType[]{loginTypes}, new BillType[]{chargeTypes},
                params, resp);
    }

    public Api(String api, String name, boolean checkToken, String[] tags, String description, AuthType[] loginTypes,
               BillType[] chargeTypes, Param[] params) {
        this(api, name, checkToken, tags, description, loginTypes, chargeTypes, params, null);
    }

    public Api(String api, String name, boolean checkToken, String[] tags, String description, AuthType[] loginTypes,
               BillType[] chargeTypes, Param[] params, Resp resp) {
        this.api = api;
        this.name = name;
        this.checkToken = checkToken;
        this.tags = tags;
        this.description = description;
        this.authTypes = loginTypes;
        this.billTypes = chargeTypes;
        this.params = params != null ? new ArrayList<>(Arrays.asList(params)) : new ArrayList<>();
        this.resp = resp;
    }

}
