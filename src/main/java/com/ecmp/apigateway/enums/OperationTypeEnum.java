package com.ecmp.apigateway.enums;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/26
 * @remark: 操作类型枚举:增，删，改
 */
public enum OperationTypeEnum {
    ADD, EDIT, DELETE;

    public static boolean checkOperationType(OperationTypeEnum operationTypeEnum, List list) {
        switch (operationTypeEnum) {
            case ADD:
                if (CollectionUtils.isNotEmpty(list)) return false;
            case EDIT:
                if (CollectionUtils.isNotEmpty(list) && list.size() > 1) return false;
                break;
        }
        return true;
    }
}
