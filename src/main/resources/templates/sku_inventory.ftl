<!DOCTYPE html>
<html lang="en">
<head>
<#include "head.ftl"/>
</head>

<body>

<#include "navigation.ftl"/>
<div class="container-fluid">
    <div class="row">
    <#include "menu.ftl"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-11 col-md-offset-1 main">
            <div class="form-group"><label>编辑库存</label></div>
            <div class="form-group row">
                <div class="input-group date col-md-4 col-sm-4 col-xs-4" id="j_start_date">
                    <input placeholder="开始日期" class="form-control"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>

                <div class="input-group date col-md-4 col-sm-4 col-xs-4" id="j_end_date">
                    <input placeholder="结束日期" class="form-control"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>


            <div class="form-group">
                <button id="j_refresh" class="btn btn-primary form-group">刷新</button>
                <div class="btn-group" id="j_session_container" data-toggle="buttons"></div>
            </div>

            <div class="form-group"><input type="number" id="j_total_count" class="form-control" placeholder="库存数量"
                                           value="">
            </div>
            <button id="j_submit" class="btn btn-primary form-group">提交</button>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/moment.js"></script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="/js/sku_inventory.js"></script>
</body>
</html>