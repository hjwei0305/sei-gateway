var defaultLangs = {
    addText: "新增",
    modifyText: "修改",
    deleteText: "删除",
    operateText: "操作",
    refreshText:"刷新",
    operateHintMessage:"请选择一条要操作的行项目!",
    saveMaskMessageText: "正在保存，请稍候...",
    hintText: "温馨提示",
    okText: "确定",
    cancelText: "取消",
    saveText: "保存",
    deleteHintMessageText: "您确定要删除吗？",
    deleteMaskMessageText: "正在删除，请稍候...",
    searchDisplayText: "请输入关键字查询!",
    queryMaskMessageText: "正在努力获取数据，请稍候...",
    unFilledText:"有必填项未输入，请确认！",
    rankText: "排序",
    addWinText: "新增{0}",
    modifyWinText: "编辑{0}",
    codeText: "代码",
    nameText: "名称",
    isValid: "是否可用",
    validateToken: "安全验证",
    remarkText: "说明",
};
if (EUI.InterfaceView) {
    EUI.InterfaceView.prototype.lang = EUI.applyIf({
        applicationText: "应用",
        interfaceText:"接口",
        getInterfaceText: "正在获取接口...",
        getInterfaceFailedText: "获取接口失败",
        interfaceRemarkText: "接口说明",
        interfaceProtocolText: "接口协议",
        interfaceURIText: "接口uri地址",
        modifyWindowText:"编辑{0}【{1}】",
        addWindowText: "新增{0}【{1}】"
    }, defaultLangs);
}
if (EUI.ApplicationView) {
    EUI.ApplicationView.prototype.lang = EUI.applyIf({
        applicationText: "应用",
        getApplicationText: "正在获取应用，请稍候..."
    }, defaultLangs);
}
if (EUI.ApplicationServiceView) {
    EUI.ApplicationServiceView.prototype.lang = EUI.applyIf({
        applicationText: "应用",
        applicationServiceText: "应用服务",
        serviceAppIdText: "应用服务ID",
        serviceAppCodeText: "应用服务代码",
        serviceAppNameText: "应用服务名称",
        servicePathText: "路由规则",
        retryAbleText: "是否重试",
        stripPrefixText: "剥离路由前缀",
        serviceAppRemarkText: "应用服务说明",
        serviceAppVersionText: "应用服务版本",
        serviceAppUrlText: "应用服务url",
        interfaceText:"接口",
        interfaceProtocolText: "接口协议",
        interfaceURIText: "接口uri地址",
        getApplicationServiceText: "正在获取应用服务，请稍候...",
        startText: "启动路由",
        endText: "停止路由",
        startHintMessageText: "确定要启动路由吗？",
        endHintMessageText: "确定要停止路由吗？",
        selectRowText: "请选择应用服务",
        selectedStartedText: "所选应用的路由已启动！",
        selectedStopedText: "所选应用的路由已停止！",
        endMaskMessageText: "正在停止路由,请稍侯...",
        doStartText: "正在启动路由,请稍侯...",
        configText: "设置",
        configTitleText: "设置{0}",
        oneSelectText: "只能选择一条应用服务",
        keyText: "路由key"
    }, defaultLangs);
}

