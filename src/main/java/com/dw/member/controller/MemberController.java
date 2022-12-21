package com.dw.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

import com.dw.member.model.Dept;
import com.dw.member.model.Member;
import com.dw.member.repository.DeptRepo;
import com.dw.member.repository.MemberRepo;
import com.dw.member.service.MainService;
import com.dw.member.utils.APIResponse;

@RestController
public class MemberController {

	@Autowired
	MemberRepo repo;

	@Autowired
	DeptRepo drepo;

	@Autowired
	MainService service;

	// JSON 으로 보낼 때 @RequestBody로 받는다.
	@PostMapping("/api/v1/login-test")
	public boolean callLogin(@RequestBody Member member, HttpServletRequest request) {

		Member m = repo.findByuserIdAndUserPassword(member.getUserId(), member.getUserPassword());
		if (m != null) {
			HttpSession session = request.getSession(); // 세션 불러오기
			session.setAttribute("userId", m.getUserId());
			// System.out.println("이름은 : " + m.getName());
			return true;
		} else {
			return false;
		}

	}

	// 전체 조회
	// @GetMapping("/api/v1/member")
	// public List<Member> callAllMembers() {
	// 	//		findAll == select * from <테이블 이름>
	// 	return repo.findAll();
	// }

	// 전체 조회
	@GetMapping("/member")
	public APIResponse<List<Member>> callAllMembers() {
		// findAll == select * from <테이블 이름>
		List<Member> list = repo.findAll();
		int size = (int) repo.count();
		return new APIResponse<>(size, list);
	}
	
	// 전체 조회 (정렬 기능 추가)
	@GetMapping("/member/sort/{column}")
	public APIResponse<List<Member>> callAllMembers(@PathVariable String column) {
		// findAll == select * from <테이블 이름>
		List<Member> list = repo.findAll(Sort.by(Sort.Direction.ASC, column));
		int size = (int) repo.count();
		return new APIResponse<>(size, list);
	}
	
	// 전체 조회 (페이징 처리, 정렬 추가)
	// 사용법: /member/pagination?offset=0&pageSize=5&column=age
	@GetMapping("/member/pagination")
	public APIResponse<Page<Member>> callAllMembers(@RequestParam int offset, @RequestParam int pageSize,
			@RequestParam String column) {

		Page<Member> members = repo
				.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(column)));
		// offset , pageSize === LIMIT offset, pageSize  
		// pageSize : 한 페이지에 몇개 보여줄 지 알려줌
		int size = members.getSize();

		return new APIResponse<>(size, members);
	}
	
	
	// 추가
	// JPA 에서는 delete할 때 int 형으로 하지 않음
	@PostMapping("/api/v1/member")
	public Member callSaveMember(@RequestBody Member member) {
		//		save == insert
		member = repo.save(member);
		return member;
	}

	//	삭제
	@DeleteMapping("/api/v1/member/{id}")
	public boolean callDeleteMemeber(@PathVariable long id) {

		//		deleteById == delete
		//		by == where
		try {
			repo.deleteById(id); // 리턴 타입이 void 
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	// 상세조회
	@GetMapping("/api/v1/member/{id}")
	public Member callMemberById(@PathVariable long id) {
		//		findById == select # from emp where empno = 333;
		return repo.findById(id).get();
	}

	//	수정
	@PatchMapping("/api/v1/memeber")
	public Member updateMember(@RequestBody Member member) {
		//		save == update or insert
		//		동일한 pk값이 있으면 update
		//		동일한 pk값이 없으면 insert
		member = repo.save(member);
		return member;
	}

	// 부서 전체 조회
	@GetMapping("/api/v1/dept")
	public List<Dept> callAllDept() {
		return drepo.findAll();
	}

	// 부서 추가
	@PostMapping("/api/v1/dept")
	public Dept callSaveDept(@RequestBody Dept dept) {
		dept = drepo.save(dept);
		return dept;
	}

	// 부서 상세 조회
	@GetMapping("/api/v1/dept/{id}")
	public Dept callDeptById(@PathVariable long id) {
		return drepo.findById(id).get();
	}

	// 부서 수정
	@PatchMapping("/api/v1/dept")
	public Dept updateDept(@RequestBody Dept dept) {
		dept = drepo.save(dept);
		return dept;
	}

	// 부서 삭제
	@DeleteMapping("/api/v1/dept/{id}")
	public boolean deleteDept(@PathVariable long id) {
		try {
			drepo.deleteById(id); // 리턴 타입이 void 
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	// 리캡챠 인증하는 Controller 만들기
	// FORM 태그로 데이터를 전송받는 방법 1. HttpServletRequest 
	@PostMapping("/api/v1/valid-recaptcha") // AJAX 에서 type 을  post 로 했기 때문
	public boolean validRecaptcha(HttpServletRequest request) {

		String reponse = request.getParameter("g-recaptcha-response");
		boolean isRecaptcha = service.verifyRecaptcha(reponse);
		// 리캡차 인증 성공시 true, 실패시 false!

		return isRecaptcha;
	}

	//html 에서 FROM 태그로 전송시
	@PostMapping("/api/v1/login") // AJAX 에서 type 을 post 로 했기 때문
	public boolean calllogin2(@ModelAttribute Member member, HttpServletRequest request) {
		Member m = repo.findByuserIdAndUserPassword(member.getUserId(), member.getUserPassword());
		if (m != null) {
			HttpSession session = request.getSession();
			session.setAttribute("userId", m.getUserId());
			return true;
		} else {
			return false;

		}

	}

}