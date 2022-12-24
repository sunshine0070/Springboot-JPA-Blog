package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@RestController
public class DummyControllerTest {

	@Autowired // 의존성 주입(DI)
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		return "삭제가 완료되었습니다. id: " + id;
	}
	
	// save 함수는 id를 전달하지 않으면 insert를 해줌
	// save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해줌
	// save 함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 해줌
	// email, password
	@Transactional // 함수 종료 시에 자동 commit 이 됨
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser){
		// json 데이터를 요청 -> Java Object(MessageConverter의 Jackson 라이브러리가 변환해서 받아줌)
		
		System.out.println("id: " + id);
		System.out.println("password: " + requestUser.getPassword());
		System.out.println("email: " + requestUser.getEmail());
		
		// jpa가 id로 user 들고올때 그 user는 영속성 컨텍스트에 영속화됌
		// 영속화된 user 오브젝트에 변경이 일어나면 변경 감지(더티 체킹)해서
		// commit 전에 update문 자동 수행
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
//		userRepository.save(user);
		// save 를 안해도 update가 일어난다? -> 더티 체킹
		return user;
	}
	
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	// 한 페이지당 2건 데이터를 리턴받을 예정
	@GetMapping("/dummy/user")
	public Page<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);
	
		List<User> users = pagingUser.getContent();
		return pagingUser; 
	}
	
	// http://localhost:8000/blog/dummy/join
	// http body에 username, password, email 데이터 가지고 요청
	@PostMapping("/dummy/join")
	public String join(User user) {
		
		System.out.println("id: " + user.getId());
		System.out.println("username: " + user.getUsername());
		System.out.println("password: " + user.getPassword());
		System.out.println("email: " + user.getEmail());
		System.out.println("role: " + user.getRole());
		System.out.println("createDate: " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다";
	}
	
	
	// http://localhost:8000/blog/dummy/user/{id}
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// findById가 Optional 타입
		// 없는 아이디 user/4을 찾아서 DB에서 못찾아오게 되면 user가 null이 되므로
		// Optional로 User 객체를 일단 감싸서 가져옴 -> null 판단은 여기서 하고 return
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
			}
		});
		
		// user 객체 = 자바 오브젝트
		// REST 컨트롤러는 데이터를 반환
		// 변환 (웹 브라우저가 이해할 수 있는 데이터) -> JSON(GSON 라이브러리)
		// 스프링부트는 MessageConverter라는 애가 응답시에 자동 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		// user 오브젝트를 json으로 변환해서 브라우저에 던져줌
		return user;
	}
}
