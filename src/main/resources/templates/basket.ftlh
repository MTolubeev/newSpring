<!DOCTYPE html>
<html>
<head>
    <title>User Basket</title>
</head>
<body>
    <h1>Basket for User</h1>
    <#if products??>
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
                        <td>${product.title!}</td>
                        <td>${product.price!}</td>
                        <td>
                            <form action="/basket/${user.id}/delete/${product.id}" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <input type="submit" value="Удалить товар"/>
                            </form>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    <#else>
        <p>No products in basket.</p>
    </#if>
</body>
</html>
