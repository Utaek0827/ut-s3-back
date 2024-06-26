package com.uts3back.controller;

import com.uts3back.dto.LoginRequest;
import com.uts3back.dto.UsersDTO;
import com.uts3back.service.AuthService;
import com.uts3back.service.UserTotalServiceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserTotalServiceService userTotalServiceService;

    @Operation(summary = "로그인", description = "로그인 페이지")
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(
            @RequestBody LoginRequest loginRequest
            ){

        // 스웨거 테스트용 컨트롤러

        return ResponseEntity.ok("로그인");
    }

    @Operation(summary = "회원가입", description = "회원가입 페이지")
    @PostMapping("/sign-up")
    public ResponseEntity signUp(
            @RequestBody UsersDTO SignUpUsers
            ){

        authService.signUp(SignUpUsers);
        userTotalServiceService.insertUserTotalService(SignUpUsers.getEmail());
        System.out.println(SignUpUsers);
        return ResponseEntity.ok("회원가입 완료");
    }

    /*
    @GetMapping("/ttt")
    public String ttt(String msg){

        System.out.println("관리자 확인");
        return msg;
    }
    */







}


