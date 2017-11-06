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
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main" skuId="${sku.id?c}">
            <div class="row form-group">
                <label class="col-md-1">Sku: </label>
                <div class="col-md-offset-1">
                    <span>${sku.name}</span>
                </div>
            </div>

            <div class="row form-group">
                <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_date">
                    <input placeholder="Date" class="form-control"/>
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"/>
                        </span>
                </div>
            </div>

            <div class="row form-group">
                <button id="j_query" class="btn btn-primary">Query</button>
            </div>

            <div class="row form-group">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Sold</th>
                        <th>Total</th>
                    </tr>
                    </thead>
                    <tbody id="j_inventory_container">
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/moment.js"></script>
<script type="text/javascript" src="/js/transition.js"></script>
<script type="text/javascript" src="/js/collapse.js"></script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/query_inventory.js"></script>
</body>
</html>