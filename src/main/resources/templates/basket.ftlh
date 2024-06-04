<!DOCTYPE html>
<html>
<head>
    <title>User Basket</title>
</head>
<body>
    <h1>Basket for User</h1>
    <#if products?size == 0>
        <p>No products in basket.</p>
    <#else>
        <table>
            <thead>
                <tr>
                    <th>Product Name</th>
                    <th>Product Price</th>
                </tr>
            </thead>
            <tbody>
                <#list products as product>
                    <tr>
                        <td>${product.name}</td>
                        <td>${product.price}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </#if>
</body>
</html>
