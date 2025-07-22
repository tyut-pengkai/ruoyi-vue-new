package com.ruoyi.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-06-05 11:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredential {

    private ELoginType loginType;

    private String username;

    private String password;

    private String phoneNumber;

    private String smsVerificationCode;

}
