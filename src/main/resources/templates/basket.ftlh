<!DOCTYPE html>
<html>
<head>
    <title>User Basket</title>
</head>
<body>
    <h1>Basket for User</h1>
    <#if products??>
        <p>No products in basket.</p>
    <#else>
        <table>
            <thead>
                <tr>
                    <th>Product Name</th>
                    <th>Product Price</th>
                    <th>Status</th>

                </tr>
            </thead>
            <tbody>
                <#list products as product>
                    <tr>
                        <td>${product.title}</td>
                        <td>${product.price}</td>
                        <td><form action="/basket/delete/${product.id}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                            <input type="submit" value="Удалить товар"/>
                            </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </#if>
</body>
</html>