/**
 * <p>
 * 实现功能：通用
 * <p/>
 *
 * @author 秦有宝
 * @version 1.0.00
 */
EUI.Common = {
    lang: {},
    uuid: {
        set() {
            var value;
            if (this.isValid(arguments[0])) {
                value = arguments[0];
            } else {
                value = this.empty();
            }
            $(this).data("value", value);
            return value;
        },
        empty() {
            return "00000000-0000-0000-0000-000000000000";
        },
        isEmpty(uid) {
            return uid == EUI.Common.uuid.empty() || typeof (uid) == 'undefined' || uid == null || uid == '';
        },
        isValid(value) {
            var rGx = new RegExp("\\b(?:[A-F0-9]{8})(?:-[A-F0-9]{4}){3}-(?:[A-F0-9]{12})\\b");
            return rGx.exec(value) != null;
        },
        newUuid() {
            var value;
            if (arguments.length == 1 && this.isValid(arguments[0])) {
                $(this).data("value", arguments[0]);
                value = arguments[0];
                return value;
            }
            var res = [], hv;
            var rgx = new RegExp("[2345]");
            for (var i = 0; i < 8; i++) {
                hv = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
                if (rgx.exec(i.toString()) != null) {
                    if (i == 3) {
                        hv = "6" + hv.substr(1, 3);
                    }
                    res.push("-");
                }
                res.push(hv.toUpperCase());
            }
            value = res.join('');
            $(this).data("value", value);
            return value;
        },
        value() {
            if ($(this).data("value")) {
                return $(this).data("value");
            }
            var val = this.newUuid();
            $(this).data("value", val);
            return val;
        }
    }
};