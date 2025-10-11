package com.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class BidKnowledgeBean extends HybaseBean implements Serializable {

    private String uuid;

    private String id;

    private String title;

    private String content;

    private String loadTime;
}
