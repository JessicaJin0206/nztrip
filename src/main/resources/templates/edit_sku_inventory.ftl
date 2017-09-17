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
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="form-group"><label>Edit Sku Inventory</label></div>
            <div class="form-group row">
                <div class="input-group date col-md-4 col-sm-4 col-xs-4" id="j_date">
                    <input placeholder="Date" class="form-control"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>


            <div class="form-group">
                <div class="btn-group" id="j_session_container" data-toggle="buttons"></div>
            </div>

            <button id="j_close" class="btn btn-primary form-group">Close</button>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/moment.js"></script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="/js/edit_sku_inventory.js"></script>
</body>
</html>