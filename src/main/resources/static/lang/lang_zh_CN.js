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
    modifyWinText: "编辑{0}"
};
if (EUI.InterfaceView) {
    EUI.InterfaceView.prototype.lang = EUI.applyIf({
        interfaceText:"接口",
        codeText: "代码",
        nameText: "名称"
    }, defaultLangs);
}
if (EUI.ApplicationView) {
    EUI.ApplicationView.prototype.lang = EUI.applyIf({
        applicationText: "应用",
        getApplicationText: "正在获取应用模块",
        codeText: "代码",
        nameText: "名称",
        remarkText: "说明"
    }, defaultLangs);
}


