<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container">
	<form:form method="post" modelAttribute="todo">
		<form:hidden path="id" />
		<fieldset class="form-group">
			<label>Description</label>
			<form:input path="desc" type="text" class="form-control"
				required="required" />
			<form:errors path="desc" cssClass="text-warning" />
		</fieldset>

		<fieldset class="form-group">
			<label>Target date</label>
			<form:input path="targetDate" type="text" class="form-control"
				required="required" />
			<form:errors path="targetDate" cssClass="text-warning" />
		</fieldset>

		<button type="submit" class="btn btn-success">Add</button>
	</form:form>
</div>
<%@ include file="common/footer.jspf" %>