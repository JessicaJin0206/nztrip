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
<div class="form-group"><label>备用联系人</label></div>
<div class="form-group">
    <table class="table">
        <thead>
        <tr>
            <th><input type="text" id="j_secondary_contact" class="form-control" placeholder="姓名"/>
            </th>
            <th><input type="text" id="j_secondary_contact_email" class="form-control"
                       placeholder="Email"/></th>
            <th><input type="text" id="j_secondary_contact_phone" class="form-control"
                       placeholder="联系电话"/></th>
            <th><input type="text" id="j_secondary_contact_wechat" class="form-control"
                       placeholder="微信"/></th>
        </tr>
        </thead>
    </table>
</div>
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
        <#--<tr>
            <td>
                <input class="form-control" id="j_user_name" value="${user.name}"/>
            </td>
            <td>
                <input class="form-control" id="j_user_age" value=""/>
            </td>
            <td>
                <input class="form-control" id="j_user_weight" value=""/>
            </td>
        </tr>-->
        </tbody>
    </table>
</div>
<div class="form-group dropdown" style="display:none" id="city_dropdown">
    <button class="btn btn-default dropdown-toggle" type="button" id="selected_city_button"
            data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="true">
        <span id="j_selected_city" value="0">选择城市</span>
        <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" id="j_city_drop_down" aria-labelledby="selected_city_button">
    <#list cities as city>
        <li><a value="${city.id?c}">${city.name}</a></li>
    </#list>
    </ul>
</div>
<div class="form-group">
    <label>游玩信息</label>
    <a id="add_date"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
</div>
<div id="dates">

</div>
<#--<div class="form-group row">
    <div class="col-xs-4">
        <input type="text" id="j_sku_name" class="form-control"
               placeholder="SKU名称" value="" autocomplete="off">
    </div>
    <div class="col-xs-2">
        <button id="j_team_submit" class="btn btn-primary">查询自动下单</button>
    </div>
</div>-->
<#--<div class="form-group">
    <button id="j_team_submit" class="btn btn-primary">提交</button>
</div>-->
<div class="form-group">
    <button id="j_submit" class="btn btn-primary">提交</button>
</div>
<div id="modals">

</div>
<#--<div class="modal fade" style="width:800px ;height:1500px " id="checkModal1" tabindex="-1"
     role="dialog" aria-labelledby="myModalLabel1">
    <div class="modal-dialog" style="width:780px; " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">查看订单</h4>
            </div>
            <div class="modal-body" style="height: 600px;overflow:auto">

            </div>
        </div>
    </div>
</div>-->
