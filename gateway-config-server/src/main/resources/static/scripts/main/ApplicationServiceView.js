/**
 * 实现功能：路由配置
 *
 * @author cs
 * @version 1.0.00
 */
EUI.ApplicationServiceView = EUI.extend(EUI.CustomUI, {
    renderTo: "",
    isEdit: false,
    checkRowIds: [],
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
            width: "50%",
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
                        width: 100,
                        align: "center",
                        formatter: function (cellvalue, options, rowObject) {
                            var html= "<i  class='ecmp-common-edit icon-space' title='" + g.lang.modifyText + "' targetId='"+rowObject.id+"'></i>" +
                                "<i class='ecmp-common-delete icon-space' title='" + g.lang.deleteText + "' targetId='"+rowObject.id+"'></i>";

                            if(rowObject.serviceAppEnabled==true||rowObject.serviceAppEnabled==="true"){
                                html+="<i class='item ecmp-flow-end' style='display:inline-block' title='"+g.lang.endText+"' targetId='"+rowObject.id+"'></i>" +
                                    "<i class='item ecmp-flow-start' style='display:none'  title='"+g.lang.startText+"' targetId='"+rowObject.id+"'></i>";
                            }else{
                                html+="<i class='item ecmp-flow-end' style='display:none' title='"+g.lang.endText+"' targetId='"+rowObject.id+"'></i>" +
                                    "<i class='item ecmp-flow-start' style='display:inline-block'  title='"+g.lang.startText+"' targetId='"+rowObject.id+"'></i>";
                            }
                            return html;
                        }
                    },
                    { name: 'id', hidden: true},
                    { name: 'serviceAppEnabled', hidden: true},
                    // codeText: 代码
                    {name: 'serviceAppId',hidden: true},
                    {name: 'serviceAppCode',hidden: true},
                    {name: 'applicationCode',hidden: true},
                    {name: 'serviceAppUrl',hidden: true},
                    //serviceAppNameText: 名称
                    {name: 'serviceAppName', index: 'serviceAppName', sortable: true, width: 150, label: g.lang.serviceAppNameText},
                    //serviceAppRemarkText: 说明
                    {name: 'serviceAppRemark', index: 'serviceAppRemark', sortable: true, width: 150, label: g.lang.serviceAppRemarkText},
                    //serviceAppVersionText: 版本
                    {name: 'serviceAppVersion', index: 'serviceAppVersion', sortable: true, width: 90, label: g.lang.serviceAppVersionText},
                ],
                rowNum: 15,
                shrinkToFit: false,
                sortname: 'serviceAppName',
                onSelectRow: function (rowid,status,e) {
                    //还原每行复选框状态
                    var tempi = g.checkRowIds.indexOf(rowid);
                    if(tempi==-1){
                        $("#jqg_g_applicationServiceGrid_"+rowid).attr("checked",false);
                    }
                    //还原总复选框的选中状态
                    if(g.checkRowIds.length>0&&g.totalRecords==g.checkRowIds.length){
                        $("#cb_g_applicationServiceGrid").attr("checked",true);
                    }
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
                        g.totalRecords = data.rows.length;
                        g.gridCmp.setSelectRowById(data.rows[0].id);
                        g.lastSelectRowId = data.rows[0].id;
                    }
                },
                beforeSelectRow: function (rowid, e) {
                       var attrId = $(e.target).closest('td').attr('aria-describedby');
                        if(attrId == 'g_applicationServiceGrid_cb' || attrId =='g_applicationServiceGrid_operate'){
                            if(attrId == 'g_applicationServiceGrid_cb'){
                                if($("#jqg_g_applicationServiceGrid_"+rowid).attr("checked")){
                                    var tempi = g.checkRowIds.indexOf(rowid);
                                    if(tempi==-1){
                                        g.checkRowIds.push(rowid);
                                    }
                                }else{
                                    var i = g.checkRowIds.indexOf(rowid);
                                    g.checkRowIds.splice(i,1);
                                }
                                //还原总复选框的选中状态
                                if(g.checkRowIds.length>0&&g.totalRecords==g.checkRowIds.length){
                                    $("#cb_g_applicationServiceGrid").attr("checked",true);
                                }else{
                                    $("#cb_g_applicationServiceGrid").attr("checked",false);
                                }
                            }
                        }else{
                            //控制只能单选
                            if(rowid && rowid!==g.lastSelectRowId){
                                var checkrowflag = false,$lastRow = $("#jqg_g_applicationServiceGrid_"+g.lastSelectRowId);
                                if($lastRow.attr("checked")){
                                   checkrowflag = true;
                                }
                                //还原选中的行
                                $(this).jqGrid('setSelection',g.lastSelectRowId,false);
                                //还原选中行的复选框状态
                                checkrowflag && $lastRow.attr("checked",true);
                                g.lastSelectRowId = rowid;
                                return true;
                            }

                        }
                    return false;
                },
                onSelectAll: function(aRowids, status){
                    if(status){
                        g.checkRowIds = [];
                        for(var ri = 0;ri<aRowids.length;ri++){
                            var temp = aRowids[ri];
                            g.checkRowIds.push(temp);
                            if(temp!=g.lastSelectRowId){
                                $(this).jqGrid('setSelection',temp,false);
                                $("#jqg_g_applicationServiceGrid_"+temp).attr("checked",true);
                            }
                        }
                        $("#cb_g_applicationServiceGrid").attr("checked",true);
                    }else{
                        g.checkRowIds = [];
                        $(this).jqGrid('setSelection',g.lastSelectRowId,false);
                        $("#jqg_g_applicationServiceGrid_"+g.lastSelectRowId).attr("checked",false);
                    }
                    return true;
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
               //  var rows = g.gridCmp.getSelectRow();
                var rows = [];
                for (var i = 0; i < g.checkRowIds.length; i++) {
                    rows.push(g.gridCmp.getRowData(g.checkRowIds[i]));
                }
                if (!rows||rows.length == 0) {
                    g.message(g.lang.selectRowText);
                    return false;
                }
                var applicationServiceIds = [];
                for (var i=0,len=rows.length; i<len; i++) {
                    if(rows[i].serviceAppEnabled=='false'){
                        applicationServiceIds[i] = rows[i].id;
                    }
                }
                if(applicationServiceIds.length==0){
                    g.message("所选应用的路由已启动");
                    return false;
                }
                applicationServiceIds = applicationServiceIds.join(",");
                g.doStart(applicationServiceIds,true);

            }
        },{
            xtype: "Button",
            title: g.lang.endText,
            iconCss: "ecmp-flow-end",
            selected: true,
            handler: function () {
                var rows = [];
                for (var i = 0; i < g.checkRowIds.length; i++) {
                    rows.push(g.gridCmp.getRowData(g.checkRowIds[i]));
                }
                if (!rows||rows.length == 0) {
                    g.message(g.lang.selectRowText);
                    return false;
                }
                var applicationServiceIds = new Array();
                for (var i=0,len=rows.length; i<len; i++) {
                    if(rows[i].serviceAppEnabled=='true'){
                        applicationServiceIds[i] = rows[i].id;
                    }
                }
                if(applicationServiceIds.length==0){
                    g.message("所选应用的路由已停止");
                    return false;
                }
                applicationServiceIds = applicationServiceIds.join(",");
                g.doStart(applicationServiceIds,false);
            }
        }, "->", {
            xtype: "SearchBox",
            width: 190,
            //searchDisplayText: "请输入关键字查询",
            displayText: g.lang.searchDisplayText,
            onSearch: function (v) {
                g.gridCmp.setPostParams({
                    keywords: v
                },true);
            }
        }];
    },
    initCenterTbar: function(){
        var g = this;
        return [
            "->",
            {
                xtype: "SearchBox",
                width: 190,
                //searchDisplayText: "请输入关键字查询",
                displayText: this.lang.searchDisplayText,
                onSearch: function (v) {
                    g.interfaceGridCmp.setPostParams({
                        keywords: v,
                        applicationCode: g.curApplication && g.curApplication.applicationCode
                    },true);
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
    getApplicationData: function(applicationCode){
        var g = this;
        EUI.Store({
            url: _ctxPath+"/gateway_application/find_gateway_application",
            params: {applicationCode: applicationCode},
            async: false,
            success: function (status) {
                g.application  = status.data;
            },
            failure: function (status) {
                g.message(status.message);
            }
        });
    },
    addEvents: function () {
        var g = this;
        $(".ecmp-common-edit").live("click", function (e) {
            var data = g.gridCmp.getRowData($(this).attr("targetId"));
            g.isEdit = true;
            g.addAndEdit();
            g.getApplicationData(data.applicationCode);
            data.applicationName =g.application&& g.application.applicationName;
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
            data.serviceId = g.curApplication.id;
            data.url = data["interfaceURI"];
            g.getSettingData(data);
            data.routeKey = g.routeKey;
            g.configForm();
            g.configFormCmp.loadData(data);
        });
        $(".ecmp-flow-start.item").live("click",function () {
            var data = g.gridCmp.getRowData($(this).attr("targetId"));
            g.doStart(data.id,true);
        });
        $(".ecmp-flow-end.item").live("click",function () {
            var data = g.gridCmp.getRowData($(this).attr("targetId"));
            g.doStart(data.id,false);
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
                items: [
                {
                    xtype: "TextField",
                    hidden: true,
                    name: "serviceId"
                },
                {
                    xtype: "TextField",
                    title: g.lang.nameText,
                    name: "interfaceName",
                    readonly: true
                }, {
                    xtype: "TextArea",
                    title: g.lang.interfaceURIText,
                    name: "url",
                    readonly: true
                }, {
                    xtype: "TextArea",
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
    doStart: function(id,startFlag){
        var g = this;
        var maskMsg="",winMsg="",title="",flag = false;
        var param = {id:id};
        title = g.lang.hintText;
        var url = "";
        if(startFlag){
            maskMsg= g.lang.doStartText;
            winMsg= g.lang.startHintMessageText;
            url = _ctxPath+"/gateway_api_service/router/startById";
        }else{
            maskMsg= g.lang.endMaskMessageText;
            winMsg= g.lang.endHintMessageText;
            url = _ctxPath+"/gateway_api_service/router/stopById";
        }
        var mes = EUI.MessageBox({
            title: title,
            msg: winMsg,
            buttons: [{
                title: g.lang.cancelText,
                handler: function () {
                    mes.remove();
                }
            },{
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
                            if(startFlag){
                               $(".ecmp-flow-start[targetId*='"+id+"']").css("display","inline-block");
                               $(".ecmp-flow-end[targetId*='"+id+"']").css("display","none");
                            }else{
                                $(".ecmp-flow-start[targetId*='"+id+"']").css("display","none");
                                $(".ecmp-flow-end[targetId*='"+id+"']").css("display","inline-block");
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
            }]
        });
    },
    getSettingData: function (params) {
        var g = this;
        EUI.Store({
            url: _ctxPath + "/gateway_api_service/router/getting",
            params: params,
            async: false,
            success: function (status) {
               g.routeKey = status.data&&status.data.routeKey;
            },
            failure: function (status) {
                myMask.hide();
                g.message(status.message);
            }
        });
    },
    doSetting: function () {
        var g = this;
        if (!g.configFormCmp.isValid()) {
            g.message(g.lang.unFilledText);
            return;
        }
        var data = g.configFormCmp.getFormValue();
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
    addAndEdit: function () {
        var g = this;
        g.editWin = EUI.Window({
            title: g.isEdit ? String.format(g.lang.modifyWinText, g.lang.applicationServiceText) : String.format(g.lang.addWinText, g.lang.applicationServiceText),
            iconCss: g.isEdit ? "ecmp-eui-edit" : "ecmp-eui-add",
            height: 360,
            padding: 15,
            width: 430,
            items: [{
                xtype: "FormPanel",
                id: "editForm",
                padding: 0,
                defaultConfig: {
                    labelWidth: 110,
                    width: 300,
                    allowBlank: false
                },
                items: [
                    {
                        xtype: "TextField",
                        name: "id",
                        hidden: true
                    },
                    {
                        xtype: "TextField",
                        title: g.lang.serviceAppIdText,
                        name: "serviceAppId",
                        readonly:g.isEdit
                    },
                    {
                        xtype: "TextField",
                        name: "serviceAppCode",
                        title: g.lang.serviceAppCodeText,
                        readonly:g.isEdit
                    },
                    {
                        xtype: "TextArea",
                        title: g.lang.serviceAppUrlText,
                        name: "serviceAppUrl",
                        allowBlank: true,
                        readonly: true,
                        hidden: !g.isEdit
                    },
                    {
                        xtype: "TextField",
                        title: g.lang.serviceAppNameText,
                        name: "serviceAppName"
                    },
                    {
                        xtype: "TextField",
                        title: g.lang.serviceAppVersionText,
                        name: "serviceAppVersion"
                    },
                    {
                        xtype: "TextArea",
                        title: g.lang.serviceAppRemarkText,
                        name: "serviceAppRemark"
                    },
                    {
                        xtype: "ComboGrid",
                        //  applicationText: "应用",
                        title: this.lang.applicationText,
                        listWidth: 400,
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
                                {name: 'applicationRemark', index: 'applicationRemark', sortable: true, width: 200, label: g.lang.remarkText}
                            ],
                            shrinkToFit: false,
                            sortname: 'applicationName',
                            sortorder: "asc"
                        },
                        onSearch: function (v) {
                            this.grid.setPostParams({keywords: v},true);
                        }
                    }
                ]
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