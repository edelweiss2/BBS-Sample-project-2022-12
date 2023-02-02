<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	pageContext.setAttribute("newline", "\n");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="../common/heading.jsp" %>
    <style>
        .disabled-link { pointer-events: none; }
    </style>
</head>

<body>
	<%@ include file="../common/top.jsp" %>
	
		<div class="container-fluid">
		<div class="row">
			<div class="col-1"></div>			
			<!-- =================== main =================== -->
				<div class="col-10 box" style="padding: 30px">						
                <h3 class="page-small-subtitle"><strong>회원 정보 조회</strong></h3>
                <hr>
                <!-- 프로필 이미지 -->
                <img src="../img/profile.png" alt="profile" style="height:250px;" class="mx-auto d-block">
                <br>
                <c:forEach var="user" items="${userList}" varStatus="loop">
                	<!-- 수정 및 삭제 권한 -->
                    <span style="font-size: 1.0em;">                     
                        <c:if test="${uid eq user.uid}">
                        	<a href="/ncpl/user/update?uid=${user.uid}" class="ms-3"><i class="far fa-edit"></i>정보 수정</a>
                         </c:if>
                    </span>
                    <br>
                    <!-- 마이페이지 내 정보 -->                   
					<c:if test="${uid eq user.uid}">
					<table  class="table table-hover" style="margin-bottom: 80px;">
					 	<tr class="table-success">
		                    <th class="col-2" id="bgc1">항목</th>
		                    <th class="col-6" id="bgc1">회원 정보</th>
	                	</tr>
						<tr class="col-3">
						<!-- 정보 띄우는 구간 -->
							<td><label for="uid" class="col-form-label" id="midP">아이디</label></td>
							<td class="col-2">${user.uid}</td>
						</tr >
						<tr>
							<td><label for="uname" class="col-form-label" id="midP">성명</label></td>
							<td class="col-2">${user.uname}</td>
						</tr>  
						<tr>
							<td><label for="email" class="col-form-label" id="midP">이메일</label></td>
							<td class="col-2">${user.email}</td>
						</tr>
						<tr>
							<td><label for="regDate" class="col-form-label" id="midP">가입일</label></td>
							<td class="col-2">${user.regDate}</td>
						</tr>
					</table>
					<br><br><br><br><br><br><br><br><br>
					</c:if>
					<span style="font-size: 1.0em;">
                         <c:if test="${uid eq user.uid}">
                        	<a href="/ncpl/user/delete?uid=${user.uid}" class="ms-3"><i class="fas fa-trash-alt"></i>계정 탈퇴</a>
                         </c:if>
                    </span>
				</c:forEach>					          
				</div>
				<div class="col-1"></div>
			</div>
		</div>

    <%@ include file="../common/bottom.jsp" %>
</body>
</html>