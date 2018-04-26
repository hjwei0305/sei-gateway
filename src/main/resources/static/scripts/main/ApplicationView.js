/**
 * 实现功能：应用
 *
 * @author cs
 * @version 1.0.00
 */
EUI.ApplicationView = EUI.extend(EUI.CustomUI, {
    renderTo: "",
    isEdit: false,
    initComponent: function () {
        this.gridCmp = EUI.GridPanel({
            renderTo: this.renderTo,
            title: this.lang.applicationText,
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
                g.gridCmp.localSearch(v);
            }
        }];
    },
    initGridCfg: function () {
        var g = this;
        return {
            loadonce: true,
            // datatype: "local",
            url:_ctxPath + "/gateway_application/find_gateway_applications",
           /* postData: {
                //weatherPage: false
            },*/
            colModel: [
                {
                    label: g.lang.operateText,
                    name: "operate",
                    index: "operate",
                    width: 80,
                    align: "center",
                    formatter: function (cellvalue, options, rowObject) {
                        return "<i  class='ecmp-common-edit icon-space' title='" + g.lang.modifyText + "'></i><i class='ecmp-common-delete' title='" + g.lang.deleteText + "'></i>";
                    }
                },
                { name: 'id', hidden: true},
                // codeText: 代码
                {name: 'applicationCode',hidden: true},
                //nameText: 名称
                {name: 'applicationName', index: 'applicationName', sortable: true, width: 300, label: g.lang.nameText},
                //remarkText: 说明
                {name: 'applicationRemark', index: 'applicationRemark', sortable: true, width: 300, label: g.lang.remarkText}
            ],
            rowNum: 15,
            shrinkToFit: false,
            sortname: 'applicationCode'
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
                            url: _ctxPath + "/gateway_application/remove_gateway_application",
                            params: {
                                id: rowData.id
                            },
                            success: function (status) {
                                myMask.hide();
                               // g.gridCmp.refreshGrid();
                                g.gridCmp.setPostParams({},true);
                                EUI.ProcessStatus({
                                    success: true,
                                    msg: status.message
                                });
                            },
                            failure: function (status) {
                                myMask.hide();
                                EUI.ProcessStatus({
                                    success: false,
                                    msg: status.message
                                });
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
            title: g.isEdit ? String.format(g.lang.modifyWinText, g.lang.applicationText) : String.format(g.lang.addWinText, g.lang.applicationText),
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
                items: [{
                    xtype: "TextField",
                    hidden: true,
                    name: "id"
                }, {
                    xtype: "TextField",
                    //codeText:"代码"
                    title: g.lang.codeText,
                    name: "applicationCode",
                    hidden: true
                }, {
                    xtype: "TextField",
                    //nameText:"名称"
                    title: g.lang.nameText,
                    name: "applicationName",
                    maxlength: 50,
                    allowBlank: false
                }, {
                    xtype: "TextArea",
                    //noteText:"说明"
                    title: g.lang.remarkText,
                    name: "applicationRemark",
                    maxlength: 200,
                    height: 100,
                    allowBlank: false
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
        var g = this, url = "";
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
        if(g.isEdit){
            url = _ctxPath + "/gateway_application/modify_gateway_application";
        }else{
            url = _ctxPath + "/gateway_application/add_gateway_application";
        }
        this.checkAndSave(data,url);

    },
    checkAndSave: function(data,url){
        var g = this;
        var myMask = EUI.LoadMask({
            //saveMaskMessageText:"正在加载，请稍候..."
            msg: g.lang.saveMaskMessageText
        });
        EUI.Store({
            async: true,
            url: _ctxPath + "/gateway_application/check_application_name",
            params: {applicationName: data.applicationName},
            success: function (result) {
                if(result.data){
                    myMask.hide();
                    EUI.ProcessStatus({
                        success: false,
                        msg: '名称重复，请修改'
                    });
                }else{
                    g.saveData(data,url,myMask);
                }
            },
            failure: function (re) {
                myMask.hide();
                EUI.ProcessStatus({
                    success: false,
                    msg: '名称校验请求失败'
                });
            }
        });
    },
    saveData: function(data,url,myMask){
        var g = this;
        EUI.Store({
            url: url,
            params: data,
            success: function (status) {
                myMask.hide();
                g.editWin.remove();
              //  g.gridCmp.refreshGrid();
                g.gridCmp.setPostParams({},true);
                EUI.ProcessStatus({
                    success: true,
                    msg: status.message
                });
            },
            failure: function (status) {
                myMask.hide();
                EUI.ProcessStatus({
                    success: false,
                    msg: status.message
                });
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