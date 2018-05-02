/**
 * 实现功能：路由配置
 *
 * @author cs
 * @version 1.0.00
 */
EUI.ApplicationServiceView = EUI.extend(EUI.CustomUI, {
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
        this.gridCmp = EUI.getCmp("applicationServiceGrid");
        this.interfaceGridCmp = EUI.getCmp("interfaceGrid");
        this.addEvents();
    },
    initWestContainer: function () {
        var g = this;
        return {
            xtype: "GridPanel",
            region: "west",
            id: "applicationServiceGrid",
            width: "45%",
            title: this.lang.applicationServiceText,
            border: true,
            padding: 0,
            tbar: this.initTbar(),
            gridCfg: {
                url:_ctxPath + "/gateway_api_service/findAllByPage",
                loadonce: true,
                multiselect: true,
                colModel: [
                    {
                        label: g.lang.operateText,
                        name: "operate",
                        index: "operate",
                        width: 150,
                        align: "center",
                        formatter: function (cellvalue, options, rowObject) {
                            var html= "<i  class='ecmp-common-edit icon-space' title='" + g.lang.modifyText + "' targetId='"+rowObject.id+"'></i>" +
                                "<i class='ecmp-common-delete icon-space' title='" + g.lang.deleteText + "' targetId='"+rowObject.id+"'></i>";

                            if(rowObject.serviceAppEnabled==true||rowObject.serviceAppEnabled==="true"){
                                html+="<i class='ecmp-common-pause' style='display:inline-block' title='"+g.lang.startText+"' targetId='"+rowObject.id+"'></i>" +
                                    "<i class='ecmp-flow-start' style='display:none'  title='"+g.lang.endText+"' targetId='"+rowObject.id+"'></i>";
                            }else{
                                html+="<i class='ecmp-common-pause' style='display:none' title='"+g.lang.endText+"' targetId='"+rowObject.id+"'></i>" +
                                    "<i class='ecmp-flow-start' style='display:inline-block'  title='"+g.lang.startText+"' targetId='"+rowObject.id+"'></i>";
                            }
                            return html;
                        }
                    },
                    { name: 'id', hidden: true},
                    // codeText: 代码
                    {name: 'serviceAppId',hidden: true},
                    {name: 'applicationCode',hidden: true},
                    //serviceAppNameText: 名称
                    {name: 'serviceAppName', index: 'serviceAppName', sortable: true, width: 150, label: g.lang.serviceAppNameText},
                    //serviceAppRemarkText: 说明
                    {name: 'serviceAppRemark', index: 'serviceAppRemark', sortable: true, width: 150, label: g.lang.serviceAppRemarkText},
                    //serviceAppVersionText: 版本
                    {name: 'serviceAppVersion', index: 'serviceAppVersion', sortable: true, width: 100, label: g.lang.serviceAppVersionText}
                ],
                rowNum: 15,
                shrinkToFit: false,
                sortname: 'serviceAppName',
                onSelectRow: function () {
                    g.curApplication = g.gridCmp.getSelectRow();
                    if(!g.curApplication || g.curApplication.length == 0){
                        g.curApplication = null;
                    } else if(g.curApplication&&g.curApplication.length > 1){
                        g.message(g.lang.oneSelectText);
                        return false;
                    }else{
                        g.curApplication = g.curApplication[0];
                    }
                    g.refreshInterfaceGrid();
                },
                loadComplete: function (data) {
                    if(data&&data.rows && data.rows.length>0){
                        g.gridCmp.setSelectRowById(data.rows[0].id);
                    }
                }
            }

        }
    },
    initCenterContainer: function () {
        return {
            xtype: "GridPanel",
            region: "center",
            id: "interfaceGrid",
            title: this.lang.interfaceText,
            border: true,
            padding: 0,
            tbar: this.initCenterTbar(),
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
        },{
            xtype: "Button",
            title: g.lang.startText,
            iconCss: "ecmp-flow-start",
            selected: true,
            handler: function () {
                var rows = g.gridCmp.getSelectRow();
                if (!rows||rows.length == 0) {
                    g.message(g.lang.selectRowText);
                    return false;
                }
                var applicationServiceIds = new Array();
                for (var i=0,len=rows.length; i<len; i++) {
                    applicationServiceIds[i] = rows[i].id;
                }
                var infoBox = EUI.MessageBox({
                    //hintText: 提示
                    title: g.lang.hintText,
                    msg: g.lang.startHintMessageText,
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
                            g.doMultiStart(applicationServiceIds);
                        }
                    }]
                });
            }
        },{
            xtype: "Button",
            title: g.lang.endText,
            iconCss: "ecmp-flow-end",
            hidden: true,
            handler: function () {
                var rows = g.gridCmp.getSelectRow();
                if (!rows||rows.length == 0) {
                    g.message(g.lang.selectRowText);
                    return false;
                }
                var applicationServiceIds = new Array();
                for (var i=0,len=rows.length; i<len; i++) {
                    applicationServiceIds[i] = rows[i].id;
                }
                var infoBox = EUI.MessageBox({
                    //hintText: 提示
                    title: g.lang.hintText,
                    msg: g.lang.startHintMessageText,
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
                            g.doDistribute(applicationServiceIds);
                        }
                    }]
                });
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
    initCenterTbar: function(){
        var g = this;
        return [
            "->",
            {
                xtype: "SearchBox",
                id: "center_searchBox",
                //searchDisplayText: "请输入关键字查询",
                displayText: this.lang.searchDisplayText,
                onSearch: function (v) {

                }
            }
        ];
    },
    refreshInterfaceGrid: function(postData){
        //设置默认参数
        if(!postData){
            postData =  { applicationCode: this.curApplication && this.curApplication.applicationCode };
        }
        this.interfaceGridCmp.setGridParams({
            datatype: "json",
            url: _ctxPath + "/gateway_interface/find_gateway_interfaces",
            postData: postData
        },true);
    },

    doMultiStart:function (applicationServiceIds) {
        var g=this, myMask = EUI.LoadMask({
            msg: g.lang.doStartText
        });
        EUI.Store({
            url: _ctxPath + "/",
            params:{ids:applicationServiceIds},
            async: false,
            success: function (status) {
                myMask.remove();
                EUI.ProcessStatus({
                    success: true,
                    msg: status.message
                });
            },
            failure: function (status) {
                myMask.remove();
                g.message(status.message);
            }
        });
    },
    initGridCfg: function () {
        var g = this;
        return {
            loadonce: false,
            datatype: "local",
            colModel: [
                {
                    //operateText:操作
                    label: g.lang.operateText,
                    name: "operate",
                    index: "operate",
                    width: 80,
                    align: "center",
                    formatter: function (cellvalue, options, rowObject) {
                        return "<i  class='ecmp-common-configuration icon-space' title='" + g.lang.configText + "' targetId='"+rowObject.id+"'></i>";
                    }
                },
                {name: 'id', hidden: true},
                {name: 'applicationCode', hidden: true},
                {name: 'interfaceName', index: 'interfaceName', sortable: true, width: 200, label: g.lang.nameText},
                //interfaceURIText: "接口uri地址"
                {name: 'interfaceURI', index: 'interfaceURI', sortable: true, width: 300, label: g.lang.interfaceURIText, formatter: 'link',formatoptions:{target:"_blank"}},
            ],
            rowNum: 15,
            // shrinkToFit: false,
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
            var rowData = g.gridCmp.getRowData($(this).attr("targetId"));
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
                                g.gridCmp.setPostParams({},true);
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
        $(".ecmp-common-configuration").live("click", function () {
            var data = g.interfaceGridCmp.getRowData($(this).attr("targetId"));
            g.configForm();
            g.configFormCmp.loadData(data);
        });
        $(".ecmp-flow-start").live("click",function () {
            g.doStart($(this),1);
        });
        $(".ecmp-common-pause").live("click",function () {
            g.doStart($(this),0);
        });
    },
    configForm: function () {
        var g = this;
        g.editWin = EUI.Window({
            title: String.format(g.lang.configTitleText, g.lang.interfaceText),
            iconCss: "ecmp-common-configuration",
            height: 280,
            padding: 15,
            width: 430,
            items: [{
                xtype: "FormPanel",
                id: "configForm",
                padding: 0,
                defaultConfig: {
                    labelWidth: 110,
                    width: 315
                },
                items: [{
                    xtype: "TextField",
                    hidden: true,
                    name: "id"
                },{
                    xtype: "TextField",
                    title: g.lang.nameText,
                    name: "interfaceName",
                    readonly: true
                }, {
                    xtype: "TextArea",
                    title: g.lang.interfaceURIText,
                    name: "interfaceURI",
                    readonly: true
                }, {
                    xtype: "TextField",
                    title: g.lang.keyText,
                    name: "routeKey",
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
                    g.doSetting();
                }
            }]
        });
        g.configFormCmp = EUI.getCmp("configForm");
    },
    //执行启动或终止
    doStart: function(ele,starFlag){
        var g = this;
        var maskMsg="",winMsg="",title="",flag = false;
        var data = g.gridCmp.getRowData($(this).attr("targetId"));
        var param = {id:data[0].id};
        title = g.lang.hintText;
        var url = "";
        if(starFlag){
            maskMsg= g.lang.doStartText;
            winMsg= g.lang.startHintMessageText;
            flag = true;
            url = _ctxPath+"/gateway_api_service/router/startById";
        }else{
            maskMsg= g.lang.endMaskMessageText;
            winMsg= g.lang.endHintMessageText;
            flag = false;
            url = _ctxPath+"/gateway_api_service/router/stopById";
        }
        var mes = EUI.MessageBox({
            title: title,
            msg: winMsg,
            buttons: [{
                title: g.lang.okText,
                selected:true,
                handler: function () {
                    var myMask = EUI.LoadMask({msg: maskMsg});
                    EUI.Store({
                        url: url,
                        params: param,
                        success: function (status) {
                            myMask.hide();
                            mes.remove();
                            if(flag){
                                ele.css("display","none");
                                ele.prev().css("display","inline-block");
                            }else{
                                ele.css("display","none");
                                ele.next().css("display","inline-block");
                            }
                            g.gridCmp.setPostParams({},true);
                            EUI.ProcessStatus({
                                success: true,
                                msg: status.message
                            });
                        },
                        failure: function (status) {
                            myMask.hide();
                            mes.remove();
                            g.message(status.message);
                        }
                    });
                }
            },{
                title: g.lang.cancelText,
                handler: function () {
                    mes.remove();
                }
            }]
        });
    },
    doSetting: function () {
        var g = this;
        if (!g.configFormCmp.isValid()) {
            g.message(g.lang.unFilledText);
            return;
        }
        var data = g.configFormCmp.getFormValue();
        data.serviceId = data["id"];
        data.url = data["interfaceURI"];
        delete data["id"];
        delete data["interfaceURI"];
        var myMask = EUI.LoadMask({
            //saveMaskMessageText:"正在加载，请稍候..."
            msg: g.lang.saveMaskMessageText
        });
        EUI.Store({
            url: _ctxPath + "/gateway_api_service/router/setting",
            params: data,
            success: function (status) {
                myMask.hide();
                g.editWin.remove();
                g.message(status.message);
            },
            failure: function (status) {
                myMask.hide();
                g.message(status.message);
            }
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
                items: [{
                    xtype: "ComboGrid",
                    //  applicationServiceText: "应用服务",
                    title: this.lang.applicationServiceText,
                    allowBlank: false,
                    listWidth: 390,
                    showSearch: true,
                    canClear: false,
                    name: "serviceAppName",
                    field: ["serviceAppId","serviceAppRemark","serviceAppVersion"],
                    reader: {
                        name: "name",
                        field: ["id","remark","version"]
                    },
                    gridCfg: {
                        url:_ctxPath+"/gateway_api_service/findAllApiApp",
                        loadonce: false,
                        colModel: [
                            {name: 'id', hidden: true},
                            {name: 'appId', index: 'code', label: "应用ID", width: 100},
                            {name: 'name', index: 'name', label: "名称", width: 180},
                            {name: 'version', index: 'version', label: "版本", width: 100},
                            {name: 'host', index: 'host', label: "主机", width: 200},
                            {name: 'port', index: 'port', label: "端口", width: 100},
                            {name: 'remark', index: 'remark', label: "说明", width: 180}
                        ],
                        shrinkToFit: false,
                        sortname: 'name',
                        sortorder: "asc"
                    },
                    onSearch: function (v) {
                        this.grid.setPostParams({keywords: v},true);
                    }

                }, {
                    xtype: "ComboGrid",
                    //  applicationText: "应用",
                    title: this.lang.applicationText,
                    allowBlank: false,
                    listWidth: 390,
                    showSearch: true,
                    canClear: false,
                    name: "applicationName",
                    field: ["applicationCode"],
                    gridCfg: {
                        url:_ctxPath + "/gateway_application/find_gateway_applications",
                        loadonce: true,
                        colModel: [
                            {name: 'id', hidden: true},
                            // codeText: 代码
                            {name: 'applicationCode',hidden: true},
                            //nameText: 名称
                            {name: 'applicationName', index: 'applicationName', sortable: true, width: 180, label: g.lang.nameText},
                            //remarkText: 说明
                            {name: 'applicationRemark', index: 'applicationRemark', sortable: true, width: 100, label: g.lang.remarkText}
                        ],
                        shrinkToFit: false,
                        sortname: 'applicationName',
                        sortorder: "asc"
                    },
                    onSearch: function (v) {
                        this.grid.setPostParams({keywords: v},true);
                    }
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
        if(g.isEdit){
            saveUrl = _ctxPath + "/gateway_api_service/edit";
        }else{
            saveUrl = _ctxPath + "/gateway_api_service/save";
        }
        this.saveData(data,saveUrl);

    },
    saveData: function(data,url){
        var g = this;
        var myMask = EUI.LoadMask({msg: g.lang.saveMaskMessageText});
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