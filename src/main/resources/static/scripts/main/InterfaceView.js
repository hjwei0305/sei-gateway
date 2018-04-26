/**
 * 实现功能：接口
 *
 * @author cs
 * @version 1.0.00
 */
EUI.InterfaceView = EUI.extend(EUI.CustomUI, {
    renderTo: "",
    isEdit: false,
    initComponent: function () {
        EUI.Container({
            renderTo: this.renderTo,
            layout: "border",
            border: false,
            itemspace: 4,
            padding: 0,
            items: [this.initWestContainer(), this.initCenterContainer()]
        });
        this.gridCmp = EUI.getCmp("interfaceGrid");
        this.addEvents();
    },
    initWestContainer: function () {
        var g = this;
        return {
            xtype: "GridPanel",
            region: "west",
            id: "applicationGrid",
            width: 380,
            title: this.lang.applicationText,
            border: true,
            padding: 0,
            gridCfg: {
                url:_ctxPath + "/gateway_application/find_gateway_applications",
                loadonce: false,
                colModel: [{ name: 'id', hidden: true },
                    // codeText: 代码
                    { name: 'applicationCode',hidden: true},
                    //nameText: 名称
                    { name: 'applicationName', index: 'applicationName', sortable: true, width: 125,  label: g.lang.nameText},
                    //remarkText: '说明',
                    { name: "applicationRemark",index: "applicationRemark", label: g.lang.remarkText,sortable: true, width: 200 }
                ],
                rowNum: 15,
                shrinkToFit: false,
                sortname: 'applicationName',
                onSelectRow: function () {
                    g.curApplication = EUI.getCmp("applicationGrid").getSelectRow();
                    g.refreshInterfaceGrid();
                },
                loadComplete: function (data) {
                    if(data&&data.rows && data.rows.length>0){
                        EUI.getCmp("applicationGrid").setSelectRowById(data.rows[0].id);
                        g.refreshInterfaceGrid({
                            applicationCode: data.rows[0].applicationCode
                        });
                    }
                }
            }

        }
    },
    refreshInterfaceGrid: function(postData){
        if(!postData){
            postData =  { applicationCode: this.curApplication && this.curApplication.applicationCode };
        }
        this.gridCmp.setGridParams({
            datatype: "json",
            url: _ctxPath + "/gateway_interface/find_gateway_interfaces",
            postData: postData
        },true);
    },
    initCenterContainer: function () {
        return {
            xtype: "GridPanel",
            region: "center",
            id: "interfaceGrid",
            title: this.lang.interfaceText,
            border: true,
            padding: 0,
            tbar: this.initTbar(),
            gridCfg: this.initGridCfg()
        }
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
            //searchDisplayText: "请输入关键字查询",
            displayText: g.lang.searchDisplayText,
            onSearch: function (v) {
                g.gridCmp.localSearch(v);
            }
        }];
    },
    initGridCfg: function () {
        var g = this;
        return {
            loadonce: false,
            datatype: "local",
            colModel: [{
                //operateText:操作
                    label: g.lang.operateText,
                    name: "operate",
                    index: "operate",
                    width: 80,
                    align: "center",
                    formatter: function (cellvalue, options, rowObject) {
                        return "<i  class='ecmp-common-edit icon-space' title='" + g.lang.modifyText + "' targetId='"+rowObject.id+"'></i>" +
                            "<i class='ecmp-common-delete' title='" + g.lang.deleteText + "' targetId='"+rowObject.id+"'></i>";
                    }
                },
                {name: 'id', hidden: true},
                {name: 'applicationCode', hidden: true},
                {name: 'interfaceName', index: 'interfaceName', sortable: true, width: 200, label: g.lang.nameText},
                //interfaceProtocolText: "接口协议"
                {name: 'interfaceProtocol', index: 'interfaceProtocol', sortable: true, width: 100,label: g.lang.interfaceProtocolText },
                //interfaceURIText: "接口uri地址"
                {name: 'interfaceURI', index: 'interfaceURI', sortable: true, width: 300, label: g.lang.interfaceURIText, formatter: 'link',formatoptions:{target:"_blank"}},
                //interfaceRemarkText: "接口说明"
                {name: 'interfaceRemark', index: 'interfaceRemark', sortable: true, width: 300, label: g.lang.interfaceRemarkText}
            ],
            rowNum: 15,
            sortname: 'interfaceName'
        };
    },
    addEvents: function () {
        var g = this;
        $(".ecmp-common-edit").live("click", function () {
            var data = g.gridCmp.getRowData($(this).attr("targetId"));
            g.isEdit = true;
            g.addAndEdit();
            g.formCmp.loadData(data);
        });
        $(".ecmp-common-delete").live("click", function () {
            var rowId = $(this).attr("targetId");
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
                            url: _ctxPath + "/gateway_interface/remove_gateway_interface",
                            params: {
                                id: rowId
                            },
                            success: function (status) {
                                myMask.hide();
                                g.refreshInterfaceGrid();
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
            title: g.isEdit ? String.format(g.lang.modifyWindowText, g.lang.interfaceText, g.curApplication.applicationName) : String.format(g.lang.addWindowText, g.lang.interfaceText,g.curApplication.applicationName),
            iconCss: g.isEdit ? "ecmp-eui-edit" : "ecmp-eui-add",
            height: 280,
            padding: 15,
            width: 450,
            items: [{
                xtype: "FormPanel",
                id: "editForm",
                padding: 0,
                defaultConfig: {
                    labelWidth: 110,
                    allowBlank: false,
                    width: 330
                },
                items: [{
                    xtype: "TextField",
                    hidden: true,
                    name: "id"
                },{
                    xtype: "TextField",
                    title: g.lang.nameText,
                    name: "interfaceName",
                    maxlength: 100
                },{
                    xtype: "RadioBoxGroup",
                    title: this.lang.interfaceProtocolText,
                    name: "interfaceProtocol",
                    itemspace: 5,
                    items: [{
                        title: "HTTP协议",
                        name: "HTTP",
                        checked: true
                    }, {
                        title: "RPC协议",
                        name: "RPC"
                    }, {
                        labelWidth: 103,
                        title: "webservice协议",
                        name: "WEBSERVICE"
                    }]
                }, {
                    xtype: "TextArea",
                    title: g.lang.interfaceURIText,
                    name: "interfaceURI",
                    maxlength: 800
                }, {
                    xtype: "TextArea",
                    title: g.lang.remarkText,
                    name: "interfaceRemark",
                    maxlength: 100,
                    allowBlank: true
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
        var g = this,saveUrl='';
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
        data.applicationCode = g.curApplication.applicationCode;
        if(g.isEdit){
            saveUrl = _ctxPath + "/gateway_interface/modify_gateway_interface";
        }else{
            saveUrl = _ctxPath + "/gateway_interface/add_gateway_interface";
        }
        var myMask = EUI.LoadMask({
            //saveMaskMessageText:"正在加载，请稍候..."
            msg: g.lang.saveMaskMessageText
        });
        EUI.Store({
            url: saveUrl,
            params: data,
            success: function (status) {
                myMask.hide();
                g.editWin.remove();
                g.refreshInterfaceGrid();
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