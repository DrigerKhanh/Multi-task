<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
   
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Project Information</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style type="text/css">
.input{
	border: none;
    background: white;
	height: 20px;
	}
	.input {
	border: none;
	background: none;
	height: 20px;
}
.New {
	background: white;
}
.Done {
	background: #FFD700;
}
.In-progress {
	background: #00FFFF;
}
.Canceled {
	background: #A52A2A;
}
</style>
</head>
<body>

<table class="table">
  <thead class="thead-light">
    <tr>
      <th scope="col">ID Project</th>
      <th scope="col">Name</th>
      <th scope="col">Status</th>
      <th scope="col">Start Date</th>
      <th scope="col">End Date</th>
      <th scope="col">Owner</th>
      <th scope="col">Description</th>
      <th scope="col">Visibility</th>
      <th scope="col">Action</th> 
    </tr>
  </thead>
  <tbody>
  <%int i =0; %>
   <c:forEach var="project" items="${listInfo}">
   <%i++; %>
		<tr>
			<td scope="row">${project.idProject }</td>
			<td>${project.name}</td>
			<td>${project.status }</td>
			<td>${project.startDate }</td>
			<td>${project.endDate }</td>
			<td>${project.owner }</td>
			<td>${project.description }</td>
			<td>${project.visibility }</td>
			<td>
				<p>
  					<a class="btn btn-primary" data-toggle="collapse" href="#<%=i %>" role="button" aria-expanded="false" aria-controls="collapseExample">
   						Action
  					</a>
  				</p>
  					<div class="collapse" id="<%=i %>">
  						<div class="card card-body">
			
       							<c:if test="${project.status eq 'New' }">
									<form:form action="project/start?idProject=${project.idProject }" method="post">
										<input type="hidden" name="idProject" value="${project.idProject }">
										<button class="input" type="submit">Start</button>
									</form:form>
								</c:if>
								<c:if test="${project.status eq 'New'}">
									<form:form action="project/cancel?idProject=${project.idProject }" method="post">
										<input type="hidden" name="idProject" value="${project.idProject }">
										<button class="input" type="submit">Cancel</button>
									</form:form>
								</c:if>
								<c:if test="${project.status eq 'In-progress' }">
									<form:form action="project/end?idProject=${project.idProject }" method="post">
										<input type="hidden" name="idProject" value="${project.idProject }">
										<button class="input" type="submit">End</button>
									</form:form>
								</c:if>			
								<c:if test="${project.status eq 'New' or project.status eq 'In-progress' or project.status eq 'Done'or project.status eq 'Canceled'}">
									<form:form action="project/view?idProject=${project.idProject }" method="post">
										<input type="hidden" name="idProject" value="${project.idProject}">
										<button class="input" type="submit">View</button>
									</form:form>
								</c:if> 
								<c:if test="${project.status eq 'New'}">
									<form:form action="project/update?idProject=${project.idProject }" method="get">
										<input type="hidden" name="idProject" value="${project.idProject}">
										<button class="input" type="submit">Update</button>
									</form:form>
								</c:if> 
								<c:if test="${project.status eq 'New' or project.status eq 'In-progress' or project.status eq 'Done' or project.status eq 'Cancel'}">
									<form:form action="project/delete?idProject=${project.idProject }" method="post">
										<input type="hidden" name="idProject" value="${project.idProject}">
										<button class="input" type="submit">Delete</button>
									</form:form>
								</c:if>
       
       			</div>
			</div>
  		</td>
			
			
			
	</c:forEach>
	<button type="button" onclick="window.location.href='<spring:url value="/project/create/" />' ">Create</button>
	<div>
	
</div>

</table>


<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
  
	<script src="<spring:url value='/resources/js/app.js' />"></script>
	


	
</body>
</html>