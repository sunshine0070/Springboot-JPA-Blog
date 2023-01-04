package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) { // username, password, email
		System.out.println("UserApiController : save 호출됌");
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
		// 자바오브젝트를 JSON으로 변환해서 리턴(jackson 라이브러리가 해줌)
	}
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user, @AuthenticationPrincipal PrincipalDetail principal) { // key=value, x-www-form-urlencoded
		userService.회원수정(user, principal);
		// 여기서는 트랜잭션이 종료되기 때문에 DB 값은 변경이 됐음
		// 하지만 세션값은 변경되지 않은 상태이기 때문에 로그아웃 후 로그인하고 들어가야 변경값이 적용됌
		// 세션값을 직접 변경하는 방법으로 고고
		// 세션 수정은 서비스에서 진행... 하면 DB 값 변경 안된상태에서 세션 반영되니까 
		// 컨트롤러에서 해야함
		// authenticationManager deprecated.. 

//		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),  user.getPassword()));
//		SecurityContextHolder.getContext().setAuthentication(authentication);
	
		// 해결방법 
		// principalDetail 컨트롤러에서 파라미터로 서비스단에 넘겨줌
		// 서비스단 마지막에 principal.setUser(persistence);
			
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
}
