let index = {
	init: function(){
		$("#btn-save").on("click", ()=>{ // function(){}, ()=>{} 
		// this를 바인딩하기 위해서 화살표 함수 사용
			this.save();
		});
		$("#btn-update").on("click", ()=>{
			this.update();
		});
	},
	
	save: function(){
		// alert('user의 save 함수 호출됌');
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val(),
		};
		
		// console.log(data);
		
		// ajax 호출 시 default가 비동기 호출
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
		// ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환해주네요.
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=utf-8", // body 데이터가 어떤 타입인지(MIME)
			// dataType: "json" // 요청을 서버로 해서 응답이 왔을 때 기본적으로 모든 것이 문자열
			// (생긴게 json이라면) => javascript 오브젝트로 변경 이었는데..
			// 버전업되면서 스프링이 자동으로 자바 오브젝트로 변경해줌
		}).done(function(resp){
			alert("회원 가입이 완료되었습니다.");
			// console.log(resp);
			location.href = "/";
		}).fail(function(){
			alert(JSON.stringify(error));
		}); 
	},
	
	update: function(){
		let data = {
			id: $("#id").val(),
			password: $("#password").val(),
			email: $("#email").val(),
		};
		console.log(id);
		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8",
		}).done(function(resp){
			alert("회원 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(){
			alert(JSON.stringify(error));
		});
	}
}

index.init();