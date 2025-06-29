package com.ruoyi.framework.ocr.ali;

/**
 * @author liangyq
 * @date 2025-06-29
 */
@lombok.Data
public class IdCardOcrResponse {

    private Data data;

    @lombok.Data
    public static class Data {
        /**
         * 正面
         */
        private Face face;
        /**
         * 背面
         */
        private Back back;
    }

    @lombok.Data
    public static class Face {

        private Data data;

        @lombok.Data
        public static class Data {
            /**
             * 身份证号码
             */
            private String idNumber;
            /**
             * 姓名
             */
            private String name;
            /**
             * 性别
             */
            private String sex;
            /**
             * 民族
             */
            private String ethnicity;
            /**
             * 出生日期
             */
            private String birthDate;
            /**
             * 住址
             */
            private String address;
        }
    }

    @lombok.Data
    public static class Back {

        private Data data;

        @lombok.Data
        public static class Data {
            /**
             * 签发机关
             */
            private String issueAuthority;
            /**
             * 有效期限
             */
            private String validPeriod;
        }
    }
}
