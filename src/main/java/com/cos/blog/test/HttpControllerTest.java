package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpControllerTest {

	private static final String TAG = "HttpControllerTest : ";
	
	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = new Member(1, "ssafy", "1234", "ssafy@naver.com");
		System.out.println(TAG + "getter : " + m.getId());
		m.setId(5000);
		System.out.println(TAG + "setter : " + m.getId());
		
		return "get 요청: " + m.getId() + " " + m.getUsername() 
		+ " " + m.getPassword() + " " + m.getEmail();
	}
	
	// 인터넷 브라우저 요청은 무조건 get요청밖에 안됌
	// GET은 DTO로 받기, RequestParam도 가능
	@GetMapping("/http/get")
	public String getTest(Member m) {
		return "get 요청: " + m.getId() + " " + m.getUsername() 
		+ " " + m.getPassword() + " " + m.getEmail();
	}
	
	// POST는 RequestBody 달린 DTO로 받기
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {
		return "get 요청: " + m.getId() + " " + m.getUsername() 
		+ " " + m.getPassword() + " " + m.getEmail();
	}
	
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청: " + m.getId() + " " + m.getUsername() 
		+ " " + m.getPassword() + " " + m.getEmail();
	}
	
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
