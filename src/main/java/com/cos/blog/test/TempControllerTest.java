package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {

	// http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		// 파일리턴 기본경로 : src/main/resources/static
		// 리턴명 : /home.html
		// 풀경로 : src/main/resources/static/home.html
		// rest는 문자열 그대로를 리턴했다면, 해당 경로 이하에 있는 파일을 리턴해줌
		return "/home.html";
	}
	
	// 정적 파일은 브라우저 요청으로도 찾음
	@GetMapping("/temp/img")
	public String tempImg() {
		return "/18624709.jpg";
	}
	
	
	// jsp는 컴파일이 필요한 동적인 파일, static에 넣어놓고 브라우저가 찾을수있는 정적 파일이 아님
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		// src/main/webapp/WEB-INF/views 폴더로 옮김
		// prefix : /WEB-INF/views/
		// suffix : .jsp
		// 풀네임 :  /WEB-INF/views/test.jsp
		return "test";
	}
}
