<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1>Classic Models API (JEE)</h1>
<br/>

<h2>GraphQL</h2>
<span>POST http://localhost:8080/classicmodels/graphql</span><br/>
<br/>
<a href="/classicmodels/graphiql">GraphiQL</a>
<br/>
<br/>

<h2>Resources</h2>
<p><b>Customer</b></p>
<span>
POST http://localhost:8080/classicmodels/api/customer

<br/>
GET http://localhost:8080/classicmodels/api/customer/

<br/>
GET http://localhost:8080/classicmodels/api/customer/{{id}}

<br/>
PUT http://localhost:8080/classicmodels/api/customer/{{id}}

<br/>
DELETE http://localhost:8080/classicmodels/api/customer/{{id}}

<br/>
GET http://localhost:8080/classicmodels/api/customer/{{id}}/details
</span>
<p><b>Employee</b></p>
<span>
POST http://localhost:8080/classicmodels/api/employee

<br/>
GET http://localhost:8080/classicmodels/api/employee/

<br/>
GET http://localhost:8080/classicmodels/api/employee/{{id}}

<br/>
PUT http://localhost:8080/classicmodels/api/employee/{{id}}

<br/>
DELETE http://localhost:8080/classicmodels/api/employee/{{id}}

</span>
<p><b>Office</b></p>
<span>
POST http://localhost:8080/classicmodels/api/office

<br/>
GET http://localhost:8080/classicmodels/api/office/

<br/>
GET http://localhost:8080/classicmodels/api/office/{{id}}

<br/>
PUT http://localhost:8080/classicmodels/api/office/{{id}}

<br/>
DELETE http://localhost:8080/classicmodels/api/office/{{id}}

</span>
<p><b>Order</b></p>
<span>
POST http://localhost:8080/classicmodels/api/order

<br/>
GET http://localhost:8080/classicmodels/api/order/

<br/>
GET http://localhost:8080/classicmodels/api/order/{{id}}

<br/>
PUT http://localhost:8080/classicmodels/api/order/{{id}}

<br/>
DELETE http://localhost:8080/classicmodels/api/order/{{id}}

</span>
<p><b>OrderDetail</b></p>
<span>
POST http://localhost:8080/classicmodels/api/orderdetail

<br/>
GET http://localhost:8080/classicmodels/api/orderdetail/{{order}}

<br/>
GET http://localhost:8080/classicmodels/api/orderdetail/{{order}}/{{product}}

<br/>
PUT http://localhost:8080/classicmodels/api/orderdetail/{{order}}/{{product}}

<br/>
DELETE http://localhost:8080/classicmodels/api/orderdetail/{{order}}/{{product}}

</span>
<p><b>Payment</b></p>
<span>
POST http://localhost:8080/classicmodels/api/payment

<br/>
GET http://localhost:8080/classicmodels/api/payment/

<br/>
GET http://localhost:8080/classicmodels/api/payment/{{customer}}

<br/>
GET http://localhost:8080/classicmodels/api/payment/{{customer}}/{{checkNumber}}

<br/>
PUT http://localhost:8080/classicmodels/api/payment/{{customer}}/{{checkNumber}}

<br/>
DELETE http://localhost:8080/classicmodels/api/payment/{{customer}}/{{checkNumber}}

</span>
<p><b>Product</b></p>
<span>
POST http://localhost:8080/classicmodels/api/product

<br/>
GET http://localhost:8080/classicmodels/api/product/

<br/>
GET http://localhost:8080/classicmodels/api/product/{{id}}

<br/>
PUT http://localhost:8080/classicmodels/api/product/{{id}}

<br/>
DELETE http://localhost:8080/classicmodels/api/product/{{id}}

</span>
<p><b>ProductLine</b></p>
<span>
POST http://localhost:8080/classicmodels/api/productline

<br/>
GET http://localhost:8080/classicmodels/api/productline/

<br/>
GET http://localhost:8080/classicmodels/api/productline/{{id}}

<br/>
PUT http://localhost:8080/classicmodels/api/productline/{{id}}

<br/>
DELETE http://localhost:8080/classicmodels/api/productline/{{id}}

</span>

</body>
</html>