/**
 * Created by Administrator on 14-3-21.
 */
var FrameTab = {};
var FrameMsg = {};


FrameTab.addTab = function(title,url,icon){
    var iframe = '<iframe src="' + url + '" frameborder="0" scrolling="no" style="border: 0;width:100%;height:100%"></iframe>';
    var t = $('#index_tabs');
    var opts = {
        title : title,
        closable : true,
        iconCls : icon,
        content : iframe,
        border : false,
        fit : true
    };
    if (t.tabs('exists', opts.title)) {
        t.tabs('select', opts.title);
        parent.$.messager.progress('close');
    } else {
        t.tabs('add', opts);
        var tab = t.tabs('getSelected');
        tab.css("overflow", "hidden");  //隐藏滚轮
        tab.panel('refresh');          //刷新该面板，为了Iframe自适应（填满）父级DIV
    }
};



FrameMsg.show = function(msg, p, timeout) {
    var to = timeout ? timeout : 3000;
    $.messager.show({
        title : '系统消息提示',
        msg : msg,
        timeout : to,
        showType : 'slide',
        style : FrameMsg.position(p)
    });
};

FrameMsg.position = function(position) {
    var posi = {
        left : '',
        right : 0,
        top : '',
        bottom : -document.body.scrollTop
            - document.documentElement.scrollTop
    };
    if (position == 'top-left') {
        posi = {
            right : '',
            left : 0,
            top : document.body.scrollTop + document.documentElement.scrollTop,
            bottom : ''
        };
    } else if (position == 'top-center') {
        posi = {
            right : '',
            top : document.body.scrollTop + document.documentElement.scrollTop,
            bottom : ''
        };
    } else if (position == 'top-right') {
        posi = {
            left : '',
            right : 0,
            top : document.body.scrollTop + document.documentElement.scrollTop,
            bottom : ''
        };
    } else if (position == 'center-left') {
        posi = {
            left : 0,
            right : '',
            bottom : ''
        };
    } else if (position == 'center') {
        posi = {
            right : '',
            bottom : ''
        };
    } else if (position == 'center-right') {
        posi = {
            left : '',
            right : 0,
            bottom : ''
        };
    } else if (position == 'bottom-left') {
        posi = {
            left : 0,
            right : '',
            top : '',
            bottom : -document.body.scrollTop
                - document.documentElement.scrollTop
        };
    } else if (position == 'bottom-center') {
        posi = {
            right : '',
            top : '',
            bottom : -document.body.scrollTop
                - document.documentElement.scrollTop
        };
    }
    return posi;
};