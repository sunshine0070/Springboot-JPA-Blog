package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.auth.PrincipalDetailService;


@SuppressWarnings("deprecation")
@Configuration // 스프링 환경설정 빈 등록 (IoC 관리)
@EnableWebSecurity // 시큐리티 필터 등록, 스프링 시큐리티 설정을 이 파일에서 함
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻
// 위 세개는 세트
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주면서 password를 가로채기하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // 요청시 같이 오는 csrf 토큰 비활성화 
			// (현재 user.js에서 ajax로 요청하느라 csrf 토큰이 없음) 
			// 스프링 시큐리티는 csrf 기능 자동 활성화 (테스트시 걸어두는게 좋음)
			.authorizeRequests() 
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**")
				// 위에 포함되는 요청은
				.permitAll()
				.anyRequest()
				.authenticated()
				// 어떤 요청이든 인증되고 허가함
			.and()
				// 위에 포함되지 않은 모든 요청은
				.formLogin()
				.loginPage("/auth/loginForm")
				// 로그인폼으로 감
				.loginProcessingUrl("/auth/loginProc")
				// 컨트롤러에 만들지 않아도 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인서비스함
				.defaultSuccessUrl("/");
				// 정상적으로 요청이 완료되면 /로 이동
	}
}
