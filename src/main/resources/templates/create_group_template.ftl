<div class="form-group">
    <label>正在帮以下agent下单</label>
    <input type="text" id="j_agent" class="form-control" style="display: inline;width: auto" placeholder="姓名"
           value=""/>
</div>
<div class="form-group dropdown">
    <button class="btn btn-default dropdown-toggle" type="button" id="selected_group_type_button"
            data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="true">
        <span id="j_selected_group_type" value="">请选择类型</span>
        <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" id="j_group_type_drop_down" aria-labelledby="selected_group_type_button">
    <#list types as type>
        <li><a value="${type.value?c}">${type.desc}</a></li>
    </#list>
    </ul>
</div>
<div class="form-group"><label>主要联系人</label></div>
<div class="form-group">
    <table class="table">
        <thead>
        <tr>
            <th><input type="text" id="j_primary_contact" class="form-control" placeholder="姓名"
                       value=""/>
            </th>
            <th><input type="text" id="j_primary_contact_email" class="form-control"
                       placeholder="Email" value=""/></th>
            <th><input type="text" id="j_primary_contact_phone" class="form-control"
                       placeholder="联系电话" value=""/></th>
            <th><input type="text" id="j_primary_contact_wechat" class="form-control"
                       placeholder="微信"/></th>
        </tr>
        </thead>
    </table>
</div>
<div id="members" style="display: none">
    <div class="form-group">
        <label>游客信息</label>
        <a id="add_people"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
    </div>
    <div class="form-group">
        <table class="table" id="j_user_table">
            <thead>
            <tr>
                <th>姓名</th>
                <th>年龄</th>
                <th>体重</th>
                <th>种类</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
</div>
<div class="form-group">
    <label>备注</label>
    <input type="text" id="j_remark" class="form-control" placeholder="备注" value="">
</div>
<div class="form-group">
    <button id="j_submit" class="btn btn-primary">提交</button>
</div>

