<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <title>Accident</title>
</head>
<body>
<table class="table">
    <thead>
    <tr>
        <th scope="col">ID</th>
        <th scope="col">Name</th>
        <th scope="col">Text</th>
        <th scope="col">Address</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="accident" items="${accidents}">
        <tr>
            <td>${accident.id}</td>
            <td>${accident.name}</td>
            <td>${accident.text}</td>
            <td>${accident.address}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
