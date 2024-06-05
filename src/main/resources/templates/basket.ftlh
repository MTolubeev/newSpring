<!DOCTYPE html>
<html>
<head>
    <title>User Basket</title>
</head>
<body>
    <h1>Basket for User</h1>
    <#if products?size > 0>
        <ul>
        <#list products as product>
            <li>${product.name} - ${product.description}</li>
        </#list>
        </ul>
    <#else>
        <p>No products in the basket</p>
    </#if>
</body>
</html>