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
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Sku Id</th>
                        <th>City</th>
                        <th>Category</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Duration</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list skus as sku>
                    <tr>
                        <th scope="row">${sku.uuid}</th>
                        <td>${sku.cityEn}</td>
                        <td>${sku.category}</td>
                        <td>${sku.name}</td>
                        <td>${sku.description}</td>
                        <td>${sku.duration}</td>
                        <td>
                            <div>
                                <a href="/skus/${sku.id?c}">
                                    <span class="glyphicon glyphicon-align-justify" aria-hidden="true"></span>
                                </a>
                                <a href="/create_vendor_order?skuId=${sku.id?c}" target="_blank">
                                    <span class="glyphicon glyphicon-check" aria-hidden="true"></span>
                                </a>
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
</body>
</html>
