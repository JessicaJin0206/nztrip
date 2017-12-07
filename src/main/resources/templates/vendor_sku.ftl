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
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>SKU Id</th>
                        <th>Name</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list skus as sku>
                    <tr>
                        <td>${sku.uuid!''}</td>
                        <td>${sku.name!''}</td>
                        <td>
                            <div>
                                <a href="/create_vendor_order?skuId=${sku.id?c}">Reserve</a>
                            </div>
                            <div>
                                <a href="/skus/${sku.id?c}">Detail</a>
                            </div>
                            <div>
                                <a class="j_booking_status" sku_id="${sku.id?c}" href="javascript:void(0)">Booking Status</a>
                            </div>
                            <div>
                                <a href="/skus/${sku.id?c}/inventory/_edit">Modify</a>
                            </div>
                            <div>
                                <a href="/export/skus/${sku.id?c}/overview">Export Inventory</a>
                            </div>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/bootbox.min.js"></script>
<script type="text/javascript" src="/js/moment.js"></script>
<script type="text/javascript" src="/js/vendor_sku.js"></script>
</body>
</html>
