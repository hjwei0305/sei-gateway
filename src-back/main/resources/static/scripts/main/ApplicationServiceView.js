/**
 * 实现功能：应用服务
 *
 * @author cs
 * @version 1.0.00
 */
EUI.ApplicationServiceView = EUI.extend(EUI.CustomUI, {
    renderTo: "",
    isEdit: false,
    initComponent: function () {
        this.gridCmp = EUI.GridPanel({
            renderTo: this.renderTo,
            title: this.lang.applicationServiceText,
            border: true,
            padding: 0,
            tbar: this.initTbar(),
            gridCfg: this.initGridCfg()
        });
        this.addEvents();
    },
    initTbar: function () {
        var g = this;
        return [{
            xtype: "Button",
            //addText:新增
            title: g.lang.addText,
            iconCss: "ecmp-common-add",
            selected: true,
            handler: function () {
                g.isEdit = false;
                g.addAndEdit();
            }
        }, "->", {
            xtype: "SearchBox",
            id: "searchBox_gridPanel",
            displayText: g.lang.searchDisplayText,
            onSearch: function (v) {
                g.gridCmp.setPostParams({keywords: v}, true);
            }
        }];
    },
    getPostData: function () {
        var searchVal = EUI.getCmp("searchBox_gridPanel") ? EUI.getCmp("searchBox_gridPanel").getValue() : "";
        var postData = {keywords: searchVal};
        return postData;
    },
    initGridCfg: function () {
        var g = this;
        return {
            loadonce: false,
            url: _ctxPath + "/gateway_api_service/findAllByPage",
            postData: this.getPostData(),
            colModel: [
                {
                    label: g.lang.operateText,
                    name: "operate",
                    index: "operate",
                    width: 80,
                    align: "center",
                    formatter: function (cellvalue, options, rowObject) {
                        return "<i  class='ecmp-common-edit icon-space' title='" + g.lang.modifyText + "'></i>" +
                            "<i class='ecmp-common-delete' title='" + g.lang.deleteText + "'></i>";
                    }
                },
                {name: 'id', hidden: true},
                // codeText: 代码
                {name: 'serviceAppId', hidden: true},
                {name: 'applicationCode', hidden: true},
                //serviceAppNameText: 名称
                {
                    name: 'serviceAppName',
                    index: 'serviceAppName',
                    sortable: true,
                    width: 300,
                    label: g.lang.serviceAppNameText
                },
                //serviceAppRemarkText: 说明
                {
                    name: 'serviceAppRemark',
                    index: 'serviceAppRemark',
                    sortable: true,
                    width: 300,
                    label: g.lang.serviceAppRemarkText
                },
                //serviceAppVersionText: 版本
                {
                    name: 'serviceAppVersion',
                    index: 'serviceAppVersion',
                    sortable: true,
                    width: 300,
                    label: g.lang.serviceAppVersionText
                }
            ],
            rowNum: 15,
            shrinkToFit: false,
            sortname: 'serviceAppName'
        };
    },
    addEvents: function () {
        var g = this;
        $(".ecmp-common-edit").live("click", function () {
            var data = g.gridCmp.getSelectRow();
            g.isEdit = true;
            g.addAndEdit();
            g.formCmp.loadData(data);
        });
        $(".ecmp-common-delete").live("click", function () {
            var rowData = g.gridCmp.getSelectRow();
            var infoBox = EUI.MessageBox({
                //hintText: 提示
                title: g.lang.hintText,
                //deleteHintMessageText:您确定要删除吗？
                msg: g.lang.deleteHintMessageText,
                buttons: [{
                    //cancelText:取消
                    title: g.lang.cancelText,
                    handler: function () {
                        infoBox.remove();
                    }
                }, {
                    //okText: 确定
                    title: g.lang.okText,
                    selected: true,
                    handler: function () {
                        infoBox.remove();
                        var myMask = EUI.LoadMask({
                            //deleteMaskMessageText: 正在删除，请稍候...
                            msg: g.lang.deleteMaskMessageText
                        });
                        EUI.Store({
                            url: _ctxPath + "/gateway_api_service/removeById",
                            params: {
                                id: rowData.id
                            },
                            success: function (status) {
                                myMask.hide();
                                // g.gridCmp.refreshGrid();
                                g.gridCmp.setPostParams({}, true);
                                EUI.ProcessStatus({
                                    success: true,
                                    msg: status.message
                                });
                            },
                            failure: function (status) {
                                myMask.hide();
                                g.message(status.message);
                            }
                        });
                    }
                }]
            });
        });
    },
    addAndEdit: function () {
        var g = this;
        g.editWin = EUI.Window({
            //  applicationModuleText: "应用模块",
            title: g.isEdit ? String.format(g.lang.modifyWinText, g.lang.applicationServiceText) : String.format(g.lang.addWinText, g.lang.applicationServiceText),
            iconCss: g.isEdit ? "ecmp-eui-edit" : "ecmp-eui-add",
            height: 220,
            padding: 15,
            width: 380,
            items: [{
                xtype: "FormPanel",
                id: "editForm",
                padding: 0,
                defaultConfig: {
                    labelWidth: 90,
                    width: 270
                },
                items: [/*{
                    xtype: "ComboGrid",
                    //  applicationServiceText: "应用服务",
                    title: this.lang.applicationServiceText,
                    allowBlank: false,
                    listWidth: 390,
                    name: "serviceAppName",
                    field: ["serviceAppId,serviceAppRemark,serviceAppVersion"],
                    showSearch: true,
                    canClear: false,
                    gridCfg: {
                        url: "",
                        loadonce: false,
                        colModel: [
                            {name: 'id', hidden: true},
                            {name: 'appId', index: 'code', label: "代码", width: 135},
                            {name: 'name', index: 'name', label: "名称", width: 180},
                            {name: 'remark', index: 'name', label: "名称", width: 180}
                        ],
                        shrinkToFit: false,
                        sortname: 'code',
                        sortorder: "asc"
                    },
                    onSearch: function (v) {
                        this.grid.localSearch(v);
                    },
                    reader: {
                        name: "name",
                        field: ["id"]
                    }
                },*/ {
                    xtype: "ComboGrid",
                    //  applicationText: "应用",
                    title: this.lang.applicationText,
                    allowBlank: false,
                    listWidth: 390,
                    name: "applicationName",
                    field: ["applicationCode"],
                    showSearch: true,
                    canClear: false,
                    //  searchConfig: {searchCols: ["code", "name"]},
                    gridCfg: {
                        url: _ctxPath + "/gateway_application/find_gateway_applications",
                        loadonce: true,
                        colModel: [
                            {name: 'id', hidden: true},
                            // codeText: 代码
                            {name: 'applicationCode', hidden: true},
                            //nameText: 名称
                            {
                                name: 'applicationName',
                                index: 'applicationName',
                                sortable: true,
                                width: 180,
                                label: g.lang.nameText
                            },
                            //remarkText: 说明
                            {
                                name: 'applicationRemark',
                                index: 'applicationRemark',
                                sortable: true,
                                width: 100,
                                label: g.lang.remarkText
                            }
                        ],
                        shrinkToFit: false,
                        sortname: 'applicationName',
                        sortorder: "asc"
                    },
                    onSearch: function (v) {
                        this.grid.setPostParams({keywords: v}, true);
                    }
                    /* reader: {
                         name: "applicationName",
                         field: ["applicationCode"]
                     }*/
                }]
            }],
            buttons: [{
                //cancelText:取消
                title: g.lang.cancelText,
                handler: function () {
                    g.editWin.remove();
                }
            }, {
                // saveText:保存
                title: g.lang.saveText,
                selected: true,
                handler: function () {
                    g.save();
                }
            }]
        });
        g.formCmp = EUI.getCmp("editForm");
    },
    save: function () {
        var g = this, saveUrl = "";
        if (!g.formCmp.isValid()) {
            g.message(g.lang.unFilledText);
            return;
        }
        var data = g.formCmp.getFormValue();
        if (g.isEdit && data && data.id == "") {
            // operateHintMessage:"请选择一条要操作的行项目!",
            g.message(g.lang.operateHintMessage);
            return false;
        }
        if (!g.isEdit) {
            delete data.id;
        }
        if (g.isEdit) {
            saveUrl = _ctxPath + "/gateway_api_service/edit";
        } else {
            saveUrl = _ctxPath + "/gateway_api_service/save";
        }
        this.saveData(data, saveUrl);

    },
    saveData: function (data, url) {
        var g = this;
        var myMask = EUI.LoadMask({msg: g.lang.saveMaskMessageText});
        EUI.Store({
            url: url,
            params: data,
            success: function (status) {
                myMask.hide();
                g.editWin.remove();
                //  g.gridCmp.refreshGrid();
                g.gridCmp.setPostParams({}, true);
                EUI.ProcessStatus({
                    success: true,
                    msg: status.message
                });
            },
            failure: function (status) {
                myMask.hide();
                g.message(status.message);
            }
        });
    },
    message: function (msg) {
        var g = this;
        var message = EUI.ProcessStatus({
            sucess: false,
            //hintText: "温馨提示",
            title: g.lang.hintText,
            msg: msg
        });
    }
});