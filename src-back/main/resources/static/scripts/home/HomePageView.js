/**
 * 实现功能：首页
 *
 * @author cs
 * @version 1.0.00
 */
EUI.HomePageView = EUI.extend(EUI.CustomUI, {
    renderTo: "",
    authority: "",
    initComponent: function () {
        EUI.Container({
            renderTo: this.renderTo,
            layout: "border",
            border: false,
            padding: 8,
            itemspace: 0,
            html: this.getMainContent()
        });


    },
    getMainContent: function () {
        return '<div class="main markdown" style="overFlow:auto;">' +
            '<h1>一. 命名规则</h1>' +
            '<h2 style="margin-left: 20px;">1. 环境变量 & 全局参数</h2>' +
            '<ul style="margin-left: 40px;">' +
            '<li>平台级参数：以ECMP_开头(特殊情况除外)</li>' +
            '<li>应用级参数：以应用模块代码开头</li>' +
            '</ul>' +
            '<h1>二. 配置过程</h1>' +
            '<p style="margin-left: 20px;">以下配置均为按需配置：</p>' +
            '<h2 style="margin-left: 20px;">1. 配置应用模块</h2>' +
            '<ul style="margin-left: 40px;">' +
            '<li>API模块：以用模块代码开头，以_API结尾；例如：BASIC_API</li>' +
            '<li>WEB模块：以用模块代码开头，以_WEB结尾；例如：BASIC_WEB</li>' +
            '</ul>' +
            '<h2 style="margin-left: 20px;">2. 配置应用服务</h2>' +
            '<ul style="margin-left: 40px;">' +
            '<li>API模块对应的应用服务：需要配置API文档地址</li>' +
            '<li>WEB模块对应的应用服务：不需要配置API文档地址</li>' +
            '</ul>' +
            '</div>'
    }


});

