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
        this.gridCmp = EUI.getCmp("applicationService");
        this.addEvents();
    },
    initWestContainer: function () {
        var g = this;
        return {
            xtype: "GridPanel",
            region: "west",
            id: "runtimeEnvironment",
            width: 300,
            // runtimeEnvironmentText: "运行环境",
            title: this.lang.runtimeEnvironmentText,
            border: true,
            padding: 0,
            gridCfg: {
                url: _ctxPath + "/interface/listAllByApplicationId",
                loadonce: true,
                colModel: [{ name: 'id', hidden: true },
                    // codeText: 代码
                    { name: 'code', index: 'code', sortable: true, width: 50, label: g.lang.codeText},
                    //nameText: 名称
                    { name: 'name', index: 'name', sortable: true, width: 125,  label: g.lang.nameText},
                    //rankText: '排序',
                    { name: "rank",index: "rank", label: g.lang.rankText,sorttype:"integer",sortable: true, width: 60, align: "center" }
                ],
                rowNum: 15,
                shrinkToFit: false,
                sortname: 'rank',
                onSelectRow: function () {
                    g.currentRuntimeEnvironment = EUI.getCmp("runtimeEnvironment").getSelectRow();
                    g.getApplicationServices();
                },
                loadComplete: function (data) {
                    if(data && data.length>0){
                        EUI.getCmp("runtimeEnvironment").setSelectRowById(data[0].id);
                    }
                }
            },

        }
    },
    initCenterContainer: function () {
        return {
            xtype: "GridPanel",
            region: "center",
            id: "applicationService",
            // applicationServiceText: "应用服务",
            title: this.lang.applicationServiceText,
            border: true,
            padding: 0,
            tbar: this.initTbar(),
            gridCfg: this.initGridCfg()
        }
    },
    getPlatforms:function () {
        var g=this, myMask = EUI.LoadMask({
            //getPlatformDataText: "正在获取平台数据，请稍候...",
            msg: g.lang.getPlatformDataText,
        });
        EUI.Store({
            url: _ctxPath + "/platform/listAllPlatform",
            async: false,
            success: function (result) {
                myMask.hide();
                g.platforms = result;
                g.currentPlatForm = result[0];
            },
            failure: function () {
                myMask.remove();
                var status = {
                    //getPlatformDataFailText: "平台数据获取失败!",
                    msg: g.lang.getPlatformDataFailText,
                    success: false,
                    showTime: 60
                };
                EUI.ProcessStatus(status);
            }
        });
    },
    getApplicationServices:function(){
        var g=this, myMask = EUI.LoadMask({
            //getApplicationServiceText: "正在获取应用服务数据，请稍候...",
            msg: g.lang.getApplicationServiceText,
        });
        EUI.Store({
            url:_ctxPath + "/applicationService/listAllByPlatformAndEnv",
            params: {
                platformId: g.currentPlatForm && g.currentPlatForm.id,
                runtimeEnvironmentId: g.currentRuntimeEnvironment && g.currentRuntimeEnvironment.id
            },
            async: false,
            success: function (result) {
                myMask.hide();
                EUI.getCmp("applicationService").setDataInGrid(result);
                EUI.getCmp("searchBox_gridPanel").doSearch();
            },
            failure: function () {
                myMask.remove();
                var status = {
                    //getApplicationServiceFailText: "应用服务数据获取失败!",
                    msg: g.lang.getApplicationServiceFailText,
                    success: false,
                    showTime: 60
                };
                EUI.ProcessStatus(status);
            }
        });
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
            //distributeText: "发布",
            title: g.lang.distributeText,
            iconCss: "ecmp-common-effect",
            selected: true,
            handler: function () {
                var rows = g.gridCmp.getSelectRow();
                if (rows.length == 0) {
                    //selectServiceText:"请选择要发布的应用服务!",
                    g.message(g.lang.selectServiceText);
                    return false;
                }
                var applicationServiceIds = new Array();
                for (var i=0,len=rows.length; i<len; i++) {
                    applicationServiceIds[i] = rows[i].id;
                }
                var infoBox = EUI.MessageBox({
                    //hintText: 提示
                    title: g.lang.hintText,
                    //distributeHintMessageText:"您确定要发布吗？",
                    msg: g.lang.distributeHintMessageText,
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
        },{
            xtype: "ComboGrid",
            // platformText:"平台",
            title: this.lang.platformText,
            allowBlank: false,
            listWidth: 390,
            name: "name",
            field: ["id"],
            showSearch: true,
            canClear: false,
            searchConfig: {searchCols: ["code", "name"]},
            gridCfg: {
                data: g.platforms,
                loadonce: true,
                datatype: "local",
                colModel: [
                    {name: 'id', hidden: true},
                    // codeText: "代码",
                    // nameText: "名称",
                    {name: 'code', index: 'code', label: g.lang.codeText, width: 80},
                    {name: 'name', index: 'name', label: g.lang.nameText, width: 250},
                    { name: "rank",index: "rank",sorttype:"integer",sortable: true, width: 60, align: "center",hidden: true }
                ],
                shrinkToFit: false,
                sortname: 'rank',
                sortorder: "asc",
            },
            onSearch: function (v) {
                if (v) {
                    this.grid.localSearch(v);
                } else {
                    this.grid.restore();
                }
            },
            afterRender: function () {
                this.loadData(g.platforms[0]);
            },
            reader: {
                name: "name",
                field: ["id"]
            },
            afterSelect: function (data) {
                g.currentPlatForm = data.data;
                g.getApplicationServices();
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
    doDistribute:function (applicationServiceIds) {
        var g=this, myMask = EUI.LoadMask({
            //doDistributeText: "正在发布应用服务，请稍候...",
            msg: g.lang.doDistributeText,
        });
        EUI.Store({
            url: _ctxPath + "/applicationService/distribute",
            params:{ids:applicationServiceIds},
            async: false,
            success: function (result) {
                myMask.remove();
                EUI.ProcessStatus(result);
            },
            failure: function (result) {
                myMask.remove();
                EUI.ProcessStatus(result);
            }
        });
    },
    initGridCfg: function () {
        var g = this;
        return {
            loadonce: true,
            datatype: "local",
            colModel: [{
                //operateText:操作
                label: g.lang.operateText,
                name: "operate",
                index: "operate",
                width: 80,
                align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return "<i  class='ecmp-common-edit icon-space' title='" + g.lang.modifyText + "' targetId='"+rowObject.id+"'></i><i class='ecmp-common-delete' title='" + g.lang.deleteText + "' targetId='"+rowObject.id+"'></i>";
                }
            }, {name: 'id', hidden: true},
                {name: 'appId', hidden: true},
                {name: 'applicationModule.id', hidden: true},
                // applicationModuleText: "应用模块",
                {name: 'applicationModule.name', index: 'applicationModule.name', sortable: true, width: 100, label: g.lang.applicationModuleText},
                // remarkText: "应用服务说明",
                {name: 'remark', index: 'remark', sortable: true, width: 200, label: g.lang.remarkText},
                // apiDocsUrlText: "API文档地址",
                {name: 'apiDocsUrl', index: 'apiDocsUrl', sortable: true, width: 300, label: g.lang.apiDocsUrlText, formatter: 'link',formatoptions:{target:"_blank"},}
            ],
            rowNum: 15,
            // shrinkToFit: false,
            sortname: 'remark',
            multiselect: true,
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
                            url: _ctxPath + "/applicationService/delete",
                            params: {
                                id: rowId
                            },
                            success: function (status) {
                                myMask.hide();
                                g.getApplicationServices();
                                EUI.ProcessStatus(status);
                            },
                            failure: function (re) {
                                myMask.hide();
                                EUI.ProcessStatus(re);
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
           // applicationServiceText: "应用服务",
            title: g.isEdit ? String.format(g.lang.modifyWindowText, g.lang.applicationServiceText, g.currentRuntimeEnvironment.name) : String.format(g.lang.addWindowText, g.lang.applicationServiceText,g.currentRuntimeEnvironment.name),
            iconCss: g.isEdit ? "ecmp-eui-edit" : "ecmp-eui-add",
            height: 280,
            padding: 15,
            width: 430,
            items: [{
                xtype: "FormPanel",
                id: "editForm",
                padding: 0,
                defaultConfig: {
                    labelWidth: 110,
                    width: 315
                },
                items: [{
                    xtype: "TextField",
                    hidden: true,
                    name: "id"
                }, {
                    xtype: "TextField",
                    //appIdText:"应用标识",
                    title: g.lang.appIdText,
                    name: "appId",
                    maxlength: 36,
                    allowBlank: false,
                    validateText: "请输入有效的uuid!",
                    readonly: g.isEdit,
                    validater: function (v) {
                        return EUI.Common.uuid.isValid(v);
                    },
                },{
                    xtype: "ComboGrid",
                    //  applicationModuleText: "应用模块",
                    title: this.lang.applicationModuleText,
                    allowBlank: false,
                    listWidth: 390,
                    name: "applicationModule.name",
                    field: ["applicationModule.id"],
                    showSearch: true,
                    canClear: false,
                    searchConfig: {searchCols: ["code", "name"]},
                    gridCfg: {
                        url:_ctxPath + "/applicationModule/listAllByPlatformId",
                        postData: {
                            platformId: g.currentPlatForm.id
                        },
                        loadonce: true,
                        // datatype: "local",
                        colModel: [
                            {name: 'id', hidden: true},
                            {name: 'code', index: 'code', label: "代码", width: 135},
                            {name: 'name', index: 'name', label: "名称", width: 180}
                        ],
                        shrinkToFit: false,
                        sortname: 'code',
                        sortorder: "asc",
                    },
                    onSearch: function (v) {
                        if (v) {
                            this.grid.localSearch(v);
                        } else {
                            this.grid.restore();
                        }
                    },
                    reader: {
                        name: "name",
                        field: ["id"]
                    },
                }, {
                    xtype: "TextArea",
                    // remarkText: "应用服务说明",
                    title: g.lang.remarkText,
                    name: "remark",
                    maxlength: 100,
                    allowBlank: false
                }, {
                    xtype: "TextArea",
                    // apiDocsUrlText: "API文档地址",
                    title: g.lang.apiDocsUrlText,
                    name: "apiDocsUrl",
                    maxlength: 800,
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
        var g = this;
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

        data.applicationModuleId = data["applicationModule.id"];
        data.runtimeEnvironmentId = g.currentRuntimeEnvironment.id;
        delete data["applicationModule.id"];
        delete data["applicationModule.name"];
        var myMask = EUI.LoadMask({
            //saveMaskMessageText:"正在加载，请稍候..."
            msg: g.lang.saveMaskMessageText,
        });
        EUI.Store({
            url: _ctxPath + "/applicationService/save",
            params: data,
            success: function (result) {
                myMask.hide();
                g.editWin.remove();
                g.getApplicationServices();
                EUI.ProcessStatus(result);
            },
            failure: function (re) {
                myMask.hide();
                EUI.ProcessStatus(re);
            }
        });
    },
    message: function (msg) {
        var g = this;
        var message = EUI.ProcessStatus({
            sucess: false,
            //hintText: "温馨提示",
            title: g.lang.hintText,
            msg: msg,
        });
    }
});